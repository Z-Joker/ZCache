package io.git.zjoker.zcache;

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
    static final int C_Default_Max_Memory_Cache_Size = (int) (Runtime.getRuntime().maxMemory() / 8);
    static final int C_Default_Max_Disk_Cache_Size = 10 * 1024 * 1024;
    static final String C_Default_Disk_Cache_Dir = "zcache";

    private volatile static ZCacheConfig instance;

    int maxMemoryCacheSize;
    int maxDiskCacheSize;
    String diskCacheRootDir;
    String diskCacheDir;
    private Map<Class, IByteConverter> converterMap;

    private ZCacheConfig() {
        maxMemoryCacheSize = C_Default_Max_Memory_Cache_Size;
        maxDiskCacheSize = C_Default_Max_Disk_Cache_Size;
        diskCacheDir = C_Default_Disk_Cache_Dir;
        converterMap = new HashMap<>();
        registerConverter(Serializable.class, new SerializableByteConverter());
        registerConverter(Bitmap.class, new BitmapByteConverter());
        registerConverter(byte[].class, new BytesConverter());
        registerConverter(String.class, new StringByteConverter());
    }

    public static ZCacheConfig instance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    public static ZCacheConfig init() {
        if (instance == null) {
            synchronized (ZCacheConfig.class) {
                if (instance == null) {
                    instance = new ZCacheConfig();
                }
            }
        }
        return instance;
    }


    /**
     * Max size of the memory cache.
     * The unit is b
     */
    public ZCacheConfig setMaxMemoryCacheSize(int maxMemoryCacheSize) {
        this.maxMemoryCacheSize = maxMemoryCacheSize;
        return this;
    }

    /**
     * Max size of the disk cache.
     * The unit is b
     */
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
