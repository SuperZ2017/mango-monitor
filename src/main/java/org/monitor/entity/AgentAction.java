package org.monitor.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
public class AgentAction {

    @NotEmpty(message = "功能名不能为空")
    @Pattern(regexp = "\\b(trace|time|params)\\b", message = "不支持该功能")
    private String function;


    @NotEmpty(message = "类名不能为空")
    private String className;


    @NotEmpty(message = "方法名不能为空")
    private String methodName;

}
