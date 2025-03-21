/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.chatkit.ui.view.message.viewholder;

import static com.netease.yunxin.kit.corekit.im.utils.RouterConstant.KEY_TEAM_CREATED_TIP;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.databinding.ChatMessageTextViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.model.ChatMessageBean;
import com.netease.yunxin.kit.corekit.im.XKitImClient;

import java.util.Map;

/**
 * view holder for Text message
 */
public class ChatTipsMessageViewHolder extends ChatBaseMessageViewHolder {

    private static final String LOG_TAG = "ChatTipsMessageViewHolder";

    ChatMessageTextViewHolderBinding textBinding;

    public ChatTipsMessageViewHolder(@NonNull ViewGroup parent, int viewType) {
        super(parent, viewType);
    }

    @Override
    public void addContainer() {
        textBinding = ChatMessageTextViewHolderBinding.inflate(LayoutInflater.from(getParent().getContext()),
                getContainer(), true);
    }

    @Override
    public void bindData(ChatMessageBean message, ChatMessageBean lastMessage) {
        super.bindData(message, lastMessage);
        Map<String, Object> extension = message.getMessageData().getMessage().getRemoteExtension();
        if (extension != null && extension.get(KEY_TEAM_CREATED_TIP) != null) {
            textBinding.messageText.setTextColor(XKitImClient.getApplicationContext().getResources().getColor(R.color.color_999999));
            textBinding.messageText.setTextSize(12);
            textBinding.messageText.setText(extension.get(KEY_TEAM_CREATED_TIP).toString());
        } else {
            getBaseRoot().setVisibility(View.GONE);
        }

    }
}
