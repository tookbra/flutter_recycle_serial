package com.washer.sdk.flutter.recycle.serial;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.licheedev.serialworker.core.DataReceiver;
import com.licheedev.serialworker.core.ValidData;
import com.washer.sdk.flutter.recycle.serial.command.RecvBase;
import com.washer.sdk.flutter.recycle.serial.command.RecvCommand;

import java.nio.ByteBuffer;

public class ControlDataReceiver implements DataReceiver<RecvCommand> {

    private final ByteBuffer mByteBuffer;

    public ControlDataReceiver() {
        mByteBuffer = ByteBuffer.allocate(2048);
        mByteBuffer.clear();
    }

    @Override
    public void onReceive(@NonNull ValidData validData, @NonNull byte[] bytes, int offset, int length) {
        try {
            mByteBuffer.put(bytes, 0, length);
            mByteBuffer.flip();

            byte[] allPack = new byte[length];
            mByteBuffer.get(allPack);
            validData.add(allPack);
        } finally {
            mByteBuffer.compact();
        }
    }

    @Nullable
    @Override
    public RecvCommand adaptReceive(@NonNull byte[] allPack) {
        RecvCommand recvCommand = new RecvBase(allPack);
        return recvCommand;
    }

    @Override
    public void resetCache() {
        mByteBuffer.clear();
    }
}
