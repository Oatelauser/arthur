package com.broadtech.arthur.admin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.user.entity.po.UserPO;
import com.broadtech.arthur.admin.user.entity.vo.UserVO;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
public interface UserService extends IService<UserPO> {

    /**
     * 创建用户
     *
     * @param userName     用户名
     * @param userPassword 用户密码
     * @param msisdn       msisdn
     * @param email        电子邮件
     * @param gender       性别
     * @param isDelete     是删除
     * @return {@link Object}
     */
    UserVO createUser(
            String userName
            , String userPassword
            , int msisdn
            , String email
            , int gender
            , int isDelete
    );

    /**
     * 更新用户
     * @param id           id
     * @param userName     用户名
     * @param userPassword 用户密码
     * @param msisdn       msisdn
     * @param email        电子邮件
     * @param gender       性别
     * @param isDelete     是删除
     * @return {@link UserVO}
     */
    UserVO updateUser(
            Integer id
            , String userName
            , String userPassword
            , int msisdn
            , String email
            , int gender
            , int isDelete
    );

    /**
     * 查询所有用户
     *
     * @param current 当前
     * @param size    大小
     * @return {@link Page}<{@link UserPO}>
     */
    Page<UserPO> queryUserByPage(int current,int size);

    List<UserPO> queryAllUser();

    /**
     * 查询用户id
     *
     * @param id id
     * @return {@link Object}
     */
    UserVO queryUserById(Integer id);


    /**
     * 删除用户id
     *
     * @param id id
     * @return {@link Object}
     */
    List<Integer> deleteUserById(Integer id);

    /**
     * 删除用户id
     *
     * @param ids id
     * @return {@link Object}
     */
    List<Integer> deleteUserByIds(List<Integer> ids);

}
