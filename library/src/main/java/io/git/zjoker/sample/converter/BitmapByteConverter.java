package io.git.zjoker.zcache.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;



public class BitmapByteConverter implements IByteConverter<Bitmap> {

    public BitmapByteConverter() {

    }

    @Override
    public byte[] obj2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    @Override
    public Bitmap bytes2Obj(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
