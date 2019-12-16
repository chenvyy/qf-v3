package com.qf.v3.common;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author CYY
 * @date 2019/9/29 16:45
 */
public interface IBaseService<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    List<T> list();

    //分页
    PageInfo<T> getPageInfo(int pageNum, int pageSize);
    //批量删除
    int deleteAllByPrimaryKey(String[] ids);
}
