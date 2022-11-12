package interceptor;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class ParamsInterceptor {


    @RuntimeType
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments, @SuperCall Callable<?> callable) throws Exception {
        Object returnValue = callable.call();

        System.out.println("params agent method : " + method + " , input params : " + Arrays.toString(allArguments) +
                " , output params : " + returnValue);

        return returnValue;
    }

}
