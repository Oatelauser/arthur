package com.broadtech.arthur.admin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broadtech.arthur.admin.user.entity.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Machenike
* @description 针对表【user(路由用户表)】的数据库操作Mapper
* @createDate 2022-09-14 09:48:58
* @Entity com.broadtech.arthur.admin.user.entity.po.User
*/
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

}




