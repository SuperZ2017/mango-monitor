package org.monitor.controller;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.AgentAction;
import org.monitor.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.monitor.service.JvmService.connectPid;

@Slf4j
@RestController
@RequestMapping("agent")
public class AgentController {

    @Value("${agent.jar.path}")
    private String agentJarPath;

    @PostMapping
    public Result agent(@RequestBody @Validated AgentAction action) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException, InterruptedException {
        VirtualMachine virtualMachine = VirtualMachine.attach(String.valueOf(connectPid));

        log.info("connectPid is {}", connectPid);
        virtualMachine.loadAgent(agentJarPath, action.getFunction() + "&" + action.getClassName() + "&" + action.getMethodName());
        virtualMachine.detach();

        return Result.ok();
    }
}
