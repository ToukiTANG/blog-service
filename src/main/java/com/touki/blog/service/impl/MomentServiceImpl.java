package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.MomentMapper;
import com.touki.blog.model.entity.Moment;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.MomentService;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {
    private final MomentMapper momentMapper;
    private final RedisService redisService;

    public MomentServiceImpl(MomentMapper momentMapper, RedisService redisService) {
        this.momentMapper = momentMapper;
        this.redisService = redisService;
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
        String jsonString = redisService.getHash(RedisKeyConstant.MOMENT, pageNum.toString());
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<PageResult<Moment>>() {
            });
        }
        Page<Moment> momentPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Moment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Moment::getCreateTime);
        Page<Moment> page = this.page(momentPage, queryWrapper);
        page.getRecords().forEach(moment -> moment.setContent(MarkdownUtil.markdownToHtmlExtensions(moment.getContent())));
        PageResult<Moment> result = new PageResult<>();
        result.setDataList(page.getRecords());
        result.setTotal((int) page.getTotal());
        result.setPageSize(pageSize);
        redisService.setHash(RedisKeyConstant.MOMENT, pageNum.toString(), JsonUtil.writeValueAsString(result));
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
