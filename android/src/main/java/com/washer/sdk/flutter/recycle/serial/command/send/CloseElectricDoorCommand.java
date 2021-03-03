package com.washer.sdk.flutter.recycle.serial.command.send;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.washer.sdk.flutter.recycle.serial.ByteUtils;
import com.washer.sdk.flutter.recycle.serial.Protocol;
import com.washer.sdk.flutter.recycle.serial.command.SendCommand;

public class CloseElectricDoorCommand implements SendCommand {

    private long mSendTime;

    @Override
    public int getCmd() {
        return 0;
    }

    @NonNull
    @Override
    public byte[] toBytes() {
        return ByteUtils.hexToByteArr("023131303030303231303030033138");
    }

    @Override
    public long getSendTime() {
        return mSendTime;
    }

    @Override
    public void updateSendTime() {
        mSendTime = SystemClock.elapsedRealtime();
    }

    @Override
    public long timeout() {
        return 0;
    }
}
