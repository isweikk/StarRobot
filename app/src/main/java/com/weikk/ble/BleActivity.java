package com.weikk.ble;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.weikk.R;
import com.weikk.ble.bluetoothspp.BluetoothSPP;
import com.weikk.ble.bluetoothspp.BluetoothSPP.OnDataReceivedListener;
import com.weikk.ble.bluetoothspp.BluetoothSPP.BluetoothConnectionListener;
import com.weikk.ble.bluetoothspp.BluetoothSPP.BluetoothStateListener;
import com.weikk.ble.bluetoothspp.BluetoothState;
import com.weikk.ble.bluetoothspp.DeviceList;
import com.weikk.widget.RockerView;

import java.util.ArrayList;

public class BleActivity extends Activity {
    BluetoothSPP bt;
    private RockerView rockerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.i("Check", "Length : " + data.length);
                Log.i("Check", "Message : " + message);
            }
        });

        /**
         * 返回按钮
         */
        final Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        /**
         * 连接按钮
         */
        final Button btnDevice = (Button)findViewById(R.id.btnBleDevice);
        btnDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    getBleInfoDialog();
                } else {
                    Intent intent = new Intent(BleActivity.this, DeviceList.class);
                    intent.putExtra("ble_window_title", "Devices");
                    intent.putExtra("ble_btn_scan", "Search");
                    intent.putExtra("ble_msg_no_device", "No device");
                    intent.putExtra("ble_msg_scanning", "Scanning");
                    intent.putExtra("ble_select_device", "Select");
//                    intent.putExtra("bleListDevices", R.id.bleListDevices);
//                    intent.putExtra("bleDeviceInfo", R.id.bleDeviceInfo);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        /**
         * 自动连接结果
         */
        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });

        /**
         * 连接结果处理，变化device按钮，连接成功显示设备名
         */
        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                btnDevice.setText("+" + name);
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                btnDevice.setText("Device");
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 连接状态变化，提示信息
         */
        bt.setBluetoothStateListener(new BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if(state == BluetoothState.STATE_CONNECTED)
//                    btnDevice.setText(device.getName());
                    Log.i("Check", "State : Connected");
                else if(state == BluetoothState.STATE_CONNECTING)
                    Log.i("Check", "State : Connecting");
                else if(state == BluetoothState.STATE_LISTEN)
                    Log.i("Check", "State : Listen");
                else if(state == BluetoothState.STATE_NONE)
                    Log.i("Check", "State : None");
            }
        });

        /**
         * 显示收到的消息
         */
        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
//        Button btnScan = (Button)findViewById(R.id.btnBleScan);
//        btnScan.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
//                    getBleInfoDialog();
//                } else {
//                    getBleListDialog();
//                }
//            }
//        });

        /**
         * Rocker创建摇杆控件
         */
        rockerView = (RockerView) findViewById(R.id.rockerView);
        rockerView.setRockerListener(new rockerEven());
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);//非Android的蓝牙，要选择other，UUID会不同
                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setup() {
        Button btnSend = (Button)findViewById(R.id.btnBleSend);
        btnSend.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                bt.send("Text", true);
            }
        });
//        bt.autoConnect("IOIO"); //自动连接开启
    }

    /**
     * 系统返回键响应，返回上一页面
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void getBleListDialog(){
        ArrayList<String> list = null;
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        Context context = BleActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.ble_device_list, (ViewGroup)findViewById(R.id.layout_ble_list));
        ListView myListView = (ListView) layout.findViewById(R.id.bleListView);
        //DeviceList adapter = new DeviceList(context, list);
        //myListView.setAdapter(adapter);
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

    }

    protected void getBleInfoDialog(){
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        Context context = BleActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.ble_device_info, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
        /**
         * 断开按钮，因为该按钮在dialog之中，必须要加上View对象layout，才能找到对应的接口
         * 如果不写，会在父窗口查找接口，结果报错。
         */
        Button btnDisconnect = (Button)layout.findViewById(R.id.btnBleDisconnect);
        btnDisconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Toast.makeText(BleActivity.this
                            , "No connection yet."
                            , Toast.LENGTH_SHORT).show();
                }
                //点击自定义view的按钮，无法退出弹窗，需要加上关闭弹窗方法；用dialog类的方法生成按钮，则点击即可退出。
                alertDialog.dismiss();
            }
        });
    }

    //rocker
    class rockerEven implements com.weikk.widget.OnRockerListener {

        public void reportPosition(float x, float y) {

        }
        public void onRocker(float angle, float level) {
            // Check that we're actually connected before trying anything
            if (bt.getServiceState() != BluetoothState.STATE_CONNECTED) {
//				Toast.makeText(BluetoothChat.this, R.string.not_connected,
//						Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] value = new byte[1];
            Direction direction = get8Direction(angle);
            switch (direction) {
                case DIRECTION_UP: value[0] = 0x1;break;
                case DIRECTION_UP_RIGHT: value[0] = 0x2;break;
                case DIRECTION_RIGHT: value[0] = 0x3;break;
                case DIRECTION_DOWN_RIGHT: value[0] = 0x4;break;
                case DIRECTION_DOWN: value[0] = 0x5;break;
                case DIRECTION_DOWN_LEFT: value[0] = 0x6;break;
                case DIRECTION_LEFT: value[0] = 0x7;break;
                case DIRECTION_UP_LEFT: value[0] = 0x8;break;
                default: value[0] = 0;break;
            }
            if (level == 0) {
                value[0] = 0;
            }
            bt.send(value, false);
        }
    }

    // 360°平分8份的边缘角度
    private static final double ANGLE_8D_OF_0P = 22.5;
    private static final double ANGLE_8D_OF_1P = 67.5;
    private static final double ANGLE_8D_OF_2P = 112.5;
    private static final double ANGLE_8D_OF_3P = 157.5;
    private static final double ANGLE_8D_OF_4P = 202.5;
    private static final double ANGLE_8D_OF_5P = 247.5;
    private static final double ANGLE_8D_OF_6P = 292.5;
    private static final double ANGLE_8D_OF_7P = 337.5;

    /**
     * 方向
     */
    public enum Direction {
        DIRECTION_LEFT, // 左
        DIRECTION_RIGHT, // 右
        DIRECTION_UP, // 上
        DIRECTION_DOWN, // 下
        DIRECTION_UP_LEFT, // 左上
        DIRECTION_UP_RIGHT, // 右上
        DIRECTION_DOWN_LEFT, // 左下
        DIRECTION_DOWN_RIGHT, // 右下
        DIRECTION_CENTER // 中间
    }

    /**
     * 获取方向区域
     * @param angle
     * @return
     */
    private Direction get8Direction(float angle) {
        if (0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && 360 > angle) {
            // 右
            return Direction.DIRECTION_RIGHT;
        } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle) {
            // 右下
            return Direction.DIRECTION_DOWN_RIGHT;
        } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle) {
            // 下
            return Direction.DIRECTION_DOWN;
        } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle) {
            // 左下
            return Direction.DIRECTION_DOWN_LEFT;
        } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle) {
            // 左
            return Direction.DIRECTION_LEFT;
        } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle) {
            // 左上
            return Direction.DIRECTION_UP_LEFT;
        } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle) {
            // 上
            return Direction.DIRECTION_UP;
        } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle) {
            // 右上
            return Direction.DIRECTION_UP_RIGHT;
        }
        return Direction.DIRECTION_CENTER;
    }
}
