package com.weikk.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.weikk.R;
import com.weikk.utils.Logger;

public class CenterActivity extends FragmentActivity implements View.OnClickListener{
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
    private ControlFragment frage_control = null;
    private ModeSelectFragment frage_mode = null;
    private SensorFragment frage_sensor = null;

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
                break;
            case R.id.tab02:
                setTabSelecttion(2);
                break;
            case R.id.tab03:
                setTabSelecttion(3);
                break;
            case R.id.tab04:
                setTabSelecttion(4);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
//            Intent intent = new Intent(Intent.ACTION_MAIN);   //返回主页，但是AndroidManifest.xml中设置了filter过滤该命令
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     */

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
        /**
         * 返回按钮
         */
        final Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
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

        switch (paramInt) {
            case 1:{
                imageView01.setImageResource(R.drawable.tab_control_press);
                if (frage_control == null) {
                    frage_control = new ControlFragment();
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_control);
                } else {
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_control);
                }
                break;
            }
            case 2:{
                imageView02.setImageResource(R.drawable.tab_sensor_press);
                if (frage_sensor == null) {
                    frage_sensor = new SensorFragment();
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_sensor);
                } else {
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_sensor);
                }
                break;
            }
            case 3:{
                imageView03.setImageResource(R.drawable.tab_select_press);
                if (frage_mode == null) {
                    frage_mode = new ModeSelectFragment();
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_mode);
                } else {
                    localFragmentTransaction.replace(R.id.fragment_ui, frage_mode);
                }
                break;
            }
            case 4:{
                imageView04.setImageResource(R.drawable.tab_about_press);
                if (frage_control == null) {
                    frage_control = new ControlFragment();
                    localFragmentTransaction.show(frage_control);
                } else {
                    localFragmentTransaction.show(frage_control);
                }
                break;
            }
        }
        localFragmentTransaction.commit();
    }
}
