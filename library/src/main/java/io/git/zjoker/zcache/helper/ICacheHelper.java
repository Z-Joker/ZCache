package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.core.ICache;
/**
 * A adapter of
 *
 * */

public interface ICacheHelper<V extends ICache> extends ICache {

    /**
     * Cache a obj.
     *
     * @param key       The key of the obj in cache.
     * @param <T>       The object will be cached
     * @param converter the converter convert obj -> byte[]
     */
    <T> void put(String key, T obj, IByteConverter<T> converter);


    /**
     * Cache a obj. Need register a Converter fot this obj's Class first.
     *
     * @param key The key of the obj in cache.
     * @param obj The object to be stored
     */
    <T> void put(String key, T obj);

    /*********************************Byte[]*****************************************/

    /**
     * Cache a byte array with duration.
     *
     * @param key      The key of the obj in cache.
     * @param bytes    The bytes will be cached.
     * @param duration The duration of this cache.
     */
    void putBytes(String key, byte[] bytes, long duration);

    /**
     * Cache a byte array.
     *
     * @param key   The key of the obj in cache.
     * @param bytes The bytes to be stored.
     */
    void putBytes(String key, byte[] bytes);

    /**
     * Get a byte array cache
     *
     * @param key The key of the obj in cache.
     * @return Cached byte array. return null if no cache or expired.
     */
    byte[] getBytes(String key);


    /*********************************Bitmap*****************************************/

    /**
     * Cache a bitmap with duration.
     *
     * @param key      The key of the obj in cache.
     * @param bitmap   The bitmap will be cached.
     * @param duration The duration of this cache.
     */
    void putBitmap(String key, Bitmap bitmap, long duration);


    /**
     * Cache a bitmap.
     *
     * @param key    The key of the obj in cache.
     * @param bitmap The bitmap will be cached.
     */
    void putBitmap(String key, Bitmap bitmap);


    /**
     * Get bitmap from cache
     *
     * @param key The key of the obj in cache.
     * @return Cached Bitmap. return null if no cache or expired.
     */
    Bitmap getBitmap(String key);

    /*********************************Serializable*****************************************/

    /**
     * Cache a serializable object with duration.
     *
     * @param key      The key of the obj in cache.
     * @param obj      The serializable object will be cached.
     * @param duration The duration of this cache.
     */
    void putSerializable(String key, Serializable obj, long duration);

    /**
     * Cache a serializable object.
     *
     * @param key The key of the obj in cache.
     * @param obj The serializable object will be cached.
     */
    void putSerializable(String key, Serializable obj);


    /**
     * Get a serializable obj from cache
     *
     * @param key The key of the obj in cache.
     * @return Cached serializable obj. return null if no cache or expired.
     */
    Serializable getSerializable(String key);

    /*****************************JSONObject****************************/


    /**
     * Cache a JSONObject with duration.
     *
     * @param key      The key of the obj in cache.
     * @param obj      The JSONObject will be cached.
     * @param duration The duration of this cache.
     */
    void putJSONObject(String key, JSONObject obj, long duration);


    /**
     * Cache a JSONObject.
     *
     * @param key The key of the obj in cache.
     * @param obj The JSONObject will be cached.
     */
    void putJSONObject(String key, JSONObject obj);


    /**
     * Get a JSONObject from cache
     *
     * @param key The key of the obj in cache.
     * @return Cached JSONObject. return null if no cache or expired.
     * @throws JSONException
     */
    JSONObject getJSONObject(String key) throws JSONException;

    /*****************************String****************************/

    /**
     * Cache a String with duration.
     *
     * @param key      The key of the obj in cache.
     * @param obj      The String will be cached.
     * @param duration The duration of this cache.
     */
    void putString(String key, String obj, long duration);

    /**
     * Cache a String with duration.
     *
     * @param key The key of the obj in cache.
     * @param obj The String will be cached.
     */
    void putString(String key, String obj);

    /**
     * Get a String from cache.
     *
     * @param key The key of the obj in cache.
     * @return Cached String. return null if no cache or expired.
     */
    String getString(String key);
}
