package com.weikk.ble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
import com.weikk.ble.bluetoothspp.BluetoothSPP.AutoConnectionListener;
import com.weikk.ble.bluetoothspp.BluetoothSPP.BluetoothConnectionListener;
import com.weikk.ble.bluetoothspp.BluetoothSPP.BluetoothStateListener;
import com.weikk.ble.bluetoothspp.BluetoothState;
import com.weikk.ble.bluetoothspp.DeviceList;

import java.util.ArrayList;

public class BleActivity extends Activity {
    BluetoothSPP bt;

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
                bt.startService(BluetoothState.DEVICE_OTHER);
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
         * 断开按钮
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
                alertDialog.dismiss();
            }
        });
    }
}
