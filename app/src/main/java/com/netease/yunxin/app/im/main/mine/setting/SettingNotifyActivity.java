/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.app.im.main.mine.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.netease.yunxin.app.im.databinding.ActivityMineSettingNotifyBinding;
import com.netease.yunxin.kit.common.ui.activities.BaseActivity;
import com.netease.yunxin.kit.common.ui.viewmodel.FetchResult;
import com.netease.yunxin.kit.common.ui.viewmodel.LoadStatus;

public class SettingNotifyActivity extends BaseActivity {

    private ActivityMineSettingNotifyBinding viewBinding;
    private SettingNotifyViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeStatusBarColor(com.netease.yunxin.kit.common.ui.R.color.color_e9eff5);
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMineSettingNotifyBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(SettingNotifyViewModel.class);
        setContentView(viewBinding.getRoot());

        viewModel.getNotifyDetailLiveData().observe(this, result -> {
            if (result.getLoadStatus() == LoadStatus.Error && result.getData() != null){
                viewBinding.notifyShowInfoSc.setChecked(!result.getData());
            }
        });

        viewModel.getToggleNotificationLiveData().observe(this, result -> {
            if (result.getLoadStatus() == LoadStatus.Error && result.getData() != null){
                viewBinding.notifySc.setChecked(!result.getData());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        viewBinding.notifySc.setChecked(viewModel.getToggleNotification());
        viewBinding.notifySc.setOnClickListener( v -> viewModel.setToggleNotification(viewBinding.notifySc.isChecked()));
        viewBinding.notifyRingSc.setChecked(viewModel.getRingToggle());
        viewBinding.notifyRingSc.setOnClickListener( v -> viewModel.setRingToggle(viewBinding.notifyRingSc.isChecked()));
        viewBinding.notifyShakeSc.setChecked(viewModel.getVibrateToggle());
        viewBinding.notifyShakeSc.setOnClickListener( v -> viewModel.setVibrateToggle(viewBinding.notifyShakeSc.isChecked()));
        viewBinding.pushSyncSc.setChecked(viewModel.getMultiPortPushOpen());
        viewBinding.pushSyncSc.setOnClickListener( v -> viewModel.setMultiPortPushOpen(viewBinding.pushSyncSc.isChecked()));
        viewBinding.notifyShowInfoSc.setChecked(viewModel.getPushShowNoDetail());
        viewBinding.notifyShowInfoSc.setOnClickListener( v -> viewModel.setPushShowNoDetail(viewBinding.notifyShowInfoSc.isChecked()));
        viewBinding.settingTitleBar.setOnBackIconClickListener(v -> onBackPressed());
    }
}
