package com.hh.appraisal.springboot.core.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实体工具类
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
public class BeanUtil {

    /**
     * 字段驼峰转为下划线
     */
    public static String underFieldName(String fieldName) {
        StringBuilder result = new StringBuilder();
        if (ObjectUtils.isNotEmpty(fieldName)) {
            result.append(fieldName.substring(0, 1).toLowerCase());
            for (int i = 1; i < fieldName.length(); i++) {
                char ch = fieldName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 字段下划线转为驼峰
     */
    public static String camelCaseFieldName(String fieldName) {
        StringBuilder result = new StringBuilder();
        if (ObjectUtils.isNotEmpty(fieldName)) {
            boolean flag = false;
            for (int i = 0; i < fieldName.length(); i++) {
                char ch = fieldName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 获取null值字段名称列表
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 复制实体，忽略null字段
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
