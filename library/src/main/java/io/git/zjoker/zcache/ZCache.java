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
    private static volatile SingleLevelCacheHelper<MemoryCache> memoryCacheAdapter;
    private static Map<String, SingleLevelCacheHelper<DiskCache>> diskCacheAdapterMap = new ConcurrentHashMap<>();

    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory() {
        return memory(ZCacheConfig.instance().maxMemoryCacheSize);
    }

    /**
     * Get Memory Cache Adapter
     */
    public static ICacheHelper<MemoryCache> memory(int maxMemoryCacheSize) {
        if (memoryCacheAdapter == null) {
            synchronized (ZCache.class) {
                if (memoryCacheAdapter == null) {
                    memoryCacheAdapter = new SingleLevelCacheHelper<>(new MemoryCache(maxMemoryCacheSize));
                }
            }
        }
        return memoryCacheAdapter;
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context) {
        return disk(context, ZCacheConfig.instance().diskCacheDir);
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, int maxSize) {
        return disk(context, ZCacheConfig.instance().diskCacheDir, maxSize);
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir, int maxSize) {
        return disk(context, getDefaultRootDir(context), cacheDir, maxSize);
    }


    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String cacheDir) {
        return disk(context, getDefaultRootDir(context), cacheDir);
    }

    private static String getDefaultRootDir(Context context) {
        String rootDir = ZCacheConfig.instance().diskCacheRootDir;

        if (rootDir == null) {
            rootDir = context.getCacheDir().getAbsolutePath();
        }
        return rootDir;
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, File cacheDirFie) {
        return disk(context, parentDir, cacheDir, ZCacheConfig.instance().maxDiskCacheSize);
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     */
    public static ICacheHelper<DiskCache> disk(Context context, String parentDir, String cacheDir, int maxSize) {
        String absoluteDir = parentDir + cacheDir;

        SingleLevelCacheHelper<DiskCache> cacheHelper = diskCacheAdapterMap.get(absoluteDir);
        if (cacheHelper == null) {
            final int appVersion = CacheUtil.getVersionCode(context);
            cacheHelper
                    = new SingleLevelCacheHelper<>(new DiskCache(appVersion, absoluteDir, maxSize));
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
    public static ICacheHelper twoLevel(Context context, int maxMemoryCacheSize, String parentDir, String cacheDir, int maxDiskCacheSize) {
        return new DoubleLevelCacheHelper(memory(maxMemoryCacheSize), disk(context, parentDir, cacheDir, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context, String parentDir, String cacheDir, int maxDiskCacheSize) {
        return new DoubleLevelCacheHelper(memory(), disk(context, parentDir, cacheDir, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context, int maxMemoryCacheSize, String cacheDir, int maxDiskCacheSize) {
        return new DoubleLevelCacheHelper(memory(maxMemoryCacheSize), disk(context, cacheDir, maxDiskCacheSize));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context, int maxMemoryCacheSize, String cacheDir) {
        return new DoubleLevelCacheHelper(memory(maxMemoryCacheSize), disk(context, cacheDir));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context, int maxMemoryCacheSize) {
        return new DoubleLevelCacheHelper(memory(maxMemoryCacheSize), disk(context));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context) {
        return new DoubleLevelCacheHelper(memory(), disk(context));
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     */
    public static ICacheHelper twoLevel(Context context, String cacheDir) {
        return new DoubleLevelCacheHelper(memory(), disk(context, cacheDir));
    }


}
