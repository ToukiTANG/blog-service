package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.entity.Moment;
import com.touki.blog.entity.vo.PageResult;
import com.touki.blog.mapper.MomentMapper;
import com.touki.blog.service.MomentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {
    private final MomentMapper momentMapper;

    public MomentServiceImpl(MomentMapper momentMapper) {
        this.momentMapper = momentMapper;
    }

    /**
     * 分页查询动态moment列表
     *
     * @param pageNum  :  第几页，从1开始
     * @param pageSize : 分页大小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.Moment>
     */
    @Override
    public PageResult<Moment> momentPage(Integer pageNum, int pageSize) {
        Page<Moment> momentPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Moment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Moment::getCreateTime);
        Page<Moment> page = this.page(momentPage, queryWrapper);
        PageResult<Moment> result = new PageResult<>();
        result.setDataList(page.getRecords());
        result.setTotal((int) page.getTotal());
        result.setPageSize(pageSize);
        return result;
    }

    /**
     * 点赞动态
     *
     * @param momentId :
     * @return: void
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void likeMoment(Long momentId) {
        momentMapper.likeMoment(momentId);
    }
}
