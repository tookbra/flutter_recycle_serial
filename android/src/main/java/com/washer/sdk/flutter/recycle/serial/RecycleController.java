package com.washer.sdk.flutter.recycle.serial;

//import android.util.Log;
import android.serialport.SerialPort;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author okboom
 * @date 2021/1/23
 * @description
 */
public class RecycleController {

    private final String TAG = RecycleController.class.getSimpleName();

    /**
     * 输出流
     */
    private OutputStream outputStream;
    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 串口路径
     */
    private String devPath;

    /**
     * 波特率
     */
    private int devBaud;

    /**
     * 串口
     */
    private SerialPort serialPort;

    private static RecycleController controller;

    private static final int FLAG_SUCCESS = 0;

    /**
     * 关门未到位（不在初始位置）
     */
    private static final String NOT_CLOSED = "02 30 30 30 30 03 43 33";

    /**
     * 关门到位（在初始位置）
     */
    private static final String CLOSED = "02 35 30 30 30 03 43 38";

    private final ExecutorService mSendExecutor = Executors.newSingleThreadExecutor();

    /**
     * 创建控制器
     * @param path
     * @param baudRate
     * @return
     */
    public static RecycleController getInstance(String path, int baudRate) throws RecycleException {
        if (null == controller) {
            controller = new RecycleController();
            controller.devPath = path;
            controller.devBaud = baudRate;
            controller.onCreate();
        } else if (!controller.devPath.equals(path) || controller.devBaud != baudRate) {
            controller.close();
            controller.devPath = path;
            controller.devBaud = baudRate;
            controller.onCreate();
        }

        return controller;
    }


    /**
     * 初始化
     */
    public void onCreate() throws RecycleException {
        try {
            SerialPort.setSuPath("/system/xbin/su");
            // 7E2(7数据位、偶校验、2停止位)
            SerialPort serialPort = SerialPort.newBuilder(this.devPath, this.devBaud) // 串口地址地址，波特率
                    .parity(2) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                    .dataBits(7) // 数据位,默认8；可选值为5~8
                    .stopBits(1) // 停止位，默认1；1:1位停止位；2:2位停止位
                    .build();
            this.serialPort = serialPort;
            this.outputStream = this.serialPort.getOutputStream();
            this.inputStream = this.serialPort.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecycleException("初始化串口错误");
        }
    }

    /**
     * 关闭当前
     */
    public void close() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (this.serialPort != null) {
            this.serialPort.close();
            this.serialPort = null;
        }

    }

    private int read(byte[] wBuffer, byte[] data) {
        byte[] mBuffer = new byte[512];

        if (this.outputStream == null) {
//            throw new IOException("can not found out stream");
            return 1;
        }

        try {
            this.outputStream.write(wBuffer);
            this.outputStream.flush();
            StringBuilder str = new StringBuilder(">> ");
            str.append(ByteUtils.bytes2HexStr(wBuffer));
            Log.i(this.TAG, str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (this.inputStream == null) {
                throw new IOException("can not found input stream");
            } else {
                long startTime;
                long currTime = startTime = System.currentTimeMillis();

                long endTime;
                for(endTime = startTime + 1000L; currTime < endTime; currTime = System.currentTimeMillis()) {
                    if (this.inputStream.available() > 0) {
                        int size = this.inputStream.read(mBuffer);
                        String str = "<< ";

                        for(int i = 0; i < size; ++i) {
                            str = str + String.format("%02X ", mBuffer[i]);
                        }

                        Log.i(this.TAG, str);
                        Log.i(this.TAG, size + "");

                        System.arraycopy(mBuffer, 0, data, 0, size);
                        Log.i(this.TAG, ByteUtils.bytes2HexStr(data));
                        break;
                    }

                    Thread.sleep(10L);
                }
                if (currTime >= endTime) {
                    Log.e(this.TAG, "Receive data timeout error");
                    return 1;
                } else {
                    return 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 电动门开门控制
     *
     * <p>
     *     02 31 31 30 30 30 30 32 30 31 30 30 03 31 38
     * @return true or false
     */
    public boolean openElectricDoor() {
        byte [] data = new byte[1];
        int flag = this.read(ByteUtils.hexToByteArr("023131303030303230313030033138"), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals("60")) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 电动门关门控制
     * <p>
     *     02 31 31 30 30 30 30 32 31 30 30 30 03 31 38
     * @return true or false
     */
    public boolean closeElectricDoor() {
        byte [] data = new byte[1];
        int flag = this.read(ByteUtils.hexToByteArr("023131303030303231303030033138"), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals("60")) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 电动门开门或关门停止控制
     * <p>
     *     02 31 31 30 30 30 30 32 30 30 30 30 03 31 37
     * @return true or false
     */
    public boolean stopElectricDoor() {
        byte [] data = new byte[1];
        int flag = this.read("02 31 31 30 30 30 30 32 30 30 30 30 03 31 37".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals("60")) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 初始位置（关门）状态查询指令
     * <p>
     *     02 30 31 30 33 32 30 32 03 35 42
     * @return true or false
     */
    public boolean isDoorClosed() {
        byte [] data = new byte[512];
        int flag = this.read("02 30 31 30 33 32 30 32 03 35 42".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals(DoorStatusEnum.CLOSED.data)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 初始位置（开门）状态查询指令
     * <p>
     *     02 30 31 30 33 36 30 32 03 35 46
     * @return true or false
     */
    public boolean isDoorOpened() {
        byte [] data = new byte[512];
        int flag = this.read("02 30 31 30 33 32 30 32 03 35 42".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals(DoorStatusEnum.OPENED.data)) {
                return true;
            }
            return false;
        }
        return false;
    }


    /**
     * 开电控锁
     * <p>
     *     02 31 31 30 30 32 30 32 30 31 30 30 03 31 42
     * @return true or false
     */
    public boolean openLock() {
        byte [] data = new byte[1];
        int flag = this.read("02 31 31 30 30 32 30 32 30 31 30 30 03 31 42".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals("60")) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 电控锁开关锁查询指令
     * <p>
     *     02 30 31 30 32 30 30 32 03 35 38
     *     开锁状态返回：02 30 30 30 30 03 43 33
     *     关锁状态返回：02 30 31 30 30 03 43 34
     * @return true or false
     */
    public boolean isLockOpened() {
        byte [] data = new byte[512];
        int flag = this.read("02 30 31 30 33 32 30 32 03 35 42".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals(LockStatusEnum.OPENED.data)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 有无手术服查询指令
     * <p>
     *     02 30 31 30 33 30 30 32 03 35 39
     *     无手术服返回：02 30 30 30 30 03 43 33
     *     有手术服返回：02 30 31 30 30 03 43 34
     * @return true or false
     */
    public boolean isHaveThing() {
        byte [] data = new byte[512];
        int flag = this.read("02 30 31 30 33 30 30 32 03 35 39".getBytes(), data);
        if(flag == FLAG_SUCCESS) {
            if(new String(data).equals(InductionStatusEnum.YES.data)) {
                return true;
            }
            return false;
        }
        return false;
    }


}
