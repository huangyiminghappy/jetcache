package com.alicp.jetcache;

import com.alicp.jetcache.support.FastjsonKeyConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2016/10/7.
 *
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    private static Logger logger = LoggerFactory.getLogger(AbstractCache.class);

    protected abstract Object buildKey(K key);

    protected void logError(String oper, Object key, Throwable e) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("jetcache(")
                .append(this.getClass().getSimpleName()).append(") ")
                .append(oper)
                .append(" error. key=")
                .append(FastjsonKeyConvertor.INSTANCE.apply(key))
                .append(".");
        if (needLogStackTrace(e)) {
            logger.error(sb.toString(), e);
        } else {
            sb.append(" Exception=").append(e.getClass()).append(", Message:").append(e.getMessage());
            logger.warn(sb.toString());
        }

    }

    protected boolean needLogStackTrace(Throwable e) {
//        if (e instanceof CacheEncodeException) {
//            return true;
//        }
//        return false;
        return true;
    }
}
