<!DOCTYPE html>
<html>

<head>
    <title>所有${table.comment}</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${cfg.basePath}/assets/libs/layui/css/layui.css"/>
    <link rel="stylesheet" href="${cfg.basePath}/assets/module/admin.css" media="all"/>
</head>

<body>

<!-- 加载动画，移除位置在common.js中 -->
<div class="page-loading">
    <div class="rubik-loader"></div>
</div>

<!-- 关闭Tab时顶部标题 -->
<div class="layui-body-header">
    <span class="layui-body-header-title">所有${table.comment}</span>
    <span class="layui-breadcrumb pull-right">
        <a href="${cfg.basePath}/index">首页</a>
        <a><cite>所有${table.comment}</cite></a>
    </span>
</div>

<!-- 正文开始 -->
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">

            <div class="layui-form toolbar">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label w-auto">名称：</label>
                        <div class="layui-input-inline mr0">
                            <input id="edtSearch" class="layui-input" type="text" placeholder="输入关键字"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button class="layui-btn icon-btn" id="btnSearch">
                            <i class="layui-icon">&#xe615;</i>搜索
                        </button>
                        <!-- 下拉按钮 -->
                        <ul class="layui-nav nav-btn" style="margin-left: 10px;">
                            <li class="layui-nav-item" lay-unselect>
                                <a>&nbsp;更多&nbsp;</a>
                                <dl class="layui-nav-child layui-anim-fadein align-right">
                                    <dd lay-unselect data-type="delete"><a><i class="layui-icon layui-icon-delete"></i> 删除 </a></dd>
                                </dl>
                            </li>
                        </ul>

                    </div>
                </div>
            </div>

            <table class="layui-table" id="${table.entityPath}-table" lay-filter="${table.entityPath}-table"></table>

        </div>
    </div>

</div>

<div style="display: none;" id="detail-modal">
    <form class="layui-form model-form" id="detail-form">
        <#list table.fields as field>
            <#-- 主键 -->
            <#if field.keyFlag>
                <input type="hidden" name="${field.propertyName}">
            <#-- 普通字段 -->
            <#elseif field.fill??>
                <div class="layui-form-item layui-row">
                    <div class=" layui-col-md6">
                        <label class="layui-form-label">${field.comment}:</label>
                        <div class="layui-input-block">
                            <input type="text" placeholder="${field.comment}" class="layui-input" name="${field.propertyName}"
                                   lay-verType="tips" lay-verify="required" required readonly/>
                        </div>
                    </div>
                </div>
            </#if>
        </#list>
        <div class="layui-form-item btns">
            <div class="layui-input-block text-right">
                <button class="layui-btn layui-btn-primary" type="reset">重置</button>
                <button class="layui-btn" lay-filter="update-submit" lay-submit>保存</button>
            </div>
        </div>
    </form>
</div>

<!-- 表格操作列 -->
<script type="text/html" id="${table.entityPath}-table-bar">
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">详细信息</a>
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>

<!-- js部分 -->
<script type="text/javascript" src="${cfg.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript" src="${cfg.basePath}/assets/js/common.js"></script>

<script>
    layui.use(['layer', 'form', 'table', 'admin', 'laydate', 'clipboard', 'notice'], function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var form = layui.form;
        var table = layui.table;
        var admin = layui.admin;
        var laydate = layui.laydate;
        var notice = layui.notice;
        var clipboard = layui.clipboard;
        var DATA_UPDATE = {}; // 当前更新的列对象

        // 渲染表格
        var ins1 = table.render({
            elem: '#${table.entityPath}-table',
            url: '${cfg.basePath}/${table.entityPath}/query',
            page: true,
            cellMinWidth: 100,
            cols: [[
                {type: 'checkbox'},
                <#list table.fields as field>
                {field: '${field.propertyName}', align: 'center', sort: true, title: '${field.comment}'},
                </#list>
                {align: 'center', align: 'center', toolbar: '#user-table-bar', title: '操作', minWidth: 180}
            ]]
        });

        //监听工具条
        table.on('tool(${table.entityPath}-table)', function (obj) {
            DATA_UPDATE = obj;
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值

            if (layEvent === 'edit') {
                action.dialog(data);
            } else if (layEvent === 'del') {
                admin.req('${cfg.basePath}/${table.entityPath}/delete', {
                    id: data.id
                }, function (res) {
                    admin.message(res, function () {
                        obj.del();
                    });
                }, 'post')
            } else if (layEvent === 'detail') {
                action.dialog(data, true);
            }
        });

        //监听排序事件
        table.on('sort(${table.entityPath}-table)', function(obj){
            table.reload('${table.entityPath}-table', {
                initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
                ,where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
                    field: obj.field //排序字段
                    ,type: obj.type //排序方式
                }
            });
        });

        //提交
        form.on('submit(update-submit)', function(data){
            admin.req('${cfg.basePath}/${table.entityPath}/update', data.field, function (res) {
                admin.message(res, function () {
                    DATA_UPDATE.update(data.field);
                    admin.closeAllDialog();
                });
            }, 'post');
            return false;
        });

        // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            table.reload('${table.entityPath}-table', {
                where: {
                    param: $('#edtSearch').val()
                }
            });
        });

        $('.nav-btn dd').click(function () {
            var type = $(this).attr('data-type');
            action[type]();
        });

        var action = {
            checkIds: function () {
                var checkRows = table.checkStatus('${table.entityPath}-table');
                if (checkRows.data.length != 0) {
                    var ids = [];
                    layui.each(checkRows.data, function (i, item) {
                        ids.push(item.id);
                    });
                    return ids;
                }
                return null;
            },
            delete: function () {
                var ids = action.checkIds();
                if (ids == null) {
                    return false;
                }
                admin.req('${cfg.basePath}/${table.entityPath}/delete', {
                    id: ids.join(',')
                }, function (res) {
                    admin.message(res, function () {
                        table.reload('${table.entityPath}-table');
                    });
                }, 'post');
            },
            dialog: function (data, disabled) {
                for (var key in data) {
                    var o = $('#detail-form *[name='+ key +']');
                    o.val(data[key]);
                    if (disabled && o.attr('readonly') == undefined) {
                        o.attr('disabled', 'disabled');
                    }
                }

                if (disabled) {
                    $('#detail-form .btns').hide();
                } else {
                    $('#detail-form *[name]').removeAttr('disabled');
                    $('#detail-form .btns').show();
                }

                admin.open({
                    type: 1,
                    title: data == undefined ? '新增' ? data.name,
                    shade: 0,
                    area: ['890px', '430px'],
                    content: $('#detail-modal')
                });
            }
        };

    });
</script>
</body>

</html>