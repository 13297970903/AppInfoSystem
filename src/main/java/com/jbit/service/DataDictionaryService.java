package com.jbit.service;

import com.jbit.mapper.DataDictionaryMapper;
import com.jbit.pojo.DataDictionary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataDictionaryService {
    @Resource
    private DataDictionaryMapper dictionaryMapper;

    public DataDictionary queryData(String typecode,Long valueid){
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        dataDictionary.setValueid(valueid);
        return  dictionaryMapper.selectOne(dataDictionary);
    }

    /*查询状态与平台*/
    public List<DataDictionary> queryDataList(String typecode){
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        return  dictionaryMapper.select(dataDictionary);
    }


}
