package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.entity.Moment;
import com.touki.blog.entity.vo.PageResult;

/**
 * @author Touki
 */
public interface MomentService extends IService<Moment> {
    /**
     * 分页查询动态moment列表
     *
     * @param pageNum:  第几页，从1开始
     * @param pageSize: 分页大小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.Moment>
     */
    PageResult<Moment> momentPage(Integer pageNum, int pageSize);

    /**
     * 点赞动态
     *
     * @param momentId:
     * @return: void
     */
    void likeMoment(Long momentId);
}
