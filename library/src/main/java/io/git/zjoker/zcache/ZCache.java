package io.git.zjoker.zcache;

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
     * */
    public static ICacheHelper<MemoryCache> memory() {
        if (memoryCacheAdapter == null) {
            synchronized (ZCache.class) {
                if (memoryCacheAdapter == null) {
                    memoryCacheAdapter = new SingleLevelCacheHelper<>(new MemoryCache(ZCacheConfig.instance().maxMemoryCacheSize));
                }
            }
        }
        return memoryCacheAdapter;
    }

    /**
     * Get Disk Cache Adapter.Return different Adapter instance by different cacheDir.
     * */
    public static ICacheHelper<DiskCache> disk(String cacheDir) {
        String absoluteDir = ZCacheConfig.instance().diskCacheRootDir + cacheDir;

        SingleLevelCacheHelper<DiskCache> cacheHelper = diskCacheAdapterMap.get(absoluteDir);
        if (cacheHelper == null) {
            final int appVersion = CacheUtil.getVersionCode(ZCacheConfig.instance().context);
            final int diskCacheSize = ZCacheConfig.instance().maxDiskCacheSize;
            cacheHelper
                    = new SingleLevelCacheHelper<>(new DiskCache(appVersion, absoluteDir, diskCacheSize));
            diskCacheAdapterMap.put(absoluteDir, cacheHelper);
        }
        return cacheHelper;
    }

    /**
     * Get 2 level Cache Adapter.
     * Use Memory Cache First.
     * Use Disk if no cache or expired in memory cache.
     * Return different helper instance by different cacheDir.
     * */
    public static ICacheHelper twoLevel(String cacheDir) {
        return new DoubleLevelCacheHelper(memory(), disk(cacheDir));
    }
}
