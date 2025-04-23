package com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.utils

import com.google.gson.Gson

object TagLocationingResponse {
  private var tag: String = ""
  private var distancePercent: Int? = null
  private var isAnyReaderConnected: Boolean = false


  fun setTag(tag: String) {
    TagLocationingResponse.tag = tag
  }

  fun getTag(): String {
    return tag
  }

  fun setAnyReaderConnected(isAnyReaderConnected: Boolean) {
    TagLocationingResponse.isAnyReaderConnected = isAnyReaderConnected
  }

  fun setDistancePercent(distancePercent: Int?) {
    TagLocationingResponse.distancePercent = distancePercent
  }

  fun reset() {
    tag = ""
    isAnyReaderConnected = false
    distancePercent = null
  }

  fun toJson(): String {
    return Gson().toJson(LocationInfo(tag, distancePercent, isAnyReaderConnected))
  }

}