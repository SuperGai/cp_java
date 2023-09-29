package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import java.util.List;

import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.bean.ReportConfigCatBean;
import com.hh.appraisal.springboot.bean.ReportPanelBean;
import com.hh.appraisal.springboot.entity.Report;

/**
 * Report Service 接口
 * @author gaigai
 * @date 2023/06/02
 */
public interface ReportService extends IService<Report> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    ReportBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<ReportBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<ReportBean> findList(ReportBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<ReportBean> findPage(ReportBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    ReportBean add(ReportBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(ReportBean bean);
    
    
    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateReportConfigByCode(List<ReportConfigCatBean> ReportConfigCatList);

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
