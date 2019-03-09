package io.git.zjoker.zcache;

public class CacheSizeParams {
    public int maxMemoryCacheSize;
    public int maxDiskCacheSize;

    public CacheSizeParams(int maxMemoryCacheSize, int maxDiskCacheSize) {
        this.maxMemoryCacheSize = maxMemoryCacheSize;
        this.maxDiskCacheSize = maxDiskCacheSize;
    }
}