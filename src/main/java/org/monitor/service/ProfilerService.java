package org.monitor.service;

import lombok.extern.slf4j.Slf4j;
import org.monitor.profiler.CpuProfilerTask;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProfilerService {

    public static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);


    public void startCpuProfiler() {
        scheduledExecutorService.scheduleAtFixedRate(new CpuProfilerTask(), 0, 1000, TimeUnit.MILLISECONDS);
    }




    public void endCpuProfiler() {
        scheduledExecutorService.shutdown();
    }
}
