package io.git.zjoker.zcache.utils;

import android.os.Build;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import static io.git.zjoker.zcache.utils.ProtoStuffUtils.ObjWrapper.wrapperSet;

public class ProtoStuffUtils {
    private static final Schema<ObjWrapper> wrapperSchema = RuntimeSchema.createFrom(ObjWrapper.class);

    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    public static <T> byte[] serialize(T obj) {
        byte[] bytes = new byte[0];
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Object serializeObj = obj;
            Schema schema = wrapperSchema;
            if (!isClassNeedWrapper(obj.getClass())) {
                schema = findSchema(obj.getClass());
            } else {
                serializeObj = ObjWrapper.builder(obj);
            }
            bytes = ProtostuffIOUtil.toByteArray(serializeObj, schema, buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            if (!isClassNeedWrapper(clazz)) {
                T message = clazz.newInstance();
                Schema<T> schema = findSchema(clazz);
                ProtostuffIOUtil.mergeFrom(bytes, message, schema);
                return message;
            } else {
                ObjWrapper<T> wrapper = new ObjWrapper<>();
                ProtostuffIOUtil.mergeFrom(bytes, wrapper, wrapperSchema);
                return wrapper.getObj();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * find a schema by class.
     */
    private static <T> Schema<T> findSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            schemaCache.put(clazz, schema);
        }
        return schema;
    }

    private static boolean isClassNeedWrapper(Class clazz) {
        return wrapperSet.contains(clazz);
    }

    public static void registerWrapperClass(Class clazz) {
        wrapperSet.add(clazz);
    }

    /**
     * Collection can not be ser/deser directly, it need to wrapped as POJO class first.
     */
    public static class ObjWrapper<T> {
        public static Set<Class> wrapperSet = new HashSet<>();

        static {
            wrapperSet.add(List.class);
            wrapperSet.add(ArrayList.class);
            wrapperSet.add(CopyOnWriteArrayList.class);
            wrapperSet.add(LinkedList.class);
            wrapperSet.add(Stack.class);
            wrapperSet.add(Vector.class);

            wrapperSet.add(Map.class);
            wrapperSet.add(HashMap.class);
            wrapperSet.add(TreeMap.class);
            wrapperSet.add(Hashtable.class);
            wrapperSet.add(SortedMap.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wrapperSet.add(ArrayMap.class);
            }

            wrapperSet.add(SparseArray.class);
            wrapperSet.add(SparseIntArray.class);
            wrapperSet.add(SparseBooleanArray.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                wrapperSet.add(SparseLongArray.class);
            }

            wrapperSet.add(android.support.v4.util.ArrayMap.class);

            wrapperSet.add(Object.class);
        }

        private T obj;

        public static <T> ObjWrapper<T> builder(T data) {
            ObjWrapper<T> wrapper = new ObjWrapper<>();
            wrapper.setObj(data);
            return wrapper;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }

        public T getObj() {
            return obj;
        }
    }
}
