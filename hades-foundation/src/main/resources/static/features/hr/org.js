$(function () {
    $.CRUD.init.activate(basePath, org, validation);
});

var basePath = '/hr/org';
var tools = new SecurityTools();

var org = {
    tableId: 'orgDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns: [
        {"mData": "pkid"},
        {"mData": null}, // 序号列
        {"mData": "parentId"},
        {"mData": "orgnaizationName"},
        {"mData": "ranking"},
        {"mData": "pkid"}
    ],
    columnDefs: [
        {
            "aTargets": [3],
            "mRender": function (data, type, row) {
                return data==undefined?"":tools.Decrypt(data);
            }
        },
        {
            "aTargets": [-1],
            "mRender": function (data, type, row) {
                return '<button type="button" class="btn btn-info btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="编辑" data-name="show-update" data-id="' + data + '"><i class="material-icons">edit</i></button>' +
                    '<button type="button" class="btn btn-danger btn-xs waves-effect" data-toggle="tooltip" data-placement="top" title="删除" data-name="show-delete" data-id="' + data + '"><i class="material-icons">delete</i></button>';
            }
        }
    ]
};

var validation ={
    customize: true,//新增，传了此标识 为true 测调用自定义保存方法saveOrUpdateCustomize//用于预处理表单操作，比如加密
    rules: {
        orgnaizationName: {
            required: true
        },
        ranking: {
            required: true,
            range: [0, 100]
        },
    },
    messages: {
        orgnaizationName: {
            required: '请输入组织名称'
        },
        ranking: {
            required: "排序必须填写",
            range: "排序必须介于0-100之间"
        },
    }
};
function saveOrUpdateCustomize() {
    if ($('#form').valid()) {
        //修改组织名称
        var encName = $("#orgnaizationName").val();
        encName = tools.Encrypt(encName)
        $("#orgnaizationName").val(encName);
        saveOrUpdate();
    }
}
