package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.CourseBusiness;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 教程
 **/
@Controller
public class CourseController {

    @Autowired
    private CourseBusiness courseBusiness;

    @GetMapping("/course")
    @OperatorLog(description = "教程页展示")
    public String showCourseListPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                     Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCoursePageList(pageIndex, pageSize));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-list";
    }

    @GetMapping("/course/{courseId}")
    @OperatorLog(description = "教程详情展示")
    public String showCourseContent(@PathVariable("courseId") Long courseId,
                                    @RequestParam(value = "chapter", defaultValue = "1") Integer chapter,
                                    Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCourseContent(courseId, chapter));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-detail";
    }
}
