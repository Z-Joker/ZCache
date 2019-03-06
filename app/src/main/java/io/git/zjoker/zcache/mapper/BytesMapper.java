package io.git.zjoker.zcache.mapper;

/**
 * Created by borney on 3/8/17.
 */

public class BytesMapper implements IByteConverter<byte[]> {

    public BytesMapper() {

    }

    @Override
    public byte[] getBytes(byte[] obj) {
        return obj;
    }

    @Override
    public byte[] getObject(byte[] bytes) {
        return bytes;
    }
}
