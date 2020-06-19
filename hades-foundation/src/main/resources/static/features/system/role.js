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
 * File Name: role.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

$(function () {
    $.CRUD.init.activate(basePath, role, validation);

    $("#permissionModal").on("show.bs.modal", function (e) {
        // 这里的btn就是触发元素，即你点击的删除按钮
        var button = $(e.relatedTarget);
        var roleId = button.data('id');

        showPermissionTree(basePath, roleId);
    });

    $('#permissionModal').on('click', 'button[data-name=update-permissions]', function () {
        var id = $('#modelRoleId').val();
        updatePermission(basePath, id);
    });
});


var basePath = '/system/role';

var role = {
    tableId: 'roleDataTable',
    ajaxSource: context_path + basePath + "/list",
    columns: [
        {"mData": "rid"},
        {"mData": null},
        {"mData": "roleName"},
        {"mData": "available"},
        {"mData": "description"},
        {"mData": "rid"}
    ],
    columnDefs: [
        {
            "aTargets": [3],
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
        {
            "aTargets": [5],
            "mRender": function (data, type, row, meta) {
                return '<button type="button" class="btn bg-orange btn-xs waves-effect" data-toggle="modal" title="设置权限" data-name="show-assign" data-target="#permissionModal" data-id="' + data + '"><i class="material-icons">security</i></button>' +
                    '<button type="button" class="btn btn-info btn-xs waves-effect" title="编辑" data-name="show-update" data-id="' + data + '"><i class="material-icons">edit</i></button>' +
                    '<button type="button" class="btn btn-danger btn-xs waves-effect" title="删除" data-name="show-delete" data-id="' + data + '"><i class="material-icons">delete</i></button>';
            }
        }
    ]
};

var validation = {
    rules: {
        roleName: {
            required: true
        }
    },
    messages: {
        roleName: {
            required: '请输入角色名称'
        }
    }
};

function showPermissionTree(action, id) {
    var zNodes = [];
    var setting = {
        check: {
            enable: true,
            chkboxType: {
                "Y": "ps",
                "N": "ps"
            }
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    $.ajax({
        url: context_path + action + '/getRolePermissions',
        data: {'roleId': id},
        type: 'post',
        dataType: 'json',
        cache: false,
        async: false,
        success: function (result) {
            zNodes = result;//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
        },
        error: function (msg) {
            notification('error', msg);
        }
    });

    $('#modelRoleId').val(id);
    $.fn.zTree.init($("#permissionTree"), setting, zNodes);
}

function updatePermission(action, roleId) {

    var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
    var nodes = treeObj.getCheckedNodes(true);
    var values = [];
    for (var i = 0; i < nodes.length; i++) {
        values.push(nodes[i].id);
    }

    $.post(context_path + action + '/updateRolePermissions', {
        'permissions': values,
        'roleId': roleId
    }, function (data) {
        notification(data.type, data.msg);
        $('#permissionModal').modal('hide');
    }, 'json');
}

