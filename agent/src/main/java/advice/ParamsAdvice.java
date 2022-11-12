package advice;

import net.bytebuddy.asm.Advice;
import java.util.Arrays;

public class ParamsAdvice {


    @Advice.OnMethodEnter()
    static void enter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
        String printMessage = String.format("agent params method : %s enter params are : %s", method, Arrays.toString(args));
        System.out.println(printMessage);
    }


    @Advice.OnMethodExit()
    static void exit(@Advice.Origin String method, @Advice.Return Object arg) {
        String printMessage = String.format("agent params method : %s return params are : %s", method, arg);
        System.out.println(printMessage);
    }


}
