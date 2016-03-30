package org.spat.wf.mvc.cache;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ehcache.CacheManager;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.config.CacheConfigurationBuilder;
import org.spat.wf.mvc.WFConfig;

import javassist.NotFoundException;

public class Localcache extends Cache {

	 private org.ehcache.Cache<String, Object> localCache =null;
	public Localcache(WFConfig.Cache config)  throws Exception {
		super(config);
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
		          .withCache("preConfigured",
		               CacheConfigurationBuilder.newCacheConfigurationBuilder().buildConfig(String.class, Object.class))
		          .build(true);
		localCache = cacheManager.createCache("LocalCache",
		          CacheConfigurationBuilder.newCacheConfigurationBuilder().buildConfig(String.class, Object.class));
	}

	@Override
	boolean containsKey(String key)  throws Exception {
		return localCache.containsKey(key);
	}

	@Override
	Object get(String key)  throws Exception{
		return localCache.get(key);
	}

	@Override
	Map<String, Object> getMultiValue(String... keys)  throws Exception{
		Set<String> keyset =new HashSet<String>(Arrays.asList(keys));
		return localCache.getAll(keyset);
	}

	@Override
	boolean set(String key, Object value, int expiry)  throws Exception{
		localCache.put(key, value);
		return true;
	}

	@Override
	boolean add(String key, Object value, int expiry) throws Exception {
		localCache.put(key, value);
		return true;
	}

	@Override
	boolean delete(String key)  throws Exception{
		localCache.remove(key);
		return true;
	}

	@Override
	boolean replace(String key, Object value, int expiry) throws NotFoundException {
		localCache.remove(key);
		localCache.put(key, value);
		return true;
	}

}
