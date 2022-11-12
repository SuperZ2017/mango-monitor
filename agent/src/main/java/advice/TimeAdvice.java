package advice;


import net.bytebuddy.asm.Advice;

public class TimeAdvice {

    @Advice.OnMethodEnter()
    static long enter() {
        return System.currentTimeMillis();
    }


    @Advice.OnMethodExit()
    static void exit(@Advice.Origin String method, @Advice.Enter long start) {
        String printMessage = String.format("agent time method : %s take %d s", method, (System.currentTimeMillis() - start) / 1000);
        System.out.println(printMessage);
    }

}
