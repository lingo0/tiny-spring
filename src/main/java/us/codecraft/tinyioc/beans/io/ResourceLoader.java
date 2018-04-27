package us.codecraft.tinyioc.beans.io;

/**
 * us.codecraft.tinyioc.beans.io
 * 资源加载类接口
 *
 * @author muzhi
 * 18/4/25
 */
public interface ResourceLoader {

	/**
	 * Return a Resource handle for the specified resource location.
	 * <p>The handle should always be a reusable resource descriptor,
	 * allowing for multiple {@link Resource#getInputStream()} calls.
	 * <p>
	 * @param location the resource location
	 * @return a corresponding Resource handle (never {@code null})
	 */
	Resource getResource(String location);

}
