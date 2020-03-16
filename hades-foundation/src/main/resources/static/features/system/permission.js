$(function () {
    $.CRUD.init.activate(basePath, permission, validation);
});

var basePath = '/system/permission';

var permission = {
    tableId: 'perDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns : [
        {"mData": "pid"},
        {"mData": null},
        {"mData": "permissionName"},
        {"mData": "permission"},
        {"mData": "resourceType"},
        {"mData": "available"},
        {"mData": "url"},
        {"mData": "menuClass"},
        {"mData": "pid"}
    ],
    columnDefs : [
        {   "aTargets": [5],
            "mRender": function (data, type, row, meta) {
                var html;
                if (data) {
                    html = '<span class="label label-success">可用</span>'
                } else {
                    html = '<span class="label label-danger">不可用</span>'
                }
                return html;
            }
        },
        {   "aTargets": [-1],
            "mRender": function (data, type, row, meta) {
                return '<button type="button" class="btn btn-info btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="编辑" data-name="show-update" data-id="' + data + '"><i class="material-icons">edit</i></button>' +
                    '<button type="button" class="btn btn-danger btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="删除" data-name="show-delete" data-id="' + data + '"><i class="material-icons">delete</i></button>';
            }
        }
    ]
};

var validation = {
    rules: {
        permissionName: {
            required: true
        }
    },
    messages: {
        permissionName: {
            required: '请输入权限名称'
        }
    }
};



