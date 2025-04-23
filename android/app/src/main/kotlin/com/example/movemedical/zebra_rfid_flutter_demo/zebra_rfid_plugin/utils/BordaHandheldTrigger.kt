package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils

object  BordaHandheldTrigger {
    private var mode: TriggerMode = TriggerMode.INVENTORY_PERFORM

    fun setMode(triggerMode: TriggerMode) {
        mode = triggerMode
    }

    fun getMode(): TriggerMode {
        return mode
    }
}