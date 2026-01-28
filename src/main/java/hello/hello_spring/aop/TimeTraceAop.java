package hello.hello_spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// @Aspect : AOP의 기본 모듈, Advice + Pointcut
// Advice : 타겟 (부가 기능을 부여할 대상)에 제공할 부가 기능 담음
// Pointcut : 타겟에 적용할 메서드를 선별하는 정규 표현식, execution으로 시작
// @Component : 스프링 빈으로 등록
@Aspect
@Component
public class TimeTraceAop {
    // @Around : AOP가 적용될 디렉터리 설정
    @Around("execution(* hello.hello_spring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());

        // AOP가 적용된 메서드 호출 전 동작
        try {
            return joinPoint.proceed();
        }catch (Throwable e) {
            throw new RuntimeException(e);
        }finally {
            // AOP가 적용된 메서드 호출 후 동작
            long finish = System.currentTimeMillis();
            long duration = finish - start;
            System.out.println("END :" +joinPoint.toString() + duration + "ms");
        }

    }
}
