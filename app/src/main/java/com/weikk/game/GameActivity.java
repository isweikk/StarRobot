package com.weikk.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.weikk.MainActivity;
import com.weikk.R;
import com.weikk.ble.BleActivity;
import com.weikk.utils.Logger;

public class GameActivity extends FragmentActivity implements View.OnClickListener{
    private ImageView imageView01;
    private ImageView imageView02;
    private ImageView imageView03;
    private ImageView imageView04;
    Intent intent;
    private LinearLayout layoutTabControl;
    private LinearLayout layoutTabSensor;
    private LinearLayout layoutTabSelect;
    private LinearLayout layoutTabAbout;

    private FragmentManager fragmentManager = null;
    private GameControl frage_control = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fragmentManager = getSupportFragmentManager();

        initView();
        initEvent();
        setTabSelecttion(1);
    }

    @Override
    public void onClick(View paramView) {
        switch (paramView.getId()){
            case R.id.tab01:
                setTabSelecttion(1);
                Logger.d("tab 01");
                break;
            case R.id.tab02:
                setTabSelecttion(2);
                Logger.d("tab 02");
                break;
            case R.id.tab03:
                setTabSelecttion(3);
                Logger.d("tab 03");
                break;
            case R.id.tab04:
                setTabSelecttion(4);
                Logger.d("tab 04");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        layoutTabControl = (LinearLayout) findViewById(R.id.tab01);
        layoutTabSensor = (LinearLayout) findViewById(R.id.tab02);
        layoutTabSelect = (LinearLayout) findViewById(R.id.tab03);
        layoutTabAbout = (LinearLayout) findViewById(R.id.tab04);
        imageView01 = (ImageView) findViewById(R.id.tabImage01);
        imageView02 = (ImageView) findViewById(R.id.tabImage02);
        imageView03 = (ImageView) findViewById(R.id.tabImage03);
        imageView04 = (ImageView) findViewById(R.id.tabImage04);
    }

    private void clearImageView() {
        imageView01.setImageResource(R.drawable.tab_control);
        imageView02.setImageResource(R.drawable.tab_sensor);
        imageView03.setImageResource(R.drawable.tab_select);
        imageView04.setImageResource(R.drawable.tab_about);
    }

    private void initEvent() {
        layoutTabControl.setOnClickListener(this);
        layoutTabSensor.setOnClickListener(this);
        layoutTabSelect.setOnClickListener(this);
        layoutTabAbout.setOnClickListener(this);
    }

//    private void hideFragments(FragmentTransaction paramFragmentTransaction) {
//        if (frage_control != null)
//            paramFragmentTransaction.hide(frage_control);
//        if (frage_sensor != null)
//            paramFragmentTransaction.hide(frage_sensor);
//        if (frage_select != null)
//            paramFragmentTransaction.hide(frage_select);
//        if (frage_about != null)
//            paramFragmentTransaction.hide(frage_about);
//    }

    public void setTabSelecttion(int paramInt) {
        clearImageView();
        FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
        //hideFragments(localFragmentTransaction);
        localFragmentTransaction.commit();

        switch (paramInt) {
            case 1:{
                imageView01.setImageResource(R.drawable.tab_control_press);
                if (frage_control == null) {
                    frage_control = new GameControl();
                    localFragmentTransaction.show(frage_control);
                } else {
                    localFragmentTransaction.show(frage_control);
                }
                break;
            }
            case 2:{
                imageView02.setImageResource(R.drawable.tab_sensor_press);
                if (frage_control == null) {
                    frage_control = new GameControl();
                    localFragmentTransaction.show(frage_control);
                } else {
                    localFragmentTransaction.show(frage_control);
                }
                break;
            }
            case 3:{
                imageView03.setImageResource(R.drawable.tab_select_press);
                if (frage_control == null) {
                    frage_control = new GameControl();
                    localFragmentTransaction.show(frage_control);
                } else {
                    localFragmentTransaction.show(frage_control);
                }
                break;
            }
            case 4:{
                imageView04.setImageResource(R.drawable.tab_about_press);
                if (frage_control == null) {
                    frage_control = new GameControl();
                    localFragmentTransaction.show(frage_control);
                } else {
                    localFragmentTransaction.show(frage_control);
                }
                break;
            }

        }

    }
}
