注 1：在 Spring 的实现中，对 ApplicatinoContext 的分层更为细致。
AbstractApplicationContext 中为了实现 不同来源 的 不同类型 的资源加载类定义，把这两步分层实现。
以“从类路径读取 XML 定义”为例，首先使用 AbstractXmlApplicationContext 来实现 不同类型 的资源解析，
接着，通过 ClassPathXmlApplicationContext 来实现 不同来源 的资源解析。 

注 2：在 tiny-spring 的实现中，先用 BeanDefinitionReader 读取 BeanDefiniton 后，
保存在内置的 registry （键值对为 String - BeanDefinition 的哈希表，通过 getRigistry() 获取）中，
然后由 ApplicationContext 把 BeanDefinitionReader 中 registry 的键值对一个个赋值给 BeanFactory 中保存的 beanDefinitionMap。
而在 Spring 的实现中， 直接操作 BeanDefinition ，它的 getRegistry() 获取的不是内置的 registry，而是 BeanFactory 的实例。
如何实现呢？以 DefaultListableBeanFactory 为例，它实现了一个 BeanDefinitonRigistry 接口，
该接口把 BeanDefinition 的 注册 、获取 等方法都暴露了出来，
这样，BeanDefinitionReader 可以直接通过这些方法把 BeanDefiniton 直接加载到 BeanFactory 中去。