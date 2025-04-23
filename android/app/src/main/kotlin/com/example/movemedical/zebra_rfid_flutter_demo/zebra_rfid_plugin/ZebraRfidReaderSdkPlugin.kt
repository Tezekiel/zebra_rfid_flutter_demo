package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin

import android.content.Context
import android.util.Log
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.BordaReaderDevice
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.ConnectionStatus
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils.LOG_TAG
import com.google.gson.Gson
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel

/** com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.ZebraRfidReaderSdkPlugin */
class ZebraRfidReaderSdkPlugin : MethodCallHandler {
    private lateinit var connectionHelper: ZebraConnectionHelper

    fun onAttachedToEngine(
        context: Context,
        tagDataEventHandler: TagDataEventHandler,
        tagFindingEventHandler: TagDataEventHandler,
        readTagEventHandler: TagDataEventHandler
    ) {
        connectionHelper =
            ZebraConnectionHelper(
                context,
                tagDataEventHandler,
                tagFindingEventHandler,
                readTagEventHandler
            )
    }

    override fun onMethodCall(call: MethodCall, result: Result) =
        when (call.method) {
            "connect" -> {
                Log.d(LOG_TAG, "connect called")
                val name = call.argument<String>("name")!!
                val readerConfig = call.argument<HashMap<String, Any>>("readerConfig")!!
                Log.d(LOG_TAG, "will try to connect to -> $name")
                Log.d(LOG_TAG, "USER CONFIG -> $readerConfig")
                connectionHelper.connect(name, readerConfig)
            }

            "disconnect" -> {
                Log.d(LOG_TAG, "disconnect called")
                connectionHelper.dispose()
            }

            "setAntennaPower" -> {
                val transmitPowerIndex = call.argument<Int>("transmitPowerIndex")!!
                connectionHelper.setAntennaConfig(transmitPowerIndex)
            }

            "setDynamicPower" -> {
                val isEnable = call.argument<Boolean>("isEnable")!!
                connectionHelper.setDynamicPower(isEnable)
            }

            "setBeeperVolume" -> {
                val level = call.argument<Int>("level")!!
                connectionHelper.setBeeperVolumeConfig(level)
            }

            "findTheTag" -> {
                val tag = call.argument<String>("tag")!!
                Log.d("ENGIN", "findTheTag called with tag -> $tag")
                connectionHelper.findTheTag(tag)
            }

            "stopFindingTheTag" -> {
                connectionHelper.stopFindingTheTag()
            }

            "getAvailableReaderList" -> {
                Log.d(LOG_TAG, "getAvailableReaderList called")
                val readers = connectionHelper.getAvailableReaderList()
                val dataList = mutableListOf<BordaReaderDevice>()
                for (reader in readers) {
                    val device = BordaReaderDevice(
                        ConnectionStatus.notConnected,
                        reader.name.toString(),
                        null,
                        null
                    )
                    dataList.add(device)
                }
                result.success(Gson().toJson(dataList))
            }

            "startInventory" -> {
                Log.d(LOG_TAG, "startRfidRead called")
                connectionHelper.startRfidRead()
            }

            "stopInventory" -> {
                Log.d(LOG_TAG, "stopRfidRead called")
                connectionHelper.stopRfidRead()
            }

            else -> result.notImplemented()
        }
}
