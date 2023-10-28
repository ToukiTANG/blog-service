package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.VisitorUpdateDTO;
import com.touki.blog.model.entity.Visitor;
import com.touki.blog.model.query.VisitorQuery;
import com.touki.blog.model.vo.PageResult;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 更新统计到访客表
     *
     * @param visitorList ArrayList<VisitorUpdateDTO>
     */
    void updateRecord(ArrayList<VisitorUpdateDTO> visitorList);

    /**
     * 获取昨天新增访客的ip来源信息
     *
     * @return List<String>
     */
    List<String> getNewVisitorIpSourceYesterday();

    /**
     * 分页查询访客
     *
     * @param query VisitorQuery
     * @return PageResult<Visitor>
     */
    PageResult<Visitor> visitorList(VisitorQuery query);

    /**
     * @param visitorId 访客id
     * @param uuid      访客标识
     */
    void deleteVisitor(Long visitorId, String uuid);
}
