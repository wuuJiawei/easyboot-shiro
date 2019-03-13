package com.wf.ew.plugin.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.layui.LayTable;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 武佳伟
 * @since 2019-01-28
 */
public interface ILogService extends IService<Log> {

    LayTable<Log> listFull(Integer page, Integer limit, String startDate, String endDate, String account);

}
