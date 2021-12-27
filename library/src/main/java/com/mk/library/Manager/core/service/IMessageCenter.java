package com.mk.library.Manager.core.service;

import android.content.Context;

import com.mk.library.Manager.core.DownloadDetailsInfo;
import com.mk.library.Manager.core.DownloadListener;

public interface IMessageCenter {
    void start(Context context);

    void register(DownloadListener downloadListener);

    void unRegister(String url);

    void unRegister(DownloadListener downloadListener);

    void notifyProgressChanged(DownloadDetailsInfo downloadInfo);

}
