package eu.plumbr.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableCaching
public class CacheApplication {

  public static void main(String[] args) {
    SpringApplication.run(CacheApplication.class, args);
  }

  @Component("service")
  public static class Service {

    private final Cache cache;

    public Service(CacheManager cacheManager) {
      cache = cacheManager.getCache("time");
    }

    public long noCache(String dummy) {
      return System.currentTimeMillis();
    }

    @Cacheable("time")
    public long annotationBased(String dummy) {
      return System.currentTimeMillis();
    }

    public long manual(String dummy) {
      Cache.ValueWrapper valueWrapper = cache.get(dummy);
      long result;
      if (valueWrapper == null) {
        result = System.currentTimeMillis();
        cache.put(dummy, result);
      } else {
        result = (long) valueWrapper.get();
      }
      return result;
    }
  }
}

