package com.broadtech.arthur.admin.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.service.IdempotentService;
import com.broadtech.arthur.admin.user.entity.po.UserPO;
import com.broadtech.arthur.admin.user.entity.vo.UserVO;
import com.broadtech.arthur.admin.user.mapper.UserMapper;
import com.broadtech.arthur.admin.user.service.UserService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    private static final int DISABLED = 1;
    private static final int ACTIVE = 0;
    IdempotentService idempotentService;

    @Autowired
    public void setIdempotentService(IdempotentService idempotentService) {
        this.idempotentService = idempotentService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(
            String userName, String userPassword
            , int msisdn, String email
            , int gender, int isDelete) {

        UserPO user = UserPO.createUser(userName, userPassword, msisdn, email, gender, isDelete);
        Preconditions.checkState(save(user), BaseException.UserInsertFailException.of());

        return UserVO.po2vo(user);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(
            Integer id, String userName
            , String userPassword, int msisdn
            , String email, int gender, int isDelete) {
        UserPO user = UserPO.createUser(userName, userPassword, msisdn, email, gender, isDelete);
        Preconditions.checkState(!idempotentService.checkDupSubmit(user.getId()), BaseException.RepeatedSubmitException.of());
        Preconditions.checkState(updateById(user), BaseException.UserUpdateFailException.of());
        return UserVO.po2vo(user);
    }

    @Override
    public List<UserPO> queryAllUser() {
        return list();
    }

    @Override
    public Page<UserPO> queryUserByPage(int current, int size) {
        Page<UserPO> pageInfo = Page.of(current, size);
        Page<UserPO> rs = page(pageInfo);
        return rs;
    }

    @Override
    public UserVO queryUserById(Integer id) {
        UserPO user = getById(id);
        return UserVO.po2vo(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> deleteUserById(Integer id) {
        disableUserById(id);
        return CollectionUtil.newArrayList(id);
    }

    @Override
    public List<Integer> deleteUserByIds(List<Integer> ids) {
        CollectionUtil.newHashSet(ids)
                .forEach(this::disableUserById);
        return ids;
    }

    private void disableUserById(Integer id) {
        updateStateById(id, DISABLED);
    }

    private void activeUserById(Integer id) {
        updateStateById(id, ACTIVE);
    }

    private void updateStateById(Integer id, int state) {
        Preconditions.checkState(!idempotentService.checkDupSubmit(id), BaseException.RepeatedSubmitException.of());
        Preconditions.checkState(removeById(id), BaseException.UserDeleteFailException.of()   );
//        LambdaUpdateWrapper<UserPO> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(UserPO::getId, id).set(UserPO::getIsDelete, state);
//        Preconditions.checkState(update(updateWrapper), new BaseException.UserDeleteFailException());
    }

}
