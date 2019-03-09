package io.git.zjoker.sample;

import android.app.Application;

import io.git.zjoker.zcache.ZCacheConfig;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZCacheConfig
                .instance()
                .setMaxDiskCacheSize(5 * 1024 * 1024)
                .setMaxMemoryCacheSize(5 * 1024 * 1024)
                .setCacheRootDir(getCacheDir().getAbsolutePath());
    }
}
