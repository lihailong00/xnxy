package com.lee.xnxydev.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @author 晓龙coding
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(value = "u_id")
    private Long uId;

    /**
     *
     */
    @TableField(value = "username")
    private String username;

    /**
     * 教务处用户名
     */
    @TableField(value = "jwc_username")
    private String jwcUsername;

    /**
     * 教务处登录密码
     */
    @TableField(value = "jwc_password")
    private String jwcPassword;

    /**
     * 微信session_key
     */
    @TableField(value = "session_key")
    private String sessionKey;

    /**
     * 微信openid
     */
    @TableField(value = "openid")
    private String openid;

    /**
     * 头像URL
     */
    @TableField(value = "photo_img")
    private String photoImg;

    /**
     * 用户创建时间
     */
    @TableField(value = "createTime")
    private LocalDateTime createtime;

    /**
     * 用户是否注销，0表示未注销，1表示注销
     */
    @TableField(value = "has_deleted")
    private Integer hasDeleted;

    /**
     * 用户上次注销时间
     */
    @TableField(value = "deletedTime")
    private LocalDateTime deletedtime;

    /**
     * 课表信息
     */
    @TableField(value = "class_table")
    private String classTable;

    /**
     * 成绩信息
     */
    @TableField(value = "score_table")
    private String scoreTable;

    /**
     * 用户权限，0是未认证，1是认证
     */
    @TableField(value = "power")
    private Integer power;

    /**
     * 上一次登录教务处的时间
     */
    @TableField(value = "last_visit_jwc")
    private LocalDateTime lastVisitJwc;

    /**
     * 用户真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;

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
        User other = (User) that;
        return (this.getUId() == null ? other.getUId() == null : this.getUId().equals(other.getUId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getJwcUsername() == null ? other.getJwcUsername() == null : this.getJwcUsername().equals(other.getJwcUsername()))
                && (this.getJwcPassword() == null ? other.getJwcPassword() == null : this.getJwcPassword().equals(other.getJwcPassword()))
                && (this.getSessionKey() == null ? other.getSessionKey() == null : this.getSessionKey().equals(other.getSessionKey()))
                && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
                && (this.getPhotoImg() == null ? other.getPhotoImg() == null : this.getPhotoImg().equals(other.getPhotoImg()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
                && (this.getHasDeleted() == null ? other.getHasDeleted() == null : this.getHasDeleted().equals(other.getHasDeleted()))
                && (this.getDeletedtime() == null ? other.getDeletedtime() == null : this.getDeletedtime().equals(other.getDeletedtime()))
                && (this.getClassTable() == null ? other.getClassTable() == null : this.getClassTable().equals(other.getClassTable()))
                && (this.getScoreTable() == null ? other.getScoreTable() == null : this.getScoreTable().equals(other.getScoreTable()))
                && (this.getPower() == null ? other.getPower() == null : this.getPower().equals(other.getPower()))
                && (this.getLastVisitJwc() == null ? other.getLastVisitJwc() == null : this.getLastVisitJwc().equals(other.getLastVisitJwc()))
                && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
                && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUId() == null) ? 0 : getUId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getJwcUsername() == null) ? 0 : getJwcUsername().hashCode());
        result = prime * result + ((getJwcPassword() == null) ? 0 : getJwcPassword().hashCode());
        result = prime * result + ((getSessionKey() == null) ? 0 : getSessionKey().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getPhotoImg() == null) ? 0 : getPhotoImg().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getHasDeleted() == null) ? 0 : getHasDeleted().hashCode());
        result = prime * result + ((getDeletedtime() == null) ? 0 : getDeletedtime().hashCode());
        result = prime * result + ((getClassTable() == null) ? 0 : getClassTable().hashCode());
        result = prime * result + ((getScoreTable() == null) ? 0 : getScoreTable().hashCode());
        result = prime * result + ((getPower() == null) ? 0 : getPower().hashCode());
        result = prime * result + ((getLastVisitJwc() == null) ? 0 : getLastVisitJwc().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uId=").append(uId);
        sb.append(", username=").append(username);
        sb.append(", jwcUsername=").append(jwcUsername);
        sb.append(", jwcPassword=").append(jwcPassword);
        sb.append(", sessionKey=").append(sessionKey);
        sb.append(", openid=").append(openid);
        sb.append(", photoImg=").append(photoImg);
        sb.append(", createtime=").append(createtime);
        sb.append(", hasDeleted=").append(hasDeleted);
        sb.append(", deletedtime=").append(deletedtime);
        sb.append(", classTable=").append(classTable);
        sb.append(", scoreTable=").append(scoreTable);
        sb.append(", power=").append(power);
        sb.append(", lastVisitJwc=").append(lastVisitJwc);
        sb.append(", realName=").append(realName);
        sb.append(", gender=").append(gender);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}