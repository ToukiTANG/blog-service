package com.touki.blog.controller.admin;

import com.touki.blog.exception.MyException;
import com.touki.blog.model.dto.NewTag;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.query.SimpleQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.TagsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/tag")
public class AdminTagController {
    private final TagsService tagsService;

    public AdminTagController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/list")
    public Result tagList(SimpleQuery query) {
        PageResult<Tag> result = tagsService.tagList(query.getPageNum(), query.getPageSize());
        return Result.data(result);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/save")
    public Result saveTag(@RequestBody NewTag newTag) {
        tagsService.saveTag(newTag);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/delete")
    public Result deleteTag(Long tagId) throws MyException {
        tagsService.deleteTag(tagId);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/update")
    public Result updateTag(Tag tag) {
        tagsService.updateTag(tag);
        return Result.success();
    }
}
