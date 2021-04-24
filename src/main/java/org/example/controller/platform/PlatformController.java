package org.example.controller.platform;

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
@RequestMapping("platform")
public class PlatformController extends BaseController {

    private final UserService userService;

    @Autowired
    public PlatformController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "signIn")
    @ResponseBody
    public ResultMap signIn(@RequestBody String param) {
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            User user = userService.find("id", jsonObject.getString("id"));
            if (user == null) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "Id does not exist.", null);
            } else if (! "platform".equals(user.getType())) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "User permission error.", null);
            } else if (! EncryptUtils.SHACompare(EncryptUtils.SHAEncrypt(jsonObject.getString("password")), user.getPassword())) {
                ResultMap.setInstance(Status.ERROR.getStatus(), "Incorrect user id or password.", null);
            } else {
                ResultMap.setInstance(Status.SUCCESS.getStatus(), "Sign in successfully.", userService.findView(user.getId()));
            }
        } catch (Exception exception) {
            handleException(exception);
            return ResultMap.getInstance();
        }
        return ResultMap.getInstance();
    }
}
