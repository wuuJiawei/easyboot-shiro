package com.wf.ew;

import com.wf.ew.plugin.generator.IGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorServiceTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    IGeneratorService generatorService;

    @Test
    public void testGenerate(){
        String[] tableNames = {"a_test"};
        String[] tablePrefixs = {"sys_"};
        // "entity", "controller", "service", "serviceImpl", "dao", "xml",
        String[] files = {"list"};
        generatorService.generate(true, tableNames, tablePrefixs, "wf.ew.test", files);
    }
}
