package us.codecraft.tinyioc.beans.io;

import java.net.URL;

/**
 * 资源加载类
 * 通过getResource方法获取一个Resource对象，是获取Resouce的主要途径
 *
 * 注： 这里在设计上有一定的问题，
 * ResourceLoader 直接返回了一个 UrlResource，
 * 更好的方法是声明一个 ResourceLoader 接口，
 * 再实现一个 UrlResourceLoader 类用于加载 UrlResource。
 *
 * @author yihua.huang@dianping.com
 */
public class ResourceLoader {

    public Resource getResource(String location){
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(resource);
    }
}
