/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.chatkit.ui.page.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.kit.chatkit.ui.databinding.WatchImageViewHolderBinding;
import com.netease.yunxin.kit.common.ui.viewmodel.LoadStatus;
import com.netease.yunxin.kit.common.utils.ScreenUtil;
import com.netease.yunxin.kit.common.utils.media.BitmapDecoder;
import com.netease.yunxin.kit.common.utils.media.ImageUtil;

import java.util.List;

public class WatchImageAdapter extends RecyclerView.Adapter<WatchImageAdapter.WatchImageViewHolder> {
    private final static String TAG = "WatchImageVideo";

    private final Context mContext;
    private final List<IMMessage> messageList;

    public WatchImageAdapter(Context context, List<IMMessage> messages) {
        this.mContext = context;
        this.messageList = messages;
    }

    @NonNull
    @Override
    public WatchImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WatchImageViewHolderBinding binding = WatchImageViewHolderBinding
                .inflate(LayoutInflater.from(mContext), parent, false);
        return new WatchImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchImageViewHolder holder, int position) {
        IMMessage message = messageList.get(position);
        if (message == null) {
            return;
        }
        handlePictureView(holder, message, position);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchImageViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if (payloads.get(0) == LoadStatus.Loading) {
                holder.binding.watchImageLoading.setVisibility(View.VISIBLE);
            } else {
                holder.binding.watchImageLoading.setVisibility(View.GONE);
                updateImage(holder, messageList.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getMsgType().getValue();
    }

    private void handlePictureView(WatchImageViewHolder holder, IMMessage message, int position) {
        holder.binding.watchPhotoView.setFullScreen(true, false);
        holder.binding.watchPhotoView.enableImageTransforms(true);
        holder.binding.watchPhotoView.setFirst(position == 0);
        holder.binding.watchPhotoView.setLast(position == getItemCount() - 1);

        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
            holder.binding.watchImageLoading.setVisibility(View.GONE);
        } else {
            holder.binding.watchImageLoading.setVisibility(View.VISIBLE);
        }
        updateImage(holder, message);
    }

    private void updateImage(WatchImageViewHolder holder, IMMessage message) {
        ImageAttachment attachment = (ImageAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }
        String path = attachment.getPath();
        if (TextUtils.isEmpty(path)) {
            path = attachment.getThumbPath();
        }
        ALog.i(TAG, "updateImage path:" + path);

        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(path)) {
            bitmap = BitmapDecoder.decodeSampledForDisplay(path);
            bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
        }
        if (bitmap != null) {
            float initScale = bitmap.getWidth() > 0 ? ScreenUtil.getDisplayWidth() * 1f / bitmap.getWidth() : 1f;
            holder.binding.watchPhotoView.setMaxInitialScale(initScale);
            holder.binding.watchPhotoView.bindPhoto(bitmap);
        }
    }

    static class WatchImageViewHolder extends RecyclerView.ViewHolder {
        WatchImageViewHolderBinding binding;

        public WatchImageViewHolder(@NonNull WatchImageViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
