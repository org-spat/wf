package org.spat.wf.mvc.cache;

import java.util.Map;

import org.spat.wf.mvc.WFConfig;

public abstract class Cache {

	protected WFConfig.Cache config ;
	public Cache(WFConfig.Cache config){
		this.config = config;
	}
	/*
	 * 查看数据是否存在
	 * @param key:key值
	 * @return 成功返回true,失败返回false
	 */
	abstract boolean containsKey(String key) throws Exception;
	
	/*
	 * 获取数据
	 * @param key:key值
	 * @return 成功返回缓存数据,失败返回null
	 */
	abstract Object get(String key) throws Exception;
	
	/*
	 * 获取数据
	 * @param keys:key数组
	 * @return 成功返回缓存数据,失败返回null
	 */
	abstract Map<String, Object> getMultiValue(String ...keys) throws Exception;
	
	/*
	 * 存储数据缓存，不存在添加，存在就替换
	 * @param key:key值
	 * @param value:缓存数据
	 * @param expiry:缓存时间，单位秒
	 * @return 成功返回true,失败返回false
	 */
	abstract boolean set(String key,Object value,int expiry) throws Exception;
	
	/*
	 * 存储数据缓存，如果已存在则抛出异常
	 * @param key:key值
	 * @param value:缓存数据
	 * @param expiry:缓存时间，单位秒
	 * @return 成功返回true,失败返回false
	 * @throws AlreadyeExistsException 如果key已经存在
	 */
	abstract boolean add(String key,Object value,int expiry) throws Exception;
	
	/*
	 * 删除数据缓存
	 * @param key:key值
	 * @return 成功返回true,失败返回false
	 */
	abstract boolean delete(String key) throws Exception;
	
	/*
	 * 替换已经存在的缓存
	 * @param key:key值
	 * @param value:缓存数据
	 * @param expiry:缓存时间，单位秒
	 * @return 成功返回true,失败返回false
	 * @throws NotFoundException 当key不存在时返回异常
	 */
	abstract boolean replace(String key,Object value,int expiry) throws Exception;
}
