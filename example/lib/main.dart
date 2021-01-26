import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_recycle_serial/flutter_recycle_serial.dart';
import 'package:oktoast/oktoast.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await RecycleSerial.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> connect() async {
    RecycleSerial.connect("/dev/ttyS0", 9600).then((_connection) {
      showToast(_connection.toString());
    }).catchError((error) {
      print(error);
    });
  }

  @override
  Widget build(BuildContext context) {
    return OKToast(
        child: MaterialApp(
          home: Scaffold(
              appBar: AppBar(
                title: const Text('Plugin example app'),
              ),
              body: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  FlatButton(
                    child: Text('获取版本号'),
                    color: Colors.blue[200],
                    onPressed: (){
                      initPlatformState();
                    },
                  ),
                  Row(
                    children: <Widget>[
                      FlatButton(
                        child: Text('连接'),
                        onPressed: (){
                          connect();
                        },
                        color: Colors.blue[200],
                      ),
                    ],
                  ),
                  Row(
                    children: <Widget>[
                      FlatButton(
                        child: Text('电动门开门控制'),
                        onPressed: (){
                          RecycleSerial.openElectricDoor().then((_connection) {
                            showToast(_connection.toString());
                          }).catchError((error) {
                            print(error);
                          });
                        },
                        color: Colors.blue[200],
                      ),
                      FlatButton(
                        child: Text('电动门关门控制'),
                        onPressed: (){
                          RecycleSerial.closeElectricDoor().then((_connection) {
                            showToast(_connection.toString());
                          }).catchError((error) {
                            print(error);
                          });
                        },
                        color: Colors.blue[200],
                      ),
                    ],
                  ),
                  FlatButton(
                    child: Text('电动门开门或关门停止控制'),
                    onPressed: (){
                      RecycleSerial.stopElectricDoor().then((_connection) {
                        showToast(_connection.toString());
                      }).catchError((error) {
                        print(error);
                      });
                    },
                    color: Colors.blue[200],
                  ),
                  FlatButton(
                    child: Text('初始位置（关门）状态'),
                    onPressed: (){
                      RecycleSerial.isDoorClosed().then((_connection) {
                        showToast(_connection.toString());
                      }).catchError((error) {
                        print(error);
                      });
                    },
                    color: Colors.blue[200],
                  ),
                  FlatButton(
                    child: Text('初始位置（开门）状态'),
                    onPressed: (){
                      RecycleSerial.isDoorOpened().then((_connection) {
                        showToast(_connection.toString());
                      }).catchError((error) {
                        print(error);
                      });
                    },
                    color: Colors.blue[200],
                  ),
                  Row(
                    children: [
                      FlatButton(
                        child: Text('开电控锁'),
                        onPressed: (){
                          RecycleSerial.openLock().then((_connection) {
                            showToast(_connection.toString());
                          }).catchError((error) {
                            print(error);
                          });
                        },
                        color: Colors.blue[200],
                      ),
                      FlatButton(
                        child: Text('电控锁开关锁'),
                        onPressed: (){
                          RecycleSerial.isLockOpened().then((_connection) {
                            showToast(_connection.toString());
                          }).catchError((error) {
                            print(error);
                          });
                        },
                        color: Colors.blue[200],
                      ),
                    ],
                  ),
                  FlatButton(
                    child: Text('有无手术服'),
                    onPressed: (){
                      RecycleSerial.isHaveThing().then((_connection) {
                        showToast(_connection.toString());
                      }).catchError((error) {
                        print(error);
                      });
                    },
                    color: Colors.blue[200],
                  ),
                ],
              )
          ),
        )
    );
  }
}
