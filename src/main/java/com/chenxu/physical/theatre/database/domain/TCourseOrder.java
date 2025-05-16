package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxu.physical.theatre.database.constant.TCourseOrderStatus;
import com.chenxu.physical.theatre.database.constant.TCourseOrderType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课程订单信息
 *
 * @TableName T_COURSE_ORDER
 */
@TableName(value = "T_COURSE_ORDER")
@Data
public class TCourseOrder {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer userId;

    /**
     * 已购课数
     */
    private Integer courseNumber;

    /**
     * 1:赠送;2:支付
     */
    private TCourseOrderType type;

    /**
     * 1:有效;2:作废
     */
    private TCourseOrderStatus status;

    /**
     *
     */
    private LocalDateTime createAt;

    /**
     *
     */
    private Integer operatorId;

    /**
     * 有效期长度
     */
    private Integer validityPeriod;

    /**
     * 有效期开始时间
     */
    private LocalDate startTime;

    /**
     *
     */
    private Integer sampleCourseOrderId;

    /**
     *
     */
    private Integer payOrderId;

    /**
     *
     */
    private String couponIds;

    /**
     *
     */
    private Integer amount;

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
        TCourseOrder other = (TCourseOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCourseNumber() == null ? other.getCourseNumber() == null : this.getCourseNumber().equals(other.getCourseNumber()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateAt() == null ? other.getCreateAt() == null : this.getCreateAt().equals(other.getCreateAt()))
                && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
                && (this.getValidityPeriod() == null ? other.getValidityPeriod() == null : this.getValidityPeriod().equals(other.getValidityPeriod()))
                && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
                && (this.getSampleCourseOrderId() == null ? other.getSampleCourseOrderId() == null : this.getSampleCourseOrderId().equals(other.getSampleCourseOrderId()))
                && (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
                && (this.getCouponIds() == null ? other.getCouponIds() == null : this.getCouponIds().equals(other.getCouponIds()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCourseNumber() == null) ? 0 : getCourseNumber().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateAt() == null) ? 0 : getCreateAt().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getValidityPeriod() == null) ? 0 : getValidityPeriod().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getSampleCourseOrderId() == null) ? 0 : getSampleCourseOrderId().hashCode());
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getCouponIds() == null) ? 0 : getCouponIds().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", courseNumber=").append(courseNumber);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", createAt=").append(createAt);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", validityPeriod=").append(validityPeriod);
        sb.append(", startTime=").append(startTime);
        sb.append(", sampleCourseOrderId=").append(sampleCourseOrderId);
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", couponIds=").append(couponIds);
        sb.append(", amount=").append(amount);
        sb.append("]");
        return sb.toString();
    }
}
