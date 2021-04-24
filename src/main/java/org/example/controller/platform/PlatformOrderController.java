package org.example.controller.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.service.OrderService;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("platform")
public class PlatformOrderController extends BaseController {

    private final OrderService orderService;

    @Autowired
    public PlatformOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "dispatchOrder")
    @ResponseBody
    public ResultMap dispatchOrder(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String orderName = jsonObject.getString("order_name");
            orderService.update(orderName, "Dispatching");
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Dispatch order successfully.", orderService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getOrders")
    @ResponseBody
    public ResultMap getOrders() {
        try {
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get orders successfully.", orderService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
