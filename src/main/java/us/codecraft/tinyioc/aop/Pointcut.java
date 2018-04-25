package us.codecraft.tinyioc.aop;

/**
 * 切点对象可以获取一个 ClassFilter 对象和一个 MethodMatcher 对象。
 * 前者用于判断是否对某个对象进行拦截。（用于筛选代理的目标对象）
 * 后者用于判读是否对某个对象方法信息拦截（用于 在代理对象中对不同的方法进行不同的操作）
 * @author yihua.huang@dianping.com
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
