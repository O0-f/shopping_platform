package org.example.controller;

import org.example.utils.EncryptUtils;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController extends BaseController {

    private Map<String, String> keyPair;

    @Autowired
    public MainController(CacheManager cacheManager) {
        Cache cache = cacheManager.getCache("Cache");
        assert cache != null;
        if (cache.get("PublicKey", String.class) == null || cache.get("PrivateKey", String.class) == null) {
            try {
                keyPair = EncryptUtils.generateKeyPair();
                cache.put("PublicKey", keyPair.get("PublicKey"));
                cache.put("PrivateKey", keyPair.get("PrivateKey"));
            } catch (Exception exception) {
                handleException(exception);
            }
        } else {
            keyPair = new HashMap<>();
            keyPair.put("PublicKey", cache.get("PublicKey", String.class));
            keyPair.put("PrivateKey", cache.get("PrivateKey", String.class));
        }
    }

    @RequestMapping(value = "getPublicKey")
    @ResponseBody
    public ResultMap getPublicKey() {
        ResultMap.setInstance(Status.SUCCESS.getStatus(), "Public Key.", keyPair.get("PublicKey"));
        return ResultMap.getInstance();
    }
}
