package com.harry.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件的读取工具类
 */
public class PropertiesUtils {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
	private static volatile PropertiesUtils instance = new PropertiesUtils();
	private final String BUILDER_FILE = "apply.properties";
	private Properties properties;

	private PropertiesUtils() {
		properties = new Properties();
		InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(BUILDER_FILE);
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(String.format("加载配置文件%s失败！", BUILDER_FILE));
		}
	}

	/**
	 * 获取工具类实例
	 * @return
	 */
	public static PropertiesUtils getInstance() {
		return instance;
	}

	/**
	 * 获取指定key的配置信息
	 * 
	 * @param key -- key
	 * @param defaultValue-- 默认值
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		String value = properties.getProperty(key, defaultValue);
		return value == null ? null : value.trim();
	}

	/**
	 * 获取指定key的配置信息
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		String value = properties.getProperty(key);
		return value == null ? null : value.trim();
	}

}
