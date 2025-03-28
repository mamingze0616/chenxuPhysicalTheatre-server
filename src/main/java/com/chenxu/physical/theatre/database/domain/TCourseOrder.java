package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    @TableId
    private Integer id;

    /**
     *
     */
    private Integer userId;

    /**
     * 已购课数
     */
    private Integer coursrNumber;

    /**
     * 1:赠送;2:支付
     */
    private Integer type;

    /**
     * 1:有效;2:作废
     */
    private Integer status;

    /**
     *
     */
    private LocalDateTime createAt;

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
                && (this.getCoursrNumber() == null ? other.getCoursrNumber() == null : this.getCoursrNumber().equals(other.getCoursrNumber()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateAt() == null ? other.getCreateAt() == null : this.getCreateAt().equals(other.getCreateAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCoursrNumber() == null) ? 0 : getCoursrNumber().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateAt() == null) ? 0 : getCreateAt().hashCode());
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
        sb.append(", coursrNumber=").append(coursrNumber);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", createAt=").append(createAt);
        sb.append("]");
        return sb.toString();
    }
}
