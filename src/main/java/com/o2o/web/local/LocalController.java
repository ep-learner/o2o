package com.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {

    //账号绑定页面
    @RequestMapping(value = "/register",method = {RequestMethod.GET})
    private String accountbind(){
        return "local/register";
    }

    //修改密码页面
    @RequestMapping(value = "/changepwd", method = RequestMethod.GET)
    private String changepsw() {
        return "local/changepwd";
    }

    //登录页面
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login() {
        return "local/login";
    }

}
