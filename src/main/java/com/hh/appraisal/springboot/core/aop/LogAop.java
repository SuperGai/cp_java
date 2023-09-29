package com.hh.appraisal.springboot.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志切面 记录接口执行时间
 * 
 * @ClassName: LogAop
 * @Description:
 * @Author xuwenjuan
 * @DateTime 2019年11月11日 上午9:36:29
 */
@Aspect
@Component
@Slf4j
public class LogAop {

	/**
	 * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
	 * 
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "execution(public * com.hh.appraisal.springboot.service.*.*(..)))")
	public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		long beginTime = System.currentTimeMillis();

		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		String loggerType = className + "." + methodName;

		Object result = null;
		try {
			// System.out.println("【前置通知】：the method 【" + loggerType + "】 begins with " +
			// Arrays.asList(joinPoint.getArgs()));
			// 执行目标方法
			result = joinPoint.proceed();
			// System.out.println("【返回通知】：the method 【" + loggerType + "】 ends with " +
			// result);
		} catch (Throwable e) {
			log.error("【异常通知】：the method 【" + loggerType + "】 occurs exception ", e);
			throw e;
		}
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		log.info("【后置通知】：------the method 【" + loggerType + "】------- time:" + time + "毫秒");
		return result;
	}

}
