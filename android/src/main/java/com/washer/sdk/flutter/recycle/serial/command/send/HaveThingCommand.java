package com.washer.sdk.flutter.recycle.serial.command.send;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.washer.sdk.flutter.recycle.serial.ByteUtils;
import com.washer.sdk.flutter.recycle.serial.command.SendCommand;

public class HaveThingCommand implements SendCommand {

    private long mSendTime;

    @Override
    public int getCmd() {
        return 0;
    }

    @NonNull
    @Override
    public byte[] toBytes() {
        return ByteUtils.hexToByteArr("0230313033303032033539");
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
