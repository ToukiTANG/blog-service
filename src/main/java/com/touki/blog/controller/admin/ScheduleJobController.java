package com.touki.blog.controller.admin;

import com.touki.blog.model.dto.NewScheduleJob;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.ScheduleJobService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/add")
    public Result createJob(@RequestBody NewScheduleJob newScheduleJob) {
        jobService.createJob(newScheduleJob);
        return Result.success();
    }
}
