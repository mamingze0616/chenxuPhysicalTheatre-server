package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 活动表
 *
 * @TableName T_ACTIVITY
 */
@TableName(value = "T_ACTIVITY")
@Data
public class TActivity {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类型暂时不曲分
     */
    private Integer type;

    /**
     *
     */
    private LocalDate startTime;

    /**
     * 有效期长度
     */
    private Integer validity;

    /**
     * 1:有效;2:无效
     */
    private Integer satus;

    /**
     * 最大人数
     */
    private Integer max;

    /**
     * 最小人数
     */
    private Integer min;

    /**
     * 预约人数
     */
    private Integer booked;

    /**
     *
     */
    private String title;

    /**
     * 活动描述
     */
    private String desc;

    /**
     *
     */
    private String picture;

    /**
     * 参加活动的价格(元)
     */
    private Integer price;

    /**
     * 优惠券id按照;分割
     */
    private String couponIds;

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
        TActivity other = (TActivity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
                && (this.getValidity() == null ? other.getValidity() == null : this.getValidity().equals(other.getValidity()))
                && (this.getSatus() == null ? other.getSatus() == null : this.getSatus().equals(other.getSatus()))
                && (this.getMax() == null ? other.getMax() == null : this.getMax().equals(other.getMax()))
                && (this.getMin() == null ? other.getMin() == null : this.getMin().equals(other.getMin()))
                && (this.getBooked() == null ? other.getBooked() == null : this.getBooked().equals(other.getBooked()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
                && (this.getPicture() == null ? other.getPicture() == null : this.getPicture().equals(other.getPicture()))
                && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
                && (this.getCouponIds() == null ? other.getCouponIds() == null : this.getCouponIds().equals(other.getCouponIds()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getValidity() == null) ? 0 : getValidity().hashCode());
        result = prime * result + ((getSatus() == null) ? 0 : getSatus().hashCode());
        result = prime * result + ((getMax() == null) ? 0 : getMax().hashCode());
        result = prime * result + ((getMin() == null) ? 0 : getMin().hashCode());
        result = prime * result + ((getBooked() == null) ? 0 : getBooked().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getPicture() == null) ? 0 : getPicture().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getCouponIds() == null) ? 0 : getCouponIds().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", startTime=").append(startTime);
        sb.append(", validity=").append(validity);
        sb.append(", satus=").append(satus);
        sb.append(", max=").append(max);
        sb.append(", min=").append(min);
        sb.append(", booked=").append(booked);
        sb.append(", title=").append(title);
        sb.append(", desc=").append(desc);
        sb.append(", picture=").append(picture);
        sb.append(", price=").append(price);
        sb.append(", couponIds=").append(couponIds);
        sb.append("]");
        return sb.toString();
    }
}
