package org.example.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.entity.Address;
import org.example.entity.Store;
import org.example.service.AddressService;
import org.example.service.StoreService;
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
public class AddressController extends BaseController {

    private final AddressService addressService;
    private final StoreService storeService;
    private final Cache cache;

    @Autowired
    public AddressController(AddressService addressService, StoreService storeService, CacheManager cacheManager) {
        this.addressService = addressService;
        this.storeService = storeService;
        cache = cacheManager.getCache("Cache");
    }

    @RequestMapping(value = "addAddress")
    @ResponseBody
    public ResultMap addAddress(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String lng = jsonObject.getString("lng");
            String lat = jsonObject.getString("lat");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(id, Token.class))) {
                throw new Exception("Token error.");
            }
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", id);
            params.put("name", name);
            params.put("lng", lng);
            params.put("lat", lat);
            addressService.save(params);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(id, token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("address", addressService.find(id));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Add address successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "deleteAddress")
    @ResponseBody
    public ResultMap deleteAddress(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String addressId = jsonObject.getString("address_id");
            String userId = jsonObject.getString("user_id");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(userId, Token.class))) {
                throw new Exception("Token error.");
            }
            addressService.delete(addressId);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("address", addressService.find(userId));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Delete address successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updateAddress")
    @ResponseBody
    public ResultMap updateAddress(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String addressId = jsonObject.getString("address_id");
            String addressName = jsonObject.getString("address_name");
            String addressLng = jsonObject.getString("address_lng");
            String addressLat = jsonObject.getString("address_lat");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(id, Token.class))) {
                throw new Exception("Token error.");
            }
            Address address = new Address();
            address.setId(addressId);
            address.setUser_id(id);
            address.setName(addressName);
            address.setLng(addressLng);
            address.setLat(addressLat);
            addressService.update(address);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(jsonObject.getString("id"), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("address", addressService.find(id));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update address successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getAddress")
    @ResponseBody
    public ResultMap getAddress(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String userId = jsonObject.getString("user_id");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(userId, Token.class))) {
                throw new Exception("Token error.");
            }
            List<Address> addressList = addressService.find(userId);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(jsonObject.getString("user_id"), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("address", addressList);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get address successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getStores")
    @ResponseBody
    public ResultMap getStores(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<Store> storeList = storeService.findAll();
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("store", storeList);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get stores successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
