package org.example.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.dto.CommodityDto;
import org.example.service.CommodityService;
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
public class CommodityController extends BaseController {

    private final CommodityService commodityService;
    private final Cache cache;


    @Autowired
    public CommodityController(CommodityService commodityService, CacheManager cacheManager) {
        this.commodityService = commodityService;
        cache = cacheManager.getCache("Cache");
    }

    @RequestMapping(value = "getCommodities")
    @ResponseBody
    public ResultMap getCommodities(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<CommodityDto> commodityDtos = commodityService.findAll();
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("commodity", commodityDtos);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get commodities successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getTypes")
    @ResponseBody
    public ResultMap getTypes(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<String> types = commodityService.getTypes();
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("type", types);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get types successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getCommoditiesByType")
    @ResponseBody
    public ResultMap getCommoditiesByType(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String type = jsonObject.getString("type");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            List<CommodityDto> commodityDtos = commodityService.find(type);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("commodity", commodityDtos);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get commodities by type successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getDetails")
    @ResponseBody
    public ResultMap getDetails(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String commodityId = jsonObject.getString("commodity_id");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            String details = commodityService.getDetails(commodityId);
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("details", details);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get details successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "searchCommodity")
    @ResponseBody
    public ResultMap searchCommodity(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String commodityName = jsonObject.getString("commodity_name");
            Token token = jsonObject.getObject("token", Token.class);
            if (! token.equals(cache.get(token.getUser_id(), Token.class))) {
                throw new Exception("Token error.");
            }
            token.setTimestamp(new Timestamp(System.currentTimeMillis()));
            cache.put(token.getUser_id(), token);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("commodity", commodityService.search(commodityName));
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Search commodity successfully.", result);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
