/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.chatkit.ui.view.emoji;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StickerCategory implements Serializable {
    private static final long serialVersionUID = -81692490861539040L;

    private String name;
    private String title;
    private boolean system;
    private int order = 0;

    private transient List<StickerItem> stickers;

    public StickerCategory(String name, String title, boolean system, int order) {
        this.title = title;
        this.name = name;
        this.system = system;
        this.order = order;

        loadStickerData();
    }

    public boolean system() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StickerItem> getStickers() {
        return stickers;
    }

    public boolean hasStickers() {
        return stickers != null && stickers.size() > 0;
    }

    public InputStream getCoverNormalInputStream(Context context) {
        String filename = name + "_s_normal.png";
        return makeFileInputStream(context, filename);
    }

    public InputStream getCoverPressedInputStream(Context context) {
        String filename = name + "_s_pressed.png";
        return makeFileInputStream(context, filename);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        if (stickers == null || stickers.isEmpty()) {
            return 0;
        }

        return stickers.size();
    }

    public int getOrder() {
        return order;
    }

    private InputStream makeFileInputStream(Context context, String filename) {
        try {
            if (system) {
                AssetManager assetManager = context.getResources().getAssets();
                String path = "sticker/" + filename;
                return assetManager.open(path);
            } else {
                // for future
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<StickerItem> loadStickerData() {
        List<StickerItem> stickers = new ArrayList<>();
        AssetManager assetManager = EmojiManager.getContext().getResources().getAssets();
        try {
            String[] files = assetManager.list("sticker/" + name);
            for (String file : files) {
                stickers.add(new StickerItem(name, file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stickers = stickers;
        return stickers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StickerCategory)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        StickerCategory r = (StickerCategory) o;
        return r.getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
