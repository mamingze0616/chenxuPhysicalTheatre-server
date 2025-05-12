package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxu.physical.theatre.database.constant.TUserOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户升级成会员的订单
 *
 * @TableName T_USER_ORDER
 */
@TableName(value = "T_USER_ORDER")
@Data
public class TUserOrder {
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
     * 支付订单id
     */
    private Integer payOrderId;

    /**
     * 1:未支付;2:已支付;3:过期取消支付;4:作废
     */
    private TUserOrderStatus status;

    /**
     *
     */
    private LocalDateTime createTime;

    /**
     * 金额
     */
    private Integer amount;

    /**
     *
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
        TUserOrder other = (TUserOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getCouponIds() == null ? other.getCouponIds() == null : this.getCouponIds().equals(other.getCouponIds()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", amount=").append(amount);
        sb.append(", couponIds=").append(couponIds);
        sb.append("]");
        return sb.toString();
    }
}
