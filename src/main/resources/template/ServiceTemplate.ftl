package ${packageName};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import java.util.List;
import ${entityBeanPackage};
import ${entityPackage};

/**
 * ${entityName} Service 接口
 * @author ${author}
 * @date ${date}
 */
public interface ${entityName}Service extends IService<${entityName}> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    ${entityName}Bean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<${entityName}Bean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<${entityName}Bean> findList(${entityName}Bean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<${entityName}Bean> findPage(${entityName}Bean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    ${entityName}Bean add(${entityName}Bean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(${entityName}Bean bean);

    /**
     * 根据唯一标识删除一条记录
     * 软删除
     * @param code 唯一标识
     * @return
     */
    int deleteByCode(String code);

    /**
     * 批量删除
     * 软删除
     * @param codeList 唯一标识集合
     */
    void deleteByCode(List<String> codeList);

}
