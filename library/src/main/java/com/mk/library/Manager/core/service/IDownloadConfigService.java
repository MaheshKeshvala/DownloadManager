package com.mk.library.Manager.core.service;


import com.mk.library.Manager.DownloadConfig;
import com.mk.library.Manager.core.DownloadInterceptor;
import com.mk.library.Manager.core.connection.DownloadConnection;

import java.util.List;

public interface IDownloadConfigService {
    void setConfig(DownloadConfig downloadConfig);

    int getMaxRunningTaskNumber();

    long getMinUsableSpace();

    List<DownloadInterceptor> getDownloadInterceptors();

    DownloadConnection.Factory getDownloadConnectionFactory();
}
