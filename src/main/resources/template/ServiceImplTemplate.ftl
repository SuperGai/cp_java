package ${packageName};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ${entityBeanPackage};
import ${entityPackage};
import ${entityMapperPackage};
import ${entityServicePackage};

/**
 * ${entityName} Service 实现类
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Service
public class ${entityName}ServiceImpl extends ServiceImpl<${entityName}Mapper, ${entityName}> implements ${entityName}Service {

    private final ${entityName}Mapper ${lowEntityName}Mapper;

    public ${entityName}ServiceImpl(${entityName}Mapper ${lowEntityName}Mapper) {
            this.${lowEntityName}Mapper = ${lowEntityName}Mapper;
    }

    @Override
    public ${entityName}Bean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<${entityName}> sourceList = ${lowEntityName}Mapper.selectList(
                createWrapper(${entityName}Bean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        ${entityName}Bean restBean = ${entityName}Bean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<${entityName}Bean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<${entityName}> sourceList = ${lowEntityName}Mapper.selectList(
                createWrapper(${entityName}Bean.builder().${lowEntityName}CodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<${entityName}Bean> restBeanList = sourceList.stream().map(v -> {
            ${entityName}Bean item = new ${entityName}Bean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<${entityName}Bean> findList(${entityName}Bean bean){
        List<${entityName}> list = ${lowEntityName}Mapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<${entityName}Bean> beanList = list.stream().map(item -> {
            ${entityName}Bean srcBean = new ${entityName}Bean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<${entityName}Bean> findPage(${entityName}Bean bean, PageBean pageBean){
        Page<${entityName}> sourcePage = ${lowEntityName}Mapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<${entityName}Bean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            ${entityName}Bean itemBean = new ${entityName}Bean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public ${entityName}Bean add(${entityName}Bean bean){
        ${entityName} source = new ${entityName}();
        BeanUtils.copyProperties(bean, source);

        ${lowEntityName}Mapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(${entityName}Bean bean) {
        ${entityName} source = new ${entityName}();
        BeanUtils.copyProperties(bean,source);
        return ${lowEntityName}Mapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        ${entityName} updateSource = new ${entityName}();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return ${lowEntityName}Mapper.updateById(updateSource);
    }

    @Transactional
    @Override
    public void deleteByCode(List<String> codeList) {
        if(ObjectUtils.isEmpty(codeList)){
            return;
        }

        codeList.forEach(v -> {
            int restCount = deleteByCode(v);
            if(restCount <= 0){
                log.error("删除失败: code=" + v);
            }
        });
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(${entityName}Bean bean){
        LambdaQueryWrapper<${entityName}> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(${entityName}::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(${entityName}::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(${entityName}::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.get${entityName}CodeList())){
                wrapper.in(${entityName}::getCode, bean.get${entityName}CodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
