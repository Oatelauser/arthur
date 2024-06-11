package com.arthur.auth.upms.service.impl;

import com.arthur.auth.upms.mapper.SysUserMapper;
import com.arthur.auth.upms.service.SysUserService;
import com.arthur.auth.user.domain.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
