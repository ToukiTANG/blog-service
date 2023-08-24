package com.touki.blog.model.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Touki
 */
@Data
public class ArchiveResult {

    private Integer count;

    private HashMap<String, List<ArchiveBlog>> resultMap;
}
