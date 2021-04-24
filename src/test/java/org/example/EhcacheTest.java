package org.example;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-mvc.xml", "classpath*:spring-hibernate.xml", "classpath*:spring-ehcache.xml"})
@WebAppConfiguration
public class EhcacheTest {

    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    @Test
    public void Test() {
        URL url = getClass().getResource("/ehcache.xml");
        System.out.println(url);
        CacheManager cacheManager = CacheManager.create(url);
        Cache cache = cacheManager.getCache("Cache");
        cache.put(new Element("Test", "WangYiFei"));
        System.out.println("Key: " + cache.get("Test").getObjectKey());
        System.out.println("Value: " + cache.get("Test").getObjectValue());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Creation time: " + simpleDateFormat.format(new Date(cache.get("Test").getCreationTime())));
        System.out.println("Expiration time: " + simpleDateFormat.format(new Date(cache.get("Test").getExpirationTime())));
        cache.remove("Test");
        System.out.println(cache.get("Test"));
    }

    @Test
    public void autowiredTest() {
        org.springframework.cache.Cache cache = cacheManager.getCache("Cache");
        cache.put("Test", "Fei");
        System.out.println("Key: " + "Test");
        System.out.println("Value: " + cache.get("Test", String.class));
        cache.evict("Test");
        System.out.println(cache.get("Test"));
    }
}
