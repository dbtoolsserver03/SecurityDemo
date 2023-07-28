package com.baizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baizhi.entity.Role;
import com.baizhi.entity.User;

@Mapper
public interface UserDao {


    //提供根据用户名返回用户方法
    User loadUserByUsername(String username);

    //提供根据用户id查询用户角色信息方法
    List<Role> getRolesByUid(Integer uid);
    

    //3.根据用户名更新密码方法
    Integer updatePassword(@Param("username") String username, @Param("password") String password);
}
