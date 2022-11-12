package org.monitor.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.monitor.service.JvmService.jmxConnector;

@Aspect
@Component
public class ArgsCheck {

    @Pointcut("execution(* org.monitor.service.DashboardService.*(..))")
    public void point(){

    }


    @Before("point()")
    public void checkMmxConnector() {
        Assert.notNull(jmxConnector, "连接未建立");
    }

}
