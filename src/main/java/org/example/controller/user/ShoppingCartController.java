package org.example.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.dto.ShoppingCartDto;
import org.example.service.ShoppingCartService;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.example.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class ShoppingCartController extends BaseController {

    private final ShoppingCartService shoppingCartService;
    private final Cache cache;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, CacheManager cacheManager) {
        this.shoppingCartService = shoppingCartService;
        cache = cacheManager.getCache("Cache");
    }

    @RequestMapping(value = "addShoppingCart")
    @ResponseBody
    public ResultMap addShoppingCart(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String userId = jsonObject.getString("user_id");
            String commodityId = jsonObject.getString("commodity_id");
            long quantity = jsonObject.getLong("quantity");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("commodity_id", commodityId);
            params.put("quantity", quantity);
            shoppingCartService.save(params);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("shopping_cart", shoppingCartService.find(userId));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Add shopping cart successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "deleteShoppingCart")
    @ResponseBody
    public ResultMap deleteShoppingCart(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String shoppingCartName = jsonObject.getString("shopping_cart_name");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            shoppingCartService.delete(shoppingCartName);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("shopping_cart", shoppingCartService.find(token.getUser_id()));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Delete shopping cart successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updateShoppingCart")
    @ResponseBody
    public ResultMap updateShoppingCart(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String shoppingCartName = jsonObject.getString("shopping_cart_name");
            String userId = jsonObject.getString("user_id");
            long quantity = jsonObject.getLong("quantity");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(userId, Token.class))) {
                throw new Exception("Token error.");
            }
            shoppingCartService.update(shoppingCartName, quantity);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(userId, token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("shopping_cart", shoppingCartService.find(userId));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update shopping cart successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getShoppingCart")
    @ResponseBody
    public ResultMap getShoppingCart(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String userId = jsonObject.getString("user_id");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<ShoppingCartDto> shoppingCartDtos = shoppingCartService.find(userId);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("shopping_cart", shoppingCartDtos);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get shopping cart successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
