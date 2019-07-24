package com.weikk.center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.weikk.R;
import com.weikk.utils.Logger;

public class ModeSelectFragment extends Fragment {

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        //将fragment_main_control布局动态加载进来
        View localView = paramLayoutInflater.inflate(R.layout.fragment_mode, paramViewGroup, false);
        Logger.e("mode select", "onCreateView");
        return localView;
    }

    public void onStart() {
        super.onStart();
    }
}
