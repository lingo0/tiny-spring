package us.codecraft.tinyioc.beans.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource是spring内部定位资源的接口。
 *
 * 说明：标识一个外部资源。通过getInputStream方法 获取资源的输入流
 * @author yihua.huang@dianping.com
 */
public interface Resource {

    /**
     * 通过getInputStream方法 获取资源的输入流
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;
}
