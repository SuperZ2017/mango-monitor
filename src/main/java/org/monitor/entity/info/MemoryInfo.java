package org.monitor.entity.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoryInfo {

    // 堆内存
    public static final int HEAP = 1;

    // 非堆内存
    public static final int NON_HEAP = 2;

    /**
     * 虚拟机最初从操作系统请求进行内存管理的内存量（以字节计）
     */
    private Long init;


    /**
     * 使用的内存量（以字节计）
     */
    private Long used;


    /**
     * 虚拟机提供的内存量（以字节为单位）
     */
    private Long committed;


    /**
     * 可用于内存管理的最大内存量（以字节为单位）
     */
    private Long max;

}
