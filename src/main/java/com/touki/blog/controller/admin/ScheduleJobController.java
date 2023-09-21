package com.touki.blog.controller.admin;

import com.touki.blog.model.dto.NewScheduleJob;
import com.touki.blog.model.query.JobQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.schedule.ScheduleJob;
import com.touki.blog.service.ScheduleJobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/")
public class ScheduleJobController {
    private final ScheduleJobService jobService;

    public ScheduleJobController(ScheduleJobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/job/add")
    public Result createJob(@RequestBody NewScheduleJob newScheduleJob) {
        jobService.createJob(newScheduleJob);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/job/execute")
    public Result executeJob(@RequestParam Long jobId) {
        jobService.execute(jobId);
        return Result.success();
    }

    @GetMapping("/jobs")
    public Result jobs(JobQuery query) {
        PageResult<ScheduleJob> jobPageResult = jobService.jobs(query);
        return Result.data(jobPageResult);
    }
}
