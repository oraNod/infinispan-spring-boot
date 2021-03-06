package test.org.infinispan.spring.starter.embedded.actuator;

import static java.util.Collections.emptyList;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.starter.embedded.actuator.InfinispanCacheMeterBinder;
import org.junit.AfterClass;

import io.micrometer.core.instrument.binder.cache.CacheMeterBinder;
import io.micrometer.core.instrument.binder.cache.CacheMeterBinderCompatibilityKit;

public class InfinispanCacheMetricBinderTest extends CacheMeterBinderCompatibilityKit {

   private EmbeddedCacheManager cacheManager;
   private Cache<String, String> cache;

   @AfterClass
   void cleanup() {
      cacheManager.stop();
   }

   @Override
   public CacheMeterBinder binder() {
      cacheManager = new DefaultCacheManager();
      cache = cacheManager.administration().getOrCreateCache("mycache", new ConfigurationBuilder().jmxStatistics().enable().build());
      return new InfinispanCacheMeterBinder(cache, emptyList());
   }

   @Override
   public void put(String key, String value) {
      cache.put(key, value);
   }

   @Override
   public String get(String key) {
      return cache.get(key);
   }
}
