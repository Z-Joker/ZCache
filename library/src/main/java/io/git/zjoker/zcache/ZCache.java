package io.git.zjoker.zcache;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.git.zjoker.zcache.core.DiskCache;
import io.git.zjoker.zcache.core.MemoryCache;
import io.git.zjoker.zcache.helper.DoubleLevelCacheHelper;
import io.git.zjoker.zcache.helper.ICacheHelper;
import io.git.zjoker.zcache.helper.SingleLevelCacheHelper;
import io.git.zjoker.zcache.utils.CacheUtil;

public class ZCache {
    private static Map<String, SingleLevelCacheHelper<MemoryCache>> memoryCacheAdapterMap = new ConcurrentHashMap<>();
    private static Map<String, SingleLevelCacheHelper<DiskCache>> diskCacheAdapterMap = new ConcurrentHashMap<>();

    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory(Context context) {
        return memory(context, ZCacheConfig.instance().cacheDir);
    }


    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory(Context context, String cacheDir) {
        return memory(context, cacheDir, ZCacheConfig.instance().maxMemoryCacheSize);
    }

    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory(Context context, String cacheDir, int maxSize) {
        String absoluteCacheDir = getAbsoluteCacheDir(context, cacheDir);
        return memory(absoluteCacheDir, maxSize);
    }


    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory(Context context, int maxSize) {
        String absoluteCacheDir = getAbsoluteCacheDir(context, ZCacheConfig.instance().cacheDir);
        return memory(absoluteCacheDir, maxSize);
    }

    /**
     * Get Memory Cache Adapter
     */
    private static ICacheHelper<MemoryCache> memory(String absoluteCacheDir, int maxSize) {
        SingleLevelCacheHelper<MemoryCache> cacheHelper = memoryCacheAdapterMap.get(absoluteCacheDir);
        if (cacheHelper == null) {
            int fixMaxSize = fixCacheSize(maxSize, ZCacheConfig.instance().maxMemoryCacheSize);
            cacheHelper
                    = new SingleLevelCacheHelper<>(new MemoryCache(fixMaxSize));
            memoryCacheAdapterMap.put(absoluteCacheDir, cacheHelper);
        }
        return cacheHelper;
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context) {
        return disk(context, ZCacheConfig.instance().cacheDir);
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir) {
        File cacheDirFile = new File(getDefaultRootDir(context), cacheDir);
        return disk(context, cacheDirFile);
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, File absoluteFie) {
        return disk(context, absoluteFie, ZCacheConfig.instance().maxDiskCacheSize);
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, int maxSize) {
        return disk(context, ZCacheConfig.instance().cacheDir, maxSize);
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir, int maxSize) {
        File cacheDirFile = new File(getDefaultRootDir(context), cacheDir);
        return disk(context, cacheDirFile, maxSize);
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, File cacheDirFie, int maxSize) {
        String absoluteDir = cacheDirFie.getAbsolutePath();

        SingleLevelCacheHelper<DiskCache> cacheHelper = diskCacheAdapterMap.get(absoluteDir);
        if (cacheHelper == null) {
            final int appVersion = CacheUtil.getVersionCode(context);
            int fixMaxSize = fixCacheSize(maxSize, ZCacheConfig.instance().maxDiskCacheSize);
            cacheHelper
                    = new SingleLevelCacheHelper<>(new DiskCache(appVersion, absoluteDir, fixMaxSize));
            diskCacheAdapterMap.put(absoluteDir, cacheHelper);
        }
        return cacheHelper;
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, File absoluteFie, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, absoluteFie, new CacheSizeParams(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, File absoluteFie, CacheSizeParams cacheSizeParams) {
        CacheSizeParams sizeParams = fixCacheSizeParams(cacheSizeParams);
        return
                new DoubleLevelCacheHelper(
                        memory(absoluteFie.getAbsolutePath(), sizeParams.maxMemoryCacheSize)
                        , disk(context, absoluteFie, sizeParams.maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, String cacheDir, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, cacheDir, new CacheSizeParams(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, String cacheDir, CacheSizeParams cacheSizeParams) {
        CacheSizeParams sizeParams = fixCacheSizeParams(cacheSizeParams);
        return
                new DoubleLevelCacheHelper(
                        memory(context, cacheDir, sizeParams.maxMemoryCacheSize)
                        , disk(context, cacheDir, sizeParams.maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, String cacheDir) {
        return
                new DoubleLevelCacheHelper(
                        memory(context, cacheDir)
                        , disk(context, cacheDir));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context) {
        return new DoubleLevelCacheHelper(memory(context), disk(context));
    }


    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, int maxMemoryCacheSize, int maxDiskCacheSize) {
        return twoLevel(context, new CacheSizeParams(maxMemoryCacheSize, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static DoubleLevelCacheHelper twoLevel(Context context, CacheSizeParams cacheSizeParams) {
        CacheSizeParams sizeParams = fixCacheSizeParams(cacheSizeParams);
        return new DoubleLevelCacheHelper(memory(context, sizeParams.maxMemoryCacheSize), disk(context, sizeParams.maxDiskCacheSize));
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

    private static CacheSizeParams fixCacheSizeParams(CacheSizeParams original) {
        if (original == null) {
            return
                    new CacheSizeParams(
                            ZCacheConfig.instance().maxMemoryCacheSize
                            , ZCacheConfig.instance().maxDiskCacheSize);
        }

        int maxMemoryCacheSize = fixCacheSize(original.maxMemoryCacheSize, ZCacheConfig.instance().maxMemoryCacheSize);
        int maxDiskCacheSize = fixCacheSize(original.maxDiskCacheSize, ZCacheConfig.instance().maxDiskCacheSize);

        return new CacheSizeParams(maxMemoryCacheSize, maxDiskCacheSize);
    }

    private static int fixCacheSize(int cacheSize, int defaultSize) {
        return cacheSize > 0 ? cacheSize : defaultSize;
    }
}
