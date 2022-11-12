package org.monitor.service;

import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.info.*;
import org.monitor.entity.info.ThreadInfo;
import org.springframework.stereotype.Service;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import java.lang.management.*;



import static org.monitor.service.JvmService.jmxConnector;

@Slf4j
@Service
public class DashboardService {


    public RuntimeInfo getRuntimeInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ObjectName objectName = runtimeMXBean.getObjectName();

        try {
            MBeanServerConnection beanServerConnection = jmxConnector.getMBeanServerConnection();

            return RuntimeInfo.builder()
                    .name((String) beanServerConnection.getAttribute(objectName, "Name"))
                    .specName((String) beanServerConnection.getAttribute(objectName, "SpecName"))
                    .vmName((String) beanServerConnection.getAttribute(objectName, "VmName"))
                    .vmVendor((String) beanServerConnection.getAttribute(objectName, "VmVendor"))
                    .vmVersion((String) beanServerConnection.getAttribute(objectName, "VmVersion"))
                    .startTime((Long) beanServerConnection.getAttribute(objectName, "StartTime"))
                    .uptime((Long) beanServerConnection.getAttribute(objectName, "Uptime"))
                    .classPath(((String) beanServerConnection.getAttribute(objectName, "ClassPath")).substring(0, 100))
                    .build();
        } catch (Exception e) {
            log.error("getRuntimeInfo error : ", e);
            throw new RuntimeException(e.getMessage());
        }

    }


    public OperatingSystemInfo getOperatingSystemInfo() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        ObjectName objectName = operatingSystemMXBean.getObjectName();

        try {
            MBeanServerConnection beanServerConnection = jmxConnector.getMBeanServerConnection();

            return OperatingSystemInfo.builder()
                    .name((String) beanServerConnection.getAttribute(objectName, "Name"))
                    .arch((String) beanServerConnection.getAttribute(objectName, "Arch"))
                    .version((String) beanServerConnection.getAttribute(objectName, "Version"))
                    .availableProcessors((Integer) beanServerConnection.getAttribute(objectName, "AvailableProcessors"))
                    .systemLoadAverage((Double) beanServerConnection.getAttribute(objectName, "SystemLoadAverage"))
                    .build();
        } catch (Exception e) {
            log.error("getOperatingSystemInfo error : ", e);
            throw new RuntimeException(e.getMessage());
        }

    }



    public ClassLoadingInfo getClassLoadingInfo() {
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        ObjectName objectName = classLoadingMXBean.getObjectName();

        try {
            MBeanServerConnection beanServerConnection = jmxConnector.getMBeanServerConnection();

            return ClassLoadingInfo.builder()
                    .totalLoadedClassCount((Long) beanServerConnection.getAttribute(objectName, "TotalLoadedClassCount"))
                    .loadedClassCount((Integer) beanServerConnection.getAttribute(objectName, "LoadedClassCount"))
                    .unloadedClassCount((Long) beanServerConnection.getAttribute(objectName, "UnloadedClassCount"))
                    .build();
        } catch (Exception e) {
            log.error("getClassLoadingInfo error : ", e);
            throw new RuntimeException(e.getMessage());
        }

    }



    public ThreadInfo getThreadInfo()  {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ObjectName objectName = threadMXBean.getObjectName();

        try {
            MBeanServerConnection beanServerConnection = jmxConnector.getMBeanServerConnection();

            return ThreadInfo.builder()
                    .threadCount((Integer) beanServerConnection.getAttribute(objectName, "ThreadCount"))
                    .daemonThreadCount((Integer) beanServerConnection.getAttribute(objectName, "DaemonThreadCount"))
                    .peakThreadCount((Integer) beanServerConnection.getAttribute(objectName, "PeakThreadCount"))
                    .totalStartedThreadCount((Long) beanServerConnection.getAttribute(objectName, "TotalStartedThreadCount"))
                    .build();
        } catch (Exception e) {
            log.error("getThreadInfo error : ", e);
            throw new RuntimeException(e.getMessage());
        }

    }


    public MemoryInfo getMemoryInfo(int type) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        ObjectName objectName = memoryMXBean.getObjectName();

        try {
            MBeanServerConnection beanServerConnection = jmxConnector.getMBeanServerConnection();

            String attribute = type == MemoryInfo.HEAP ? "HeapMemoryUsage" : "NonHeapMemoryUsage";
            CompositeDataSupport compositeDataSupport = (CompositeDataSupport) beanServerConnection.getAttribute(objectName, attribute);

            return MemoryInfo.builder()
                    .init((Long) compositeDataSupport.get("init"))
                    .max((Long) compositeDataSupport.get("max"))
                    .used((Long) compositeDataSupport.get("used"))
                    .committed((Long) compositeDataSupport.get("committed"))
                    .build();
        } catch (Exception e) {
            log.error("getMemoryInfo error : ", e);
            throw new RuntimeException(e.getMessage());
        }
    }


}