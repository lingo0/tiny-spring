package us.codecraft.tinyioc.beans;

/**
 * 从配置中读取BeanDefinition
 * 说明：解析BeanDefiniton接口，通过loadBeanDefinitions来从一个地址加载类定义。
 * @author yihua.huang@dianping.com
 */
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;
}
