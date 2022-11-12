package org.monitor.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.Result;
import org.monitor.entity.info.*;
import org.monitor.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping("runtimeInfo")
    public Result<RuntimeInfo> getRuntimeInfo() {
        RuntimeInfo runtimeInfo = dashboardService.getRuntimeInfo();
        return Result.ok(runtimeInfo);
    }


    @GetMapping("operatingSystemInfo")
    public Result<OperatingSystemInfo> getOperatingSystemInfo() {
        OperatingSystemInfo operatingSystemInfo = dashboardService.getOperatingSystemInfo();
        return Result.ok(operatingSystemInfo);
    }


    @GetMapping("classLoadingInfo")
    public Result<ClassLoadingInfo> getClassLoadingInfo() {
        ClassLoadingInfo classLoadingInfo = dashboardService.getClassLoadingInfo();
        return Result.ok(classLoadingInfo);
    }


    @GetMapping("threadInfo")
    public Result<ThreadInfo> getThreadInfo() {
        ThreadInfo threadInfo = dashboardService.getThreadInfo();
        return Result.ok(threadInfo);
    }


    @GetMapping("heapMemoryInfo")
    public Result<MemoryInfo> getHeapMemoryInfo() {
        MemoryInfo memoryInfo = dashboardService.getMemoryInfo(MemoryInfo.HEAP);
        return Result.ok(memoryInfo);
    }


    @GetMapping("nonHeapMemoryInfo")
    public Result<MemoryInfo> getNonHeapMemoryInfo() {
        MemoryInfo memoryInfo = dashboardService.getMemoryInfo(MemoryInfo.NON_HEAP);
        return Result.ok(memoryInfo);
    }

}
