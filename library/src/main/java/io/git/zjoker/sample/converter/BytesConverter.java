package io.git.zjoker.zcache.converter;

public class BytesConverter implements IByteConverter<byte[]> {

    public BytesConverter() {

    }

    @Override
    public byte[] obj2Bytes(byte[] obj) {
        return obj;
    }

    @Override
    public byte[] bytes2Obj(byte[] bytes) {
        return bytes;
    }
}
