package com.broadtech.arthur.admin.resource.service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/9
 */
public interface BaseService {


   /**
    * 验证
    *
    * @param permission 许可
    * @param path       路径
    * @return boolean
    */
   boolean verify(String permission,String path);
}
