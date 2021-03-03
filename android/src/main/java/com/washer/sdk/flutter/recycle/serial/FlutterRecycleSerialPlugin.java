package com.washer.sdk.flutter.recycle.serial;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.licheedev.serialworker.core.Callback;
import com.washer.sdk.flutter.recycle.serial.command.RecvBase;
import com.washer.sdk.flutter.recycle.serial.command.RecvCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.CloseElectricDoorCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.ElectricDoorCloseStatusCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.ElectricDoorOpenStatusCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.HaveThingCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.LockStatusCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.OpenElectricDoorCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.OpenLockCommand;
import com.washer.sdk.flutter.recycle.serial.command.send.StopElectricDoorCommand;

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
    recycleController = new RecycleController();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "connect":
        try {
          SerialManager.get().initDevice();
        } catch (Exception e) {
          e.printStackTrace();
          result.error(e.getMessage(), "", "");
        }
        break;
      case "disConnect":
        SerialManager.get().release();
        break;
      case "openElectricDoor":
        SerialManager.get().sendCommand(new OpenElectricDoorCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals("06")) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("open electric door error", "", "");
          }
        });
        break;
      case "closeElectricDoor":
        SerialManager.get().sendCommand(new CloseElectricDoorCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals("06")) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("close electric door error", "", "");
          }
        });
        break;
      case "stopElectricDoor":
        SerialManager.get().sendCommand(new StopElectricDoorCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals("06")) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("stop electric door error", "", "");
          }
        });
        break;
      case "isDoorClosed":
        SerialManager.get().sendCommand(new ElectricDoorCloseStatusCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals(DoorStatusEnum.CLOSED.data)) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("is door closed error", "", "");
          }
        });
        break;
      case "isDoorOpened":
        SerialManager.get().sendCommand(new ElectricDoorOpenStatusCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals(DoorStatusEnum.OPENED.data)) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("is door opened error", "", "");
          }
        });
        break;
      case "openLock":
        SerialManager.get().sendCommand(new OpenLockCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals("06")) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("open lock error", "", "");
          }
        });
        break;
      case "isLockOpened":
        SerialManager.get().sendCommand(new LockStatusCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals(LockStatusEnum.OPENED.data)) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("is lock opened error", "", "");
          }
        });
        break;
      case "isHaveThing":
        SerialManager.get().sendCommand(new HaveThingCommand(), new Callback<RecvBase>() {
          @Override
          public void onSuccess(@Nullable RecvBase recvBase) {
            if(recvBase.getResult().equals(InductionStatusEnum.YES.data)) {
              result.success(true);
            } else {
              result.success(false);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable tr) {
            result.error("is have thing error", "", "");
          }
        });
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
