package org.example.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.controller.BaseController;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.utils.EncryptUtils;
import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class AdminUserController extends BaseController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "addUser")
    @ResponseBody
    public ResultMap addUser(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            String type = jsonObject.getString("type");
            String name = jsonObject.getString("name");
            String phone = jsonObject.getString("phone");
            String password = jsonObject.getString("password");
            User user = new User();
            user.setId(id);
            user.setType(type);
            user.setName(name);
            user.setPhone(phone);
            user.setPassword(EncryptUtils.SHAEncrypt(password));
            userService.save(user);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Add user successfully.", userService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "deleteUser")
    @ResponseBody
    public ResultMap deleteUser(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            String id = jsonObject.getString("id");
            userService.delete(id);
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Delete user successfully.", userService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }

    @RequestMapping(value = "getUsers")
    @ResponseBody
    public ResultMap getUsers() {
        try {
            ResultMap.setInstance(Status.SUCCESS.getStatus(), "Get users successfully.", userService.findAll());
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
