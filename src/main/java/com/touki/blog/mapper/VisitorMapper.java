package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.dto.VisitorUpdateDTO;
import com.touki.blog.model.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface VisitorMapper extends BaseMapper<Visitor> {
    /**
     * 数据库是否有访客标识
     *
     * @param identification 访客标识
     * @return 是否存在
     */
    int hasUUID(String identification);

    /**
     * 更新记录到访客表
     *
     * @param visitorList ArrayList<VisitorUpdateDTO>
     */
    void updateRecord(ArrayList<VisitorUpdateDTO> visitorList);

    /**
     * 获取昨天新增访客的ip来源
     *
     * @return List<String>
     */
    List<String> getNewVisitorIpSourceYesterday();
}
