package org.monitor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.Result;
import org.monitor.service.ProfilerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("profiler")
@RequiredArgsConstructor
public class ProfilerController {


    private final ProfilerService profilerService;

    @GetMapping("startCpuProfiler")
    public Result startCpuProfiler() {
        profilerService.startCpuProfiler();
        return Result.ok();
    }


    @GetMapping("endCpuProfiler")
    public Result endCpuProfiler() {
        profilerService.endCpuProfiler();
        return Result.ok();
    }


}
