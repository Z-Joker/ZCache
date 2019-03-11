package io.git.zjoker.zcache.core;

import io.git.zjoker.zcache.converter.IByteConverter;



public interface ICache {
    /**
     * Illegal Duration
     * It means cached without duration.
     */
    long C_Without_Duration = -1;

    /**
     * Cache a object with duration
     *
     * @param key    The key of the obj in cache.
     * @param <T>    The object to be stored
     * @param duration The duration of this cache.
     * @param converter the converter convert obj -> byte[]
     */
    <T> void put(String key, T obj, long duration, IByteConverter<T> converter);

    /**
     * Cache a object with deadline
     *
     * @param key    The key of the obj in cache.
     * @param <T>    The object to be stored
     * @param deadLine The deadLine of this cache.
     * @param converter the converter convert obj -> byte[]
     */
    <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter);

    /**
     * Get cache by key
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
     * Remove the cache by key
     *
     * @param key The key of the obj in cache.
     */
    void remove(String key);

    /**
     * Remove all caches
     */
    void removeAll();

    /**
     * Return whether contains this key
     *
     * @param key The key of the obj in cache.
     */
    boolean contains(String key);

    /**
     * Get the deadline of this cache.
     * return 0 if not cached this key.
     * @param key The key of the obj in cache.
     */
    long getDeadLine(String key);
}
