package com.wf.ew.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.model.LoginRecord;
import com.wf.ew.system.dao.LoginRecordMapper;
import com.wf.ew.system.service.LoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {

    @Override
    public PageResult<LoginRecord> listFull(Integer page, Integer limit, String startDate, String endDate, String account) {
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
        Page<LoginRecord> iPage = new Page<>(page, limit);
        List<LoginRecord> records = baseMapper.listFull(iPage, startDate, endDate, account);
        return new PageResult<>(records, iPage.getTotal());
    }
}
