package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils

const val LOG_TAG = "ZEBRA_RFID_READER_SDK"


enum class ConnectionStatus {
    notConnected, disconnected, connecting, connected, failed
}

enum class TriggerMode {
    INVENTORY_PERFORM, TAG_LOCATIONING_PERFORM
}