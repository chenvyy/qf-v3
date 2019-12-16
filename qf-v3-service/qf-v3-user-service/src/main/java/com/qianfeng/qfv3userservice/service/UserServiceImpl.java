package com.qianfeng.qfv3userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v3.common.IBaseDao;
import com.qf.v3.common.IBaseServiceImpl;
import com.qf.v3.common.constant.RedisConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TUser;
import com.qf.v3.mapper.TUserMapper;
import com.qf.v3.user.api.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author CYY
 * @date 2019/10/28 0028 17:21
 */
@Component
@Service
public class UserServiceImpl extends IBaseServiceImpl<TUser> implements IUserService {
    /**
     * 注入TUserMapper对象
     */
    @Resource
    private TUserMapper userMapper;
    /**
     * 注入redis模板对象
     */
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 注入B加密密码编码器对象，用于判断用户输入密码（明文）与数据库中存储的密码（暗文）是否相等
     */
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public IBaseDao getIbaseDao() {
        return userMapper;
    }

    @Override
    public TUser checkIsExists(TUser user) { //检查指定的用户是否存在
        TUser currentUser = userMapper.selectByUsername(user.getUsername());
        //currentUser的密码是一个密文
        if (currentUser!=null){
            //比较密码
            if (passwordEncoder.matches(user.getPassword(),currentUser.getPassword())){
                return currentUser;
            }
        }
        return null;
    }

    @Override
    public ResultBean checkIsLogin(String uuid) { //检查用户是否登录

        if (uuid==null){
            return new ResultBean(1,null,"当前用户未登录");
        }
        //去redis获取登录信息
        String key = new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        TUser currentUser = (TUser) redisTemplate.opsForValue().get(key);
        if (currentUser!=null){
            //刷新redis有效期
            redisTemplate.expire(key,30, TimeUnit.MINUTES);
            return new ResultBean(0,currentUser,""); //将当前用户传递回去
        }

        return new ResultBean(1,null,"当前用户未登录");
    }
}
