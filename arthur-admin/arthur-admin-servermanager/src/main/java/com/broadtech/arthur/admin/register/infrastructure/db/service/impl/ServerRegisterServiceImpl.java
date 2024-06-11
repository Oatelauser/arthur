package com.broadtech.arthur.admin.register.infrastructure.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.register.infrastructure.db.po.ServerRegisterPo;
import com.broadtech.arthur.admin.register.infrastructure.db.service.ServerRegisterService;
import com.broadtech.arthur.admin.register.infrastructure.db.mapper.ServerRegisterMapper;
import org.springframework.stereotype.Service;

/**
* @author Machenike
* @description 针对表【server_register(服务注册表)】的数据库操作Service实现
* @createDate 2022-10-19 13:29:59
*/
@Service
public class ServerRegisterServiceImpl extends ServiceImpl<ServerRegisterMapper, ServerRegisterPo>
    implements ServerRegisterService{

}




