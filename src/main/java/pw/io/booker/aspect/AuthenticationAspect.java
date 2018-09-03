package pw.io.booker.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import pw.io.booker.exceptionhandling.AuthenticationException;
import pw.io.booker.repo.AuthenticationRepository;

@Aspect
@Component
public class AuthenticationAspect {

	AuthenticationRepository authenticationRepository;

	public AuthenticationAspect(AuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}

	@Around("execution(* pw.io.booker.controller..*(..)) && args(token,..)")
	public Object getAuthentication(ProceedingJoinPoint jointpoint, String token) {

		Object returnObject = null;

		if (token == null) {
			throw new AuthenticationException("Access Denied");
		}

		if (authenticationRepository.findByToken(token) == null) {
			throw new AuthenticationException("Access Denied");
		}

		try {
			returnObject = jointpoint.proceed();
		} catch (Throwable e) {
			throw new AuthenticationException("Access Denied: " + e);
		}

		return returnObject;
	}
}
