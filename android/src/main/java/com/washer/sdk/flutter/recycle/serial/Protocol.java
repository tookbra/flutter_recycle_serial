package com.washer.sdk.flutter.recycle.serial;

/**
 * 协议
 */
public interface Protocol {

    /**
     * 电动门开门控制
     *
     * <p>
     *     02 31 31 30 30 30 30 32 30 31 30 30 03 31 38
     */
    byte[] OPEN_ELECTRIC_DOOR = ByteUtils.hexToByteArr("023131303030303230313030033138");


    /**
     * 电动门关门控制
     * <p>
     *     02 31 31 30 30 30 30 32 31 30 30 30 03 31 38
     */
    byte[] CLOSE_ELECTRIC_DOOR = ByteUtils.hexToByteArr("023131303030303231303030033138");

    /**
     * 电动门关门控制
     * <p>
     *     02 31 31 30 30 30 30 32 30 30 30 30 03 31 37
     */
    byte[] STOP_ELECTRIC_DOOR = ByteUtils.hexToByteArr("023131303030303230303030033137");

    /**
     * 初始位置（关门）状态查询指令
     * <p>
     *     02 30 31 30 33 32 30 32 03 35 42
     */
    byte[] IS_DOOR_CLOSED = ByteUtils.hexToByteArr("0230313033323032033542");

    /**
     * 开电控锁
     * <p>
     *     02 31 31 30 30 32 30 32 30 31 30 30 03 31 42
     */
    byte[] OPEN_LOCK = ByteUtils.hexToByteArr("023131303032303230313030033142");

    /**
     * 电控锁开关锁查询指令
     * <p>
     *     02 30 31 30 32 30 30 32 03 35 38
     */
    byte[] IS_LOCK_OPENED = ByteUtils.hexToByteArr("0230313032303032033538");

    /**
     * 电控锁开关锁查询指令
     * <p>
     *     02 30 31 30 33 30 30 32 03 35 39
     */
    byte[] IS_HAVE_THING = ByteUtils.hexToByteArr("0230313033303032033539");

}
