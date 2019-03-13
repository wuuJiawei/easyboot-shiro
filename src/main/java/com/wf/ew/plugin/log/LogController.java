package com.wf.ew.plugin.log;

import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.ew.common.BaseCrudController;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.layui.LayTable;
import com.wf.ew.common.layui.LayTableArg;
import com.wf.ew.plugin.log.annotation.LogSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author 武佳伟
 * @since 2019-01-28
 */
@Controller
@RequestMapping("/log")
@LogSubject(value = "系统日志", closed = true)
public class LogController extends BaseCrudController<Log, ILogService> {

    @Autowired
    private ILogService service;

    @Override
    protected ILogService getService() {
        return service;
    }

    @Override
    protected cn.hutool.log.Log getLog() {
        return LogFactory.get();
    }

    @GetMapping("/")
    public String list(){
      return "plugin/log/log";
    }

    @Override
    protected LayTable<Log> queryBefore(LayTableArg arg, QueryWrapper<Log> wrapper){
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String account = request.getParameter("account");
        return service.listFull(arg.getPage(), arg.getLimit(), startDate, endDate, account);
    }

    @Override
    protected String getParamField() {
        return "message";
    }

    @Override
    protected String getOrderField() {
        return "log_id";
    }


}
