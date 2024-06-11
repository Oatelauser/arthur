package com.arthur.auth.upms.domain;

import com.arthur.web.model.PageSearchDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

/**
 * 岗位列表查询
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
@Data
@Schema(name = "岗位列表查询")
@EqualsAndHashCode(callSuper = true)
public class SysPostPageDto extends PageSearchDto {

	@JsonProperty("name")
	@Schema(description = "岗位名称", requiredMode = NOT_REQUIRED)
	private String postName;

	@JsonProperty("number")
	@Schema(description = "岗位名称", requiredMode = NOT_REQUIRED)
	private String postNumber;

	@JsonProperty("isEnable")
	@Schema(description = "岗位名称", requiredMode = NOT_REQUIRED)
	private Integer isEnable;

}
