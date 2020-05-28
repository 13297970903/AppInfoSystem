package com.jbit.service;

import com.jbit.mapper.AppCategoryMapper;
import com.jbit.pojo.AppCategory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppCategoryService {
    @Resource
    private AppCategoryMapper appCategoryMapper;

    public AppCategory queryById(Long id){
        return  appCategoryMapper.selectByPrimaryKey(id);
    }

    public List<AppCategory> queryByPid(Long id){
        Example example = new Example(AppCategory.class);
        Example.Criteria criteria = example.createCriteria();
        if (id == null){
             //第一级
            criteria.andIsNull("parentid");
        }else{
            //第二级，第三级
            criteria.andEqualTo("parentid",id);
        }
        return appCategoryMapper.selectByExample(example);
    }
}
