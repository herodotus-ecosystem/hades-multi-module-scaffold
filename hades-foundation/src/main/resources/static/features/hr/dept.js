/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-foundation
 * File Name: dept.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

$(function () {
    $.CRUD.init.activate(basePath, org, validation);
});

var basePath = '/hr/dept';
var orgPath = '/hr/org';
var tools = new SecurityTools();

var org = {
    tableId: 'deptDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns: [
        {"mData": "pkid"},
        {"mData": null}, // 序号列
        {"mData": "orgnizationName"},
        {"mData": "parentDepartmentName"},
        {"mData": "departmentName"},
        {"mData": "ranking"},
        {"mData": "pkid"}
    ],
    columnDefs: [
        {
            "aTargets": [2],
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        },
        {
            "aTargets": [3],
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        },
        {
            "aTargets": [4],
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
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

var validation = {
    customize: true,//新增，传了此标识 为true 测调用自定义保存方法saveOrUpdateCustomize//用于预处理表单操作，比如加密
    rules: {
        orgnizationId: {
            required: true
        },
        departmentName: {
            required: true
        },
        ranking: {
            required: true,
            range: [0, 10000]
        },
    },
    messages: {
        orgnizationId: {
            required: '请选择组织'
        },
        departmentName: {
            required: '请填写部门名称'
        },
        ranking: {
            required: "排序必须填写",
            range: "排序必须介于0-10000之间"
        },
    }
};

function saveOrUpdateCustomize() {
    if ($('#form').valid()) {
        //判断最新的组织 与  上及部门的组织是否为同一个
        //修改组织名称
        var encName = $("#departmentName").val();
        encName = tools.Encrypt(encName)
        $("#departmentName").val(encName);
        saveOrUpdate();
    }
}

function orgChanged() {

    if ($("#orgnizationId").val() == undefined || $("#orgnizationId").val() == "") {
        $("#parentId").empty();
        $("#parentId").append("<option value=''>--请选择部门--</option>");
        // 缺一不可
        $('#parentId').selectpicker('refresh');
        $('#parentId').selectpicker('render');
        return;
    }
    $.post(basePath + '/dropdownlistdept', {
        "org": "" + $("#orgnizationId").val() + ""
    }, function (data) {

        $("#parentId").empty();
        $("#parentId").append("<option value=''>--请选择部门--</option>");


        for (var prop in data.data) {
            $("#parentId").append("<option value='" + data.data[prop].pkid + "' data-subtext='" + tools.Decrypt(data.data[prop].orgnizationName) + "'>" + tools.Decrypt(data.data[prop].departmentName) + "</option>");
        }

        //设置下拉框value属性为4的选项 选中
        $("#parentId").val(dbParentId);
        // 缺一不可
        $('#parentId').selectpicker('refresh');
        $('#parentId').selectpicker('render');
    }, 'json');

}