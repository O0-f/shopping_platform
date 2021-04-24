package org.example.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.example.controller.BaseController;
import org.example.service.OrderService;
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
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final Cache cache;

    @Autowired
    public OrderController(OrderService orderService, CacheManager cacheManager) {
        this.orderService = orderService;
        cache = cacheManager.getCache("Cache");
    }

    @RequestMapping(value = "confirmOrder")
    @ResponseBody
    public ResultMap confirmOrder(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            List<String> commodityId = jsonObject.getJSONArray("commodity_id").toJavaList(String.class);
            List<Long> quantity = jsonObject.getJSONArray("quantity").toJavaList(Long.class);
            List<Double> price = jsonObject.getJSONArray("price").toJavaList(Double.class);
            String storeId = jsonObject.getString("store_id");
            String addressId = jsonObject.getString("address_id");
            Timestamp time = jsonObject.getTimestamp("time");
            String status = jsonObject.getString("status");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            Multimap<String, Object> params = HashMultimap.create();
            params.putAll("commodity_id", commodityId);
            params.putAll("quantity", quantity);
            params.putAll("price", price);
            params.put("store_id", storeId);
            params.put("address_id", addressId);
            params.put("time", time);
            params.put("status", status);
            orderService.save(params);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("order", orderService.find(token.getUser_id()));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Confirm order successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "cancelOrder")
    @ResponseBody
    public ResultMap cancelOrder(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String userId = jsonObject.getString("user_id");
            String orderName = jsonObject.getString("order_name");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(userId, Token.class))) {
                throw new Exception("Token error.");
            }
            orderService.delete(orderName);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(userId, token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("order", orderService.find(userId));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Cancel order successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "confirmReceipt")
    @ResponseBody
    public ResultMap confirmReceipt(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String userId = jsonObject.getString("user_id");
            String orderName = jsonObject.getString("order_name");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            orderService.update(orderName, "Completed");
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("order", orderService.find(userId));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Confirm receipt successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getOrders")
    @ResponseBody
    public ResultMap getOrders(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<Multimap<String, Object>> orders = orderService.find(token.getUser_id());
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("order", orders);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get orders successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
