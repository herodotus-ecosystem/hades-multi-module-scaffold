$(function () {
    $.CRUD.init.activate(basePath, user, validation);
});

var basePath = '/system/user';
var tools = new SecurityTools();
var user = {
    tableId: 'userDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns : [
        {"mData": "uid"},
        {"mData": null}, // 序号列
        {"mData": "userName"},
        {"mData": "state"},
        {"mData": "roles"},
        {"mData": "openId"},
        {"mData": "uid"}
    ],
    columnDefs : [
        {   "aTargets": [3],
            "mRender": function (data, type, row) {
                var html;
                if (data) {
                    html = '<span class="label label-success">可用</span>'
                } else {
                    html = '<span class="label label-danger">不可用</span>'
                }
                return html;
            }
        },
        {   "aTargets": [4],
            "mRender": function (data, type, row) {
                var html = '';
                $.each(data, function (k, v) {
                    html += v.roleName + ',';
                });
                return html.substring(0, html.length - 1);
            }
        },
        {   "aTargets": [-1],
            "mRender": function (data, type, row) {
                return '<button type="button" class="btn btn-info btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="编辑" data-name="show-update" data-id="' + data + '"><i class="material-icons">edit</i></button>' +
                    '<button type="button" class="btn btn-danger btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="删除" data-name="show-delete" data-id="' + data + '"><i class="material-icons">delete</i></button>';
            }
        }
    ]
};

var validation = {
    rules: {
        userName: {
            required: true
        }
    },
    messages: {
        userName: {
            required: '请输入用户名称'
        }
    }
};