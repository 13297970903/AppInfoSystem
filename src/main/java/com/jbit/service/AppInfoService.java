package com.jbit.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbit.mapper.AppInfoMapper;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class AppInfoService {
    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private AppVersionService appVersionService;

    public AppInfo queryApkexist(String apkname){
        AppInfo appInfo = new AppInfo();
        appInfo.setApkname(apkname);
       return appInfoMapper.selectOne(appInfo);
    }
    /*
    App列表查询 每一个dev登陆后只查看属于自己的appinfo
    * */
    public PageInfo queryAppInfo(Integer pagenum, Long devId, String querySoftwareName, Long queryStatus, Long queryFlatformId, Long queryCategoryLevel1, Long queryCategoryLevel2, Long queryCategoryLevel3){
        //实现分页
        PageHelper.startPage(pagenum,5);
        Example example = new Example(AppInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(querySoftwareName)){
            criteria.andLike("softwarename","%"+querySoftwareName+"%");
        }
        if (queryStatus!=null&&queryStatus!=0){
            criteria.andEqualTo("status",queryStatus);
        }
        if (queryFlatformId!=null&&queryFlatformId!=0){
            criteria.andEqualTo("flatformid",queryFlatformId);
        }
        if (queryCategoryLevel1!=null&&queryCategoryLevel1!=0){
            criteria.andEqualTo("categorylevel1",queryCategoryLevel1);
        }
        if (queryCategoryLevel2!=null&&queryCategoryLevel2!=0){
            criteria.andEqualTo("categorylevel2",queryCategoryLevel2);
        }
        if (queryCategoryLevel3!=null&&queryCategoryLevel3!=0){
            criteria.andEqualTo("categorylevel3",queryCategoryLevel3);
        }
        criteria.andEqualTo("devid",devId);
        List<AppInfo> appInfos = appInfoMapper.selectByExample(example);
        bindData(appInfos);
        //处理分页
        return new PageInfo<>(appInfos);
    }

    public void bindData(List<AppInfo> appInfos){
      /*  for (AppInfo app:appInfos){
            //所属平台
            app.setFlatformname(dataDictionaryService.queryData("APP_FLATFORM",app.getFlatformid()).getValuename());
            //分类
            app.setCategorylevel1name(appCategoryService.queryById(app.getCategorylevel1()).getCategoryname());
            app.setCategorylevel2name(appCategoryService.queryById(app.getCategorylevel1()).getCategoryname());
            app.setCategorylevel3name(appCategoryService.queryById(app.getCategorylevel1()).getCategoryname());
            //状态
            app.setStatusname(dataDictionaryService.queryData("APP_STATUS",app.getStatus()).getValuename());
            //版本号
            AppVersion appVersion = appVersionService.queryById(app.getVersionid());
            if (appVersion !=null){
                app.setVersionno(appVersion.getVersionno());
            }
        }*/
        appInfos.forEach((app)->{

            app.setFlatformname(dataDictionaryService.queryData("APP_FLATFORM",app.getFlatformid()).getValuename());

            app.setCategorylevel1name(appCategoryService.queryById(app.getCategorylevel1()).getCategoryname());
            app.setCategorylevel2name(appCategoryService.queryById(app.getCategorylevel2()).getCategoryname());
            app.setCategorylevel3name(appCategoryService.queryById(app.getCategorylevel3()).getCategoryname());

            app.setStatusname(dataDictionaryService.queryData("APP_STATUS",app.getStatus()).getValuename());

            AppVersion appVersion = appVersionService.queryById(app.getVersionid());
            if (appVersion !=null){
                app.setVersionno(appVersion.getVersionno());
            }
        });
    }

    @Transactional
    public void save(AppInfo appInfo) {
        appInfoMapper.insertSelective(appInfo);

    }

    //根据id查询所有信息
    public AppInfo queryById(Long id){
       AppInfo app = appInfoMapper.selectByPrimaryKey(id);
        app.setFlatformname(dataDictionaryService.queryData("APP_FLATFORM",app.getFlatformid()).getValuename());

        app.setCategorylevel1name(appCategoryService.queryById(app.getCategorylevel1()).getCategoryname());
        app.setCategorylevel2name(appCategoryService.queryById(app.getCategorylevel2()).getCategoryname());
        app.setCategorylevel3name(appCategoryService.queryById(app.getCategorylevel3()).getCategoryname());

        app.setStatusname(dataDictionaryService.queryData("APP_STATUS",app.getStatus()).getValuename());
       return app;
    }

    //修改信息
    public int update(AppInfo appInfo) {
        return appInfoMapper.updateByPrimaryKeySelective(appInfo);
    }


    //根据id删除app应用信息
    public int deleteByid(Long id) {
        return appInfoMapper.deleteByPrimaryKey(id);
    }


}
