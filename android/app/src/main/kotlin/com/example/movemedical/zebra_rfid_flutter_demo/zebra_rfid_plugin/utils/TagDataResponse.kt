package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils

import com.google.gson.Gson

class TagDataResponse {
    private var tagId: String = ""
    private var lastSeenTime: String = ""

    constructor(tagId: String, lastSeenTime: String) {
        this.tagId = tagId
        this.lastSeenTime = lastSeenTime
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}