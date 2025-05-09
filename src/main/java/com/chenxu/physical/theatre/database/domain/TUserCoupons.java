package com.chenxu.physical.theatre.database.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxu.physical.theatre.database.constant.TUserCouponsStatus;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户拥有的优惠券的表
 *
 * @TableName T_USER_COUPONS
 */
@TableName(value = "T_USER_COUPONS")
@Data
public class TUserCoupons {
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
     *
     */
    private Integer couponId;

    /**
     * 1:未使用;2:已使用;3:已过期
     */
    private TUserCouponsStatus status;

    /**
     * 优惠券下发日期
     */
    private LocalDate issueTime;

    /**
     * 几天之内有效
     */
    private Integer effectiveDays;

    private TCoupon tCoupon;

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
        TUserCoupons other = (TUserCoupons) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getIssueTime() == null ? other.getIssueTime() == null : this.getIssueTime().equals(other.getIssueTime()))
                && (this.getEffectiveDays() == null ? other.getEffectiveDays() == null : this.getEffectiveDays().equals(other.getEffectiveDays()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIssueTime() == null) ? 0 : getIssueTime().hashCode());
        result = prime * result + ((getEffectiveDays() == null) ? 0 : getEffectiveDays().hashCode());
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
        sb.append(", couponId=").append(couponId);
        sb.append(", status=").append(status);
        sb.append(", issueTime=").append(issueTime);
        sb.append(", effectiveDays=").append(effectiveDays);
        sb.append("]");
        return sb.toString();
    }
}
