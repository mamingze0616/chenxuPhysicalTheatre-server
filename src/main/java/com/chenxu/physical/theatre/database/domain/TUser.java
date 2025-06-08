package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxu.physical.theatre.bussiness.dto.ApiRequestPageDto;
import com.chenxu.physical.theatre.database.constant.TUserCardType;
import com.chenxu.physical.theatre.database.constant.TUserType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * user info
 *
 * @TableName T_USER
 */
@TableName(value = "T_USER")
@Data
public class TUser extends ApiRequestPageDto {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String openid;

    /**
     *
     */
    private String nickname;

    /**
     *
     */
    private String avatar;

    /**
     *
     */
    private String phone;

    /**
     * 1:admin;2:user;
     */
    private TUserType type;

    /**
     * 0:注销;1在线
     */
    private Integer status;

    /**
     *
     */
    private LocalDateTime createdAt;

    /**
     *
     */
    private LocalDateTime loginAt;

    /**
     *
     */
    private Integer effectiveCourseCount;
    /**
     *
     */
    private Integer completedCourseCount;

    @TableField(exist = false)
    private List<TAppointmentInfo> appointmentInfos;

    @TableField(exist = false)
    List<TUserCoupons> userCoupons;
    /**
     * 会员卡类型
     */
    TUserCardType cardType;

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
        TUser other = (TUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
                && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
                && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getLoginAt() == null ? other.getLoginAt() == null : this.getLoginAt().equals(other.getLoginAt()))
                && (this.getEffectiveCourseCount() == null ? other.getEffectiveCourseCount() == null : this.getEffectiveCourseCount().equals(other.getEffectiveCourseCount()))
                && (this.getCompletedCourseCount() == null ? other.getCompletedCourseCount() == null : this.getCompletedCourseCount().equals(other.getCompletedCourseCount()))
                && (this.getAppointmentInfos() == null ? other.getAppointmentInfos() == null : this.getAppointmentInfos().equals(other.getAppointmentInfos()))
                && (this.getUserCoupons() == null ? other.getUserCoupons() == null : this.getUserCoupons().equals(other.getUserCoupons()))
                && (this.getCardType() == null ? other.getCardType() == null : this.getCardType().equals(other.getCardType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getLoginAt() == null) ? 0 : getLoginAt().hashCode());
        result = prime * result + ((getEffectiveCourseCount() == null) ? 0 : getEffectiveCourseCount().hashCode());
        result = prime * result + ((getCompletedCourseCount() == null) ? 0 : getCompletedCourseCount().hashCode());
        result = prime * result + ((getAppointmentInfos() == null) ? 0 : getAppointmentInfos().hashCode());
        result = prime * result + ((getUserCoupons() == null) ? 0 : getUserCoupons().hashCode());
        result = prime * result + ((getCardType() == null) ? 0 : getCardType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openid=").append(openid);
        sb.append(", nickname=").append(nickname);
        sb.append(", avatar=").append(avatar);
        sb.append(", phone=").append(phone);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", createdat=").append(createdAt);
        sb.append(", loginat=").append(loginAt);
        sb.append(", appointmentInfos=").append(appointmentInfos);
        sb.append(", effectiveCourseCount=").append(effectiveCourseCount);
        sb.append(", completedCourseCount=").append(completedCourseCount);
        sb.append(", cardType=").append(cardType);
        sb.append("]");
        return sb.toString();
    }
}
