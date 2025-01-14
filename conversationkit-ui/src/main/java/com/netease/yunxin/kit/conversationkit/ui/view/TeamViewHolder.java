/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.conversationkit.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.netease.nimlib.sdk.team.model.Team;
import com.netease.yunxin.kit.common.ui.utils.AvatarColor;
import com.netease.yunxin.kit.common.ui.utils.TimeFormatUtils;
import com.netease.yunxin.kit.common.ui.viewholder.BaseViewHolder;
import com.netease.yunxin.kit.conversationkit.ui.R;
import com.netease.yunxin.kit.conversationkit.ui.databinding.TeamViewHolderLayoutBinding;
import com.netease.yunxin.kit.conversationkit.ui.model.ConversationBean;

public class TeamViewHolder extends BaseViewHolder<ConversationBean> {

    private TeamViewHolderLayoutBinding viewBinding;

    public TeamViewHolder(@NonNull ViewGroup itemView) {
        super(itemView);
    }

    public TeamViewHolder(@NonNull TeamViewHolderLayoutBinding binding) {
        this(binding.getRoot());
        viewBinding = binding;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindData(ConversationBean data, int position) {
        Context context = viewBinding.getRoot().getContext();
        if(data.infoData.getTeamInfo() != null) {
            Team teamInfo = data.infoData.getTeamInfo();
            viewBinding.avatarView.setData(teamInfo.getIcon(), teamInfo.getName(), AvatarColor.avatarColor(teamInfo.getId()));
            viewBinding.conversationNameTv.setText(teamInfo.getName());
        }
        if (data.infoData.isStickTop()){
            viewBinding.rootView.setBackground(context.getResources().getDrawable(R.drawable.view_select_selector));
        }else {
            viewBinding.rootView.setBackground(context.getDrawable(R.drawable.view_normal_selector));
        }

        if (data.infoData.getMute()){
            viewBinding.conversationMuteIv.setVisibility(View.VISIBLE);
            viewBinding.conversationUnreadTv.setVisibility(View.GONE);
        }else {
            viewBinding.conversationMuteIv.setVisibility(View.GONE);
            if (data.infoData.getUnreadCount() > 0){
                int count = data.infoData.getUnreadCount();
                String content;
                if (count >= 100) {
                    content = "99+";
                } else {
                    content = String.valueOf(count);
                }
                viewBinding.conversationUnreadTv.setText(content);
                viewBinding.conversationUnreadTv.setVisibility(View.VISIBLE);
            }else {
                viewBinding.conversationUnreadTv.setVisibility(View.GONE);
            }
        }
        viewBinding.conversationMessageTv.setText(data.infoData.getContent());
        viewBinding.conversationTime.setText(TimeFormatUtils.formatMillisecond(context, data.infoData.getTime()));
        viewBinding.rootView.setOnClickListener( v -> itemListener.onClick(data,position));
        viewBinding.rootView.setOnLongClickListener(v -> itemListener.onLongClick(data,position));
    }

}
