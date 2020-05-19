package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-06
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoMapper videoMapper;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //查询所有章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(chapterWrapper);
        //查询所有小结
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoMapper.selectList(videoWrapper);
        //小结封装成VideoVo
        //章节封装成ChapterVo
        List<ChapterVo> chapterVoList = new ArrayList<>();
        eduChapterList.forEach(eduChapter -> {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            List<VideoVo> videoVoList = new ArrayList<>();
            eduVideoList.forEach(eduVideo -> {
                if (eduVideo.getChapterId()!=null && eduVideo.getChapterId().equals(chapterVo.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            });
            chapterVo.setChildren(videoVoList);
            chapterVoList.add(chapterVo);
        });
        return chapterVoList;
    }

    @Override
    public Boolean deleteChapter(String chapterId) {
        //有小结不删除，没有则删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        Integer count = videoMapper.selectCount(wrapper);
        if (count>0){
            throw new GuliException(20001,"不能删除");
        }else {
            int delete = baseMapper.deleteById(chapterId);
            return delete>0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        baseMapper.delete(new QueryWrapper<EduChapter>().eq("course_id",courseId));
    }
}
