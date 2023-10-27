package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.Visitor;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Touki
 */
public interface VisitorService extends IService<Visitor> {
    /**
     * 保存访客
     *
     * @param visitor Visitor
     */
    @Async
    void saveVisitor(Visitor visitor);

    /**
     * 访客是否再数据库中
     *
     * @param identification 访客标识
     * @return 是否存在
     */
    boolean hasUUID(String identification);
}
