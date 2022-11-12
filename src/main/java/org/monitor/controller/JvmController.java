package org.monitor.controller;

import com.sun.tools.attach.AttachNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.JVM;
import org.monitor.entity.Result;
import org.monitor.service.JvmService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("jvm")
@RequiredArgsConstructor
public class JvmController {

    private final JvmService jvmService;


    /**
     * 获取当前运行中的 jvm pid & name 列表
     */
    @GetMapping
    public Result<List<JVM>> getJVMList() {
        List<JVM> jvms = jvmService.getJvm();
        return Result.ok(jvms);
    }


    @GetMapping(path = "attach/{pid}")
    public void attach(@PathVariable("pid") Integer pid) throws IOException, AttachNotSupportedException {
        log.info("attach pid : {}", pid);
        jvmService.attach(pid);
    }
}
