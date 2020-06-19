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
 * File Name: user.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

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