package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.core.ICacheCore;

/**
 * @author borney
 * @date 3/1/17
 */

public interface ICacheHelper extends ICacheCore {
    /**
     * Illegal Duration
     * It means cached without duration.
     * */
    long C_Illegal_Duration = -1;

    /**
     * putByteMapper a object to cache
     *
     * @param key The relative name of the storage object file can be a directory tree
     * @param <T> The object to be stored
     */
    <T> void putByteMapper(String key, T obj);

    /**
     * cache byte array
     *
     * @param key
     * @param bytes
     */
    void putBytes(String key, byte[] bytes);

    /**
     * get byte array from cache
     *
     * @param key
     * @return
     */
    byte[] getBytes(String key);

    /**
     * cache bitmap
     *
     * @param key
     * @param bitmap
     */
    void putBitmap(String key, Bitmap bitmap);

    /**
     * get bitmap from cache
     *
     * @param key
     * @return
     */
    Bitmap getBitmap(String key);

    /**
     * cache Serializable object
     *
     * @param key
     * @param obj which extends Serializable {@link Serializable}
     * @param <T>
     */
    <T extends Serializable> void putSerializable(String key, T obj);

    /**
     * cache Serializable object
     *
     * @param key
     * @param obj      which extends Serializable {@link Serializable}
     * @param <T>
     * @param duration cache duration
     */
    <T extends Serializable> void putSerializable(String key, T obj, long duration);

    /**
     * get Serializable object from cache
     *
     * @param key
     * @param <T>
     * @return
     */
    <T extends Serializable> T getSerializable(String key);

    /**
     * cache JSONObject
     *
     * @param key
     * @param obj
     */
    void putJSONObject(String key, JSONObject obj);

    /**
     * get JSONObject from cache
     *
     * @param key
     * @return
     * @throws JSONException
     */
    JSONObject getJSONObject(String key) throws JSONException;

    String getString(String key);

    void putString(String key, String obj);

    void putString(String key, String obj, long duration);

    ICacheCore getCacheCore();
}
