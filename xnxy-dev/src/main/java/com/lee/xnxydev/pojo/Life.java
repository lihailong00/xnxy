package com.lee.xnxydev.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName life
 */
@TableName(value ="life")
@Data
public class Life implements Serializable {
    /**
     * 
     */
    @TableId(value = "p_id")
    private Long pId;

    /**
     * 
     */
    @TableField(value = "u_id")
    private Long uId;

    /**
     * 文章标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 仅有life和study
     */
    @TableField(value = "type")
    private String type;

    /**
     * 文章发布时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 文章是否被删除，0表示未删除，1表示删除
     */
    @TableField(value = "has_deleted")
    private Integer hasDeleted;

    /**
     * 发布IP
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
        Life other = (Life) that;
        return (this.getPId() == null ? other.getPId() == null : this.getPId().equals(other.getPId()))
            && (this.getUId() == null ? other.getUId() == null : this.getUId().equals(other.getUId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getHasDeleted() == null ? other.getHasDeleted() == null : this.getHasDeleted().equals(other.getHasDeleted()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPId() == null) ? 0 : getPId().hashCode());
        result = prime * result + ((getUId() == null) ? 0 : getUId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getHasDeleted() == null) ? 0 : getHasDeleted().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pId=").append(pId);
        sb.append(", uId=").append(uId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", hasDeleted=").append(hasDeleted);
        sb.append(", ip=").append(ip);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}