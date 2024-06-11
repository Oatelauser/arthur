package com.broadtech.arthur.admin.user.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 路由用户表
 *
 * @TableName User
 */
@TableName(value = "user")
@Data
public class UserPO implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String id;

    /**
     *
     */
    private LocalDateTime createTime;

    /**
     *
     */
    private LocalDateTime updateTime;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String userPassword;

    /**
     *
     */
    private Integer msisdn;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Integer gender;

    /**
     * 0:启用，1：禁用
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserPO other = (UserPO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(
                other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(
                other.getUpdateTime()))
                && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
                && (this.getUserPassword() == null ? other.getUserPassword() == null : this.getUserPassword().equals(
                other.getUserPassword()))
                && (this.getMsisdn() == null ? other.getMsisdn() == null : this.getMsisdn().equals(other.getMsisdn()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserPassword() == null) ? 0 : getUserPassword().hashCode());
        result = prime * result + ((getMsisdn() == null) ? 0 : getMsisdn().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", userName=").append(userName);
        sb.append(", userPassword=").append(userPassword);
        sb.append(", msisdn=").append(msisdn);
        sb.append(", email=").append(email);
        sb.append(", gender=").append(gender);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public static UserPO createUser(
            String userName
            , String userPassword
            , int msisdn
            , String email
            , int gender
            , int isDelete) {
        UserPO userPO = new UserPO();
        userPO.setUserName(userName);
        userPO.setUserPassword(userPassword);
        userPO.setMsisdn(msisdn);
        userPO.setEmail(email);
        userPO.setGender(gender);
        userPO.setIsDelete(isDelete);
        LocalDateTime now = LocalDateTime.now();
        userPO.setCreateTime(now);
        userPO.setUpdateTime(now);
        return userPO;
    }


}