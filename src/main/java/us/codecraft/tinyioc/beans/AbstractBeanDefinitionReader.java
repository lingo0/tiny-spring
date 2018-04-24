package us.codecraft.tinyioc.beans;

import us.codecraft.tinyioc.beans.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 从配置中读取BeanDefinition
 * 说明：实现BeanDefinitionReader接口的抽象类（未具体实现loadBeanDefinitions，而是规范了BeanDefinitionReader的基本结构）
 * 内置了HashMap registry, 用于保存String - beanDefinition 的键值对。
 * 内置一个 ResourceLoader resourceLoader，用于保存类加载器。
 * 用意在于，使用时，只需要向其loadBeanDefinitions（） 传入一个资源地址，就可以自动调用其类加载器，
 * 并把解析到的BeanDefinition保存到registry中去。
 *
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private Map<String,BeanDefinition> registry;

    /**
     * 保存类加载器
     */
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
