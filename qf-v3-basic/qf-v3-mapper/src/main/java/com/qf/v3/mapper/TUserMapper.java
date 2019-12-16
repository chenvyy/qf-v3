package com.qf.v3.mapper;

import com.qf.v3.common.IBaseDao;
import com.qf.v3.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser> {
    TUser selectByUsername(String user);
}