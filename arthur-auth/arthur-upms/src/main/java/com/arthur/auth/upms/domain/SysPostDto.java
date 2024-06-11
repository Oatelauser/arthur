package com.arthur.auth.upms.domain;

import com.arthur.boot.file.excel.ExcelRows;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.IOException;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 岗位管理-新增前端参数收类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
@Data
@Schema(name = "岗位管理新增请求")
public class SysPostDto {

    /**
     * 岗位状态
     */
	@JsonProperty("isEnabled")
	@Schema(description = "岗位状态", defaultValue = "false", requiredMode = NOT_REQUIRED)
    private boolean status;

    /**
     * 岗位名称
     */
	@JsonProperty("name")
	@NotBlank(message = "岗位名称不能为空")
	@Schema(description = "岗位名称", requiredMode = REQUIRED)
    private String postName;

    /**
     * 岗位编码
     */
	@JsonProperty("number")
    @NotBlank(message = "岗位编码不能为空")
	@Schema(description = "岗位编码", requiredMode = REQUIRED)
    private String postCode;

    /**
     * 显示排序
     */
	@JsonProperty("sort")
    @NotNull(message = "显示排序不能为空")
	@Schema(description = "显示排序", requiredMode = REQUIRED)
    private Integer orderNum;

	@JsonProperty("description")
	@Schema(description = "备注", requiredMode = NOT_REQUIRED)
	private String remark;

	public static void main(String[] args) throws IOException {
		SysPostDto s1 = new SysPostDto();
		s1.postName = "国师";
		ValidatorFactory validatorFactory = Validation.byDefaultProvider()
			.configure()
			.addValueExtractor(new ExcelRows.Extractor())
			.buildValidatorFactory();

		Validator validator = validatorFactory.getValidator();

		ExcelRows.ExcelRow<SysPostDto> row = new ExcelRows.ExcelRow<>(0, s1);
		ExcelRows<SysPostDto> excelRows = new ExcelRows<>();
		excelRows.setRows(List.of(row));

		ExcelRows.ExcelRowsContainer container = new ExcelRows.ExcelRowsContainer();
		container.excelRows = excelRows;
		Object validate = validator.validate(excelRows);
		int a = 1;
	}

}
