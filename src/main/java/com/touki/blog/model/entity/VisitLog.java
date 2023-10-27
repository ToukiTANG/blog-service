package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Touki
 */
@Data
@NoArgsConstructor
public class VisitLog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long logId;
    private String uuid;// 访客标识码
    private String uri;// 请求接口
    private String method;// 请求方式
    private String param;// 请求参数
    private String behavior;// 访问行为
    private String content;// 访问内容
    private String remark;// 备注
    private String ip;// ip
    private String ipSource;// ip来源
    private String os;// 操作系统
    private String browser;// 浏览器
    private Integer timeConsumed;// 请求耗时（毫秒）
    private String userAgent;
    private Date createTime;

    public VisitLog(String uuid, String uri, String method, String behavior, String content, String remark, String ip
            , Integer timeConsumed, String userAgent) {
        this.uuid = uuid;
        this.uri = uri;
        this.method = method;
        this.behavior = behavior;
        this.content = content;
        this.remark = remark;
        this.ip = ip;
        this.timeConsumed = timeConsumed;
        this.createTime = new Date();
        this.userAgent = userAgent;
    }
}
