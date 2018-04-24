package us.codecraft.tinyioc.beans.factory;

import us.codecraft.tinyioc.beans.BeanDefinition;
import us.codecraft.tinyioc.beans.BeanPostProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的一种抽象类实现，规范了IoC 容器的基本结构，
 * 但是把生成Bean的具体实现方式留给子类实现。
 * IoC容器的结构：AbstractBeanFactory 维护一个beanDefinitionMap 哈希表用于保存类的定义信息。
 * 获取bean时，如果Bean已经存在于容器中，则返回之，否则通过调用doCreateBean 方法装备一个Bean。
 * （所谓存在于容器中，是指容器可以通过 beanDefinitionMap 获取 BeanDefinition 进而通过其 getBean() 方法获取 Bean。）
 *
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanFactory implements BeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	@Override
	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		Object bean = beanDefinition.getBean();
		if (bean == null) {
			// 实例化 Bean
			bean = doCreateBean(beanDefinition);
			// 初始化 Bean
            bean = initializeBean(bean, name);
            // 装载 Bean
            beanDefinition.setBean(bean);
		}
		return bean;
	}

	/**
	 * 初始化Bean
	 *
	 * @param bean
	 * @param name
	 * @return
	 * @throws Exception
	 */
	protected Object initializeBean(Object bean, String name) throws Exception {
		// 从 BeanPostProcessor 列表中，依次取出 BeanPostProcessor 执行
		//（为什么调用 BeanPostProceesor 中提供方法时，不是直接 post...(bean,beanName) 而是 bean = post...(bean,beanName) 呢？见分析1 。
		// 另外，BeanPostProcessor 列表的获取有问题，见分析2。）
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		// TODO:call initialize method
		// 初始化方法（tiny-spring 未实现对初始化方法的支持）。

		//从 BeanPostProcessor 列表中， 依次取出 BeanPostProcessor 执行其 bean = postProcessAfterInitialization(bean,beanName)。
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
        return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}

	public void preInstantiateSingletons() throws Exception {
		for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
			String beanName = (String) it.next();
			getBean(beanName);
		}
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		// 生成一个新的实例
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		// 注入属性，包括依赖注入的过程。
		// 在依赖注入的过程中，如果 Bean 实现了 BeanFactoryAware 接口，则将容器的引用传入到 Bean 中去，
		// 这样，Bean 将获取对容器操作的权限，也就允许了 编写扩展 IoC 容器的功能的 Bean。
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}

	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List getBeansForType(Class type) throws Exception {
		List beans = new ArrayList<Object>();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

}
