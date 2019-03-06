package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCacheConfig;
import io.git.zjoker.zcache.core.DiskCacheCore;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.core.MemoryCacheCore;
import io.git.zjoker.zcache.converter.IByteConverter;


public final class DoubleLevelCacheHelper implements ICacheHelper<ICacheCore> {
    private ICacheHelper<MemoryCacheCore> memoryCacheHelper;
    private ICacheHelper<DiskCacheCore> diskCacheHelper;

    public DoubleLevelCacheHelper(ICacheHelper<MemoryCacheCore> memoryCacheHelper, ICacheHelper<DiskCacheCore> diskCacheHelper) {
        this.memoryCacheHelper = memoryCacheHelper;
        this.diskCacheHelper = diskCacheHelper;
    }

    @Override
    public <T> void put(String key, T obj) {
        put(key, obj, ZCacheConfig.instance().getConverter((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> converter) {
        put(key, obj, C_Illegal_Duration, converter);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        memoryCacheHelper.put(key, obj, duration, converter);
        diskCacheHelper.put(key, obj, duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        memoryCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
        diskCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        T obj = memoryCacheHelper.get(key, converter);
        if (obj == null) {
            obj = diskCacheHelper.get(key, converter);
            if (obj != null) {
                long deadLine = diskCacheHelper.getDeadLine(key);
                if (deadLine > 0) {
                    memoryCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
                } else {
                    memoryCacheHelper.put(key, obj, C_Illegal_Duration, converter);
                }
            }
        }
        return obj;
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        putBytes(key, bytes, C_Illegal_Duration);
    }

    @Override
    public void putBytes(String key, byte[] bytes, long duration) {
        put(key, bytes, duration, ZCacheConfig.instance().getConverter(byte[].class));
    }

    @Override
    public byte[] getBytes(String key) {
        return get(key, ZCacheConfig.instance().getConverter(byte[].class));
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        putBitmap(key, bitmap, C_Illegal_Duration);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap, long duration) {
        put(key, bitmap, duration, ZCacheConfig.instance().getConverter(Bitmap.class));
    }


    @Override
    public Bitmap getBitmap(String key) {
        return get(key, ZCacheConfig.instance().getConverter(Bitmap.class));
    }

    @Override
    public void putSerializable(String key, Serializable obj) {
        putSerializable(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putSerializable(String key, Serializable obj, long duration) {
        put(key, obj, duration, ZCacheConfig.instance().getConverter(Serializable.class));
    }

    @Override
    public Serializable getSerializable(String key) {
        return get(key, ZCacheConfig.instance().getConverter(Serializable.class));
    }


    @Override
    public void putJSONObject(String key, JSONObject obj) {
        putJSONObject(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putJSONObject(String key, JSONObject obj, long duration) {
        putString(key, obj.toString(), duration);
    }


    @Override
    public JSONObject getJSONObject(String key) throws JSONException {
        return new JSONObject(getString(key));
    }


    @Override
    public void putString(String key, String obj) {
        putString(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putString(String key, String obj, long duration) {
        put(key, obj, duration, ZCacheConfig.instance().getConverter(String.class));
    }

    @Override
    public String getString(String key) {
        return get(key, ZCacheConfig.instance().getConverter(String.class));
    }

    @Override
    public ICacheCore getCacheCore() {
        return memoryCacheHelper != null ? memoryCacheHelper : diskCacheHelper;
    }

    @Override
    public boolean isExpired(String key) {
        if (memoryCacheHelper.isCached(key)) {
            return memoryCacheHelper.isExpired(key);
        }
        return diskCacheHelper.isExpired(key);
    }

    @Override
    public synchronized void evict(String key) {
        diskCacheHelper.evict(key);
        memoryCacheHelper.evict(key);
    }

    @Override
    public synchronized void evictAll() {
        diskCacheHelper.evictAll();
        memoryCacheHelper.evictAll();
    }

    @Override
    public boolean isCached(String key) {
        return memoryCacheHelper.isCached(key) || diskCacheHelper.isCached(key);
    }

    @Override
    public long getDeadLine(String key) {
        long deadLine = memoryCacheHelper.getDeadLine(key);
        if (deadLine > 0) {
            return deadLine;
        }
        return diskCacheHelper.getDeadLine(key);
    }

    /**
     * recycle current cache manager and memory cache
     */
    @SuppressWarnings("unused")
    public void recycle() {
        memoryCacheHelper.evictAll();
        memoryCacheHelper = null;
        diskCacheHelper.evictAll();
        memoryCacheHelper = null;
    }
}
