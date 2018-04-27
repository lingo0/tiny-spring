package us.codecraft.tinyioc.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * us.codecraft.tinyioc.util
 * <p>
 * 类工具类
 * 提供与类操作相关的方法，比如获取类加载器, 加载类
 *
 * @author muzhi
 * 18/4/26
 */
public class ClassUtils {

	/**
	 * 获取类加载器
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}

		if (cl == null) {
			// 如果没有当前线程的ClassLoader 则 使用当前类的ClassLoader
			cl = ClassUtils.class.getClassLoader();
			if (cl == null) {
				try {
					// 如果还是null，则获取启动程序的ClassLoader
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
	}

	/**
	 * 获取类
	 */
	public static Class<?> loadClass(String className, boolean initialize) throws ClassNotFoundException {
		return loadClass(className, initialize, getDefaultClassLoader());
	}

	/**
	 * 获取类
	 *
	 * @param className 类名
	 * @param initialize 是否初始化，一般选false，交给IOC容器去初始化
	 * @param loader 类加载器
	 * @return 类
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String className, boolean initialize, ClassLoader loader)
			throws ClassNotFoundException {
		return Class.forName(className, initialize, loader);
	}


	/**
	 * 获取指定包下面的类
	 * 根据包名并将其转换成文件路径，读取class文件或jar包，获取指定的类名去加载类。
	 */
	public static Set<Class<?>> getClassSet(String location) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();

		try {
			Enumeration<URL> urls = getDefaultClassLoader().getResources(location.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if (protocol.equals("file")) {
						String packagePath = url.getPath().replaceAll("%20", "");
						addClass(classSet, packagePath, location);
					} else if (protocol.equals("jar")) {
						// 获取Jar包中的类
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if (jarURLConnection != null) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if (jarFile != null) {
								Enumeration<JarEntry> jarEntrys = jarFile.entries();
								while (jarEntrys.hasMoreElements()) {
									JarEntry jarEntry = jarEntrys.nextElement();
									String jarEntryName = jarEntry.getName();
									if (jarEntryName.endsWith(".class")) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}

						}
					}
				}
			}


		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return classSet;
	}

	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		final File[] files = new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
			}
		});

		for (File file : files) {
			String fileName = file.getName();
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (className != null) {
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			} else {
				String subPackagePath = fileName;
				if (packagePath != null) {
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				String subPackageName = fileName;
				if (packageName != null) {
					subPackageName = packageName + "." + subPackageName;
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
	}

	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> cls = null;
		try {
			cls = loadClass(className, false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		classSet.add(cls);
	}

	public static void main(String[] args) {
		Set<Class<?>> set = getClassSet("us.codecraft.tinyioc");
	}

}
