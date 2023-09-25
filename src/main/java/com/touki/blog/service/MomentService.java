package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.MomentUpdate;
import com.touki.blog.model.dto.NewMoment;
import com.touki.blog.model.entity.Moment;
import com.touki.blog.model.vo.PageResult;

/**
 * @author Touki
 */
public interface MomentService extends IService<Moment> {
    /**
     * 分页查询动态moment列表
     *
     * @param pageNum  分页索引
     * @param pageSize 分页大小
     */
    PageResult<Moment> momentPage(Integer pageNum, int pageSize);

    /**
     * 点赞动态
     *
     * @param momentId:
     * @return: void
     */
    void likeMoment(Long momentId);

    /**
     * 新建动态
     *
     * @param newMoment NewMoment
     */
    void saveMoment(NewMoment newMoment);

    /**
     * 后台分页查询动态
     *
     * @param pageNum  分页索引
     * @param pageSize 分页大小
     * @return PageResult<Moment>
     */
    PageResult<Moment> adminMomentPage(Integer pageNum, Integer pageSize);

    /**
     * 更新动态
     *
     * @param momentUpdate MomentUpdate
     */
    void updateMoment(MomentUpdate momentUpdate);
}
