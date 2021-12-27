package com.mk.library.Manager.core.interceptor;

import static com.mk.library.Manager.utils.ErrorCode.ERROR_NETWORK_UNAVAILABLE;

import com.mk.library.Manager.core.DownloadDetailsInfo;
import com.mk.library.Manager.core.DownloadInfo;
import com.mk.library.Manager.core.DownloadInterceptor;
import com.mk.library.Manager.core.DownloadRequest;
import com.mk.library.Manager.core.RealDownloadChain;

public class RetryInterceptor implements DownloadInterceptor {
    private int retryUpperLimit;
    private int tryCount;
    private DownloadDetailsInfo downloadDetailsInfo;

    @Override
    public DownloadInfo intercept(DownloadChain chain) {
        RealDownloadChain realDownloadChain = (RealDownloadChain) chain;
        DownloadRequest downloadRequest = chain.request();
        downloadDetailsInfo = downloadRequest.getDownloadInfo();
        int retryDelay = downloadRequest.getRetryDelay();
        retryUpperLimit = downloadRequest.getRetryCount();
        DownloadInfo downloadInfo;
        boolean shouldRetry = false;
        while (true) {
            downloadInfo = realDownloadChain.proceed(downloadRequest, shouldRetry);
            shouldRetry = shouldRetry();
            if (shouldRetry) {
                if(downloadDetailsInfo.isForceRetry()){
                    downloadDetailsInfo.deleteTempDir();
                    downloadDetailsInfo.setForceRetry(false);
                }else{
                    tryCount++;
                }
                downloadDetailsInfo.setStatus(DownloadInfo.Status.RUNNING);
                downloadDetailsInfo.clearErrorCode();
                if (retryDelay > 0) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                break;
            }
        }
        return downloadInfo;
    }

    private boolean shouldRetry() {
        return downloadDetailsInfo.isForceRetry() || downloadDetailsInfo.getErrorCode() == ERROR_NETWORK_UNAVAILABLE
                && retryUpperLimit > tryCount;
    }
}
