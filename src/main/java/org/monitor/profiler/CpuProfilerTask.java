package org.monitor.profiler;

import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.info.ClassAndMethod;
import org.monitor.entity.info.StackTrace;
import org.monitor.util.JsonUtils;
import org.springframework.stereotype.Component;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import static org.monitor.service.JvmService.jmxConnector;

@Slf4j
@Component
public class CpuProfilerTask implements Runnable {

    private volatile Map<StackTrace, LongAdder> metrics = new ConcurrentHashMap<>();

    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    ObjectName objectName = threadMXBean.getObjectName();

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        MBeanServerConnection beanServerConnection = null;
        CompositeData[] allThreads = null;
        Object[] params = {false, false};
        String[] signature = {boolean.class.getName(), boolean.class.getName()};
        try {
            beanServerConnection = jmxConnector.getMBeanServerConnection();
            allThreads = (CompositeData[]) beanServerConnection.invoke(objectName, "dumpAllThreads", params, signature);
        } catch (Exception e) {
            log.error("CpuProfilerTask error ", e);
            return;
        }

        if (allThreads == null || allThreads.length == 0) {
            return;
        }

        for (CompositeData threadInfo : allThreads) {
            StackTrace stacktrace = new StackTrace();
            stacktrace.setThreadName((String) threadInfo.get("threadName"));
            stacktrace.setThreadState(threadInfo.get("threadState").toString());

            CompositeData[] stackTraceElements = (CompositeData[]) threadInfo.get("stackTrace");
            List<ClassAndMethod> stack = new ArrayList<>(stackTraceElements.length);
            int totalLength = 0;

            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                CompositeData stackTraceElement = stackTraceElements[i];
                ClassAndMethod classAndMethod = new ClassAndMethod();
                String className = (String) stackTraceElement.get("className");
                String methodName = (String) stackTraceElement.get("methodName");

                classAndMethod.setClassName(className);
                classAndMethod.setMethodName(methodName);

                stack.add(classAndMethod);

                totalLength += className.length() + methodName.length();

                // 避免过长
                if (totalLength >= 800000) {
                    stack.add(new ClassAndMethod("_stack_", "_trimmed_"));
                    break;
                }
            }

            Collections.reverse(stack);

            stacktrace.setStack(stack.toArray(new ClassAndMethod[0]));

            appendValue(stacktrace);
        }

        long endTime = System.currentTimeMillis();


        // export
        for (Map.Entry<StackTrace, LongAdder> entry : metrics.entrySet()) {
            StackTrace stackTrace = entry.getKey();
            Map<String, Object> map = new HashMap<>();
            map.put("startEpoch", startTime);
            map.put("endEpoch", endTime);
            map.put("threadName", stackTrace.getThreadName());
            map.put("threadState", stackTrace.getThreadState());

            ClassAndMethod[] classAndMethodArray = stackTrace.getStack();
            if (classAndMethodArray != null) {
                List<String> stackArray = new ArrayList<>(classAndMethodArray.length);
                for (ClassAndMethod classAndMethod : classAndMethodArray) {
                    stackArray.add(classAndMethod.getClassName() + "." + classAndMethod.getMethodName());
                }
                map.put("stacktrace", stackArray);
            }

            map.put("count", entry.getValue().longValue());

            System.out.printf("ConsoleOutputReporter - %s: %s%n", "cpuProfiler", JsonUtils.serialize(map));
        }

    }


    public void appendValue(StackTrace stackTrace) {
        LongAdder count = metrics.computeIfAbsent(stackTrace, key -> new LongAdder());
        count.increment();
    }


}
