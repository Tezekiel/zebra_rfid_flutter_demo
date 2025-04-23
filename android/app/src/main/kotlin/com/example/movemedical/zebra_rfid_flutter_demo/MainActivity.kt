package com.example.movemedical.zebra_rfid_flutter_demo

import android.content.Intent
import android.os.Bundle
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterFragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    allowRfidWhileCharging()
  }

  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    NativeMethodChannel.configureRfidChannel(flutterEngine, this.baseContext)
  }

  private fun allowRfidWhileCharging() {
    val intent = Intent()
    intent.action = "rfid.intent.action.OPERATE_WHILE_CHARGING"
    intent.putExtra("ON", true)
    sendBroadcast(intent)
  }
}

