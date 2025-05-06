package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券表
 * @TableName T_COUPON
 */
@TableName(value ="T_COUPON")
@Data
public class TCoupon {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 1:课程;2;折扣
     */
    private Integer type;

    /**
     * 有效期长度
     */
    private Integer validityPeriod;

    /**
     * 有效期默认开始时间
     */
    private Date startTime;

    /**
     * 折扣力度例如80:打八折
     */
    private Integer discount;

    /**
     * 赠送的课程数量
     */
    private Integer courseNumber;

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
        TCoupon other = (TCoupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getValidityPeriod() == null ? other.getValidityPeriod() == null : this.getValidityPeriod().equals(other.getValidityPeriod()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getDiscount() == null ? other.getDiscount() == null : this.getDiscount().equals(other.getDiscount()))
            && (this.getCourseNumber() == null ? other.getCourseNumber() == null : this.getCourseNumber().equals(other.getCourseNumber()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getValidityPeriod() == null) ? 0 : getValidityPeriod().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getDiscount() == null) ? 0 : getDiscount().hashCode());
        result = prime * result + ((getCourseNumber() == null) ? 0 : getCourseNumber().hashCode());
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
        sb.append(", validityPeriod=").append(validityPeriod);
        sb.append(", startTime=").append(startTime);
        sb.append(", discount=").append(discount);
        sb.append(", courseNumber=").append(courseNumber);
        sb.append("]");
        return sb.toString();
    }
}