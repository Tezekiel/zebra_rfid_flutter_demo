import android.content.Context
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.TagDataEventHandler
import com.example.movemedical.zebra_rfid_flutter_demo.zebra_rfid_plugin.ZebraRfidReaderSdkPlugin
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.EventChannel

object NativeMethodChannel {
    private const val channelRfid = "borda/zebra_rfid_reader_sdk"
    private const val tagHandlerEvent =  "tagHandlerEvent"
    private const val tagFindingEvent = "tagFindingEvent"
    private const val readTagEvent = "readTagEvent"
    private lateinit var rfidChannel: MethodChannel
    private lateinit var tagHandlerEventChannel: EventChannel
    private lateinit var tagFindingEventChannel: EventChannel
    private lateinit var readTagEventChannel: EventChannel

    fun configureRfidChannel(flutterEngine: FlutterEngine, context: Context) {
        val plugin = ZebraRfidReaderSdkPlugin()
        tagHandlerEventChannel =
            EventChannel(flutterEngine.dartExecutor.binaryMessenger, tagHandlerEvent)
        tagFindingEventChannel =
            EventChannel(flutterEngine.dartExecutor.binaryMessenger, tagFindingEvent)
        readTagEventChannel =
            EventChannel(flutterEngine.dartExecutor.binaryMessenger, readTagEvent)
        val tagHandler = TagDataEventHandler()
        val tagFinding = TagDataEventHandler()
        val readTag = TagDataEventHandler()
        tagHandlerEventChannel.setStreamHandler(tagHandler)
        tagFindingEventChannel.setStreamHandler(tagFinding)
        readTagEventChannel.setStreamHandler(readTag)

        plugin.onAttachedToEngine(context, tagHandler, tagFinding, readTag)
        rfidChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelRfid)
        rfidChannel.setMethodCallHandler(plugin)
    }
}