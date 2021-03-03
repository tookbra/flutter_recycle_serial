package com.washer.sdk.flutter.recycle.serial;


import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.licheedev.serialworker.core.DataReceiver;
import com.licheedev.serialworker.worker.RxRs232SerialWorkerX;
import com.washer.sdk.flutter.recycle.serial.command.RecvCommand;
import com.washer.sdk.flutter.recycle.serial.command.SendCommand;

public class ControlWorker extends RxRs232SerialWorkerX<SendCommand, RecvCommand> {

    private final Handler receiverHandler;

    public ControlWorker(@Nullable Handler receiverHandler) {
        this.receiverHandler = receiverHandler;
    }

    @Override
    public boolean isMyResponse(@NonNull SendCommand sendData, @NonNull RecvCommand recvData) {
        return true;
    }

    @Override
    public DataReceiver<RecvCommand> newReceiver() {
        return new ControlDataReceiver();
    }

    @Override
    public void onReceiveData(@NonNull RecvCommand recvData) {
        // 把数据暴露出去
        if (mReceiveCallback != null) {
            if (receiverHandler != null) {
                receiverHandler.post(() -> mReceiveCallback.onReceive(recvData));
            } else {
                mReceiveCallback.onReceive(recvData);
            }
        }
    }

    private ReceiveCallback mReceiveCallback;

    public interface ReceiveCallback {

        void onReceive(RecvCommand recvCommand);
    }

    public void setReceiveCallback(ReceiveCallback receiveCallback) {
        mReceiveCallback = receiveCallback;
    }
}
