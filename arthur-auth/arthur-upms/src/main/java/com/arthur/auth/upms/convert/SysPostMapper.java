package com.arthur.auth.upms.convert;

import com.arthur.auth.upms.domain.SysPostDto;
import com.arthur.auth.user.domain.entity.SysPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * {@link SysPost}转换类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
@Mapper
public interface SysPostMapper {

    SysPostMapper INSTANCE = Mappers.getMapper(SysPostMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
    SysPost dtoToSysPost(SysPostDto sysPostDto);

	List<SysPost> dtoToSysPosts(List<SysPostDto> sysPosts);

}
