/**
 * Created on  13-10-28 23:42
 */
package com.alicp.jetcache.test.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

/**
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
public class MyFactoryBean implements FactoryBean, InitializingBean {

    private FactoryBeanTarget target;

    public Object getObject() throws Exception {
        return target;
    }

    public Class<?> getObjectType() {
        return FactoryBeanTarget.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        final FactoryBeanTarget SRC = new FactoryBeanTargetImpl();
        target = (FactoryBeanTarget) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{FactoryBeanTarget.class},
                (proxy, method, args) -> method.invoke(SRC, args));
    }
}
