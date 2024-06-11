package com.arthur.auth.upms.service;

import com.arthur.auth.upms.domain.SysPostDto;
import com.arthur.auth.upms.domain.SysPostPageDto;
import com.arthur.auth.user.domain.entity.SysPost;
import com.arthur.web.model.Pageable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPostService extends IService<SysPost> {

    boolean savePost(SysPostDto sysPostDto);

	boolean batchSavePost(List<SysPostDto> sysPosts);

	Pageable<SysPostDto> findAllByPage(SysPostPageDto sysPostPage);

	List<SysPostDto> findAllPosts(SysPostPageDto sysPostPage);

}
