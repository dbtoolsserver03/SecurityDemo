package com.baizhi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.Role;
import com.baizhi.entity.User;

/**
 * @ClassName MyUserDetailsService
 * @Description TODO
 * @Author Jiangnan Cui
 * @Date 2022/8/7 11:26
 * @Version 1.0
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1.查询用户
		User user = userDao.loadUserByUsername(username);
		if (ObjectUtils.isEmpty(user))
			throw new RuntimeException("用户不存在");
		//2.查询权限信息
		List<Role> roles = userDao.getRolesByUid(user.getId());
		user.setRoles(roles);
		return user;
	}
}