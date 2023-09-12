package com.touki.blog.controller.admin;

import com.touki.blog.model.dto.NewScheduleJob;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.ScheduleJobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/scheduleJob")
public class ScheduleJobController {
    private final ScheduleJobService jobService;

    public ScheduleJobController(ScheduleJobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/add")
    public Result createJob(@RequestBody NewScheduleJob newScheduleJob) {
        jobService.createJob(newScheduleJob);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/execute")
    public Result executeJob(@RequestParam Long jobId) {
        jobService.execute(jobId);
        return Result.success();
    }
}
