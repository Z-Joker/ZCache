package io.git.zjoker.zcache.converter;

import io.git.zjoker.zcache.utils.ProtoStuffUtils;

public class ObjByteConverter implements IByteConverter<Object> {
    private Class objClass;

    public ObjByteConverter(Class objClass) {
        this.objClass = objClass;
    }

    @Override
    public byte[] obj2Bytes(Object obj) {
        return ProtoStuffUtils.serialize(obj);
    }

    @Override
    public Object bytes2Obj(byte[] bytes) {
        return ProtoStuffUtils.deserialize(bytes, objClass);
    }
}
