package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.MomentMapper;
import com.touki.blog.model.dto.MomentUpdate;
import com.touki.blog.model.dto.NewMoment;
import com.touki.blog.model.entity.Moment;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.MomentService;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.springframework.beans.BeanUtils;
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
     */
    @Override
    public PageResult<Moment> momentPage(Integer pageNum, int pageSize) {
        Page<Moment> momentPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Moment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Moment::getCreateTime);
        Page<Moment> page = this.page(momentPage, queryWrapper);
        page.getRecords().forEach(moment -> moment.setContent(MarkdownUtil.markdownToHtmlExtensions(moment.getContent())));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMoment(NewMoment newMoment) {
        Moment moment = new Moment();
        BeanUtils.copyProperties(newMoment, moment);
        this.save(moment);
    }

    @Override
    public PageResult<Moment> adminMomentPage(Integer pageNum, Integer pageSize) {
        Page<Moment> momentPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Moment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Moment::getCreateTime);
        momentPage = this.page(momentPage, queryWrapper);

        PageResult<Moment> result = new PageResult<>();
        result.setDataList(momentPage.getRecords());
        result.setTotal((int) momentPage.getTotal());
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoment(MomentUpdate momentUpdate) {
        Moment moment = new Moment();
        BeanUtils.copyProperties(momentUpdate, moment);
        this.updateById(moment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishOrNot(Long momentId, Boolean published) {
        LambdaUpdateWrapper<Moment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Moment::getMomentId, momentId).set(Moment::getPublished, published);
        this.update(updateWrapper);
    }
}
