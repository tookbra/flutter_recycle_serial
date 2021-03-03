package com.washer.sdk.flutter.recycle.serial;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.serialport.SerialPort;

import androidx.annotation.Nullable;

import com.licheedev.serialworker.core.Callback;
import com.washer.sdk.flutter.recycle.serial.command.RecvBase;
import com.washer.sdk.flutter.recycle.serial.command.RecvCommand;
import com.washer.sdk.flutter.recycle.serial.command.SendCommand;

public class SerialManager {

    private static final String TAG = "SerialManager";

    private static String CONTROL_SERIAL = "/dev/ttyS4";

    private static int CONTROL_BAUD_RATE = 9600;

    private static volatile SerialManager serialManager = null;

    private final ControlWorker controlWorker;

    private final HandlerThread dispatchThread;

    private final Handler dispatchThreadHandler;

    /**
     * [单例]获取串口管理器
     *
     * @return
     */
    public static SerialManager get() {

        SerialManager manager = serialManager;
        if (manager == null) {
            synchronized (SerialManager.class) {
                manager = serialManager;
                if (manager == null) {
                    manager = new SerialManager();
                    serialManager = manager;
                }
            }
        }
        return manager;
    }

    private SerialManager() {

        dispatchThread = new HandlerThread("serial-dispatch-thread");
        dispatchThread.start();
        dispatchThreadHandler = new Handler(dispatchThread.getLooper());

        controlWorker = new ControlWorker(dispatchThreadHandler);
        controlWorker.setDevice(CONTROL_SERIAL, CONTROL_BAUD_RATE); // 串口地址，波特率
        controlWorker.setParams(7, 2, 1); // 8数据位，奇校验，1停止位

        controlWorker.enableLog(true, true);
    }

    public void initDevice() throws Exception {
        SerialPort.setSuPath("/system/xbin/su");
        controlWorker.openSerial();
    }

    /**
     * 释放资源
     */
    public synchronized void release() {

        controlWorker.release();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dispatchThread.quitSafely();
        } else {
            dispatchThread.quit();
        }
        serialManager = null;
    }


    /**
     *
     * @param command
     * @param callback
     */
    public void sendCommand(SendCommand command, @Nullable Callback<RecvBase> callback) {
        controlWorker.send(command, RecvBase.class, callback);
    }
}
