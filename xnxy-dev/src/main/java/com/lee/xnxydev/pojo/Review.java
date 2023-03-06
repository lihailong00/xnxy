package com.lee.xnxydev.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName review
 */
@TableName(value ="review")
@Data
public class Review implements Serializable {
    /**
     *
     */
    @TableId(value = "r_id")
    private Long rId;

    /**
     *
     */
    @TableField(value = "p_id")
    private Long pId;

    /**
     *
     */
    @TableField(value = "u_id")
    private Long uId;

    /**
     *
     */
    @TableField(value = "content")
    private String content;

    /**
     *
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "ip")
    private String ip;

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
        Review other = (Review) that;
        return (this.getRId() == null ? other.getRId() == null : this.getRId().equals(other.getRId()))
                && (this.getPId() == null ? other.getPId() == null : this.getPId().equals(other.getPId()))
                && (this.getUId() == null ? other.getUId() == null : this.getUId().equals(other.getUId()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRId() == null) ? 0 : getRId().hashCode());
        result = prime * result + ((getPId() == null) ? 0 : getPId().hashCode());
        result = prime * result + ((getUId() == null) ? 0 : getUId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rId=").append(rId);
        sb.append(", pId=").append(pId);
        sb.append(", uId=").append(uId);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append(", ip=").append(ip);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}