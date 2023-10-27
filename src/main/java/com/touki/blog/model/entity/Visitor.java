package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Visitor {
    @TableId(type = IdType.ASSIGN_ID)
    private Long visitorId;
    private String uuid;// 访客标识码
    private String ip;// ip
    private String ipSource;// ip来源
    private String os;// 操作系统
    private String browser;// 浏览器
    private Date createTime;// 首次访问时间
    private Date lastTime;// 最后访问时间
    private Integer pv;// 访问页数统计
    private String userAgent;
}
