package com.hh.appraisal.springboot.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * EhCache 缓存服务
 * 使用String格式的value
 * @author gaigai
 * @date 2019/05/15
 */
@Component
public class EhcacheService {

    /**
     * 默认的缓存存在时间（秒）
     */
    private static final int DEFAULT_LIVE_SECOND = 20 * 60;

    private final CacheManager cacheManager;

    /**
     * 本项目的cache名称
     */
    private String appName;

    public EhcacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Value("${spring.application.name}")
    public void setAppName(String appName){
        this.appName = appName;
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    public void set(String key, String value, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(appName);
        Element element = new Element(
                key, value,
                0,
                timeToLiveSeconds);
        cache.put(element);
    }

    /**
     * 添加缓存
     * 使用默认生存时间
     * @param key
     * @param value
     */
    public void set(String key, String value){
        Cache cache = cacheManager.getCache(appName);
        Element element = new Element(
                key, value,
                0,
                DEFAULT_LIVE_SECOND);
        cache.put(element);
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToIdleSeconds 对象空闲时间，指对象在多长时间没有被访问就会失效。
     *                          只对eternal为false的有效。传入0，表示一直可以访问。以秒为单位。
     * @param timeToLiveSeconds 缓存生存时间（秒）
     *                          只对eternal为false的有效
     */
    public void set(String key, String value, int timeToIdleSeconds, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(appName);
        Element element = new Element(
                key, value,
                timeToIdleSeconds,
                timeToLiveSeconds);
        cache.put(element);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public String get(String key){
        Cache cache = cacheManager.getCache(appName);
        Element element = cache.get(key);
        if(element == null){
            return null;
        }
        return (String) element.getObjectValue();
    }

    /**
     * 删除缓存
     * @param key
     */
    public void delete(String key){
        Cache cache = cacheManager.getCache(appName);
        cache.remove(key);
    }

    /**
     * 删除本项目所有缓存
     */
    public void deleteAll(){
        Cache cache = cacheManager.getCache(appName);
        cache.removeAll();
    }

    public String getAppName(){
        if(this.appName == null) {
            return "";
        }
        return this.appName;
    }
}
