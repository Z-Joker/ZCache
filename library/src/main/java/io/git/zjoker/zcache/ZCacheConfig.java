package io.git.zjoker.zcache;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.git.zjoker.zcache.converter.BitmapByteConverter;
import io.git.zjoker.zcache.converter.BytesConverter;
import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.converter.ObjByteConverter;
import io.git.zjoker.zcache.converter.SerializableByteConverter;
import io.git.zjoker.zcache.converter.StringByteConverter;

public class ZCacheConfig {
    private static final int C_Default_Max_Memory_Cache_Size = (int) (Runtime.getRuntime().maxMemory() / 8);
    private static final int C_Default_Max_Disk_Cache_Size = 10 * 1024 * 1024;
    private static final String C_Default_Cache_Dir = "zcache";

    private volatile static ZCacheConfig instance;

    int maxMemoryCacheSize;
    int maxDiskCacheSize;
    String cacheRootDir;
    String cacheDir;
    private Map<Class<?>, IByteConverter<?>> converterMap;
    private Map<Class<?>, IByteConverter<?>> objConverterMap;

    private ZCacheConfig() {
        maxMemoryCacheSize = C_Default_Max_Memory_Cache_Size;
        maxDiskCacheSize = C_Default_Max_Disk_Cache_Size;
        cacheDir = C_Default_Cache_Dir;
        converterMap = new HashMap<>();
        registerConverter(Serializable.class, new SerializableByteConverter());
        registerConverter(Bitmap.class, new BitmapByteConverter());
        registerConverter(byte[].class, new BytesConverter());
        registerConverter(String.class, new StringByteConverter());
        objConverterMap = new HashMap<>();
    }

    public static ZCacheConfig instance() {
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

    /**
     * Globle cache root path.
     * Default: Context.getCacheDir();
     */
    public ZCacheConfig setCacheRootDir(String cacheRootDir) {
        this.cacheRootDir = cacheRootDir;
        return this;
    }

    /**
     * Default cache dir.
     */
    public ZCacheConfig setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
        return this;
    }

    public <T> IByteConverter<T> getConverter(Class<T> cacheClass) {
        return (IByteConverter<T>) converterMap.get(cacheClass);
    }

    public <T> IByteConverter<T> getObjConverter(Class<T> cacheClass) {
        IByteConverter<?> converter = objConverterMap.get(cacheClass);
        if (converter == null) {
            converter = new ObjByteConverter(cacheClass);
            objConverterMap.put(cacheClass, converter);
        }
        return (IByteConverter<T>) converter;
    }

    public <T> ZCacheConfig registerConverter(Class<T> cacheClass, IByteConverter<T> mapper) {
        converterMap.put(cacheClass, mapper);
        return this;
    }

}
