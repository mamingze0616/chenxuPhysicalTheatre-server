package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.chenxu.physical.theatre.bussiness.dto.ApiPayCallbackRequest;
import com.chenxu.physical.theatre.bussiness.dto.pay.PayUnifiedOrderResponse;
import com.chenxu.physical.theatre.database.constant.TPayOrderStatus;
import com.chenxu.physical.theatre.database.constant.TPayOrderType;
import lombok.Data;

/**
 * 用户支付订单表
 *
 * @TableName T_PAY_ORDER
 */
@TableName(value = "T_PAY_ORDER", autoResultMap = true)
@Data
public class TPayOrder {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 1:未支付;2:已支付;3:已取消
     */
    private TPayOrderStatus status;

    /**
     * 1:会员;2:课程;3:形体服
     */
    private TPayOrderType type;

    /**
     *
     */
    private String openid;

    /**
     *
     */
    private String body;

    /**
     * 总金额
     */
    private Integer totalFee;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     *
     */
    private String timeStamp;

    /**
     *
     */
    private String resultCode;

    /**
     *
     */
    private String timeEnd;

    /**
     *
     */
    private String transactionId;

    /**
     * 下单结果json
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PayUnifiedOrderResponse preJson;

    /**
     * 支付结果json
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ApiPayCallbackRequest payJson;

    @TableField(exist = false)
    private TUserOrder userOrder;
    @TableField(exist = false)
    private TClothesOrder clothesOrder;

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
        TPayOrder other = (TPayOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
                && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
                && (this.getTotalFee() == null ? other.getTotalFee() == null : this.getTotalFee().equals(other.getTotalFee()))
                && (this.getOutTradeNo() == null ? other.getOutTradeNo() == null : this.getOutTradeNo().equals(other.getOutTradeNo()))
                && (this.getTimeStamp() == null ? other.getTimeStamp() == null : this.getTimeStamp().equals(other.getTimeStamp()))
                && (this.getResultCode() == null ? other.getResultCode() == null : this.getResultCode().equals(other.getResultCode()))
                && (this.getTimeEnd() == null ? other.getTimeEnd() == null : this.getTimeEnd().equals(other.getTimeEnd()))
                && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
                && (this.getPreJson() == null ? other.getPreJson() == null : this.getPreJson().equals(other.getPreJson()))
                && (this.getPayJson() == null ? other.getPayJson() == null : this.getPayJson().equals(other.getPayJson()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getTotalFee() == null) ? 0 : getTotalFee().hashCode());
        result = prime * result + ((getOutTradeNo() == null) ? 0 : getOutTradeNo().hashCode());
        result = prime * result + ((getTimeStamp() == null) ? 0 : getTimeStamp().hashCode());
        result = prime * result + ((getResultCode() == null) ? 0 : getResultCode().hashCode());
        result = prime * result + ((getTimeEnd() == null) ? 0 : getTimeEnd().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getPreJson() == null) ? 0 : getPreJson().hashCode());
        result = prime * result + ((getPayJson() == null) ? 0 : getPayJson().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", openid=").append(openid);
        sb.append(", body=").append(body);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", timeStamp=").append(timeStamp);
        sb.append(", resultCode=").append(resultCode);
        sb.append(", timeEnd=").append(timeEnd);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", preJson=").append(preJson);
        sb.append(", payJson=").append(payJson);
        sb.append("]");
        return sb.toString();
    }
}
