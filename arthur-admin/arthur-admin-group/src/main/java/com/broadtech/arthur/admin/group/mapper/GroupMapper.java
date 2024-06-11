package com.broadtech.arthur.admin.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broadtech.arthur.admin.group.entity.po.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Machenike
* @description 针对表【route_group(路由归属组信息)】的数据库操作Mapper
* @createDate 2022-08-02 16:14:48
* @Entity com.broadtech.arthur.admin.entity.route.definition.RouteGroup
*/
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    /***
     * 统计当前分组下有多少条目
     * @param groupId
     * @return
     */
    @Select("select count(group_id) from route where group_id=#{groupId}")
    int countByGroupId(@Param("groupId") String groupId);

}




