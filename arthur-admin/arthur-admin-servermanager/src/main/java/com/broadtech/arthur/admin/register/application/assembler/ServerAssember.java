package com.broadtech.arthur.admin.register.application.assembler;

import com.broadtech.arthur.admin.register.application.dto.ServerDto;
import com.broadtech.arthur.admin.register.application.vo.ServerVo;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import com.broadtech.arthur.admin.register.infrastructure.db.po.ServerRegisterPo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Component
public class ServerAssember {

    public Server dto2Entity(ServerDto dto) {
        Server entity = new Server();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public ServerVo po2vo(ServerRegisterPo serverRegisterPo) {
        ServerVo serverVo = new ServerVo();
        BeanUtils.copyProperties(serverRegisterPo, serverVo);
        return serverVo;
    }

}
