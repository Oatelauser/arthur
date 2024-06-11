package com.broadtech.arthur.admin.user.entity.vo;

import com.broadtech.arthur.admin.user.entity.po.UserPO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户id
     */

    private String id;

    /**
     *
     */
    private LocalDateTime createTime;

    /**
     *
     */
    private LocalDateTime updateTime;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String userPassword;

    /**
     *
     */
    private Integer msisdn;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Integer gender;

    /**
     * 0:启用，1：禁用
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    public static UserVO po2vo(UserPO po) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }


}
