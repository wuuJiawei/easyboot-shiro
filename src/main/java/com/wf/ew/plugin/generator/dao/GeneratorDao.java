package com.wf.ew.plugin.generator.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * TableDao
 *
 * @name: TableDao
 * @author: 12093
 * @date: 2019/1/17
 * @time: 9:45
 */
public interface GeneratorDao {

    IPage<Map<String, Object>> queryList(Page page, @Param("tableName") String tableName);

    Map<String, String> queryTable(String tableName);

    IPage<Map<String, Object>> queryColumns(Page page, String tableName);

}