package io.git.zjoker.zcache.mapper;

import io.git.zjoker.zcache.StandardCharsets;

public class StringByteMapper implements IByteConverter<String> {
    @Override
    public byte[] getBytes(String obj) {
        return obj.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getObject(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
