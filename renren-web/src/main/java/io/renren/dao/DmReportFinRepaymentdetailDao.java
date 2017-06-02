package io.renren.dao;

import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.entity.MarketChannelEntity;

import java.util.List;

/**
 * 项目台帐明细
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 10:07:16
 */
public interface DmReportFinRepaymentdetailDao extends BaseDao<DmReportFinRepaymentdetailEntity> {
    List<DmReportFinRepaymentdetailEntity> queryExport();
}
