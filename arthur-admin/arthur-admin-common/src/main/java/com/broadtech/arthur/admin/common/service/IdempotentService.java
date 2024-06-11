package com.broadtech.arthur.admin.common.service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
public interface IdempotentService {


    /**
     * 检查重复提交
     *
     * @param key key
     * @return boolean
     */
    boolean checkDupSubmit(Object key);

}
