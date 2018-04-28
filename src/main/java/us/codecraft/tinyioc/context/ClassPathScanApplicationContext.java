package us.codecraft.tinyioc.context;

import us.codecraft.tinyioc.BeanReference;
import us.codecraft.tinyioc.beans.BeanDefinition;
import us.codecraft.tinyioc.beans.PropertyValue;
import us.codecraft.tinyioc.beans.factory.AbstractBeanFactory;
import us.codecraft.tinyioc.beans.factory.AutowireCapableBeanFactory;
import us.codecraft.tinyioc.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 从包路径扫描和加载的具体实现类。
 * 内部通过扫描对应的包来获取类信息并解析成BeanDefinition。
 * 然后将其保存到内置的 BeanFactory 中。
 *
 * @author muzhi
 * 18/4/27
 */
public class ClassPathScanApplicationContext extends AbstractApplicationContext {

	// 先显示的传入包路径
	private String configLocation;

	public ClassPathScanApplicationContext(String configLocation) throws Exception {
		this(configLocation, new AutowireCapableBeanFactory());
	}

	public ClassPathScanApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
		super(beanFactory);
		this.configLocation = configLocation;
		refresh();
	}


	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
		// todo 后续继续封装成Scanner
		Set<Class<?>> classes = ClassUtils.getClassSet(this.configLocation);
		Map<String, BeanDefinition> tempFactory = new HashMap<String, BeanDefinition>();
		for (Class cl : classes) {
			BeanDefinition beanDefinition = new BeanDefinition();
			String className = cl.getName();

			beanDefinition.setBeanClassName(className);
			beanDefinition.setBeanClass(cl);

			// 注入属性 默认使用@Resource
			Field[] beanFields = cl.getDeclaredFields();
			for (Field beanField : beanFields) {
				if (beanField.isAnnotationPresent(Resource.class)) {
					String fieldName = beanField.getName();
					BeanReference beanReference = new BeanReference(fieldName);
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(fieldName, beanReference));
				}
			}
			tempFactory.put(className, beanDefinition);
		}

		for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : tempFactory.entrySet()) {
			beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
		}

	}
}
