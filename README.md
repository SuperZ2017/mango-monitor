# mango-monitor
一个监控 jvm 信息的 springboot 学习的 demo 项目。

## 实现功能

- 监控 jvm 系统信息、类加载状态、线程状态、内存使用状态等。
- 分析出 cpu 热点 (cpu profiler)
- 在代码运行时动态查看方法执行耗时、堆栈、入参和返回值

## 使用技术

- java agent、jmx、attach api、Byte Buddy、SpringBoot

## 原理

- 监控 jvm 信息

1. 通过 VirtualMachine.list() 获取本地 jvm 列表，主要是 pid 和 name
2. 选择需要监控的 jvm，通过 pid 和该 jvm 建立连接，此 springboot 项目作为一个 jmx 客户端
3. 通过建立的连接获取各种 MXBean 的信息。

- cpu profiler

通过定时任务 dump 线程堆栈信息，然后统计汇总。

- 动态查看方法执行耗时、堆栈、入参和返回值

1. 定义 agentmain 方法，通过使用 java attach api 可以动态加载 agent 到 jvm 中
2. 使用 Byte Buddy AgentBuilder 类开发 agent 
3. 开发执行耗时、堆栈、入参和返回值对应的 Advice

## Demo

该项目分为 2 个分支：agent、monitor

- agent 实现 java agent，查看运行时方法执行耗时、堆栈、入参和返回值。
- monitor 是 springBoot 程序，提供交互接口。

1. 将 agent 项目打包，将打成的 jar 包路径放在 monitor 项目中 application.properites 中。
2. 启动 monitor 项目，调用 JvmController 中 getJVMList 接口

![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668260309548-5633a617-4829-4e9a-801a-161992d2da57.png#averageHue=%23fefefd&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=340&id=ude7e1fd3&margin=%5Bobject%20Object%5D&name=image.png&originHeight=680&originWidth=937&originalType=binary&ratio=1&rotation=0&showTitle=false&size=81110&status=done&style=none&taskId=uc5f40250-8cdc-47cd-a286-2d916bdf1dc&title=&width=468.5)

3. 选择感兴趣的 jvm 程序，此处选择当前启动的 springBoot 程序，将该 pid 作为参数，调用 JvmController 中 attach 接口，建立与 jmx 的连接。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668260342260-fc9fd38b-3548-4e85-9cc8-87d6eb9c5c9b.png#averageHue=%23fefefd&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=270&id=u0c6b784d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=539&originWidth=659&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40571&status=done&style=none&taskId=u80535fe8-38dd-49e7-9746-144339ffcef&title=&width=329.5)

4. 调用 DashboardController 中的 getOperatingSystemInfo 接口获取操作信息信息。其他 jvm 信息也是调用 DashboardController 的接口。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668260485479-4c0c4f1d-a176-4ac3-8357-a104a545f66b.png#averageHue=%23fefefd&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=273&id=ueefabb12&margin=%5Bobject%20Object%5D&name=image.png&originHeight=545&originWidth=525&originalType=binary&ratio=1&rotation=0&showTitle=false&size=55541&status=done&style=none&taskId=ua8e2c79c-df1d-43dd-9651-85026b16067&title=&width=262.5)

5. 调用 AgentController 的 agent 接口，请求参数如下图

![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668261548458-5d0cd7da-6229-4e29-9b7a-d5d17623ef62.png#averageHue=%23fefefd&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=286&id=u9521aa0c&margin=%5Bobject%20Object%5D&name=image.png&originHeight=572&originWidth=660&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52419&status=done&style=none&taskId=u3d7ceea6-c6f9-4d76-9a9e-6f44eec6da4&title=&width=330)

请求体信息封装在 AgentAction 类中
![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668261638603-293a3dc7-76f8-4735-b592-ca4436a29578.png#averageHue=%23423854&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=307&id=uc0fdc435&margin=%5Bobject%20Object%5D&name=image.png&originHeight=614&originWidth=1040&originalType=binary&ratio=1&rotation=0&showTitle=false&size=224756&status=done&style=none&taskId=u15b3cb8d-b838-451c-a266-1b39e909bed&title=&width=520)

function 表示拦截的功能，方法耗时对应 time，方法堆栈对应 trace，方法入参和返回值对应 params；
className 表示需要拦截类的全类名；
methodName 表示需要拦截的方法名；

上面请求就是拦截 DashboardService 类的 getMemoryInfo 方法，获取方法入参和返回值。

当上面接口响应成功后，可以看到控制台的打印：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668261863380-76c907e8-9285-4244-8cf5-0e7e14104483.png#averageHue=%2330343c&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=ub334510b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=65&originWidth=1013&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16317&status=done&style=none&taskId=u0b983a71-3a93-407f-8cba-f1134ab7c43&title=)

6. 调用 DashboardController 类的 heapMemoryInfo 方法，查看拦截打印的内容：

![image.png](https://cdn.nlark.com/yuque/0/2022/png/435905/1668262019235-8f2be62f-67f8-48dc-a360-bb60accd59bc.png#averageHue=%23fefefd&clientId=u492ea547-7079-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=296&id=u323fc8c8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=591&originWidth=637&originalType=binary&ratio=1&rotation=0&showTitle=false&size=55351&status=done&style=none&taskId=uc0080557-6773-4a1d-bd98-59246498194&title=&width=318.5)

```
agent params method : public org.monitor.entity.info.MemoryInfo org.monitor.service.DashboardService.getMemoryInfo(int) enter params are : [1]
agent params method : public org.monitor.entity.info.MemoryInfo org.monitor.service.DashboardService.getMemoryInfo(int) return params are : MemoryInfo(init=268435456, used=183800936, committed=359137280, max=3817865216)
```

7. cpuProfiler 功能需要调用 ProfilerController 的 startCpuProfiler 接口开始进行统计，一段时间后最后调用 endCpuProfiler 接口结束统计。

## 参考

1. cpu profiler 参考：[https://github.com/uber-common/jvm-profiler](https://github.com/uber-common/jvm-profiler)

## todo

1. 支持连接远程 jvm 进程。




