package com.jbit.web;

import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import com.jbit.pojo.DevUser;
import com.jbit.service.AppInfoService;
import com.jbit.service.AppVersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private AppInfoService appInfoService;

    //反填版本信息
    @GetMapping("/appversionadd")
    public String queryByid(Model model,Long id){
        model.addAttribute("appVersionList",appVersionService.queryByAppid(id));
        model.addAttribute("appid",id);
        return "developer/appversionadd";
    }

    //添加版本信息
    @PostMapping("/addversionsave")
    public String addversionsave(HttpSession session, HttpServletRequest request, AppVersion appVersion, MultipartFile a_downloadlink){
        String server_path = session.getServletContext().getRealPath("/statics/uploadfiles/");
        try {
            a_downloadlink.transferTo(new File(server_path,a_downloadlink.getOriginalFilename()));
        } catch (IOException e) {
        }
        DevUser devuser = (DevUser) session.getAttribute("devuser");
        Long appid =Long.parseLong(request.getParameter("appid")) ;
        appVersion.setAppid(appid);
        appVersion.setCreatedby(devuser.getId());
        appVersion.setCreationdate(new Date());
        appVersion.setDownloadlink("/statics/uploadfiles/"+a_downloadlink.getOriginalFilename());   //相对路径
        appVersion.setApklocpath(server_path+a_downloadlink.getOriginalFilename());
        appVersion.setApkfilename(a_downloadlink.getOriginalFilename());
        appVersionService.addversion(appVersion);
        Long id = appVersion.getId();
        AppInfo appInfo = new AppInfo();
        appInfo.setId(appid);
        appInfo.setVersionid(id);
        appInfoService.update(appInfo);
        return "redirect:/dev/app/list";
    }
}
