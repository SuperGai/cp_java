package com.hh.appraisal.springboot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hh.appraisal.springboot.core.baen.RestBean;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 控制层常用工具
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
public class ControllerUtil {

    /**
     * 获取请求对象
     * @return
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }

    /**
     * 获取响应对象
     * @return
     */
    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * get方式文件下载
     * @param location
     * @param request
     * @param response
     * @return
     */
    public static RestBean downloadGet(String location,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        try{
            // 对默认下载的文件名编码。不编码的结果就是，在客户端下载时文件名乱码
            File file = new File(location);
            if (file.exists()) {
                // 写明要下载的文件的大小
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment;filename="
                        // 设置在下载框默认显示的文件名
                        + location.split("/")[location.split("/").length - 1]);
                // 指明response的返回对象是文件流
                response.setContentType("application/octet-stream");
                // 读出文件到response, 需要把要把文件内容先读到缓冲区, 再把缓冲区的内容写到response的输出流供用户下载
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                byte[] b = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(b);
                outputStream = response.getOutputStream();
                outputStream.write(b);
            }else{
                return RestBean.error("文件不存在");
            }
        }catch(Exception e){
            log.error("文件下载出现异常：" + e);
            return RestBean.error("文件下载出现异常: " + e);
        }finally {
            try{
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                if(bufferedInputStream != null){
                    bufferedInputStream.close();
                }
                if(outputStream != null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception ee){
                log.error(ee.toString());
            }
        }
        return null;
    }

    /**
     * 获取客户端真实ip
     * nginx里需要配置x-forwarded-for等字段
     *
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.debug("获取本地ip失败：" + e);
                    return "";
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 输出图片到web输出流
     * @param bufferedImage
     * @param imageType 图片类型 jpg、png
     * @param response
     * @return
     */
    public static boolean outPutImage(BufferedImage bufferedImage, String imageType, HttpServletResponse response){
        ServletOutputStream out = null;
        try{
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, imageType, out);// write the data out
            out.flush();
        }catch (IOException ioe){
            return false;
        }finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException ioe2){
                log.error("资源关闭异常：" + ioe2);
            }
        }
        return true;
    }

    /**
     * 输出json字符串到 HttpServletResponse
     * @param response
     * @param str : 字符串
     */
    public static void writeJsonToResponse(HttpServletResponse response, String str){
        PrintWriter jsonOut = null;
        response.setContentType("application/json;charset=UTF-8");
        try {
            jsonOut = response.getWriter();
            jsonOut.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(jsonOut != null){
                jsonOut.close();
            }
        }
    }

    /**
     * 获取前端返回的json字符串
     * @param request
     * @return 获取到的json字符串
     */
    public String getJson(HttpServletRequest request){
        String json = null;
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding("UTF-8");
            reader = request.getReader();
            json = reader.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(reader != null){
                    reader.close();
                }
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }

        return json;
    }
}
