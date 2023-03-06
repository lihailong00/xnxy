package com.lee.xnxydev.pojo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeVO {
    private Long gId;

    private String username;

    private String goodsName;

    private String goodsDesc;

    private BigDecimal price;

    private String type;

    private String goodsImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
