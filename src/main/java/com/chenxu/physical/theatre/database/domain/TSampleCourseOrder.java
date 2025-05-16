package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 月卡,季卡年卡课程订单样例
 * @TableName T_SAMPLE_COURSE_ORDER
 */
@TableName(value ="T_SAMPLE_COURSE_ORDER")
@Data
public class TSampleCourseOrder {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 1:月卡;2:季卡;3:年卡
     */
    private Integer type;

    /**
     * 8
     */
    private Integer courseNember;

    /**
     * 月卡
     */
    private String title;

    /**
     * 正常价格单位元
     */
    private Integer fee;

    /**
     * 会员价格
     */
    private Integer membershipFee;

    /**
     * 有效期
     */
    private Integer validity;

    /**
     * 1:有效;2;无效
     */
    private Integer status;

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
        TSampleCourseOrder other = (TSampleCourseOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCourseNember() == null ? other.getCourseNember() == null : this.getCourseNember().equals(other.getCourseNember()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getMembershipFee() == null ? other.getMembershipFee() == null : this.getMembershipFee().equals(other.getMembershipFee()))
            && (this.getValidity() == null ? other.getValidity() == null : this.getValidity().equals(other.getValidity()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCourseNember() == null) ? 0 : getCourseNember().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getMembershipFee() == null) ? 0 : getMembershipFee().hashCode());
        result = prime * result + ((getValidity() == null) ? 0 : getValidity().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", courseNember=").append(courseNember);
        sb.append(", title=").append(title);
        sb.append(", fee=").append(fee);
        sb.append(", membershipFee=").append(membershipFee);
        sb.append(", validity=").append(validity);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}