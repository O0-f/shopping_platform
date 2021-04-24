package org.example.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.entity.Store;
import org.example.service.StoreService;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class StoreController extends BaseController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @RequestMapping(value = "addStore")
    @ResponseBody
    public ResultMap addStore(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String name = jsonObject.getString("name");
            String lng = jsonObject.getString("lng");
            String lat = jsonObject.getString("lat");
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("lng", lng);
            params.put("lat", lat);
            storeService.save(params);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Add store successfully.", storeService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "deleteStore")
    @ResponseBody
    public ResultMap deleteStore(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String storeId = jsonObject.getString("store_id");
            storeService.delete(storeId);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Delete store successfully.", storeService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updateStore")
    @ResponseBody
    public ResultMap updateStore(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String lng = jsonObject.getString("lng");
            String lat = jsonObject.getString("lat");
            Store store = new Store();
            store.setId(id);
            store.setName(name);
            store.setLng(lng);
            store.setLat(lat);
            storeService.update(store);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update store successfully.", storeService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getStores")
    @ResponseBody
    public ResultMap getStores() {
        try {
            List<Store> storeList = storeService.findAll();
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get stores successfully.", storeList);
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
