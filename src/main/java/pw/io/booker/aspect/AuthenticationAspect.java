package pw.io.booker.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {

	@Around("execution(* pw.io.booker.controller..*(..)) && args(token,..)")
	public Object getAuthentication(ProceedingJoinPoint jointpoint, String token) {

		Object returnObject = null;

		if (token == null) {
			throw new RuntimeException("Access Denied");
		}

		try {
			returnObject = jointpoint.proceed();
		} catch (Throwable e) {
			throw new RuntimeException("Access Denied");
		}

		return returnObject;
	}
}
