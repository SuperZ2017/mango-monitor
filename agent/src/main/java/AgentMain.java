import advice.ParamsAdvice;
import advice.TimeAdvice;
import advice.TraceAdvice;
import interceptor.ParamsInterceptor;
import interceptor.TimeInterceptor;
import interceptor.TraceInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    /**
     * jvm 运行前 agent
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation) {
        if (args == null || !args.contains("&")) {
            System.out.println("agent args is null");
            return;
        }

        String[] split = args.split("&");
        String function = split[0];
        String className = split[1];
        String methodName = split[2];

        System.out.println("agent is running and function is : " + function + " and class is " + className + " and methodName is : " +  methodName);

        Object interceptor = null;
        if (function.equals("trace")) {
            interceptor = new TraceInterceptor();
        } else if (function.equals("time")) {
            interceptor = new TimeInterceptor();
        } else if (function.equals("params")) {
            interceptor = new ParamsInterceptor();
        }

        assert interceptor != null;

        final Object obj = interceptor;
        new AgentBuilder.Default()
                .type(named(className))
                .transform((builder, type, classLoader, module, protectionDomain) ->
                    builder.method(named(methodName))
                            .intercept(MethodDelegation.to(obj)))
                .installOn(instrumentation);

        System.out.println("agent install success");
    }





    /**
     * jvm 运行时 agent
     * @param args
     * @param instrumentation
     */
    public static void agentmain(String args, Instrumentation instrumentation) {
        if (args == null || !args.contains("&")) {
            System.out.println("agent args is null");
            return;
        }

        String[] split = args.split("&");
        String function = split[0];
        String className = split[1];
        String methodName = split[2];

        System.out.println("agent is running and function is : " + function + " and class is " + className + " and methodName is : " +  methodName);

        Object advice = null;
        if (function.equals("trace")) {
            advice = new TraceAdvice();
        } else if (function.equals("time")) {
            advice = new TimeAdvice();
        } else if (function.equals("params")) {
            advice = new ParamsAdvice();
        }

        assert advice != null;

        final Object obj = advice;

        System.out.println("advice : " + obj.getClass());

        new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(named(className))
                .transform((builder, type, classLoader, module, protectionDomain) ->
                     builder.visit(Advice.to(obj.getClass()).on(named(methodName))))
                .installOn(instrumentation);

        System.out.println("agent install success");
    }



}
