package com.wf.ew.plugin.log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.ew.plugin.log.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author 武佳伟
 * @since 2019-01-28
 */
public interface LogDao extends BaseMapper<Log> {

    List<Log> listFull(Page<Log> page, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("account") String account);

}
