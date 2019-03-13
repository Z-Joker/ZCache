package io.git.zjoker.zcache.converter;

/**
 * IByteConverter used to convert obj to byte[] for storage cache and convert byte[] to obj for get cache
 * <p>
 */

public interface IByteConverter<T> {
    /**
     * Convert obj to bye[]
     */
    byte[] obj2Bytes(T obj);

    /**
     * Convert byte[] to obj
     */
    T bytes2Obj(byte[] bytes);
}
