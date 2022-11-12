package org.monitor.entity.info;

import lombok.Data;

@Data
public class StackTrace {

    private String threadName;
    private String threadState;
    private ClassAndMethod[] stack = new ClassAndMethod[0];
}
