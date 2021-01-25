part of flutter_recycle_serial;

class RecycleSerial {
  static Future<String> get platformVersion =>
      FlutterCardSerial._methodChannel.invokeMethod('getPlatformVersion');

  /// 打开连接
  static Future<bool> connect(
      String path, int baudRate) async {
    return FlutterCardSerial._methodChannel.invokeMethod("connect", {"path": path, "baudRate": baudRate});
  }

  /// 断开连接
  static Future<bool> disconnect() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('disconnect');
  }

  /// 电动门开门控制
  static Future<bool> openElectricDoor() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('openElectricDoor');
  }

  /// 电动门关门控制
  static Future<bool> closeElectricDoor() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('closeElectricDoor');
  }

  /// 电动门开门或关门停止控制
  static Future<bool> stopElectricDoor() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('stopElectricDoor');
  }

  /// 初始位置（关门）状态查询指令
  static Future<bool> isDoorClosed() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('isDoorClosed');
  }


  /// 初始位置（开门）状态查询指令
  static Future<bool> isDoorOpened() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('isDoorOpened');
  }

  /// 开电控锁
  static Future<bool> openLock() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('openLock');
  }

  /// 电控锁开关锁查询指令
  static Future<bool> isLockOpened() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('isLockOpened');
  }

  /// 有无手术服查询指令
  static Future<bool> isHaveThing() async {
    return await FlutterCardSerial._methodChannel.invokeMethod('isHaveThing');
  }

}