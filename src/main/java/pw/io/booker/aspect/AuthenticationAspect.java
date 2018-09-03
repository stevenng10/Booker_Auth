package pw.io.booker.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import pw.io.booker.exceptionhandling.AuthenticationException;
import pw.io.booker.repo.AuthenticationRepository;

@Aspect
@Component
public class AuthenticationAspect {

	AuthenticationRepository authenticationRepository;
	Logger logger = Logger.getLogger(AuthenticationAspect.class);

	public AuthenticationAspect(AuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}

	@Around("execution(* pw.io.booker.controller..*(..)) && args(token,..)")
	public Object getAuthentication(ProceedingJoinPoint jointpoint, String token) {

		Object returnObject = null;

		if (token == null) {
			logger.error("Token is null! Access is denied!");
			throw new AuthenticationException("Access Denied");
		}

		if (authenticationRepository.findByToken(token) == null) {
			logger.error("Cannot find token in repository! Access is denied!");
			throw new AuthenticationException("Access Denied");
		}

		try {
			returnObject = jointpoint.proceed();
		} catch (Throwable e) {
			logger.error("ThrowableException Occured: " + e.getMessage());
			throw new AuthenticationException("ThrowableException Occured: " + e.getMessage());
		}

		return returnObject;
	}

	@Before("execution(* pw.io.booker.service..*(..))")
	public void logMethodStart(JoinPoint jointpoint) {
		logger.info("**********************************");
		logger.info("Start of method: ");
		logger.info(jointpoint.getSignature().getName());
		logger.info("**********************************");
	}

	@After("execution(* pw.io.booker.service..*(..))")
	public void logMethodEnd(JoinPoint jointpoint) {
		logger.info("**********************************");
		logger.info("End of method: ");
		logger.info(jointpoint.getSignature().getName());
		logger.info("**********************************");
	}
}
