package com.facebook;

import android.os.Handler;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphRequest.OnProgressCallback;

class RequestProgress {
    private final Handler callbackHandler;
    private long lastReportedProgress;
    private long maxProgress;
    private long progress;
    private final GraphRequest request;
    private final long threshold = FacebookSdk.getOnProgressThreshold();

    RequestProgress(Handler callbackHandler2, GraphRequest request2) {
        this.request = request2;
        this.callbackHandler = callbackHandler2;
    }

    /* access modifiers changed from: 0000 */
    public long getProgress() {
        return this.progress;
    }

    /* access modifiers changed from: 0000 */
    public long getMaxProgress() {
        return this.maxProgress;
    }

    /* access modifiers changed from: 0000 */
    public void addProgress(long size) {
        this.progress += size;
        if (this.progress >= this.lastReportedProgress + this.threshold || this.progress >= this.maxProgress) {
            reportProgress();
        }
    }

    /* access modifiers changed from: 0000 */
    public void addToMax(long size) {
        this.maxProgress += size;
    }

    /* access modifiers changed from: 0000 */
    public void reportProgress() {
        if (this.progress > this.lastReportedProgress) {
            Callback callback = this.request.getCallback();
            if (this.maxProgress > 0 && (callback instanceof OnProgressCallback)) {
                final long currentCopy = this.progress;
                final long maxProgressCopy = this.maxProgress;
                final OnProgressCallback callbackCopy = (OnProgressCallback) callback;
                if (this.callbackHandler == null) {
                    callbackCopy.onProgress(currentCopy, maxProgressCopy);
                } else {
                    this.callbackHandler.post(new Runnable() {
                        public void run() {
                            callbackCopy.onProgress(currentCopy, maxProgressCopy);
                        }
                    });
                }
                this.lastReportedProgress = this.progress;
            }
        }
    }
}
