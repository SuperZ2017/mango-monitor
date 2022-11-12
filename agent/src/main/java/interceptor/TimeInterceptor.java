package interceptor;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TimeInterceptor {


    @RuntimeType
    public Object intercept(@Origin Class clazz, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        System.out.println("start....");
        long start = System.currentTimeMillis();

        try {
            return callable.call();
        } finally {
            long end = System.currentTimeMillis();

//            System.out.println("Time agent method : " + method.getName() + " , take : " + (end - start) / 1000 + " s");
            System.out.println("Time agent class : " + clazz.getName() + ", method name :" +  method.getName());
            System.out.println("Time agent class : " + clazz + ", method : " + method);
        }

    }

}
