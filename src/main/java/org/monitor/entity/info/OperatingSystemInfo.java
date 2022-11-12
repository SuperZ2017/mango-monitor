package org.monitor.entity.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperatingSystemInfo {

    /**
     * OS 名称
     */
    private String name;


    /**
     * OS 架构
     */
    private String arch;


    /**
     * OS 版本
     */
    private String version;


    /**
     * 可用于 Java 虚拟机的处理器数量
     */
    private Integer availableProcessors;


    /**
     * 最后一分钟的系统负载平均值
     */
    private Double systemLoadAverage;
}
