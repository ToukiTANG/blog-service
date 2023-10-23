package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Touki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long logId;
    private String username;// 用户名称
    private String ip;// ip
    private String ipSource;// ip来源
    private String os;// 操作系统
    private String browser;// 浏览器
    private Boolean status;// 登录状态
    private String description;// 操作信息
    private Date createTime;// 操作时间
    private String userAgent;

    public LoginLog(String username, String ip, boolean status, String description, String userAgent) {
        this.username = username;
        this.ip = ip;
        this.status = status;
        this.description = description;
        this.createTime = new Date();
        this.userAgent = userAgent;
    }
}
