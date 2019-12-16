package com.qf.v3.user.api;

import com.qf.v3.common.IBaseService;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TUser;

/**
 * @author CYY
 * @date 2019/10/28 0028 17:10
 */
public interface IUserService extends IBaseService<TUser> {
    //检查一个用户是否存在
    TUser checkIsExists(TUser user);

    //检查当前用户是否登录
    ResultBean checkIsLogin(String uuid);
}
