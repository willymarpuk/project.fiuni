package com.fiuni.distri.project.fiuni.utils.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${ttl.seconds}")
    private int ttlseconds;

    @Value("${ttl.minutos}")
    private int ttlminutos;

    @Value("${ttl.horas}")
    private int ttlhoras;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        HashMap<String, RedisCacheConfiguration> mapcache = new HashMap<>();

        mapcache.put("Puesto", redisCacheConfiguration.entryTtl(Duration.ofSeconds(ttlseconds)));
        mapcache.put("Vacante", redisCacheConfiguration.entryTtl(Duration.ofMinutes(ttlminutos)));
        mapcache.put("AplicacionVacante", redisCacheConfiguration.entryTtl(Duration.ofHours(ttlhoras)));

        //mapcache.put("Puesto", redisCacheConfiguration.entryTtl(RedisCacheWriter.TtlFunction.persistent()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(mapcache)
                .build();
    }


}
