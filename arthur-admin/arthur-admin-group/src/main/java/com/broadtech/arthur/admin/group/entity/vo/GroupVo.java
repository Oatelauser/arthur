package com.broadtech.arthur.admin.group.entity.vo;

import com.broadtech.arthur.common.config.ConfigMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路由归属组信息
 * @author Machenike
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupVo implements Serializable {
    /**
     * 归属组id
     */

    private String id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 
     */
    private String confDataId;

    private String confDataType;

    /**
     * 
     */
    private String confGroupId;

    /**
     * 
     */
    private String confNameSpace;

    /**
     * 最大允许插入条数，达到最大条数分裂
     */
    private Integer maxInsertSize;

    /**
     * 
     */
    private String groupName;


    public static ConfigMetaData assembleConfigMetaData(GroupVo group){
        return new ConfigMetaData(group.getConfNameSpace()
                , group.getConfGroupId()
                , group.getConfDataId()
                , group.getConfDataType());

    }
}