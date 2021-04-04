package org.example;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EhcacheTest {

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
}
