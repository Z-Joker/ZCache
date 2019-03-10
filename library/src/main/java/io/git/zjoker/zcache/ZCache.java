package io.git.zjoker.zcache;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.git.zjoker.zcache.core.DiskCache;
import io.git.zjoker.zcache.core.MemoryCache;
import io.git.zjoker.zcache.helper.ICacheHelper;
import io.git.zjoker.zcache.helper.L2CacheHelper;
import io.git.zjoker.zcache.helper.SingleLevelCacheHelper;
import io.git.zjoker.zcache.utils.CacheUtil;

public class ZCache {
    private static Map<String, SingleLevelCacheHelper<MemoryCache>> memoryCacheHelperMap = new ConcurrentHashMap<>();
    private static Map<String, SingleLevelCacheHelper<DiskCache>> diskCacheHelperMap = new ConcurrentHashMap<>();

    /**
     * Get Memory Cache Helper instance on default cacheDir.
     */
    public static ICacheHelper<MemoryCache> memory(Context context) {
        return memory(context, ZCacheConfig.instance().cacheDir);
    }


    /**
     * Get Memory Cache Helper
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<MemoryCache> memory(Context context, String cacheDir) {
        return memory(context, cacheDir, ZCacheConfig.instance().maxMemoryCacheSize);
    }

    /**
     * Get Memory Cache Helper
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<MemoryCache> memory(Context context, String cacheDir, int maxSize) {
        String absoluteCacheDir = getAbsoluteCacheDir(context, cacheDir);
        return memory(absoluteCacheDir, maxSize);
    }


    /**
     *  Get Memory Cache Helper instance on default cacheDir.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<MemoryCache> memory(Context context, int maxSize) {
        String absoluteCacheDir = getAbsoluteCacheDir(context, ZCacheConfig.instance().cacheDir);
        return memory(absoluteCacheDir, maxSize);
    }

    /**
     * Get Memory Cache Helper
     * Return different Helper instance by different cacheDir.
     */
    private static ICacheHelper<MemoryCache> memory(String absoluteCacheDir, int maxSize) {
        SingleLevelCacheHelper<MemoryCache> cacheHelper = memoryCacheHelperMap.get(absoluteCacheDir);
        if (cacheHelper == null) {
            int fixMaxSize = fixCacheSize(maxSize, ZCacheConfig.instance().maxMemoryCacheSize);
            cacheHelper
                    = new SingleLevelCacheHelper<>(new MemoryCache(fixMaxSize));
            memoryCacheHelperMap.put(absoluteCacheDir, cacheHelper);
        }
        return cacheHelper;
    }


    /**
     * Get Disk Cache Helper on default cache dir.
     */
    public static ICacheHelper<DiskCache> disk(Context context) {
        return disk(context, ZCacheConfig.instance().cacheDir);
    }


    /**
     * Get Disk Cache Helper.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir) {
        File cacheDirFile = new File(getDefaultRootDir(context), cacheDir);
        return disk(context, cacheDirFile);
    }


    /**
     * Get Disk Cache Helper.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, File absoluteFie) {
        return disk(context, absoluteFie, ZCacheConfig.instance().maxDiskCacheSize);
    }


    /**
     * Get Disk Cache Helper.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, int maxSize) {
        return disk(context, ZCacheConfig.instance().cacheDir, maxSize);
    }

    /**
     * Get Disk Cache Helper.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir, int maxSize) {
        File cacheDirFile = new File(getDefaultRootDir(context), cacheDir);
        return disk(context, cacheDirFile, maxSize);
    }

    /**
     * Get Disk Cache Helper.
     * Return different Helper instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, File cacheDirFie, int maxSize) {
        String absoluteCacheDir = cacheDirFie.getAbsolutePath();

        SingleLevelCacheHelper<DiskCache> cacheHelper = diskCacheHelperMap.get(absoluteCacheDir);
        if (cacheHelper == null) {
            final int appVersion = CacheUtil.getVersionCode(context);
            int fixMaxSize = fixCacheSize(maxSize, ZCacheConfig.instance().maxDiskCacheSize);
            cacheHelper
                    = new SingleLevelCacheHelper<>(new DiskCache(appVersion, absoluteCacheDir, fixMaxSize));
            diskCacheHelperMap.put(absoluteCacheDir, cacheHelper);
        }
        return cacheHelper;
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, File absoluteCacheDir, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, absoluteCacheDir, new CacheSizeParam(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, File absoluteCacheDir, CacheSizeParam cacheSizeParam) {
        CacheSizeParam sizeParams = fixCacheSizeParams(cacheSizeParam);
        return
                new L2CacheHelper(
                        memory(absoluteCacheDir.getAbsolutePath(), sizeParams.maxMemoryCacheSize)
                        , disk(context, absoluteCacheDir, sizeParams.maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, String cacheDir, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, cacheDir, new CacheSizeParam(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, String cacheDir, CacheSizeParam cacheSizeParam) {
        CacheSizeParam sizeParams = fixCacheSizeParams(cacheSizeParam);
        return
                new L2CacheHelper(
                        memory(context, cacheDir, sizeParams.maxMemoryCacheSize)
                        , disk(context, cacheDir, sizeParams.maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, String cacheDir) {
        return
                new L2CacheHelper(
                        memory(context, cacheDir)
                        , disk(context, cacheDir));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance on default cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context) {
        return new L2CacheHelper(memory(context), disk(context));
    }


    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, new CacheSizeParam(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Helper.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static L2CacheHelper twoLevel(Context context, CacheSizeParam cacheSizeParam) {
        CacheSizeParam sizeParams = fixCacheSizeParams(cacheSizeParam);
        return new L2CacheHelper(memory(context, sizeParams.maxMemoryCacheSize), disk(context, sizeParams.maxDiskCacheSize));
    }

    private static String getDefaultRootDir(Context context) {
        String rootDir = ZCacheConfig.instance().cacheRootDir;

        if (rootDir == null) {
            rootDir = context.getCacheDir().getAbsolutePath();
        }
        return rootDir;
    }

    private static String getAbsoluteCacheDir(Context context, String cacheDir) {
        return new File(getDefaultRootDir(context), cacheDir).getAbsolutePath();
    }


    private static CacheSizeParam fixCacheSizeParams(CacheSizeParam original) {
        if (original == null) {
            return
                    new CacheSizeParam(
                            ZCacheConfig.instance().maxMemoryCacheSize
                            , ZCacheConfig.instance().maxDiskCacheSize);
        }

        int maxMemoryCacheSize = fixCacheSize(original.maxMemoryCacheSize, ZCacheConfig.instance().maxMemoryCacheSize);
        int maxDiskCacheSize = fixCacheSize(original.maxDiskCacheSize, ZCacheConfig.instance().maxDiskCacheSize);

        return new CacheSizeParam(maxMemoryCacheSize, maxDiskCacheSize);
    }

    private static int fixCacheSize(int cacheSize, int defaultSize) {
        return cacheSize > 0 ? cacheSize : defaultSize;
    }


    public static class CacheSizeParam {
        public int maxMemoryCacheSize;
        public int maxDiskCacheSize;

        public CacheSizeParam(int maxMemoryCacheSize, int maxDiskCacheSize) {
            this.maxMemoryCacheSize = maxMemoryCacheSize;
            this.maxDiskCacheSize = maxDiskCacheSize;
        }
    }
}
