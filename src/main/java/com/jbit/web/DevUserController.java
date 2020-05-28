package com.jbit.web;

import com.jbit.pojo.DevUser;
import com.jbit.service.DevUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("dev")
public class DevUserController {

    @Resource
    private DevUserService devUserService;

    @PostMapping("login")
    public String login(Model model, HttpSession session,String code, String devpassword){
        DevUser devUser = devUserService.queryLogin(code, devpassword);
        if (devUser !=null){
            session.setAttribute("devuser",devUser);
            return "redirect:/jsp/developer/main.jsp";   //重定向
        }
            model.addAttribute("error","用户名或密码有误，请重新登陆");
        return "devlogin";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
       session.invalidate();
       return "redirect:/jsp/devlogin.jsp";
    }
}
