package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    //根据课程id查询课程信息
    @GetMapping("getCourseInfoById/{id}")
    public CourseWebVoOrder getCourseInfoById(@PathVariable String id) {
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo, courseWebVoOrder);
        return courseWebVoOrder;
    }

    //条件查询带分页
    @ApiOperation("条件查询讲师")
    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam("当前页") @PathVariable("current") Long current,
                                  @ApiParam("显示数量") @PathVariable("limit") Long limit,
                                  @RequestBody(required = false) CourseFrontVo eduCourse) {
        Page<EduCourse> page = new Page<>(current, limit);
        Map<String, Object> map = courseService.getFrontCourseList(page, eduCourse);
        return R.ok().data(map);
    }

    //查询课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId) {
        //查询课程基本信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //查询课程包含章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("oourseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList);
    }

}
