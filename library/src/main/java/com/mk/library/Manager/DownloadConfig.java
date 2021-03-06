package com.mk.library.Manager;

import com.mk.library.Manager.core.DownloadInterceptor;
import com.mk.library.Manager.core.connection.DownloadConnection;
import com.mk.library.Manager.core.connection.OkHttpDownloadConnection;
import com.mk.library.Manager.core.service.IDownloadConfigService;
import com.mk.library.Manager.utils.OKHttpUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadConfig {
    private int maxRunningTaskNumber = 3;
    private long minUsableStorageSpace = 4 * 1024L;

    private DownloadConnection.Factory connectionFactory;
    private List<DownloadInterceptor> interceptors = new ArrayList<>();

    private DownloadConfig() {
    }

    public int getMaxRunningTaskNumber() {
        return maxRunningTaskNumber;
    }

    public long getMinUsableSpace() {
        return minUsableStorageSpace;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<DownloadInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }

    public DownloadConnection.Factory getDownloadConnectionFactory() {
        return connectionFactory == null ? new OkHttpDownloadConnection.Factory(OKHttpUtil.get())
                : connectionFactory;
    }

    public static class Builder {
        private DownloadConfig downloadConfig;

        private Builder() {
            this.downloadConfig = new DownloadConfig();
        }

        /**
         * Set the maximum number of tasks to run, default 3.
         *
         * @param maxRunningTaskNumber maximum number of tasks to run
         */
        public Builder setMaxRunningTaskNum(int maxRunningTaskNumber) {
            downloadConfig.maxRunningTaskNumber = maxRunningTaskNumber;
            return this;
        }

        /**
         * Set the minimum available storage space size for downloading to avoid insufficient
         * storage space during downloading, default is 4kb.
         *
         * @param minUsableStorageSpace minimum available storage space size
         */
        public Builder setMinUsableStorageSpace(long minUsableStorageSpace) {
            downloadConfig.minUsableStorageSpace = minUsableStorageSpace;
            return this;
        }

        public Builder addDownloadInterceptor(DownloadInterceptor interceptor) {
            downloadConfig.interceptors.add(interceptor);
            return this;
        }

        public Builder setDownloadConnectionFactory(DownloadConnection.Factory factory) {
            downloadConfig.connectionFactory = factory;
            return this;
        }

        public void build() {
            DownloadFactory.getService(IDownloadConfigService.class).setConfig(downloadConfig);
        }
    }
}
