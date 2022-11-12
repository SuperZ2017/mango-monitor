package org.monitor.entity.info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassLoadingInfo {

    /**
     * 从虚拟机开始执行以来已加载的类的总数
     */
    private Long totalLoadedClassCount;


    /**
     * 虚拟机中加载的类的数量
     */
    private Integer loadedClassCount;


    /**
     * 虚拟机未加载类的数量
     */
    private Long unloadedClassCount;
}
