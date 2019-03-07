package io.git.zjoker.zcache.core;

import android.support.annotation.Size;

import io.git.zjoker.zcache.converter.IByteConverter;



public interface ICacheCore {
    /**
     * Illegal Duration
     * It means cached without duration.
     */
    long C_Illegal_Duration = 0;

    /**
     * put a object to cache with duration
     *
     * @param key    The key of the obj in cache.
     * @param <T>    The object to be stored
     * @param duration The duration of this cache.
     * @param converter the converter convert obj -> byte[]
     */
    <T> void put(String key, T obj, long duration, IByteConverter<T> converter);

    /**
     * put a object to cache with duration
     *
     * @param key    The key of the obj in cache.
     * @param <T>    The object to be stored
     * @param deadLine The deadLine of this cache.
     * @param converter the converter convert obj -> byte[]
     */
    <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter);

    /**
     * get a object from cache
     *
     * @param key The key of the obj in cache.
     * @param <T> the converter convert byte[] -> obj
     * @return Stored object. return null if no cache or expired.
     */
    <T> T get(String key, IByteConverter<T> converter);

    /**
     * Return cache data is expired or not by key
     *
     * @param key The key of the obj in cache.
     */
    boolean isExpired(String key);


    /**
     * Clear the cache by key
     *
     * @param key The key of the obj in cache.
     */
    void evict(String key);

    /**
     * Clear all caches
     */
    void evictAll();

    /**
     * Return whether cached this key
     *
     * @param key
     */
    boolean isCached(String key);

    /**
     * Get the duration info info by key.
     * return null if not cached this key or without duration info.
     *
     * @param key The key of the obj in cache.
     * @return long[0] = storageTime, long[1] = duration
     */
    @Size(2) long getDeadLine(String key);
}
