package com.weikk.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weikk.R;
import com.weikk.utils.Logger;

public class GameControl extends Fragment {

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_main_control, paramViewGroup, false);
        Logger.e("Mainctrl", "onCreateView");
        return localView;
    }

    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Logger.e("Mainctrl", "onActivityCreated");
    }

    public void onStart() {
        super.onStart();
    }
}
