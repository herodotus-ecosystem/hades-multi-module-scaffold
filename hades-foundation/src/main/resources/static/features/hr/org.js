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
 * File Name: org.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

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
