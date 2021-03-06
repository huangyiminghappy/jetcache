package com.alicp.jetcache;

/**
 * Created on 2016/11/1.
 *
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
public interface DelegateCache<K, V> {
    Cache<K, V> getTargetCache();
}
