package com.lee.xnxydev.pojo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 晓龙coding
 */
@Data
public class ReviewVO {
    private Long rId;

    private String username;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime createTime;
}
