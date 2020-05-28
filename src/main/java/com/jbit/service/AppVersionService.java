package com.jbit.service;

import com.jbit.mapper.AppInfoMapper;
import com.jbit.mapper.AppVersionMapper;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppVersionService {
    @Resource
    private AppVersionMapper appVersionMapper;

    @Resource
    private AppInfoService appInfoService;

    @Resource
    private  DataDictionaryService dataDictionaryService;

    //根据主键查询所有信息
    public AppVersion queryById(Long id){
       return appVersionMapper.selectByPrimaryKey(id);
    }

    //根据id查询版本信息
    public List<AppVersion> queryByAppid(Long appid){
        AppVersion appVersion = new AppVersion();
        appVersion.setAppid(appid);
        List<AppVersion> select = appVersionMapper.select(appVersion);
        bindData(select);
        return select;
    }

    //绑定
    public void bindData(List<AppVersion> appVersions){
        appVersions.forEach((app)->{
            //软件名称
            app.setAppname(appInfoService.queryById(app.getAppid()).getSoftwarename());
            //发布状态
            app.setPublishstatusname(dataDictionaryService.queryData("APP_STATUS",app.getPublishstatus()).getValuename());
        });
    }

    //添加版权
    @Transactional
    public void addversion(AppVersion appVersion) {
        appVersionMapper.insertSelective(appVersion);
    }
}

