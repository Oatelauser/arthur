package com.arthur.auth.upms.service.impl;

import com.arthur.auth.upms.domain.SysPostDto;
import com.arthur.auth.upms.domain.SysPostPageDto;
import com.arthur.auth.upms.mapper.SysPostMapper;
import com.arthur.auth.upms.service.SysPostService;
import com.arthur.auth.user.domain.entity.SysPost;
import com.arthur.web.model.Pageable;
import com.arthur.web.server.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.arthur.auth.upms.constant.ServerStatusEnum.UPLOAD_EMPTY_DATA;
import static com.arthur.auth.upms.convert.SysPostMapper.INSTANCE;

@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

	@Override
	public List<SysPostDto> findAllPosts(SysPostPageDto sysPostPage) {
		return null;
	}

	@Override
	public Pageable<SysPostDto> findAllByPage(SysPostPageDto sysPostPage) {
		return null;
	}

	@Override
    public boolean savePost(SysPostDto sysPostDto) {
        SysPost sysPost = INSTANCE.dtoToSysPost(sysPostDto);
        return this.save(sysPost);
    }

	@Override
	public boolean batchSavePost(List<SysPostDto> sysPosts) {
		if (CollectionUtils.isEmpty(sysPosts)) {
			throw new ServiceException(UPLOAD_EMPTY_DATA);
		}
		List<SysPost> posts = INSTANCE.dtoToSysPosts(sysPosts);
		return this.baseMapper.insertBatchSomeColumn(posts) != 0;
	}

}
