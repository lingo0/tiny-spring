package us.codecraft.tinyioc.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于jdk的动态代理
 *
 * @author yihua.huang@dianping.com
 */

/**
 * 假设以下情况：
 * 对象 obj 实现了 IObj 接口，接口中有一个方法func（Object[] args）
 * 对象 handler 是 InvocationHandler 的实例。
 * 那么，通过 Proxy 的 newProxyInstance 可以返回 obj 的代理对象 proxy。
 * <p>
 * 当调用 proxy.func(args)时，对象内部将委托给 handler.invoke(proxy, func, args) 函数实现。
 * 因此，在 handler 的 invoke 中，可以完全对方法拦截的处理。
 * 可以先判断是不是要拦截的方法，如果是，进行拦截 比如先做一些操作，在调用原来的方法，对应了Spring的前置通知；
 * 如果不是，则调用原来的方法。
 */

public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

	public JdkDynamicAopProxy(AdvisedSupport advised) {
		super(advised);
	}

	@Override
	public Object getProxy() {

		/**
		 * Proxy 来自 JDK API。提供生成对象的动态代理功能，
		 * 通过Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h) 方法返回一个代理对象
		 */
		return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getInterfaces(), this);
	}

	/**
	 * InvocationHandler 来自 JDK API。
	 * 通过 Object invoke(Object proxy, Method method,Object[] args) 方法实现代理对象中方法的调用和其他处理。
	 *
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
		if (advised.getMethodMatcher() != null
				&& advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
			return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(),
					method, args));
		} else {
			return method.invoke(advised.getTargetSource().getTarget(), args);
		}
	}


}
