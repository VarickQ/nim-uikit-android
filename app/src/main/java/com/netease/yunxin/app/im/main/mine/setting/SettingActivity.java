/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.app.im.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.netease.yunxin.app.im.IMApplication;
import com.netease.yunxin.app.im.databinding.ActivityMineSettingBinding;
import com.netease.yunxin.app.im.welcome.WelcomeActivity;
import com.netease.yunxin.kit.common.ui.activities.BaseActivity;
import com.netease.yunxin.kit.corekit.im.XKitImClient;
import com.netease.yunxin.kit.corekit.im.repo.ConfigRepo;

public class SettingActivity extends BaseActivity {

    private ActivityMineSettingBinding viewBinding;
    private SettingViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeStatusBarColor(com.netease.yunxin.kit.common.ui.R.color.color_e9eff5);
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMineSettingBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        setContentView(viewBinding.getRoot());
        initView();
    }

    private void initView(){
        // delete alias
        viewBinding.friendDeleteSc.setChecked(viewModel.getDeleteAlias());
        viewBinding.friendDeleteSc.setOnClickListener(v -> {
            boolean checked = viewBinding.friendDeleteSc.isChecked();
            viewModel.setDeleteAlias(checked);
        });

        // show read and unread status
        viewBinding.messageReadSc.setChecked(viewModel.getShowReadStatus());
        viewBinding.messageReadSc.setOnClickListener(v -> {
            boolean checked = viewBinding.messageReadSc.isChecked();
            viewModel.setShowReadStatus(checked);
        });

        //audio play mode AUDIO_PLAY_EARPIECE or AUDIO_PLAY_OUTSIDE
        viewBinding.playModeSc.setChecked(viewModel.getAudioPlayMode() == ConfigRepo.AUDIO_PLAY_EARPIECE);
        viewBinding.playModeSc.setOnClickListener(v -> {
            boolean checked = viewBinding.playModeSc.isChecked();
            viewModel.setAudioPlayMode(checked?ConfigRepo.AUDIO_PLAY_EARPIECE:ConfigRepo.AUDIO_PLAY_OUTSIDE);
        });

        viewBinding.notifyFl.setOnClickListener( v -> startActivity(new Intent(SettingActivity.this, SettingNotifyActivity.class)));

        viewBinding.clearFl.setOnClickListener( v -> startActivity(new Intent(SettingActivity.this, ClearCacheActivity.class)));

        viewBinding.tvLogout.setOnClickListener(v -> {
            // logout your own account here
            //...

            XKitImClient.logoutIMWithQChat(new com.netease.yunxin.kit.corekit.im.login.LoginCallback<Void>() {
                @Override
                public void onError(int errorCode, @NonNull String errorMsg) {
                    Toast.makeText(SettingActivity.this, "error code is " + errorCode + ", message is " + errorMsg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(@Nullable Void data) {
                    if (getApplicationContext() instanceof IMApplication){
                        ((IMApplication) getApplicationContext()).clearActivity(SettingActivity.this);
                    }
                    startActivity(new Intent(SettingActivity.this, WelcomeActivity.class));
                    finish();
                }
            });
        });

        viewBinding.settingTitleBar.setOnBackIconClickListener(v -> onBackPressed());
    }
}
