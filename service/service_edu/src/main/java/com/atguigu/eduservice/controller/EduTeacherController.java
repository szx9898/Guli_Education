package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-29
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    @Resource
    private EduTeacherService teacherService;

    @ApiOperation("修改讲师数据")
    @PostMapping("updateTeacher")
    public R update(@RequestBody EduTeacher eduTeacher) {
        boolean update = teacherService.updateById(eduTeacher);
        return update ? R.ok() : R.error();
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("query/{id}")
    public R queryTeacherById(@PathVariable String id) {
        EduTeacher eduTeacher = this.teacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.save(eduTeacher);
        return flag ? R.ok() : R.error();
    }


    @ApiOperation("查询所有数据")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("delete/{id}")
    public R removeTeacher(@ApiParam("所需id") @PathVariable("id") String id) {
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("分页查询讲师")
    @GetMapping("pageTeacher/{current}/{size}")
    public R pageListTeacher(@ApiParam("当前页") @PathVariable("current") Long current,
                             @ApiParam("显示数量") @PathVariable("size") Long size) {

        if (current <= 0) {
            current = 1l;
        }
        Page<EduTeacher> page = new Page<>(current, size);
        IPage<EduTeacher> ipage = teacherService.page(page, null);
        List<EduTeacher> records = ipage.getRecords();
        long pages = ipage.getPages();
        Map<String, Object> map = new HashMap<>();
        map.put("total", pages);
        map.put("records", records);
        return R.ok().data(map);
    }

    @ApiOperation("条件查询讲师")
    @PostMapping("pageTeacherCondition/{current}/{size}")
    public R pageTeacherCondition(@ApiParam("当前页") @PathVariable("current") Long current,
                                  @ApiParam("显示数量") @PathVariable("size") Long size,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {

        Page<EduTeacher> pageTeacher = new Page<>(current <= 0 ? 1l : current, size);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String end = teacherQuery.getEnd();
        String begin = teacherQuery.getBegin();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_modified", end);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }

        wrapper.orderByDesc("gmt_create");
        IPage<EduTeacher> ipage = teacherService.page(pageTeacher, wrapper);
        List<EduTeacher> records = ipage.getRecords();

        long pages = ipage.getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("total", pages);
        map.put("records", records);
        return R.ok().data(map);
    }
}

