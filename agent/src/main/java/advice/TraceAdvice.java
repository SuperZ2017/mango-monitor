package advice;

import net.bytebuddy.asm.Advice;

public class TraceAdvice {

    @Advice.OnMethodExit()
    static void exit() {
        StringBuilder stringBuilder = new StringBuilder("Trace agent >>>>>>>>>>>>> \n");

        for (StackTraceElement element : new Exception().getStackTrace()) {
            stringBuilder.append(element.toString()).append("\n");
        }

        stringBuilder.append("trace end <<<<<<<<<<<<<<<<<<<<<< \n");
        System.out.println(stringBuilder.toString());
    }

}
