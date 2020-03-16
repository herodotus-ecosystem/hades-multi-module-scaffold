$(function () {
    $.CRUD.init.activate(basePath, permission, validation);
});

var basePath = '/system/weapp';

var permission = {
    tableId: 'weappDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns : [
        {"mData": "wid"},
        {"mData": null},
        {"mData": "weappName"},
        {"mData": "appid"},
        {"mData": "available"},
        {"mData": "wid"}
    ],
    columnDefs : [
        {   "aTargets": [4],
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
        weappName: {
            required: true
        },
        appid: {
            required: true
        }
    },
    messages: {
        weappName: {
            required: '请小程序名称'
        },
        appid: {
            required: '请小程序AppID'
        }
    }
};



