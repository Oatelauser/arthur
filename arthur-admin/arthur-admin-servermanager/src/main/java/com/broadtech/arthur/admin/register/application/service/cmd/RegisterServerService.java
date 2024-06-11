package com.broadtech.arthur.admin.register.application.service.cmd;

import com.broadtech.arthur.admin.register.application.dto.ServerDto;
import com.broadtech.arthur.admin.register.application.vo.ServerVo;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
public interface RegisterServerService {
    /**
     * 注册服务
     * @param serverDto 服务器dto
     * @return {@link ServerVo}
     */
     ServerVo register(ServerDto serverDto);

    /**
     * 取消
     *
     * @param serverDto 服务器dto
     * @return {@link ServerVo}
     */
    ServerVo deregister(ServerDto serverDto);
}
