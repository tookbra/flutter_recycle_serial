package com.washer.sdk.flutter.recycle.serial;

/**
 * @author okboom
 */
public enum LockStatusEnum {

    /**
     * 开锁状态
     * 02 30 31 30 30 03 43 34
     */
    OPENED("0230313030034334"),

    /**
     * 关锁状态
     * 02 30 30 30 30 03 43 33
     */
    CLOSED("0230303030034333"),
    /**
     * 未知
     */
    UNKNOWN("");

    String data;

    LockStatusEnum(String data) {
        this.data = data;
    }
}
