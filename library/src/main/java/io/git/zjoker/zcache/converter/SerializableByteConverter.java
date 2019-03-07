package io.git.zjoker.zcache.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.git.zjoker.zcache.utils.CacheUtil;


public class SerializableByteConverter implements IByteConverter<Serializable> {
    public SerializableByteConverter() {
    }

    @Override
    public byte[] obj2Bytes(Serializable obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream out = null;
        try {
            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CacheUtil.close(bos, out);
        }
        return new byte[0];
    }

    @Override
    public Serializable bytes2Obj(byte[] bytes) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            return (Serializable) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            CacheUtil.close(bis, ois);
        }
        return null;
    }
}
