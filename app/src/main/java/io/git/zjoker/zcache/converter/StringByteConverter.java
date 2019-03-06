package io.git.zjoker.zcache.converter;

import java.nio.charset.Charset;


public class StringByteConverter implements IByteConverter<String> {
    @Override
    public byte[] obj2Bytes(String obj) {
        return obj.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String bytes2Obj(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }
}
