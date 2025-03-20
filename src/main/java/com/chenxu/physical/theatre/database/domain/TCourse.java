package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 课程信息表
 * @TableName T_COURSE
 */
@TableName(value ="T_COURSE")
@Data
public class TCourse {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String courseName;

    /**
     * 1:未上;2:已上;3:已删除;
     */
    private Integer type;

    /**
     * 课程在某一天的具体第几节
     */
    private Integer lesson;

    /**
     * 课程时间
     */
    private Date date;

    /**
     * 课程最大人数
     */
    private Integer maximum;

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
        TCourse other = (TCourse) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCourseName() == null ? other.getCourseName() == null : this.getCourseName().equals(other.getCourseName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getLesson() == null ? other.getLesson() == null : this.getLesson().equals(other.getLesson()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getMaximum() == null ? other.getMaximum() == null : this.getMaximum().equals(other.getMaximum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCourseName() == null) ? 0 : getCourseName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLesson() == null) ? 0 : getLesson().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getMaximum() == null) ? 0 : getMaximum().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", courseName=").append(courseName);
        sb.append(", type=").append(type);
        sb.append(", lesson=").append(lesson);
        sb.append(", date=").append(date);
        sb.append(", maximum=").append(maximum);
        sb.append("]");
        return sb.toString();
    }
}