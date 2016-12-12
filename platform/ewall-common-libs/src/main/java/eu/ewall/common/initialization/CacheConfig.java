package eu.ewall.common.initialization;

import java.util.Collections;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.spymemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.spring.SSMCache;
import com.google.code.ssm.spring.SSMCacheManager;

/**
 * The Class CacheConfig.
 */
@EnableCaching
@Configuration
public class CacheConfig {
  
	/** The env. */
	@Autowired
	//private Properties properties;
	private Environment env;
	
	/**
	 * Cache manager.
	 *
	 * @return the SSM cache manager
	 */
	@Bean
	public SSMCacheManager cacheManager() {
		Cache cache;
		try {
			cache = defaultCache().getObject();
		} catch (Exception e) {
			throw new BeanCreationException("Could not create cache", e);
		}
		SSMCache serviceCache = new SSMCache(cache, 600);
		SSMCacheManager cacheManager = new SSMCacheManager();
		cacheManager.setCaches(Collections.singletonList(serviceCache));
		return cacheManager;
	}
    
	/**
	 * Default cache.
	 *
	 * @return the cache factory
	 */
	@Bean
	public CacheFactory defaultCache(){
		String cacheName= env.getProperty("cache.name");

		CacheFactory cf = new CacheFactory();
		cf.setCacheName(cacheName);
		cf.setCacheClientFactory(new MemcacheClientFactoryImpl());
		DefaultAddressProvider addressProvider = new DefaultAddressProvider();
		String memcachedHost= env.getProperty("memcached.address");
		String memcachedPort= env.getProperty("memcached.port");
		if(memcachedHost == null || memcachedHost.equals("")){
			memcachedHost = "127.0.0.1";
		}
		if(memcachedPort == null || memcachedPort.equals("")){
			memcachedPort = "11211";
		}
		addressProvider.setAddress(memcachedHost+":"+memcachedPort);
		cf.setAddressProvider(addressProvider);
		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setConsistentHashing(true);
		cf.setConfiguration(cacheConfiguration);
		return cf;
	}


}
