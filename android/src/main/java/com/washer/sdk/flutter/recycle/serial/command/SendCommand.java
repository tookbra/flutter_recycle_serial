package com.washer.sdk.flutter.recycle.serial.command;

import com.licheedev.serialworker.core.SendData;

/**
 * 发送的数据
 */

public interface SendCommand extends SendData {

    int getCmd();
}
