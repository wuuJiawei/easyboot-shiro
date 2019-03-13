package com.wf.ew.plugin.generator;

import cn.hutool.core.util.ArrayUtil;
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
import com.wf.ew.common.Constants;
import com.wf.ew.plugin.generator.dao.GeneratorDao;
import com.wf.ew.system.model.Authorities;
import com.wf.ew.system.model.RoleAuthorities;
import com.wf.ew.system.service.AuthoritiesService;
import com.wf.ew.system.service.RoleAuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GeneratorServiceImpl
 *
 * @name: GeneratorServiceImpl
 * @author: 12093
 * @date: 2019/1/17
 * @time: 14:36
 */
@Service
public class GeneratorServiceImpl implements IGeneratorService{

    @Autowired
    private GeneratorDao generatorDao;
    @Autowired
    private AuthoritiesService menuService;
    @Autowired
    private RoleAuthoritiesService roleAuthoritiesService;
    @Autowired
    private Environment environment;

    @Override
    public IPage<Map<String, Object>> queryList(Page<Map<String, Object>> page, String tableName) {
        return generatorDao.queryList(page, tableName);
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    @Override
    public IPage<Map<String, Object>> queryColumns(Page<Map<String, Object>> page, String tableName) {
        return generatorDao.queryColumns(page, tableName);
    }

    @Override
    public void generate(boolean override, String[] tableName, String[] tablePrefix, String projectName, String[] files) {
        AutoGenerator mpg = new AutoGenerator();
        // 设置模板引擎
        mpg.setTemplateEngine(new BeetlTemplateEngine());

        // 全局设置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("wujiawei0926@yeah.net")
                .setBaseResultMap(false)
                .setBaseColumnList(false)
                .setEnableCache(false)
                .setBaseColumnList(true)
                // 自定义文件名,%s 会自动填充表实体属性
                .setMapperName("%sDao")
                .setXmlName("%sMapper")
                .setServiceImplName("%sServiceImpl")
                // 直接输出到项目中
                .setOutputDir("src\\main\\java")
                // 是否打开输出目录
                .setOpen(false)
                .setFileOverride(override);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL)
                .setDriverName(environment.getProperty("spring.datasource.driver-class-name"))
                .setUsername(environment.getProperty("spring.datasource.username"))
                .setPassword(environment.getProperty("spring.datasource.password"))
                .setUrl(environment.getProperty("spring.datasource.url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setSuperControllerClass("com.wf.ew.common.BaseCrudController")
                // 需要生成的表名
                .setInclude(tableName)
                // 类名使用驼峰
                .setNaming(NamingStrategy.underline_to_camel)
                // 表前缀
                .setTablePrefix(tablePrefix);
        mpg.setStrategy(sc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com." + projectName)
                .setController("controller")
                .setEntity("model")
                .setMapper("dao")
                //.setXml("mapper")
                .setService("service")
                .setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        // 注入自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                this.setMap(map);
            }
        };

        // 自定义文件生成
        cfg.setFileOutConfigList(configFileOut(files));
        mpg.setCfg(cfg);

        // 自定义模板配置
        mpg.setTemplate(configTemplateFiles(files));

        // 执行生成
        mpg.execute();
    }

    /**
     *
     * @param table
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean createMenu(TableInfo table){
        // 删除menu
        QueryWrapper<Authorities> wrapper = new QueryWrapper<>();
        wrapper.eq("authority_name", table.getComment())
                .eq("menu_url", "/" + table.getEntityPath() + "/")
                .eq("is_menu", 0);
        Authorities oldMenu = menuService.getOne(wrapper);
        menuService.removeById(oldMenu.getAuthorityId());

        // 插入menu
        Authorities menu = new Authorities();
        menu.setMenuIcon("layui-icon layui-icon-app");
        menu.setAuthorityName(table.getComment());
        menu.setOrderNumber(1);
        menu.setParentId(-1);
        menu.setIsMenu(0);
        menu.setMenuUrl("/" + table.getEntityPath() + "/");
        menu.setCreateTime(LocalDateTime.now());
        menuService.save(menu);

        // 删除user_menu
        QueryWrapper<RoleAuthorities> roleAuthoritiesQueryWrapper = new QueryWrapper<>();
        roleAuthoritiesQueryWrapper.eq("role_id", 1)
                .eq("authority_id", oldMenu.getAuthorityId());
        roleAuthoritiesService.remove(roleAuthoritiesQueryWrapper);

        // 添加user_menu
        RoleAuthorities roleAuthorities = new RoleAuthorities();
        roleAuthorities.setAuthorityId(menu.getAuthorityId());
        roleAuthorities.setCreateTime(LocalDateTime.now());
        roleAuthorities.setRoleId(1);
        roleAuthoritiesService.save(roleAuthorities);

        return true;
    }

    /**
     * 配置是否生成模板文件
     * @param files
     * @return
     */
    private TemplateConfig configTemplateFiles(String[] files){
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        tc.setEntity(null);
        tc.setController(null);
        tc.setMapper(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        for (String file : files) {
            if ("controller".equals(file)) {
                tc.setController("static/generator/controller.java");
            }
            /*if ("xml".equals(file)) {
                tc.setXml("static/generator/mapper.xml");
            }*/
            if ("service".equals(file)) {
                tc.setService("static/generator/service.java");
                tc.setServiceImpl("static/generator/serviceImpl.java");
            }
            if ("dao".equals(file)) {
                tc.setMapper("static/generator/mapper.java");
            }
            if ("entity".equals(file)) {
                tc.setEntity("static/generator/entity.java");
            }
        }
        return tc;
    }

    /**
     * 自定义文件生成
     * @param files 需要生成的文件列表
     * @return
     */
    private List<FileOutConfig> configFileOut(String[] files){
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        if (ArrayUtil.contains(files, "list")){
            focList.add(new FileOutConfig("/static/generator/list.html.btl") {
                @Override
                public String outputFile(TableInfo table) {
                    createMenu(table);
                    return "src/main/resources/templates/" + table.getEntityPath() + "/list.html";
                }
            });
        }

        if (ArrayUtil.contains(files, "xml")) {
            focList.add(new FileOutConfig("/static/generator/mapper.xml.btl") {
                @Override
                public String outputFile(TableInfo table) {
                    createMenu(table);
                    return "src/main/resources/mapper/" + table.getXmlName() + "Mapper.xml";
                }
            });
        }

        return focList;
    }


}