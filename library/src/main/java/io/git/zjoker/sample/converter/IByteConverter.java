package io.git.zjoker.zcache.converter;

/**
 * IByteMapper is a object convert to byte array or byte array to object mapper, It is used to
 * complete the conversion of objects and byte arrays
 * <p>
 */

public interface IByteConverter<T> {
    /**
     * The byte array of objects
     *
     * @param obj
     * @return
     */
    byte[] obj2Bytes(T obj);

    /**
     * The byte array is converted to an object
     *
     * @param bytes
     * @return
     */
    T bytes2Obj(byte[] bytes);
}
