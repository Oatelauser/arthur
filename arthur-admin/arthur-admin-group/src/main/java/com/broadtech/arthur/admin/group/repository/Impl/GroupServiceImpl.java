package com.broadtech.arthur.admin.group.repository.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.mapper.GroupMapper;
import com.broadtech.arthur.admin.group.repository.GroupService;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @description 针对表【route_group(路由归属组信息)】的数据库操作Service实现
 * @createDate 2022-08-02 16:14:48
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
        implements GroupService {
}




