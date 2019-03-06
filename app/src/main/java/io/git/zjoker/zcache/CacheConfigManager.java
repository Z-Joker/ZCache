package io.git.zjoker.zcache;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.git.zjoker.zcache.mapper.BitmapByteMapper;
import io.git.zjoker.zcache.mapper.BytesMapper;
import io.git.zjoker.zcache.mapper.IByteConverter;
import io.git.zjoker.zcache.mapper.SerializableByteMapper;
import io.git.zjoker.zcache.mapper.StringByteMapper;

public class CacheConfigManager {
    private volatile static CacheConfigManager instance;

    public int _memoryCacheSize;
    public int _diskCacheSize;
    public int _appVersion;
    public String _diskCacheRootPath;
    public Map<Class, IByteConverter> mappers;

    private CacheConfigManager() {
        mappers = new HashMap<>();
        mappers.put(Serializable.class, new SerializableByteMapper());
        mappers.put(Bitmap.class, new BitmapByteMapper());
        mappers.put(byte[].class, new BytesMapper());
        mappers.put(String.class, new StringByteMapper());
    }

    public static CacheConfigManager instance() {
        if (instance == null)
            throw new IllegalArgumentException("GlobalCacheConfig not initialized.");
        return instance;
    }

    public static CacheConfigManager init() {
        if (instance == null) {
            synchronized (CacheConfigManager.class) {
                if (instance == null) {
                    instance = new CacheConfigManager();
                }
            }
        }
        return instance;
    }

    public CacheConfigManager setMemoryCacheSize(int memoryCacheSize) {
        this._memoryCacheSize = memoryCacheSize;
        return this;
    }

    public CacheConfigManager setDiskCacheSize(int diskCacheSize) {
        this._diskCacheSize = diskCacheSize;
        return this;
    }

    public CacheConfigManager setAppVersion(int appVersion) {
        this._appVersion = appVersion;
        return this;
    }

    public CacheConfigManager setDiskCacheRootDirectory(String diskCacheRootPath) {
        this._diskCacheRootPath = diskCacheRootPath;
        return this;
    }

    public <T> IByteConverter<T> getMapper(Class<T> cacheClass) {
        return mappers.get(cacheClass);
    }

    public <T> CacheConfigManager registerMappers(Class<T> cacheClass, IByteConverter<T> mapper) {
        mappers.put(cacheClass, mapper);
        return this;
    }
}
