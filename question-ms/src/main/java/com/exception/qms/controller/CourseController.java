package com.exception.qms.controller;

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

    @GetMapping({"", "/home"})
//    @OperatorLog(description = "教程页展示")
    public String showCourseListPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                     Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCoursePageList(pageIndex, pageSize));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.HOME.getCode());
        return "course/course-list";
    }

    @GetMapping("/{courseEnTitle}")
//    @OperatorLog(description = "教程详情展示")
    public String showCourseContent(@PathVariable("courseEnTitle") String courseEnTitle,
                                    Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCourseContent(courseEnTitle, null));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-detail";
    }

    @GetMapping("/{courseEnTitle}/{chapterEnTitle}")
//    @OperatorLog(description = "教程详情展示(带标题)")
    public String showCourseContentWithChapterEnTitle(@PathVariable("courseEnTitle") String courseEnTitle,
                                                      @PathVariable("chapterEnTitle") String chapterEnTitle,
                                                      Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.queryCourseContent(courseEnTitle, chapterEnTitle));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-detail";
    }

    @PostMapping("/course/publish")
//    @OperatorLog(description = "发布教程")
    @ResponseBody
    public BaseResponse publishCourse(@Validated PublishCourseForm publishCourseDTO, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return courseBusiness.publishCourse(publishCourseDTO, user);
    }

    @GetMapping("/course/publish")
//    @OperatorLog(description = "教程发布页")
    public String showCoursePublishPage(Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "course/course-publish";
    }

    @GetMapping("/course/{courseEnTitle}/{chapterEnTitle}/edit")
//    @OperatorLog(description = "章节编辑页")
    public String showEditChapterPage(@PathVariable("courseEnTitle") String courseEnTitle,
                                      @PathVariable("chapterEnTitle") String chapterEnTitle,
                                      Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), courseBusiness.showEditChapterPage(courseEnTitle, chapterEnTitle));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.COURSE.getCode());
        return "chapter/chapter-edit";
    }

    @PostMapping("/chapter/edit")
    @ResponseBody
    public BaseResponse editChapter(@Validated EditCourseChapterForm editCourseChapterForm, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return courseBusiness.editChapter(editCourseChapterForm, user);
    }

    @GetMapping("/course/{courseEnTitle}/{chapterEnTitle}/pushToBaidu")
//    @OperatorLog(description = "推送章节连接给百度")
    @ResponseBody
    public Boolean pushToBaidu(@PathVariable("courseEnTitle") String courseEnTitle,
                               @PathVariable("chapterEnTitle") String chapterEnTitle) {
        return courseBusiness.pushToBaidu(courseEnTitle, chapterEnTitle);
    }

}
