// 以下代码是配置layui扩展模块的目录，每个页面都需要引入
layui.config({
    base: getProjectUrl() + 'assets/module/'
}).extend({
    formSelects: 'formSelects/formSelects-v4',
    treetable: 'treetable-lay/treetable',
    dropdown: 'dropdown/dropdown',
    notice: 'notice/notice',
    step: 'step-lay/step',
    dtree: 'dtree/dtree',
    citypicker: 'city-picker/city-picker',
    tableSelect: 'tableSelect/tableSelect',
    iconPicker: 'iconPicker/iconPicker'
}).use(['layer', 'admin', 'element'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var admin = layui.admin;
    var element = layui.element;

    // 单标签模式需要根据子页面的地址联动侧边栏的选中，用于适配浏览器前进后退按钮
    if (window != top && top.layui && top.layui.index && !top.layui.index.pageTabs) {
        top.layui.admin.activeNav(location.href.substring(getProjectUrl().length));
    }

    // 移除loading动画
    setTimeout(function () {
        admin.removeLoading();
    }, window == top ? 300 : 150);

});

// 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}

var common = {
    /**
     * 根据返回值进行提示
     * @param response
     */
    message: function (response, successFn) {
        var o = {title: '消息通知', message: response.msg, position: 'topRight'};
        if (response.code == 10000) {
            if (successFn) {
                successFn();
            }
            top.notice.success(o);
        } else {
            top.notice.warning(o);
        }
    },
};

// 字符串处理
var strUtil = {
    /**
     * 是否为空
     */
    isEmpty: function (str) {
        if (str === null || str === undefined || str === "") {
            return true;
        }
        return false;
    }
};