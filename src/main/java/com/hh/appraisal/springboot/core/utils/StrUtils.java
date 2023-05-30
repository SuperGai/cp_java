package com.hh.appraisal.springboot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 字符串相关函数
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
public class StrUtils {

    private static final Pattern PATTERN_NUMBER = Pattern.compile("[0-9]*");

    /**
     * 字符串首字母小写
     * @param str
     * @return
     */
    public static String strFirstLowerCase(String str){
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * 获取字段所属类的import语句
     * eg. field是BigDecimal类型，则返回 import java.math.BigDecimal;
     * @param field
     * @return
     */
    public static String getImportStr(Field field) {
        StringBuilder builder = new StringBuilder("import ");
        if(BigDecimal.class.getSimpleName().equals(field.getType().getSimpleName())){
            builder.append(BigDecimal.class.getTypeName());
        }else if(Date.class.getSimpleName().equals(field.getType().getSimpleName())){
            builder.append(Date.class.getTypeName());
        }else if(Integer.class.getSimpleName().equals(field.getType().getSimpleName())){
            builder.append(Integer.class.getTypeName());
        }else if(Long.class.getSimpleName().equals(field.getType().getSimpleName())){
            builder.append(Long.class.getTypeName());
        }else {
            return null;
        }
        return builder.append(";").toString();
    }

    /**
     * 获取指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * MD5加密
     * @param content
     * @return
     */
    public static String md5(String content) {
        // 用于加密的字符
        char[] md5String = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 使用平台默认的字符集将md5String编码为byte序列,并将结果存储到一个新的byte数组中
            byte[] byteInput = content.getBytes();

            // 信息摘要是安全的单向哈希函数,它接收任意大小的数据,并输出固定长度的哈希值
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用update方法处理数据,使用指定的byte数组更新摘要
            mdInst.update(byteInput);

            //摘要更新后通过调用digest() 执行哈希计算,获得密文
            byte[] md = mdInst.digest();

            //把密文转换成16进制的字符串形式
            int j = md.length;
            char[] str = new char[j*2];
            int k = 0;
            for (int i=0;i<j;i++) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 0xf];
                str[k++] = md5String[byte0 & 0xf];
            }
            // 返回加密后的字符串
            return new String(str);
        }catch (Exception e) {
            log.error("加密出现错误：" + e.toString());
            return null;
        }

    }

    /**
     * 分割字符串进SET
     */
    @SuppressWarnings("unchecked")
    public static Set<String> split(String str, String spilt) {

        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(str)){
            return set;
        }
        set.addAll(CollectionUtils.arrayToList(str.split(spilt)));
        return set;
    }

    /**
     * 检查字符串是否由数字组成
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        return PATTERN_NUMBER.matcher(str).matches();
    }

    /**
     * 校验字符串是否为汉字
     * 举例：123士大夫 为 false
     *       13-dq    为 false
     *       阿斯蒂芬  为 true
     * @param string
     * @return
     */
    public static boolean isChinese(String string){
        int n = 0;
        for(int i = 0; i < string.length(); i++) {
            n = (int)string.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(md5("123456"));
    }
}
