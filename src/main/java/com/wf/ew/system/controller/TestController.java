package com.wf.ew.system.controller;

import com.wf.ew.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2019/3/14.
 */
@Controller
public class TestController extends BaseController{

    @RequestMapping("socket")
    public String socket(){
        return "test/socketTest.html";
    }

}
