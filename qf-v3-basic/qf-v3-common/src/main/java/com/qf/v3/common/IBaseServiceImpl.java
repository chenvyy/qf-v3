package com.qf.v3.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public abstract class IBaseServiceImpl<T> implements IBaseService<T> {

    public abstract IBaseDao getIbaseDao();


    public int deleteByPrimaryKey(Long id){

        return getIbaseDao().deleteByPrimaryKey(id);
    }
    public int deleteAllByPrimaryKey(String[] ids){

        return getIbaseDao().deleteAllByPrimaryKey(ids);
    }
    public int insert(T record){

        return getIbaseDao().insert(record);
    }

    public int insertSelective(T record){

        return getIbaseDao().insertSelective(record);
    }

    public T selectByPrimaryKey(Long id){

        return (T) getIbaseDao().selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record){

        return getIbaseDao().updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record){

        return getIbaseDao().updateByPrimaryKey(record);
    }

    public List<T> list() {
        return getIbaseDao().list();
    }

    public PageInfo<T> getPageInfo(int pageNum, int pageSize) {
        //开始分页
        PageHelper.startPage(pageNum,pageSize);
        //获取数据
        List<T> list = list();
        //构建pageInfo对象
        PageInfo<T> pageInfo=new PageInfo<T>(list,3);
        return pageInfo;
    }
}
