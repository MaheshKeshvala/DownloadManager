package com.mk.library.Manager.config;


import com.mk.library.Manager.DownloadConfig;
import com.mk.library.Manager.core.DownloadInterceptor;
import com.mk.library.Manager.core.connection.DownloadConnection;
import com.mk.library.Manager.core.connection.OkHttpDownloadConnection;
import com.mk.library.Manager.core.service.IDownloadConfigService;
import com.mk.library.Manager.utils.OKHttpUtil;

import java.util.Collections;
import java.util.List;

public class DownloadConfigService implements IDownloadConfigService {
    /**
     * 允许同时下载的最大任务数量
     */
    private int maxRunningTaskNumber = 3;
    /**
     * 最小可用的内存空间
     */
    private long minUsableStorageSpace = 4 * 1024L;
    private DownloadConfig downloadConfig;
    private DownloadConnection.Factory connectionFactory;
    private List<DownloadInterceptor> interceptors;

    private DownloadConfigService() {
    }

    @Override
    public void setConfig(DownloadConfig downloadConfig) {
        this.downloadConfig = downloadConfig;
    }

    public int getMaxRunningTaskNumber() {
        if (downloadConfig == null) {
            return maxRunningTaskNumber;
        }
        return downloadConfig.getMaxRunningTaskNumber();
    }

    public long getMinUsableSpace() {
        if (downloadConfig == null) {
            return minUsableStorageSpace;
        }
        return downloadConfig.getMinUsableSpace();
    }


    public List<DownloadInterceptor> getDownloadInterceptors() {
        if (downloadConfig == null) {
            interceptors = Collections.emptyList();
        }else{
            interceptors = downloadConfig.getInterceptors();
        }
        return interceptors;
    }

    @Override
    public DownloadConnection.Factory getDownloadConnectionFactory() {
        if (downloadConfig == null) {
            connectionFactory = new OkHttpDownloadConnection.Factory(OKHttpUtil.get());
        } else {
            connectionFactory = downloadConfig.getDownloadConnectionFactory();
        }
        return connectionFactory;
    }
}
