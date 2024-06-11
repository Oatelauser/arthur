package com.arthur.auth.upms.controller;

import com.arthur.auth.upms.domain.SysPostDto;
import com.arthur.auth.upms.domain.SysPostPageDto;
import com.arthur.auth.upms.service.SysPostService;
import com.arthur.boot.file.annotation.ExcelMapping;
import com.arthur.boot.file.annotation.ExcelParam;
import com.arthur.boot.file.annotation.ExcelResponse;
import com.arthur.web.model.GenericServerResponse;
import com.arthur.web.model.Pageable;
import com.arthur.web.model.ServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.arthur.web.constant.ServerStatusEnum.SAVE_DATA_FAIL;
import static com.arthur.web.model.ServerResponse.ofError;
import static com.arthur.web.model.ServerResponse.ofSuccessMessage;

/**
 * 岗位管理接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/post")
@Tag(name = "岗位管理", description = "岗位管理接口")
public class SysPostController {

	private final SysPostService sysPostService;

	@PostMapping("/save")
	@Operation(summary = "新增岗位")
	public ServerResponse savePost(@RequestBody @Validated SysPostDto sysPostDto) {
		boolean status = sysPostService.savePost(sysPostDto);
		return status ? ofSuccessMessage("保存成功") : ofError(SAVE_DATA_FAIL);
	}

	@PostMapping("/list")
	@Operation(summary = "岗位列表")
	public GenericServerResponse<Pageable<SysPostDto>> list(@RequestBody SysPostPageDto sysPostPage) {
		Pageable<SysPostDto> sysPosts = sysPostService.findAllByPage(sysPostPage);
		return GenericServerResponse.ofSuccess(sysPosts);
	}

	@PostMapping("/download")
	@Operation(summary = "岗位信息文件下载")
	@ExcelResponse(fileName = "岗位管理信息", allowEmptyFile = false)
	public List<SysPostDto> download(@RequestBody SysPostPageDto sysPostPage) {
		return sysPostService.findAllPosts(sysPostPage);
	}

	@PostMapping("/upload")
	@Operation(summary = "岗位信息文件上传")
	public ServerResponse upload(@ExcelMapping @ExcelParam @Valid List<SysPostDto> sysPosts) {
		boolean status = sysPostService.batchSavePost(sysPosts);
		return status ? ofSuccessMessage("上传成功") : ofError(SAVE_DATA_FAIL);
	}

}
