package com.washer.sdk.flutter.recycle.serial;

/**
 * @author okboom
 */
public enum InductionStatusEnum {

    /**
     * 有手术服
     * 02 30 31 30 30 03 43 34
     */
    YES("0230313030034334"),

    /**
     * 无手术服
     * 02 30 30 30 30 03 43 33
     */
    NO("0230303030034333"),
    /**
     * 未知
     */
    UNKNOWN("");

    String data;

    InductionStatusEnum(String data) {
        this.data = data;
    }
}
