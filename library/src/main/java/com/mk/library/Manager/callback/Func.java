package com.mk.library.Manager.callback;

import androidx.annotation.NonNull;

public interface Func<R> {
    /**
     *
     * @param result result Result is never null.
     */
    void call(@NonNull R result);
}
