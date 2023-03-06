package com.lee.xnxydev.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @author 晓龙coding
 * @TableName trade
 */
@TableName(value ="trade")
@Data
public class Trade implements Serializable {
    /**
     * 商品id
     */
    @TableId(value = "g_id")
    private Long gId;

    /**
     * 发布者id
     */
    @TableField(value = "u_id")
    private Long uId;

    /**
     * 商品名
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 商品描述
     */
    @TableField(value = "goods_desc")
    private String goodsDesc;

    /**
     * 商品图片链接
     */
    @TableField(value = "goods_image")
    private String goodsImage;

    /**
     * 商品价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品是否被删除
     */
    @TableField(value = "has_deleted")
    private Integer hasDeleted;

    /**
     * 商品创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 商品发布ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * ip属地
     */
    @TableField(value = "ip_area")
    private String ipArea;

    /**
     * 商品类型，只有want和sell
     */
    @TableField(value = "type")
    private String type;

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
        Trade other = (Trade) that;
        return (this.getGId() == null ? other.getGId() == null : this.getGId().equals(other.getGId()))
                && (this.getUId() == null ? other.getUId() == null : this.getUId().equals(other.getUId()))
                && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
                && (this.getGoodsDesc() == null ? other.getGoodsDesc() == null : this.getGoodsDesc().equals(other.getGoodsDesc()))
                && (this.getGoodsImage() == null ? other.getGoodsImage() == null : this.getGoodsImage().equals(other.getGoodsImage()))
                && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
                && (this.getHasDeleted() == null ? other.getHasDeleted() == null : this.getHasDeleted().equals(other.getHasDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
                && (this.getIpArea() == null ? other.getIpArea() == null : this.getIpArea().equals(other.getIpArea()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGId() == null) ? 0 : getGId().hashCode());
        result = prime * result + ((getUId() == null) ? 0 : getUId().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getGoodsDesc() == null) ? 0 : getGoodsDesc().hashCode());
        result = prime * result + ((getGoodsImage() == null) ? 0 : getGoodsImage().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getHasDeleted() == null) ? 0 : getHasDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getIpArea() == null) ? 0 : getIpArea().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", gId=").append(gId);
        sb.append(", uId=").append(uId);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsDesc=").append(goodsDesc);
        sb.append(", goodsImage=").append(goodsImage);
        sb.append(", price=").append(price);
        sb.append(", hasDeleted=").append(hasDeleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", ip=").append(ip);
        sb.append(", ipArea=").append(ipArea);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}