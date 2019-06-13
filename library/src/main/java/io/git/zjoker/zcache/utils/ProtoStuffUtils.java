package io.git.zjoker.zcache.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtoStuffUtils {
    /**
     * 缓存Schema
     */
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    public static <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes = new byte[0];
        try {
            bytes = ProtobufIOUtil.toByteArray(obj, (Schema<T>) findSchema(obj.getClass()), buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> tClass) {
        T obj = null;
        try {
            Schema<T> schema = findSchema(tClass);
            obj = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static <T> Schema<T> findSchema(Class<T> tClass) {
        Schema<?> schema = schemaCache.get(tClass);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(tClass);
            schemaCache.put(tClass, schema);
        }
        return (Schema<T>) schema;
    }
}
