package test.org.infinispan.spring.starter.remote;

import static org.assertj.core.api.Assertions.assertThat;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.spring.provider.SpringRemoteCacheManager;
import org.infinispan.spring.starter.remote.InfinispanRemoteAutoConfiguration;
import org.infinispan.spring.starter.remote.InfinispanRemoteCacheManagerAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.org.infinispan.spring.starter.remote.testconfiguration.InfinispanCacheTestConfiguration;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
    classes = {
      InfinispanRemoteAutoConfiguration.class,
      InfinispanRemoteCacheManagerAutoConfiguration.class,
      InfinispanCacheTestConfiguration.class
    },
    properties = {
        "spring.main.banner-mode=off",
        "infinispan.remote.client-properties=test-hotrod-client.properties"
    }
)
public class IntegrationTest {

   @Autowired
   private RemoteCacheManager remoteCacheManager;

   @Autowired
   private SpringRemoteCacheManager springRemoteCacheManager;

   @Test
   public void testConfiguredClient() {
      //when
      int portObtainedFromPropertiesFile = remoteCacheManager.getConfiguration().servers().get(0).port();

      //then
      assertThat(portObtainedFromPropertiesFile).isEqualTo(InfinispanCacheTestConfiguration.PORT);
   }

   @Test
   public void testIfSpringCachingWasCreatedUsingProperEmbeddedCacheManager() throws Exception {
      assertThat(remoteCacheManager).isEqualTo(springRemoteCacheManager.getNativeCacheManager());
   }
}
