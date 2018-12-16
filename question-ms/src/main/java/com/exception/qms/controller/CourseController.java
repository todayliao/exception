package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.CourseBusiness;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.model.form.course.EditCourseChapterForm;
import com.exception.qms.model.form.course.PublishCourseForm;
import com.exception.qms.utils.SpringMVCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.exception.common.BaseResponse;

import javax.servlet.http.HttpSession;

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

    @GetMapping("/course/{enTitle}")
    @OperatorLog(description = "教程详情展示")
    public String showCourseContent(@PathVariable("enTitle") String enTitle,
                                    Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCourseContent(enTitle, null));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-detail";
    }

    @GetMapping("/course/{enTitle}/{chapterEnTitle}")
    @OperatorLog(description = "教程详情展示(带标题)")
    public String showCourseContentWithChapterEnTitle(@PathVariable("enTitle") String enTitle,
                                                      @PathVariable("chapterEnTitle") String chapterEnTitle,
                                                      Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCourseContent(enTitle, chapterEnTitle));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-detail";
    }

    @PostMapping("/course/publish")
    @OperatorLog(description = "发布教程")
    @ResponseBody
    public BaseResponse publishCourse(@Validated PublishCourseForm publishCourseDTO, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return courseBusiness.publishCourse(publishCourseDTO, user);
    }

    @GetMapping("/course/publish")
    @OperatorLog(description = "教程发布页")
    public String showCoursePublishPage(Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-publish";
    }

    @GetMapping("/course/{enTitle}/{chapterEnTitle}/edit")
    @OperatorLog(description = "章节编辑页")
    public String showEditChapterPage(@PathVariable("enTitle") String enTitle,
                              @PathVariable("chapterEnTitle") String chapterEnTitle,
                              Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.showEditChapterPage(enTitle, chapterEnTitle));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "chapter/chapter-edit";
    }

    @PostMapping("/chapter/edit")
    @ResponseBody
    public BaseResponse editChapter(@Validated EditCourseChapterForm editCourseChapterForm, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return courseBusiness.editChapter(editCourseChapterForm, user);
    }


}
