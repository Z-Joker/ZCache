package io.git.zjoker.zcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.git.zjoker.zcache.core.DiskCacheCore;
import io.git.zjoker.zcache.core.MemoryCacheCore;
import io.git.zjoker.zcache.helper.DoubleLevelCacheHelper;
import io.git.zjoker.zcache.helper.ICacheHelper;
import io.git.zjoker.zcache.helper.SingleLevelCacheHelper;
import io.git.zjoker.zcache.utils.CacheUtil;

public class ZCache {
    private static volatile SingleLevelCacheHelper<MemoryCacheCore> memoryCacheAdapter;
    private static Map<String, SingleLevelCacheHelper<DiskCacheCore>> diskCacheAdapterMap = new ConcurrentHashMap<>();

    /**
     * Get Memory Cache Helper
     * */
    public static ICacheHelper<MemoryCacheCore> memoryCache() {
        if (memoryCacheAdapter == null) {
            synchronized (ZCache.class) {
                if (memoryCacheAdapter == null) {
                    memoryCacheAdapter = new SingleLevelCacheHelper<>(new MemoryCacheCore(ZCacheConfig.instance().memoryCacheSize));
                }
            }
        }
        return memoryCacheAdapter;
    }

    /**
     * Get Disk Cache Helper.Return different helper instance by different cacheDir.
     * */
    public static ICacheHelper<DiskCacheCore> diskCache(String cacheDir) {
        String absoluteDir = ZCacheConfig.instance().diskCacheRootPath + cacheDir;

        SingleLevelCacheHelper<DiskCacheCore> cacheHelper = diskCacheAdapterMap.get(absoluteDir);
        if (cacheHelper == null) {
            final int appVersion = CacheUtil.getVersionCode(ZCacheConfig.instance().context);
            final int diskCacheSize = ZCacheConfig.instance().diskCacheSize;
            cacheHelper
                    = new SingleLevelCacheHelper<>(new DiskCacheCore(appVersion, absoluteDir, diskCacheSize));
            diskCacheAdapterMap.put(absoluteDir, cacheHelper);
        }
        return cacheHelper;
    }

    /**
     * Get Double level Cache Helper.
     * Return different helper instance by different cacheDir.
     * */
    public static ICacheHelper doubleCache(String cacheDir) {
        return new DoubleLevelCacheHelper(memoryCache(), diskCache(cacheDir));
    }
}
