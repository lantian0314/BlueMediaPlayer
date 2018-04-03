package com.blue.mediaplayer.mvp.persenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class BasePresenter<T> {
    private Reference<T> mReference = null;

    public void bindView(T bindView) {
        mReference = new WeakReference<>(bindView);
    }

    public T getView() {
        return mReference.get();
    }

    public void detechView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }
}
