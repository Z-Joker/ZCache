package io.git.zjoker.zcache;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.git.zjoker.zcache.converter.BitmapByteConverter;
import io.git.zjoker.zcache.converter.BytesConverter;
import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.converter.SerializableByteConverter;
import io.git.zjoker.zcache.converter.StringByteConverter;

public class ZCacheConfig {
    private volatile static ZCacheConfig instance;

    int maxMemoryCacheSize;
    int maxDiskCacheSize;
    String diskCacheRootDir;
    private Map<Class, IByteConverter> converterMap;
    Context context;

    private ZCacheConfig(Context context) {
        this.context = context;
        converterMap = new HashMap<>();
        registerConverter(Serializable.class, new SerializableByteConverter());
        registerConverter(Bitmap.class, new BitmapByteConverter());
        registerConverter(byte[].class, new BytesConverter());
        registerConverter(String.class, new StringByteConverter());
    }

    public static ZCacheConfig instance() {
        if (instance == null)
            throw new IllegalArgumentException("ZCacheConfig not initialized.");
        return instance;
    }

    public static ZCacheConfig initWith(Context context) {
        if (instance == null) {
            synchronized (ZCacheConfig.class) {
                if (instance == null) {
                    instance = new ZCacheConfig(context);
                }
            }
        }
        return instance;
    }


    /**
     * Max size of the memory cache.
     * The unit is b
     * */
    public ZCacheConfig setMaxMemoryCacheSize(int maxMemoryCacheSize) {
        this.maxMemoryCacheSize = maxMemoryCacheSize;
        return this;
    }

    /**
     * Max size of the disk cache.
     * The unit is b
     * */
    public ZCacheConfig setMaxDiskCacheSize(int maxDiskCacheSize) {
        this.maxDiskCacheSize = maxDiskCacheSize;
        return this;
    }

    public ZCacheConfig setDiskCacheRootDir(String diskCacheRootDir) {
        this.diskCacheRootDir = diskCacheRootDir;
        return this;
    }

    public <T> IByteConverter<T> getConverter(Class<T> cacheClass) {
        return converterMap.get(cacheClass);
    }

    public <T> ZCacheConfig registerConverter(Class<T> cacheClass, IByteConverter<T> mapper) {
        converterMap.put(cacheClass, mapper);
        return this;
    }
}
