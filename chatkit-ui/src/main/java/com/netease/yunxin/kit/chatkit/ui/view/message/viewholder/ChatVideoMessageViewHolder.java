/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.chatkit.ui.view.message.viewholder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.model.ChatMessageBean;
import com.netease.yunxin.kit.common.ui.utils.ToastX;
import com.netease.yunxin.kit.common.utils.ScreenUtil;
import com.netease.yunxin.kit.common.utils.TimeUtils;
import com.netease.yunxin.kit.common.utils.media.BitmapDecoder;

import java.io.File;
import java.util.Locale;

public class ChatVideoMessageViewHolder extends ChatThumbBaseViewHolder {
    private final static String TAG = "ChatVideoMessageViewHolder";
    private static final int PROGRESS_MAX = 100;

    public ChatVideoMessageViewHolder(@NonNull ViewGroup parent, int viewType) {
        super(parent, viewType);
    }

    @Override
    public void bindData(ChatMessageBean message, ChatMessageBean lastMessage) {
        super.bindData(message, lastMessage);
        long second = TimeUtils.getSecondsByMilliseconds(getAttachment(message).getDuration());
        binding.duration.setText(String.format(Locale.CHINA, "%02d:%02d", second / 60, second % 60));
        binding.duration.setVisibility(View.VISIBLE);
        binding.progressBar.setMax(PROGRESS_MAX);
        binding.progressBar.setIndeterminate(true);

        updateStatus(message.getMessageData().getMessage());
    }

    @Override
    protected void onMessageStatus(ChatMessageBean data) {
        super.onMessageStatus(data);
        binding.progressBar.setIndeterminate(false);
        IMMessage message = data.getMessageData().getMessage();
        if (message.getAttachStatus() == AttachStatusEnum.fail) {
            ToastX.showShortToast(R.string.chat_message_video_download_fail);
        }
        updateStatus(message);
    }

    @Override
    protected void onProgressUpdate(ChatMessageBean data) {
        super.onProgressUpdate(data);
        binding.progressBar.setIndeterminate(false);
        updateProgress((int) data.getLoadProgress());
    }

    private void updateStatus(IMMessage message) {
        if (message.getStatus() == MsgStatusEnum.sending ||
                message.getAttachStatus() == AttachStatusEnum.transferring) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBarInsideIcon.setVisibility(View.VISIBLE);
            binding.playIcon.setVisibility(View.GONE);
        } else {
            updateProgress(PROGRESS_MAX);
        }
    }

    private void updateProgress(int progress) {
        if (progress >= PROGRESS_MAX) {
            // finish
            binding.progressBar.setVisibility(View.GONE);
            binding.progressBarInsideIcon.setVisibility(View.GONE);
            binding.playIcon.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBarInsideIcon.setVisibility(View.VISIBLE);
            binding.playIcon.setVisibility(View.GONE);
            binding.progressBar.setProgress(progress);
        }
    }

    private VideoAttachment getAttachment(ChatMessageBean messageBean) {
        return (VideoAttachment) messageBean.getMessageData().getMessage().getAttachment();
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        VideoAttachment attachment = (VideoAttachment) getMsgInternal().getAttachment();
        String thumbPath = attachment.getThumbPathForSave();
        return BitmapDecoder.extractThumbnail(path, thumbPath) ? thumbPath : attachment.getThumbUrl();
    }

    @Override
    protected int[] getBounds(String path) {
        int[] bounds = null;
        if (path != null) {
            bounds = BitmapDecoder.decodeBound(new File(path));
        }
        if (bounds == null) {
            VideoAttachment attachment = (VideoAttachment) getMsgInternal().getAttachment();
            bounds = new int[] { attachment.getWidth(), attachment.getHeight() };
        }
        return bounds;
    }

    @Override
    protected float[] getCorners() {
        float corner = ScreenUtil.dip2px(12);
        return new float[] { corner, corner, corner, corner };
    }
}
