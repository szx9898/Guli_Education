package com.atguigu.eduservice.entity.chapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterVo {
    private String id;
    private String title;
    //表示小结
    private List<VideoVo> children = new ArrayList<>();
}
