package com.wf.ew.plugin.generator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * Created by 12093 on 2019/1/17.
 */
public interface IGeneratorService {

    IPage<Map<String, Object>> queryList(Page<Map<String, Object>> page, String tableName);

    Map<String, String> queryTable(String tableName);

    IPage<Map<String, Object>> queryColumns(Page<Map<String, Object>> page, String tableName);

    /**
     * 生成代码
     * @param override 是否覆盖原文件
     * @param tableName 表名
     * @param tablePrefix 表前缀
     * @param projectName 项目路径
     * @param files 生成的文件数组
     */
    void generate(boolean override, String[] tableName, String[] tablePrefix, String projectName, String[] files);

}
