package com.hh.appraisal.springboot.core.utils;

/**
 * 异常处理相关工具
 * @author gaigai
 * @date 2019/05/15
 */
public class ExceptionUtil {

    /**
     * 将异常详细详细提取到字符串返回，建议输出到日志
     * 返回字符串内含：异常信息、异常栈
     * 返回的异常栈包含：[异常文件名：行数] 类名.方法名
     * @param e
     * @return null 提取失败
     */
    public static String getDetailExceptionMsg(Throwable e){
        if(e == null || e.getStackTrace() == null ||
                e.getStackTrace().length <= 0){
            return null;
        }

        StackTraceElement[] stackTraces = e.getStackTrace();
        String msg = e.getMessage() + "\n";
        for(StackTraceElement s : stackTraces){
            msg += "\t[" + s.getFileName() + ":" + s.getLineNumber() + "] " +
                    s.getClassName() + "." + s.getMethodName() + "()" +
                    "\n";
        }
        return msg;
    }

}
