package com.wf.ew.common;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.layui.LayTable;
import com.wf.ew.common.layui.LayTableArg;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * BaseCrudController
 *
 * @name: BaseCrudController
 * @author: 12093
 * @date: 2019/1/18
 * @time: 13:46
 */
public abstract class BaseCrudController<T, S extends IService<T>> extends BaseController {

    /**
     * 获得服务层
     * @return
     */
    protected abstract S getService();

    /**
     * 获取日志对象
     * @return
     */
    protected abstract cn.hutool.log.Log getLog();

    /**
     * 获得模糊查询的字段名称
     * @return
     */
    protected abstract String getParamField();

    /**
     * 获得排序字段名称
     * @return
     */
    protected abstract String getOrderField();

    /**
     * 加载完成后通过反射动态设置Log注解的完整value
     */
    @PostConstruct
    public void initCrudController() {
        /*Class controllerClass = getClass();
        LogSubject logSubject = (LogSubject) controllerClass.getAnnotation(LogSubject.class);
        String subjectName = "";
        if (logSubject != null) {
            subjectName = logSubject.value();
        }

        try {
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                Log log = method.getAnnotation(Log.class);
                if (log == null) { continue; }
                String logValue = log.value();
                //获取这个代理实例所持有的 InvocationHandler
                InvocationHandler h = Proxy.getInvocationHandler(log);
                // 获取 AnnotationInvocationHandler 的 memberValues 字段
                Field hField = null;
                hField = h.getClass().getDeclaredField("memberValues");
                // 因为这个字段是 private final 修饰，所以要打开权限
                hField.setAccessible(true);
                Map memberValues = (Map) hField.get(h);
                // 修改 权限注解value 属性值
                memberValues.put("value", logValue + subjectName);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 数据查询前
     * @param arg
     * @param wrapper
     * @return
     */
    protected LayTable<T> queryBefore(LayTableArg arg, QueryWrapper<T> wrapper){
        return null;
    }

    /**
     * 数据查询前，重写查询条件
     * @param wrapper
     * @return
     */
    protected QueryWrapper<T> queryBefore(QueryWrapper<T> wrapper){return null;}

    @RequestMapping("query")
    @ResponseBody
    public LayTable<T> query(LayTableArg arg){
        QueryWrapper<T> wrapper = new QueryWrapper<T>();

        LayTable<T> beforeResult = queryBefore(arg, wrapper);
        if (beforeResult != null) {
            return beforeResult;
        }

        if (StrUtil.isNotBlank(arg.getParam()) && StrUtil.isNotBlank(getParamField())) {
            wrapper.like(getParamField(), arg.getParam());
        }

        String field = StrUtil.isNotBlank(arg.getField()) ? StrUtil.toSymbolCase(arg.getField(), '_') : getOrderField();
        wrapper.orderBy(true, arg.isAsc(), field);

        // 查询前重写条件
        QueryWrapper<T> newWrapper = queryBefore(wrapper);
        wrapper = newWrapper == null ? wrapper : newWrapper;

        Page<T> page = new Page<>(arg.getPage(), arg.getLimit());
        IPage<T> list = getService().page(page, wrapper);

        return new LayTable<T>(list.getRecords(), (int)list.getTotal());
    }

    /**
     * 数据插入前
     * @param m
     * @return
     */
    protected void insertBefore(T m) {}

    /**
     * 数据库插入后
     * @param m
     * @return
     */
    protected JsonResult insertAfter(T m) {
        return null;
    }

    @PostMapping("insert")
    @ResponseBody
    public JsonResult insert(T m) {
        insertBefore(m);

        try {
            if (getService().save(m)) {
                JsonResult afterResult = insertAfter(m);
                if (afterResult != null) {
                    return afterResult;
                }
                return JsonResult.ok(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e.getMessage());
            JsonResult.error("添加数据出现异常");
        }
        return JsonResult.error();
    }

    /**
     * 数据删除前
     *
     * @param ids
     * @param wrapper
     * @return
     */
    protected JsonResult deleteBefore(String ids, Wrapper<T> wrapper) {
        return null;
    }

    /**
     * 数据删除后
     *
     * @param ids
     * @return
     */
    protected JsonResult deleteAfter(String ids) {
        return null;
    }

    @PostMapping("delete")
    @ResponseBody
    public JsonResult delete(String ids) {
        List<String> idArr = StrUtil.split(ids, ',');
        if (idArr == null || idArr.size() == 0){
            return JsonResult.error("请选择要删除的数据");
        }

        QueryWrapper<T> wrapper = new QueryWrapper<>();
        JsonResult beforeResult = deleteBefore(ids, wrapper);
        if (beforeResult != null) {
            return beforeResult;
        }

        try {
            if (getService().removeByIds(idArr)){
                JsonResult afterResult = deleteAfter(ids);
                if (afterResult != null) {
                    return afterResult;
                }
                return JsonResult.ok();
            }
        }  catch (Exception e) {
            e.printStackTrace();
            getLog().error(e.getMessage());
            JsonResult.error("删除数据出现异常");
        }
        return JsonResult.error();
    }

    /**
     * 数据更新前
     * @param m
     * @return
     */
    protected JsonResult updateBefore(T m) {
        return null;
    }

    /**
     * 数据更新后
     * @param m
     * @return
     */
    protected JsonResult updateAfter(T m) {
        return null;
    }

    @PostMapping("update")
    @ResponseBody
    public JsonResult update(T m) {
        if (m == null) {
            return JsonResult.error("请指定要修改的数据");
        }

        JsonResult beforeResult = updateBefore(m);
        if (beforeResult != null) {
            return beforeResult;
        }

        try {
            if (getService().updateById(m)){
                JsonResult afterResult = updateAfter(m);
                if (afterResult != null) {
                    return afterResult;
                }
                return JsonResult.ok(m);
            }
        }  catch (Exception e) {
            e.printStackTrace();
            getLog().error(e.getMessage());
            JsonResult.error("更新数据出现异常");
        }
        return JsonResult.error();
    }

}