package com.hh.appraisal.springboot.core.config;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Ehcache Config
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Component
public class EhcacheConfig {

    /**
     * 本项目的cache名称
     */
    @Value("${spring.application.name}")
    private String appName;

    /**
     * 获取缓存管理器
     * @return
     */
    @Bean
    public CacheManager getCacheManager(){
        // 本项目的cache
        CacheConfiguration config = new CacheConfiguration();
        config.setName(appName);

        //是否永不过期，为false则过期需要通过timeToIdleSeconds，timeToLiveSeconds判断
        config.setEternal(false);

        //最少使用
        config.setMemoryStoreEvictionPolicy("LFU");

        //内存中存放的最大记录数
        config.setMaxElementsInMemory(10000);

        config.setMaxElementsOnDisk(20000);

        //内存中过多则存入硬盘
        config.setOverflowToDisk(true);

        //重启服务后是否恢复缓存
        config.setDiskPersistent(false);

        // 设置ehcache配置文件，获取CacheManager
        Configuration configuration = new Configuration();
        configuration.addCache(config);
        CacheManager cacheManager = CacheManager.newInstance(configuration);

        // 将CacheManager注册为bean，供缓存工具类使用
        return cacheManager;
    }
}
