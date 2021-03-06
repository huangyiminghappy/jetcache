package com.alicp.jetcache.examples;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.MonitoredCache;
import com.alicp.jetcache.MultiLevelCache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.support.DefaultCacheMonitor;
import com.alicp.jetcache.support.DefaultCacheMonitorManager;
import com.alicp.jetcache.support.FastjsonKeyConvertor;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/11/2.
 *
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
public class CacheMonitorWithMultiLevelCacheExample {
    public static void main(String[] args) throws Exception {
        Cache<String, Integer> l1Cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .buildCache();
        Cache<String, Integer> l2Cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .buildCache();
        DefaultCacheMonitor l1CacheMonitor = new DefaultCacheMonitor("OrderCache_L1");
        DefaultCacheMonitor l2CacheMonitor = new DefaultCacheMonitor("OrderCache_L2");
        DefaultCacheMonitor orderCacheMonitor = new DefaultCacheMonitor("OrderCache");

        l1Cache = new MonitoredCache(l1Cache, l1CacheMonitor);
        l2Cache = new MonitoredCache(l2Cache, l2CacheMonitor);
        Cache<String, Integer> multiLevelCache = new MultiLevelCache<>(l1Cache, l2Cache);
        Cache<String, Integer> orderCache = new MonitoredCache<>(multiLevelCache, orderCacheMonitor);

        boolean verboseLog = true;
        DefaultCacheMonitorManager statLogger = new DefaultCacheMonitorManager(1, TimeUnit.SECONDS, verboseLog);
        statLogger.add(l1CacheMonitor, l2CacheMonitor, orderCacheMonitor);
        statLogger.start();

        Thread t = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                orderCache.put("20161111", 123456789);
                orderCache.get("20161111");
                orderCache.get("20161212");
                orderCache.remove("20161111");
                orderCache.remove("20161212");
                orderCache.computeIfAbsent("20161111", (k) -> 100000);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        });
        t.start();
        t.join();

        statLogger.stop();
    }
}
