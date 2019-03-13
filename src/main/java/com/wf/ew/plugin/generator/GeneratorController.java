package com.wf.ew.plugin.generator;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.wf.ew.common.BaseController;
import com.wf.ew.common.Constants;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.layui.LayTable;
import com.wf.ew.common.layui.LayTableArg;
import com.wf.ew.system.model.Authorities;
import com.wf.ew.system.service.AuthoritiesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GeneratorController
 *
 * @name: GeneratorController
 * @author: 12093
 * @date: 2019/1/17
 * @time: 9:36
 */
@Controller
@RequestMapping("generator")
public class GeneratorController extends BaseController {

    @Autowired
    private IGeneratorService generatorService;

    @RequiresPermissions("generator:view")
    @GetMapping("/")
    public String page(){
        return "plugin/generator/page.html";
    }

    @PostMapping("query/table")
    @ResponseBody
    public LayTable<Map<String, Object>> queryTables(LayTableArg arg) {
        Page<Map<String, Object>> page = new Page<>(arg.getPage(), arg.getLimit());
        IPage<Map<String, Object>> list = generatorService.queryList(page, arg.getParam());
        return new LayTable<>(list.getRecords(), (int)list.getTotal());
    }

    @PostMapping("query/column")
    @ResponseBody
    public LayTable<Map<String, Object>> queryColumn(LayTableArg arg) {
        Page<Map<String, Object>> page = new Page<>(arg.getPage(), arg.getLimit());
        IPage<Map<String, Object>> list = generatorService.queryColumns(page, arg.getParam());
        return new LayTable<>(list.getRecords(), (int)list.getTotal());
    }

    @RequiresPermissions("generator:create")
    @PostMapping("create")
    @ResponseBody
    public JsonResult create(String tableName, String tablePrefix, Boolean override, String projectName, String files){
        String[] tableNameArr = StrUtil.split(tableName, ",");
        String[] tablePrefixArr = StrUtil.split(tablePrefix, ",");
        override = override == null ? false : override;

        generatorService.generate(override, tableNameArr, tablePrefixArr, projectName, StrUtil.split(files, ","));

        return JsonResult.ok();
    }

    @GetMapping("createTable")
    public String createTablePage(){
        return "plugin/generator/createTable";
    }

    @PostMapping("createTable")
    @ResponseBody
    public JsonResult createTable(String tableName, String tableComment, String fields){
        return JsonResult.ok();
    }







}