package com.jbit.web;

import com.jbit.entity.JsonResult;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import com.jbit.pojo.DevUser;
import com.jbit.service.AppCategoryService;
import com.jbit.service.AppInfoService;
import com.jbit.service.AppVersionService;
import com.jbit.service.DataDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("dev/app")
public class AppinfoController {

    @Resource
    private AppInfoService appInfoService;

    @Resource
     private AppCategoryService appCategoryService;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppVersionService appVersionService;

    //遍历app应用信息
    @RequestMapping("/list")
    public String list(HttpSession session, Model model,
                       @RequestParam(defaultValue = "1",value = "pageIndex") Integer pagenum,
                       String querySoftwareName,
                       Long queryStatus,
                       Long queryFlatformId,
                       Long queryCategoryLevel1,
                       Long queryCategoryLevel2,
                       Long queryCategoryLevel3
    ){
        DevUser devuser = (DevUser) session.getAttribute("devuser");
        model.addAttribute("pageInfo",appInfoService.queryAppInfo(pagenum,devuser.getId(),querySoftwareName,queryStatus,queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3));
        //处理状态与所属平台
        model.addAttribute("statusList",dataDictionaryService.queryDataList("APP_STATUS"));
        model.addAttribute("flatFormList",dataDictionaryService.queryDataList("APP_FLATFORM"));
        //处理一级分类
        model.addAttribute("categoryLevel1List",appCategoryService.queryByPid(null));
        //处理二级分类
        if(queryCategoryLevel1 !=null){
            model.addAttribute("categoryLevel2List",appCategoryService.queryByPid(queryCategoryLevel1));
        }
        //处理三级分类
        if (queryCategoryLevel2!=null){
            model.addAttribute("categoryLevel3List",appCategoryService.queryByPid(queryCategoryLevel2));
        }
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
        return "developer/appinfolist";
    }

    @PostMapping("/appinfoadd")
    public String appinfoadd(HttpSession session,AppInfo appInfo, MultipartFile a_logoPicPath){
        //1.实现文件上传(服务器 tomcat 位置)
        String server_path = session.getServletContext().getRealPath("/statics/uploadfiles/");
        //验证大小和图片规格 [略]
        try {
            a_logoPicPath.transferTo(new File(server_path,a_logoPicPath.getOriginalFilename()));
        } catch (IOException e) {
        }
        //2.app添加
        DevUser devuser = (DevUser) session.getAttribute("devuser");
        appInfo.setUpdatedate(new Date());
        appInfo.setDevid(devuser.getId());
        appInfo.setCreatedby(devuser.getId());
        appInfo.setCreationdate(new Date());
        appInfo.setLogopicpath("/statics/uploadfiles/"+a_logoPicPath.getOriginalFilename());   //相对路径
        appInfo.setLogolocpath(server_path+a_logoPicPath.getOriginalFilename());
        appInfoService.save(appInfo);
        return  "redirect:/dev/app/list";
    }

    //应用名称验证
    @GetMapping("/apkexist")
    @ResponseBody
    public JsonResult apkexist(String apkname){
        AppInfo appInfo = appInfoService.queryApkexist(apkname);
        if (appInfo == null){
            return  new  JsonResult(true);
        }
        return  new  JsonResult(false);
    }

    @GetMapping("/appinfomodify")
    public String queryById(Model model,Long id){
        model.addAttribute("appInfo",appInfoService.queryById(id));
        return "developer/appinfomodify";
    }

    //删除logo图片
    @GetMapping("/delfile")
    @ResponseBody
    public JsonResult delfile(Long id,String flag){
        if (flag.equals("logo")) {   //删除logo
            AppInfo appInfo = appInfoService.queryById(id);
            try {
                File file = new File(appInfo.getLogolocpath());
                file.delete();
                appInfo.setLogopicpath("");
                appInfo.setLogolocpath("");
                appInfoService.update(appInfo);
                return new JsonResult(true);
            } catch (Exception e) {
                return new JsonResult(false);
            }
        }
        return  new JsonResult(false);

    }

    //修改app信息
    @PostMapping("/appinfomodifysave")
    public String appinfomodifysave(HttpSession session ,AppInfo appInfo,MultipartFile attach){
        if(!attach.isEmpty()){
            String server_path = session.getServletContext().getRealPath("/statics/uploadfiles/");
            //验证大小和图片规格 [略]
            try {
                attach.transferTo(new File(server_path,attach.getOriginalFilename()));
                appInfo.setLogopicpath("/statics/uploadfiles/"+attach.getOriginalFilename());   //相对路径
                appInfo.setLogolocpath(server_path+attach.getOriginalFilename());
            } catch (IOException e) {
            }
        }
        DevUser devuser = (DevUser) session.getAttribute("devuser");
        appInfo.setDevid(devuser.getId());
        appInfo.setModifyby(devuser.getId());
        appInfo.setModifydate(new Date());
        appInfoService.update(appInfo);
        return  "redirect:/dev/app/list";
    }

    //删除
    @GetMapping("/delapp")
    @ResponseBody
    public JsonResult delapp(Long id){
        int i = appInfoService.deleteByid(id);
        if  (i !=0){
            return  new JsonResult(true);
        }
        return new JsonResult(false);
    }

    /*查看*/
    @GetMapping("/appview/{id}")
    public String appview(Model model,@PathVariable("id") Long id){
        AppInfo appInfo = appInfoService.queryById(id);
        List<AppVersion> appVersions = appVersionService.queryByAppid(id);
        model.addAttribute("appInfo",appInfo);
        model.addAttribute("appVersionList",appVersions);
        return "developer/appinfoview";
    }
}
