package com.wf.ew.plugin.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.layui.LayTable;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.plugin.log.dao.LogDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author 武佳伟
 * @since 2019-01-28
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements ILogService {

    @Override
    public LayTable<Log> listFull(Integer page, Integer limit, String startDate, String endDate, String account) {
        if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
        if (StringUtil.isBlank(startDate)) {
            startDate = null;
        } else {
            startDate += " 00:00:00";
        }
        if (StringUtil.isBlank(endDate)) {
            endDate = null;
        } else {
            endDate += " 23:59:59";
        }
        if (StringUtil.isBlank(account)) {
            account = null;
        }
        Page<Log> iPage = new Page<>(page, limit);
        List<Log> records = baseMapper.listFull(iPage, startDate, endDate, account);
        return new LayTable<>(records, (int)iPage.getTotal());
    }

}
