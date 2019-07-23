package com.weikk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.weikk.ble.BleActivity;
import com.weikk.ble.bluetoothspp.BluetoothState;
import com.weikk.ble.bluetoothspp.DeviceList;
import com.weikk.game.GameActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 切换到蓝牙界面
         */
        final Button btnSwitchBle = (Button)findViewById(R.id.btnSwitchBle);
        btnSwitchBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, BleActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        /**
         * 切换到WiFi界面
         */
        final Button btnSwitchWifi = (Button)findViewById(R.id.btnSwitchWifi);
        btnSwitchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
//        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        super.onResume();
    }
}
