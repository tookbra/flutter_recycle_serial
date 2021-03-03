package com.washer.sdk.flutter.recycle.serial;

/**
 * @author okboom
 */
public enum DoorStatusEnum {

    /**
     * 关门未到位（不在初始位置）
     * 02 30 30 30 30 03 43 33
     */
    NOT_CLOSED("0230303030034333"),

    /**
     * 关门到位（在初始位置）
     * 02 35 30 30 30 03 43 38
     */
    CLOSED("0235303030034338"),
    /**
     * 开门未到位
     * 02 30 30 30 30 03 43 33
     */
    NOT_OPENED("0230303030034333"),

    /**
     * 开门到位
     * 02 30 36 30 30 03 43 39
     */
    OPENED("0230363030034339"),
    /**
     * 未知
     */
    UNKNOWN("");

    String data;

    DoorStatusEnum(String data) {
        this.data = data;
    }
}
