package org.monitor.entity.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuntimeInfo {

    /**
     * 虚拟机名称
     */
    private String name;


    /**
     * Java 虚拟机的启动时间（以毫秒为单位）。
     */
    private Long startTime;


    /**
     * Java 虚拟机的正常运行时间（以毫秒为单位）
     */
    private Long uptime;


    /**
     * 系统类加载器用于搜索类文件的 Java 类路径。
     */
    private String classPath;


    /**
     * 虚拟机规范名称
     */
    private String specName;


    /**
     * 实现虚拟机规范的厂商名称，如 OpenJDK、ZuluJDK
     */
    private String vmName;


    /**
     * 实现虚拟机规范的厂商
     */
    private String vmVendor;


    /**
     * 虚拟机实现的版本
     */
    private String vmVersion;
}
