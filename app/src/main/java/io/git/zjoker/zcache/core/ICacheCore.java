package io.git.zjoker.zcache.core;

import io.git.zjoker.zcache.mapper.IByteMapper;

/**
 * Created by borney on 3/7/17.
 */

public interface ICacheCore {
    /**
     * putByteMapper a object to cache
     *
     * @param key    The relative name of the storage object file can be a directory tree
     * @param mapper Store the object of the converter
     * @param <T>    The object to be stored
     */
    <T> void putByteMapper(String key, T obj, IByteMapper<T> mapper);

    /**
     * putByteMapper a object to cache
     *
     * @param key    The relative name of the storage object file can be a directory tree
     * @param <T>    The object to be stored
     * @param mapper Store the object of the converter
     */
    <T> void putByteMapper(String key, T obj, long age, IByteMapper<T> mapper);


    /**
     * get a object from cache
     *
     * @param key The relative name of the storage object file can be a directory tree
     * @param <T> Store the object of the converter
     * @return Stored object
     */
    <T> T getByteMapper(String key, IByteMapper<T> mapper);

    /**
     * cache data is expired or not by key
     *
     * @param key The relative name of the storage object file can be a directory tree
     */
    boolean isExpired(String key);


    /**
     * Clear the cache corresponding to key
     *
     * @param key The relative name of the storage object file can be a directory tree
     */
    void evict(String key);

    /**
     * Clear all caches
     */
    void evictAll();

    /**
     * Whether the cache corresponding to the data
     *
     * @param key
     * @return
     */
    boolean isCached(String key);

    long[] getDurationInfo(String key);
}
