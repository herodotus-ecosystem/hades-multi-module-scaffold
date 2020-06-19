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
 * File Name: emp.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

$(function () {
    $.CRUD.init.activate(basePath, org, validation);
});

var basePath = '/hr/emp';
var orgPath = '/hr/org';
var deptPath = '/hr/dept';
var tools = new SecurityTools();

function loadQueryPara() {
    return  {"name": "queryJson", "value": JSON.stringify({
                "orgnizationId": $('#queryOrgId').val(),
                "department":{"pkid":$('#queryDeptId').val()
                }
             })};
}
var postObjValue =loadQueryPara();
var org = {
    tableId: 'empDataTable',
    ajaxSource: context_path + basePath + "/list",
    postObj: loadQueryPara(),
    columns: [
        {"mData": "pkid"},
        {"mData": null}, // 序号列
        {"mData": "fullName"},
        {"mData": "department.departmentName"},
        {"mData": "telephone"},
        {"mData": "phoneNumber"},
        {"mData": "gender"},
        {"mData": "nation"},
        {"mData": "birthday"},//"birthday"},
        {"mData": "entryTime"},//"entry_time"
        {"mData": "partyTime"},//"party_time"},
        {"mData": "educationDegree"},
        {"mData": "technicalTitle"},
        {"mData": "hierarchy"},
        {"mData": "identity"},
        {"mData": "pkid"}
    ],
    columnDefs: [
        {
            "aTargets": [2],//姓名
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        },
        {
            "aTargets": [3],//部门
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        },
        {
            "aTargets": [4],//座机
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        }, {
            "aTargets": [5],//手机
            "mRender": function (data, type, row) {
                return data == undefined ? "" : tools.Decrypt(data);
            }
        },
        {
            "aTargets": [6],//性别
            "mRender": function (data, type, row) {
                var result = "";
                switch (data) {
                    case "MAN":
                        result = "男";
                        break;
                    case "WOMAN":
                        result = "女";
                        break;
                }
                return result;
            }
        },
        {
            "aTargets": [7],//民族
            "bVisible": false
        },
        {
            "sWidth": "90px",
            "aTargets": [8],//生日
            "bVisible": false,
            "mRender": function (data, type, row) {
                return data == undefined ? "" : data.substr(0, 7);
            }
        },
        {
            "aTargets": [9],//工作
            "bVisible": false,
            "mRender": function (data, type, row) {
                return data == undefined ? "" : data.substr(0, 7);
            }
        },
        {
            "aTargets": [10],//入党
            "bVisible": false,
            "mRender": function (data, type, row) {
                return data == undefined ? "" : data.substr(0, 7);
            }
        },
        {
            "aTargets": [11],//文化程度
            "bVisible": false
        },
        {
            "aTargets": [12],//技术职称
            "bVisible": false
        },
        {
            "aTargets": [13],//层级
            "bVisible": false
        },
        {
            "aTargets": [14],//身份
            "mRender": function (data, type, row) {
                var result = "";
                switch (data) {
                    case "LEADERSHIP":
                        result = "领导";
                        break;
                    case "STAFF":
                        result = "员工";
                        break;
                }
                return result;
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
        deptid: {
            required: true
        },
        fullName: {
            required: true
        }
        // ,
        // phoneNumber: {
        //     required: true
        // },
    },
    messages: {
        orgnizationId: {
            required: '请选择组织'
        },
        deptid: {
            required: '请选择部门'
        },
        fullName: {
            required: '请填写姓名'
        }
        // ,
        // phoneNumber: {
        //     required: '请填写座机'
        // },
    }
};

function saveOrUpdateCustomize() {
    if ($('#form').valid()) {
        //判断最新的组织 与  上及部门的组织是否为同一个
        //修改组织名称
        var encName = $("#fullName").val();
        encName = tools.Encrypt(encName)
        $("#fullName").val(encName);

        var encPhone = $("#phoneNumber").val();
        if(encPhone!=undefined && encPhone!=''){
            encPhone = tools.Encrypt(encPhone)
            $("#phoneNumber").val(encPhone);
        }
        var encTel = $("#telephone").val();
        if(encTel!=undefined && encTel!='') {
            encTel = tools.Encrypt(encTel)
            $("#telephone").val(encTel);
        }
        saveOrUpdate();
    }
}


function orgChanged(isReDraw,ctlMainName,ctlSubName) {
    if(isReDraw){
        reDraw();
    }

    var mainctrl =$("#"+ctlMainName);
    var subctrl =$("#"+ctlSubName);//document.getElementById(ctlSubName);

    if (mainctrl.val() == undefined || mainctrl.val() == "") {
        subctrl.empty();
        subctrl.append("<option value=''>--请选择部门--</option>");
        // 缺一不可
        subctrl.selectpicker('refresh');
        subctrl.selectpicker('render');
        return;
    }
    $.post(deptPath + '/dropdownlistdept', {
        "org": "" + mainctrl.val() + ""
    }, function (data) {

        subctrl.empty();
        subctrl.append("<option value=''>--请选择部门--</option>");

        for (var prop in data.data) {
            subctrl.append("<option value='" + data.data[prop].pkid + "' data-subtext='" + tools.Decrypt(data.data[prop].orgnizationName) + "'>" + tools.Decrypt(data.data[prop].departmentName) + "</option>");
        }

        if(!isReDraw){
            debugger;
            document.getElementById("department.pkid").value = editDeptPkid;
            subctrl.val(editDeptPkid);
        }
        // 缺一不可
        subctrl.selectpicker('refresh');
        subctrl.selectpicker('render');
    }, 'json');

}

function reDraw() {
    $.CRUD.settings.dataObject.postObj = loadQueryPara();
    $('#empDataTable').DataTable().draw();
}

