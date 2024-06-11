package com.broadtech.arthur.admin.group.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value ="route_group")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    /**
     * 归属组id
     */
    @TableId
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


    public static ConfigMetaData assembleConfigMetaData(Group group){
        return new ConfigMetaData(group.getConfNameSpace()
                , group.getConfGroupId()
                , group.getConfDataId()
                , group.getConfDataType());
    }
}