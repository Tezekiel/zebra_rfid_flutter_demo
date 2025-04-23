package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.BordaHandheldTrigger
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.ConnectionStatus
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.LOG_TAG
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.ReaderResponse
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.TagLocationingResponse
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.TriggerMode
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.*
import com.zebra.rfid.api3.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.System
import java.util.concurrent.Executors


/**
 * ZebraConnectionHelper is a helper class for connecting to a Zebra RFID reader via Bluetooth
 * and managing the configuration of the device.
 *
 * @property context Application context.
 * @property tagFindHandler A lambda function to handle tag locationing events.
 */
class ZebraConnectionHelper(
  private var context: Context,
  private var tagHandlerEvent: TagDataEventHandler,
  private var tagFindHandler: TagDataEventHandler,
  private var readTagEvent: TagDataEventHandler,
) :
    ViewModel() {
    private var readers: Readers
    private var availableRFIDReaderList: ArrayList<ReaderDevice>? = null
    private var readerDevice: ReaderDevice? = null
    private var reader: RFIDReader? = null
    private var rfidEventHandler: RfidEventHandler? = null
    private var timer = 0L
    val nvramDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    init {
        Log.d(LOG_TAG, "Creating reader for serial connection")
        readers = Readers(context, ENUM_TRANSPORT.RE_SERIAL)
        availableRFIDReaderList = readers.GetAvailableRFIDReaderList()
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    /**
     * Connects to the RFID reader with the given user configuration.
     *
     * @param readerConfig User configuration.
     */
    @Synchronized
    fun connect(name: String, readerConfig: HashMap<String, Any>, resetTimer: Boolean = true) {
        if (reader != null && reader!!.isConnected) return

        if (resetTimer) {
            timer = System.currentTimeMillis()
        }
        viewModelScope.launch(nvramDispatcher) {
            ReaderResponse.setConnectionStatus(ConnectionStatus.connecting)
            ReaderResponse.setName(name)
            tagHandlerEvent.sendEvent(ReaderResponse.toJson())

            Log.d(LOG_TAG, "connect called! ADDRESS -> $name")
            try {
                Log.d(LOG_TAG, "Reader is created")
                if (availableRFIDReaderList == null || availableRFIDReaderList!!.isEmpty()) {
                    Log.d(LOG_TAG, "No readers found, fetching them again")
                    availableRFIDReaderList = readers.GetAvailableRFIDReaderList()
                }
                if (availableRFIDReaderList != null) {
                    Log.d(LOG_TAG, "Readers found: " + availableRFIDReaderList.toString())

                    if (availableRFIDReaderList!!.size != 0) {
                        /// get first reader from list
                        readerDevice = availableRFIDReaderList!!.first()
                        reader = readerDevice!!.getRFIDReader()

                        Log.d(LOG_TAG, "Connecting to reader")
                        reader!!.connect()
                        Log.d(LOG_TAG, "Connected! : Device -> ${readerDevice!!.name}")

                        /// Configure the reader with the user configuration
                        configureReader(readerConfig)

                        Log.d(
                            LOG_TAG,
                            "Connected after ${(System.currentTimeMillis() - timer) / 1000} seconds"
                        )
                        timer = 0

                        /// Trigger battery status
                        reader!!.Config.getDeviceStatus(true, true, false)

                        ReaderResponse.setConnectionStatus(ConnectionStatus.connected)
                        tagHandlerEvent.sendEvent(ReaderResponse.toJson())

                    }
                }

            } catch (e: InvalidUsageException) {
                e.printStackTrace()
                Log.d(LOG_TAG, "CONNECTION FAILED 1 -> InvalidUsageException")
                ReaderResponse.setAsConnectionError()
                tagHandlerEvent.sendEvent(ReaderResponse.toJson())

            } catch (e: OperationFailureException) {
                if (e.results == RFIDResults.RFID_READER_REGION_NOT_CONFIGURED) {
                    if (reader != null && reader!!.isConnected) reader!!.disconnect()
                    delay(1000)
                    connect(name, readerConfig, resetTimer = false)
                    Log.d(LOG_TAG, "CONNECTION FAILED 2 -> RFID_READER_REGION_NOT_CONFIGURED")
                } else {
                    Log.d(LOG_TAG, "CONNECTION FAILED 2 -> ${e.results}")
                    e.printStackTrace()
                    if (reader != null && reader!!.isConnected) reader!!.disconnect()
                    delay(1000)
                    ReaderResponse.setConnectionStatus(ConnectionStatus.failed)
                    tagHandlerEvent.sendEvent(ReaderResponse.toJson())
                }

            }
        }
    }

    fun findTheTag(tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (reader != null && reader!!.isConnected) {
                    Log.d(LOG_TAG, "findTheTag called!")
                    BordaHandheldTrigger.setMode(TriggerMode.TAG_LOCATIONING_PERFORM)
                    TagLocationingResponse.setTag(tag)
                    TagLocationingResponse.setAnyReaderConnected(true)
                    tagFindHandler.sendEvent(TagLocationingResponse.toJson())
                }
            } catch (e: InvalidUsageException) {
                e.printStackTrace()
                Log.d(LOG_TAG, "InvalidUsageException " + e.vendorMessage)
            } catch (e: OperationFailureException) {
                e.printStackTrace()
                Log.d(
                    LOG_TAG, "OperationFailureException " + e.vendorMessage
                )
            }
        }
    }

    fun stopFindingTheTag() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (reader != null && reader!!.isConnected) {
                    Log.d(LOG_TAG, "stopFindingTag called!")
                    BordaHandheldTrigger.setMode(TriggerMode.INVENTORY_PERFORM)
                    TagLocationingResponse.reset()
                    tagFindHandler.sendEvent(TagLocationingResponse.toJson())
                }
            } catch (e: InvalidUsageException) {
                e.printStackTrace()
                Log.d(LOG_TAG, "InvalidUsageException " + e.vendorMessage)
            } catch (e: OperationFailureException) {
                e.printStackTrace()
                Log.d(
                    LOG_TAG, "OperationFailureException " + e.vendorMessage
                )
            }
        }
    }

    private fun clearConfiguration() {
        readers = Readers(context, ENUM_TRANSPORT.RE_SERIAL)
        availableRFIDReaderList = null
        readerDevice = null
        reader = null
        rfidEventHandler = null
    }

    fun disconnect() {
        try {
            reader!!.disconnect()

            ReaderResponse.reset()
            tagHandlerEvent.sendEvent(ReaderResponse.toJson())

        } catch (e: InvalidUsageException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "InvalidUsageException " + e.vendorMessage)
        } catch (e: OperationFailureException) {
            e.printStackTrace()
            Log.d(
                LOG_TAG, "OperationFailureException " + e.vendorMessage
            )
        }
    }

    private fun configureReader(readerConfig: HashMap<String, Any>) {
        if (reader!!.isConnected) {
            Log.d(TAG, "ConfigureReader " + reader!!.hostName)

            var antennaPower = 0

            antennaPower = if (readerConfig["antennaPower"] == null) {
                getMaxAntennaPower()
            } else {
                readerConfig["antennaPower"] as Int
            }

            var beeperVolume = readerConfig["beeperVolume"] as Int
            var isDynamicPowerEnable = readerConfig["isDynamicPowerEnable"] as Boolean

            val triggerInfo = TriggerInfo()
            triggerInfo.StartTrigger.triggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE
            triggerInfo.StopTrigger.triggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE
            try {

                rfidEventHandler =
                    RfidEventHandler(reader!!, tagHandlerEvent, tagFindHandler, readTagEvent)
                reader!!.Events.addEventsListener(rfidEventHandler)
                reader!!.Events.setHandheldEvent(true)
                reader!!.Events.setTagReadEvent(true)
//                reader!!.Events.setBatteryEvent(true)
//                reader!!.Events.setReaderDisconnectEvent(true)
//                reader!!.Events.setOperationEndSummaryEvent(true)
//                reader!!.Events.setInventoryStartEvent(true)
//                reader!!.Events.setInventoryStopEvent(true)
                reader!!.Events.setAttachTagDataWithReadEvent(false)
//                reader!!.Events.setInfoEvent(true)
//                reader!!.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true)
//                reader!!.Config.startTrigger = triggerInfo.StartTrigger
//                reader!!.Config.stopTrigger = triggerInfo.StopTrigger

//                val s1SingulationControl = reader!!.Config.Antennas.getSingulationControl(1)
//                s1SingulationControl.session = SESSION.SESSION_S0
//                s1SingulationControl.Action.inventoryState = INVENTORY_STATE.INVENTORY_STATE_A
//                s1SingulationControl.Action.slFlag = SL_FLAG.SL_ALL
//                reader!!.Config.Antennas.setSingulationControl(1, s1SingulationControl)
//                reader!!.Actions.PreFilters.deleteAll()
//
//                setDynamicPower(isDynamicPowerEnable)
//                setBeeperVolumeConfig(beeperVolume)
//                setAntennaConfig(antennaPower)

                setRegion()
            } catch (e: InvalidUsageException) {
                Log.d(LOG_TAG, "InvalidUsageException -> configureReader")
                e.printStackTrace()
            } catch (e: OperationFailureException) {
                Log.d(LOG_TAG, "OperationFailureException -> configureReader")
                e.printStackTrace()
            }
        }
    }

    private fun setRegion() {
        val regionConfig = reader!!.Config.getRegulatoryConfig()
        val regions = reader!!.ReaderCapabilities.SupportedRegions
        val regionInfo = regions.getRegionInfo(0)
        regionConfig.setRegion(regionInfo.getRegionCode())
        regionConfig.setIsHoppingOn(regionInfo.isHoppingConfigurable())
        regionConfig.setEnabledChannels(regionInfo.getSupportedChannels())
        regionConfig.setStandardName(regionInfo.getName())
        reader!!.Config.setRegulatoryConfig(regionConfig)
        Log.d(
            LOG_TAG,
            "GettRegulatory for region " + regionConfig.getRegion() + " Standard " + regionConfig.getStandardName()
        )
    }

    /**
     * Sets the singulation.
     *
     * @param tagPopulation Tag population.
     */
    private fun setSingulation(tagPopulation: Short?) {
        try {
            val s1_singulationControl = reader!!.Config.Antennas.getSingulationControl(1)
            s1_singulationControl.session = SESSION.SESSION_S0
            s1_singulationControl.Action.inventoryState = INVENTORY_STATE.INVENTORY_STATE_A
            s1_singulationControl.Action.setPerformStateAwareSingulationAction(true)
            s1_singulationControl.Action.slFlag = SL_FLAG.SL_ALL

            // TODO: tagPopulation info is taken from users. If it is not provided, it is set to 100.
            s1_singulationControl.tagPopulation = tagPopulation ?: 100
            reader!!.Config.Antennas.setSingulationControl(1, s1_singulationControl)
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
        } catch (e: OperationFailureException) {
            e.printStackTrace()
        }
    }

    /**
     * Sets the default region.
     *
     * @param region The region code.
     * @param name The reader name.
     * @param readerConfig User configuration.
     */
    private fun setDefaultRegion(
        region: String,
        name: String,
        readerConfig: HashMap<String, Any>,
    ) {
        try {
            if (reader != null) {
                val regulatoryConfig = reader!!.Config.regulatoryConfig
                val regions = reader!!.ReaderCapabilities.SupportedRegions
                val len = regions.length()
                for (i in 0 until len) {
                    val regionInfo = regions.getRegionInfo(i)

                    if (region == regionInfo.regionCode) {
                        val channels = arrayOf("865700", "866300", "866900", "867500")
                        regulatoryConfig.region = regionInfo.regionCode
                        regulatoryConfig.setEnabledChannels(channels)
                        regulatoryConfig.setIsHoppingOn(true)
                        reader!!.Config.regulatoryConfig = regulatoryConfig
                    }
                }
                connect(name, readerConfig)
            }
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "setDefaultRegion2 -> InvalidUsageException " + e.vendorMessage)
        } catch (e: OperationFailureException) {
            e.printStackTrace()
            Log.d(
                LOG_TAG, "setDefaultRegion2 -> OperationFailureException " + e.vendorMessage
            )
        }
    }

    /**
     * Retrieves the list of paired RFID readers.
     *
     * @return The list of paired RFID readers.
     */
    fun getAvailableReaderList(): java.util.ArrayList<ReaderDevice> {
        return readers.GetAvailableRFIDReaderList()
    }

    fun startRfidRead() {
        if (reader != null) {
            reader!!.Actions.Inventory.perform()
        } else {
            Log.d(LOG_TAG, "Reader is NULL")
        }
    }

    fun stopRfidRead() {
        if (reader != null) {
            reader!!.Actions.Inventory.stop()
        } else {
            Log.d(LOG_TAG, "Reader is NULL")
        }
    }

    /**
     * Sets the antenna configuration.
     *
     * @param transmitPowerIndex Transmit power index.
     */
    fun setAntennaConfig(transmitPowerIndex: Int) {
        val antennaRfConfig = reader!!.Config.Antennas.getAntennaRfConfig(1)
        antennaRfConfig.setrfModeTableIndex(0)
        antennaRfConfig.tari = 0
        antennaRfConfig.transmitPowerIndex = transmitPowerIndex
        reader!!.Config.Antennas.setAntennaRfConfig(1, antennaRfConfig)
        setAntennaRange()
    }

    /**
     * Sets the antenna range.
     */

    private fun setAntennaRange() {
        var transmitPowerLevelValues = reader!!.ReaderCapabilities.transmitPowerLevelValues
        var antennaRange: IntArray =
            intArrayOf(transmitPowerLevelValues.first(), transmitPowerLevelValues.last())
        ReaderResponse.setAntennaRange(antennaRange)
        tagHandlerEvent.sendEvent(ReaderResponse.toJson())
    }

    private fun getMaxAntennaPower(): Int {
        var transmitPowerLevelValues = reader!!.ReaderCapabilities.transmitPowerLevelValues
        return transmitPowerLevelValues.last() - transmitPowerLevelValues.first()
    }

    /**
     * Sets the dynamic power optimization.
     *
     * @param setEnable Enable dynamic power optimization.
     */
    fun setDynamicPower(setEnable: Boolean) {
        reader!!.Config.dpoState =
            if (setEnable) DYNAMIC_POWER_OPTIMIZATION.ENABLE else DYNAMIC_POWER_OPTIMIZATION.DISABLE
    }

    /**
     * Sets the beeper volume configuration.
     *
     * @param level Beeper volume level.
     */
    fun setBeeperVolumeConfig(level: Int) {
        val config = when (level) {
            0 -> BEEPER_VOLUME.QUIET_BEEP
            1 -> BEEPER_VOLUME.LOW_BEEP
            2 -> BEEPER_VOLUME.MEDIUM_BEEP
            3 -> BEEPER_VOLUME.HIGH_BEEP
            else -> return
        }
        reader!!.Config.beeperVolume = config
    }

    /**
     * Disposes resources.
     */
    fun dispose() {
        try {
            if (reader != null) {
                reader!!.disconnect()
                reader = null
                readers.Dispose()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}


