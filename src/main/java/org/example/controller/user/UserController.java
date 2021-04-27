package org.example.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.utils.EncryptUtils;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.example.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    private final UserService userService;
    private final Cache cache;

    @Autowired
    public UserController(UserService userService, CacheManager cacheManager) {
        this.userService = userService;
        cache = cacheManager.getCache("Cache");
    }

    @RequestMapping(value = "verifyUser")
    @ResponseBody
    public ResultMap verifyUser(@RequestParam Map<String, Object> params) {
        try {
            User user = userService.find("id", params.get("id"));
            if (user == null) {
                ResultMap.setInstance(Status.SUCCESS.getStatus(), "Verification success.", true);
            } else {
                ResultMap.setInstance(Status.SUCCESS.getStatus(), "Verification success.", false);
            }
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "signUp")
    @ResponseBody
    public ResultMap signUp(@RequestBody String param) {
        try {
            String privateKeyString = cache.get("PrivateKey", String.class);
            Key privateKey = EncryptUtils.getPrivateKey(privateKeyString);
            String json = EncryptUtils.RSADecrypt(param, privateKey);
            JSONObject jsonObject = JSON.parseObject(json);
            User user = new User();
            user.setId(jsonObject.getString("id"));
            user.setType("user");
            user.setName(jsonObject.getString("name"));
            user.setPhone(jsonObject.getString("phone"));
            user.setPassword(EncryptUtils.SHAEncrypt(jsonObject.getString("password")));
            userService.save(user);
            Token token = new Token();
            token.setUser_id(user.getId());
            token.setDevice_id(jsonObject.getString("device_id"));
            token.setDevice_type(jsonObject.getString("device_type"));
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(user.getId(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", userService.findView(user.getId()));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Sign up successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "signIn")
    @ResponseBody
    public ResultMap signIn(@RequestBody String param) {
        try {
            String privateKeyString = cache.get("PrivateKey", String.class);
            Key privateKey = EncryptUtils.getPrivateKey(privateKeyString);
            String json = EncryptUtils.RSADecrypt(param, privateKey);
            JSONObject jsonObject = JSON.parseObject(json);
            User user = userService.find("id", jsonObject.getString("id"));
            if (user == null) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "Id does not exist.", null);
            } else if (! "user".equals(user.getType())) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "User permission error.", null);
            } else if (! EncryptUtils.SHACompare(EncryptUtils.SHAEncrypt(jsonObject.getString("password")), user.getPassword())) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "Incorrect user id or password.", null);
            } else {
                String publicKeyString = cache.get("PublicKey", String.class);
                Key publicKey = EncryptUtils.getPublicKey(publicKeyString);
                Token token = new Token();
                token.setUser_id(jsonObject.getString("id"));
                token.setDevice_id(EncryptUtils.RSAEncrypt(jsonObject.getString("device_id"), publicKey));
                token.setDevice_type(EncryptUtils.RSAEncrypt(jsonObject.getString("device_type"), publicKey));
                token.setTimestamp(new Timestamp(System.currentTimeMillis()));
                cache.put(jsonObject.getString("id"), token);
                Map<String, Object> result = new HashMap<>();
                result.put("token", token);
                result.put("user", userService.findView(user.getId()));
                ResultMap.setInstance(Status.SUCCESS.getStatus(), "Sign in successfully.", result);
            }
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "signOut")
    @ResponseBody
    public ResultMap signOut(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(id, Token.class))) {
                throw new Exception("Token error.");
            }
            cache.evict(id);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Sign out successfully.", null);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updateName")
    @ResponseBody
    public ResultMap updateName(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String newName = jsonObject.getString("new_name");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(id, Token.class))) {
                throw new Exception("Token error.");
            }
            userService.update(id, "name", newName);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(id, token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", userService.findView(id));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update name successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updatePhone")
    @ResponseBody
    public ResultMap updatePhone(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String newPhone = jsonObject.getString("new_phone");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(id, Token.class))) {
                throw new Exception("Token error.");
            }
            userService.update(id, "phone", newPhone);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", userService.findView(id));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update phone successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updatePassword")
    @ResponseBody
    public ResultMap updatePassword(@RequestBody String param) {
        try {
            String privateKeyString = cache.get("PrivateKey", String.class);
            Key privateKey = EncryptUtils.getPrivateKey(privateKeyString);
            String json = EncryptUtils.RSADecrypt(param, privateKey);
            JSONObject jsonObject = JSON.parseObject(json);
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(jsonObject.getString("id"), Token.class))) {
                throw new Exception("Token error.");
            }
            userService.update(jsonObject.getString("id"), "password", EncryptUtils.SHAEncrypt(jsonObject.getString("new_password")));
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(jsonObject.getString("id"), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", userService.findView(jsonObject.getString("id")));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update password successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
