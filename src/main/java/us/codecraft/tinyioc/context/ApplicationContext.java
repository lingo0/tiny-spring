package us.codecraft.tinyioc.context;

import us.codecraft.tinyioc.beans.factory.BeanFactory;

/**
 * 以 ApplicationContext 接口为核心发散出的几个类，
 * 主要是对前面 Resouce 、 BeanFactory、BeanDefinition 进行了功能的封装，解决 根据地址获取 IoC 容器并使用 的问题。
 *
 * 标记接口，继承了 BeanFactory。
 * 通常，要实现一个IoC容器时，需要先通过ResourceLoader 获取一个 Resource，其中包括了容器的配置，Bean的定义信息。
 * 最后，把 BeanDefinition 保存在 BeanFactory 中，容器配置完毕可以使用。注意到BeanFactory只实现了Bean的 装配，获取，
 * 并未说明Bean的来源，也就是BeanDefinition 是如何加载的。
 *
 * 该接口把 BeanFactory 和 BeanDefinitionReader 结合在了一起。
 *
 * @author yihua.huang@dianping.com
 */
public interface ApplicationContext extends BeanFactory {
}
