package org.example.controller.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.service.CommodityService;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("platform")
public class PlatformCommodityController extends BaseController {

    private final CommodityService commodityService;

    @Autowired
    public PlatformCommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(value = "addCommodity")
    @ResponseBody
    public ResultMap addCommodity(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String type = jsonObject.getString("type");
            String name = jsonObject.getString("name");
            double price = jsonObject.getDouble("price");
            long quantity = jsonObject.getLong("quantity");
            long views = 0;
            String details = jsonObject.getString("details");
            Map<String, Object> params = new HashMap<>();
            params.put("type", type);
            params.put("name", name);
            params.put("price", price);
            params.put("quantity", quantity);
            params.put("views", views);
            params.put("details", details);
            params.put("img", null);
            commodityService.save(params);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Add commodity successfully.", commodityService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "deleteCommodity")
    @ResponseBody
    public ResultMap deleteCommodity(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            commodityService.delete(id);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Delete commodity successfully.", commodityService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "updateCommodity")
    @ResponseBody
    public ResultMap updateCommodity(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String column = jsonObject.getString("column");
            Object value = jsonObject.get("value");
            commodityService.update(id, column, value);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Update commodity successfully.", commodityService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "uploadImage")
    @ResponseBody
    public ResultMap uploadImage(HttpServletRequest httpServletRequest,
                                 @RequestParam("id") String id,
                                 @RequestParam("multipartFile") MultipartFile multipartFile) {
        try {
            String path = httpServletRequest.getSession().getServletContext().getRealPath("/static/image/");
            File file = new File(path);
            if (! file.exists()) {
                if (! file.mkdirs()) {
                    throw new Exception("Make dictionary error.");
                }
            }
            String fileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            multipartFile.transferTo(new File(file, uuid + "_" + fileName));
            commodityService.update(id, "img", "/image/" + uuid + "_" + fileName);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Upload image successfully.", commodityService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getCommodities")
    @ResponseBody
    public ResultMap getCommodities() {
        try {
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get commodities successfully.", commodityService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
