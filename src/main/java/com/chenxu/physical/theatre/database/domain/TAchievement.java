package com.chenxu.physical.theatre.database.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 样例课程
 *
 * @TableName t_achievement
 */
@TableName(value = "T_ACHIEVEMENT")
@Data
public class TAchievement {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 1:基础准备练习;2:基础关节训练;3:基础技巧训练;4:舞台五觉表达;5:力学原理训练;6:空间的运用和演员的关系;7:舞台上的生活动作;8:元素;9:道具的运用与表达;10:动物模拟;11:理论总结;
     */
    private Integer skillType;

    /**
     * 技巧名字
     */
    private String skillTypeName;

    /**
     * 具体部位的名字
     */
    private String specificName;

    /**
     *
     */
    private String desction;

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
        TAchievement other = (TAchievement) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSkillType() == null ? other.getSkillType() == null : this.getSkillType().equals(other.getSkillType()))
                && (this.getSkillTypeName() == null ? other.getSkillTypeName() == null : this.getSkillTypeName().equals(other.getSkillTypeName()))
                && (this.getSpecificName() == null ? other.getSpecificName() == null : this.getSpecificName().equals(other.getSpecificName()))
                && (this.getDesction() == null ? other.getDesction() == null : this.getDesction().equals(other.getDesction()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSkillType() == null) ? 0 : getSkillType().hashCode());
        result = prime * result + ((getSkillTypeName() == null) ? 0 : getSkillTypeName().hashCode());
        result = prime * result + ((getSpecificName() == null) ? 0 : getSpecificName().hashCode());
        result = prime * result + ((getDesction() == null) ? 0 : getDesction().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", skillType=").append(skillType);
        sb.append(", skillTypeName=").append(skillTypeName);
        sb.append(", specificName=").append(specificName);
        sb.append(", desction=").append(desction);
        sb.append("]");
        return sb.toString();
    }
}
