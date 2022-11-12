package org.monitor.entity.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThreadInfo {

    /**
     * 当前线程数，包括守护进程线程和非守护程序线程数
     */
    private Integer threadCount;


    /**
     * 虚拟机启动或峰值复位后的峰值活动线程数
     */
    private Integer peakThreadCount;


    /**
     * 当前的守护进程线程数
     */
    private Integer daemonThreadCount;


    /**
     * 虚拟机启动以来创建并启动的线程总数
     */
    private Long totalStartedThreadCount;
}
