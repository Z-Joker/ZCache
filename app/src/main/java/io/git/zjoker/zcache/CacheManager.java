package io.git.zjoker.zcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.git.zjoker.zcache.core.DiskCacheCore;
import io.git.zjoker.zcache.core.MemoryCacheCore;
import io.git.zjoker.zcache.helper.DoubleCacheHelper;
import io.git.zjoker.zcache.helper.ICacheHelper;
import io.git.zjoker.zcache.helper.SingleCacheHelper;

public class CacheManager {
    private static SingleCacheHelper _memoryCacheAdapter;
    private static Map<String, SingleCacheHelper> _diskCacheAdapterMap = new ConcurrentHashMap<>();

    public static ICacheHelper memoryCache() {
        if (_memoryCacheAdapter == null) {
            _memoryCacheAdapter = new SingleCacheHelper(new MemoryCacheCore(CacheConfigManager.instance()._memoryCacheSize));
        }
        return _memoryCacheAdapter;
    }

    public static ICacheHelper diskCache(String cacheDir) {
        String absoluteDir = CacheConfigManager.instance()._diskCacheRootPath + cacheDir;

        SingleCacheHelper cacheHelper = _diskCacheAdapterMap.get(absoluteDir);
        if (cacheHelper == null) {
            final int appVersion = CacheConfigManager.instance()._appVersion;
            final int diskCacheSize = CacheConfigManager.instance()._diskCacheSize;
            cacheHelper
                    = new SingleCacheHelper(new DiskCacheCore(appVersion, absoluteDir, diskCacheSize));
            _diskCacheAdapterMap.put(absoluteDir, cacheHelper);
        }
        return cacheHelper;
    }


    public static ICacheHelper doubleCache(String cacheDir) {
        return new DoubleCacheHelper(
                (MemoryCacheCore) memoryCache().getCacheCore()
                , (DiskCacheCore) diskCache(cacheDir).getCacheCore());
    }
}
