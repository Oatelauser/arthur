package com.broadtech.arthur.admin.common.detect;


/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
public interface DetectBehavior<T> {
     /**
      * 检查名字
      *
      * @param name 名字
      * @return boolean
      */
       boolean checkName(String name);


     /**
      * 检查参数
      *
      * @param t t
      * @return boolean
      */
     boolean checkArgs(T t);
}
