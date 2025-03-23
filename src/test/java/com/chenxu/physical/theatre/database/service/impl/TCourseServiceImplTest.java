package com.chenxu.physical.theatre.database.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenxu.physical.theatre.bussiness.dto.ApiWeekCourseModel;
import com.chenxu.physical.theatre.database.constant.ChineseDayOfWeek;
import com.chenxu.physical.theatre.database.domain.TCourse;
import com.chenxu.physical.theatre.database.service.TCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;


/**
 * @author mamingze
 * @version 1.0
 * @title TCourseServiceImplTest
 * @description
 * @create 2025/3/23 14:12
 */
@SpringBootTest(classes = {com.chenxu.physical.theatre.ChenxuPhysicalTheatreServerApplication.class})
class TCourseServiceImplTest {

    @Autowired
    private TCourseService courseService;


    @Test
    void testAddCourse() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 8; j++) {
                TCourse course = new TCourse();
                course.setCourseName("test" + i + j);
                course.setType(1);
                course.setLesson(j + 0);
                course.setDate(LocalDate.now().plusDays(i));
                course.setMaximum(30);
                System.out.println(course);
//                courseService.save(course);

            }

        }

    }

    @Test
    void testGetTwoWeekCourses() {
        LocalDate currentDate = LocalDate.now();
        LocalDate newDate = currentDate.plusDays(14); // 加14天
        List<TCourse> list = courseService.list(new QueryWrapper<TCourse>().ge("date", currentDate).lt("date", newDate));
        list.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        for (int i = 0; i < 14; i++) {
            System.out.println(currentDate.plusDays(i));
            LocalDate date = currentDate.plusDays(i);
            ApiWeekCourseModel apiWeekCourseModel = new ApiWeekCourseModel();
            apiWeekCourseModel.setDate(date.toString());
            apiWeekCourseModel.setWeekday(ChineseDayOfWeek.of(date.getDayOfWeek().getValue()).getDesc());
            apiWeekCourseModel.setList(list.stream().filter(c -> c.getDate().equals(date)).toList());

        }
    }

}
