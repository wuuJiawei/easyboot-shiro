package com.wf.ew.common;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import cn.hutool.setting.dialect.Props;

import java.io.IOException;
import java.util.Properties;

/**
 * Constants
 *
 * @name: Constants
 * @author: 武佳伟
 * @date: 2019/1/3
 * @time: 9:31
 */
public class Constants {

    public static Props config = null;

    static {
        config = new Props("config.properties");
    }
}