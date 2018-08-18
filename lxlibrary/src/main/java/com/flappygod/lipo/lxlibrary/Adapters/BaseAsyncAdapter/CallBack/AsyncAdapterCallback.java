package com.flappygod.lipo.lxlibrary.Adapters.BaseAsyncAdapter.CallBack;

import java.util.List;

/**
 * Created by yang on 2016/8/25.
 */
public interface AsyncAdapterCallback<T> {
    void success(List<T> data);
    void failure(Exception e);
}
