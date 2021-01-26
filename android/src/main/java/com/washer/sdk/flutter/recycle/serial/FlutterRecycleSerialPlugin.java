package com.washer.sdk.flutter.recycle.serial;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.washer.sdk.recycle.RecycleController;
import com.washer.sdk.recycle.RecycleException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterRecycleSerialPlugin */
public class FlutterRecycleSerialPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private static final String TAG = "FlutterRecycleSerialPlugin";

  private static final String NAMESPACE = "flutter_recycle_serial";

  private RecycleController recycleController = null;

  private boolean isClient = false;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), NAMESPACE);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "connect":
        if (!call.hasArgument("path")) {
          result.error("invalid_argument", "argument 'path' not found", null);
          break;
        }
        if (!call.hasArgument("baudRate")) {
          result.error("invalid_argument", "argument 'baudRate' not found", null);
          break;
        }
        String path = call.argument("path");
        Integer baudRate = call.argument("baudRate");

        try {
          recycleController = RecycleController.getInstance(path, baudRate);
          isClient = true;
          result.success(true);
        } catch (RecycleException e) {
          result.success(false);
        }
        break;
      case "openElectricDoor":
        result.success(recycleController.openElectricDoor());
        break;
      case "closeElectricDoor":
        result.success(recycleController.closeElectricDoor());
        break;
      case "stopElectricDoor":
        result.success(recycleController.stopElectricDoor());
        break;
      case "isDoorClosed":
        result.success(recycleController.isDoorClosed());
        break;
      case "isDoorOpened":
        result.success(recycleController.isDoorOpened());
        break;
      case "openLock":
        result.success(recycleController.openLock());
        break;
      case "isLockOpened":
        result.success(recycleController.isLockOpened());
        break;
      case "isHaveThing":
        result.success(recycleController.isHaveThing());
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
