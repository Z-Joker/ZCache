package io.git.zjoker.zcache.utils;

import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DiskLruCacheUtil {
    private static final String TAG = "DiskLruCacheUtil";
    private static final int DEFAULT_VALUE_COUNT = 1;
    private DiskLruCache mDiskLruCache;
    private int appVersion;
    private String dirName;
    private long maxSize;

    public DiskLruCacheUtil(int appVersion, String dirName, long maxSize) throws IOException {
        this.appVersion = appVersion;
        this.dirName = dirName;
        this.maxSize = maxSize;
        mDiskLruCache = initDiskLruCache(appVersion, dirName, maxSize);
    }

    private DiskLruCache initDiskLruCache(int appVersion, String dirName, long maxSize) throws IOException {
        return DiskLruCache.open(
                new File(dirName),
                appVersion,
                DEFAULT_VALUE_COUNT,
                maxSize);
    }

    public void put(String key, byte[] value) {
        DiskLruCache.Editor edit = null;
        BufferedOutputStream bw = null;

        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedOutputStream(os);
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //s
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public byte[] getAsByte(String key) {
        InputStream inputStream = null;
        inputStream = get(key);
        if (inputStream == null) return null;
        try {
            return CacheUtil.readFully(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private DiskLruCache.Editor editor(String key) {
        try {
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            //edit maybe null :the entry is editing
            if (edit == null) {
                Log.w(TAG, "the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean remove(String key) {
        try {
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() throws IOException {
        mDiskLruCache.close();
    }

    public void clear() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.delete();
            } catch (IOException ignoreExc) {
            }
        }

        try {
            mDiskLruCache = initDiskLruCache(appVersion, dirName, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() throws IOException {
        mDiskLruCache.flush();
    }

    public boolean isClosed() {
        return mDiskLruCache.isClosed();
    }

    public long size() {
        return mDiskLruCache.size();
    }

    public void setMaxSize(long maxSize) {
        mDiskLruCache.setMaxSize(maxSize);
    }

    public File getDirectory() {
        return mDiskLruCache.getDirectory();
    }

    public long getMaxSize() {
        return mDiskLruCache.getMaxSize();
    }

    public boolean contains(String key) {
        try {
            return mDiskLruCache.edit(key) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //basic get
    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot == null) //not find entry , or entry.readable = false
            {
                Log.e(TAG, "not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
