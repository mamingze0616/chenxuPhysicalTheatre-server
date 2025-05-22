package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxu.physical.theatre.database.constant.TCourseOrderSpiltStatusEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * 课程订单拆分表,将每个订单拆分成一节一节课程来实现过期未用完的情况
 *
 * @TableName T_COURSE_ORDER_SPILT
 */
@TableName(value = "T_COURSE_ORDER_SPILT")
@Data
public class TCourseOrderSpilt {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer courseOrderId;

    /**
     *
     */
    private Integer userId;

    /**
     *
     */
    private Integer appointmentId;

    /**
     * 1:已核销;2:未核销
     */
    private TCourseOrderSpiltStatusEnum status;

    /**
     * 核销时间
     */
    private LocalDate writeOffDate;

    /**
     * 拆分id
     */
    private Integer splitIndex;

    /**
     * 有效期截止时间结束当天+1
     */
    private LocalDate endTime;
    /**
     * 有效期截止时间结束当天+1
     */
    private LocalDate startTime;

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
        TCourseOrderSpilt other = (TCourseOrderSpilt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCourseOrderId() == null ? other.getCourseOrderId() == null : this.getCourseOrderId().equals(other.getCourseOrderId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getAppointmentId() == null ? other.getAppointmentId() == null : this.getAppointmentId().equals(other.getAppointmentId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getWriteOffDate() == null ? other.getWriteOffDate() == null : this.getWriteOffDate().equals(other.getWriteOffDate()))
                && (this.getSplitIndex() == null ? other.getSplitIndex() == null : this.getSplitIndex().equals(other.getSplitIndex()))
                && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
                && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCourseOrderId() == null) ? 0 : getCourseOrderId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAppointmentId() == null) ? 0 : getAppointmentId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getWriteOffDate() == null) ? 0 : getWriteOffDate().hashCode());
        result = prime * result + ((getSplitIndex() == null) ? 0 : getSplitIndex().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", courseOrderId=").append(courseOrderId);
        sb.append(", userId=").append(userId);
        sb.append(", appointmentId=").append(appointmentId);
        sb.append(", status=").append(status);
        sb.append(", writeOffDate=").append(writeOffDate);
        sb.append(", splitIndex=").append(splitIndex);
        sb.append(", endTime=").append(endTime);
        sb.append(", startTime=").append(startTime);
        sb.append("]");
        return sb.toString();
    }
}
