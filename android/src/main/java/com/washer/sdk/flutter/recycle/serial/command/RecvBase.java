package com.washer.sdk.flutter.recycle.serial.command;

import android.os.SystemClock;
import android.util.Log;

import com.licheedev.hwutils.ByteUtil;
import com.washer.sdk.flutter.recycle.serial.ByteUtils;

import java.util.Arrays;


public class RecvBase implements RecvCommand {

    private final byte[] mAllPack;
    private final long mRecvTime;

    public RecvBase(byte[] allPack) {
        mAllPack = allPack;
        mRecvTime = SystemClock.uptimeMillis();
    }

    @Override
    public long getRecvTime() {
        return mRecvTime;
    }

    @Override
    public byte[] getAllPack() {
        return mAllPack;
    }

    @Override
    public String toString() {
        return "数据=" + ByteUtils.bytes2HexStr(mAllPack);
    }

    public String getResult() {
        return ByteUtil.bytes2HexStr(mAllPack);
    }
}
