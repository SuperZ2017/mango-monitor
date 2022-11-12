package interceptor;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import java.util.concurrent.Callable;

public class TraceInterceptor {


    @RuntimeType
    public Object intercept(@SuperCall Callable<?> callable) throws Exception {
        try {
            return callable.call();
        } finally {
            StringBuilder stringBuilder = new StringBuilder("Trace agent >>>>>>>>>>>>> \n");

            for (StackTraceElement element : new Exception().getStackTrace()) {
                stringBuilder.append(element.toString()).append("\n");
            }

            stringBuilder.append("end <<<<<<<<<<<<<<<<<<<<<< \n");
            System.out.println(stringBuilder.toString());
        }
    }

}
