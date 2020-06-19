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
 * File Name: common-datatable.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

var constants = {
    data_tables: {
        default_option: {
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 条/页",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                "sInfoEmpty": "第 0 至 0 页，共 0 页",
                "sInfoFiltered": "(共 _MAX_ 条记录)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "未获得数据",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            // 取消默认排序查询,否则复选框一列会出现小箭头
            sorting: [],
            sort: false,
            // 启用服务器端分页
            serverSide: true,
            // 禁用原生搜索
            searching: false,
            // 禁用分页的页数选择
            lengthChange: false,
            // 更改每页显示记录数
            displayLength: 10,
            lengthMenu: [
                [10, 15, 20, -1],
                [10, 15, 20, '全部']
            ],
            ajaxDataProp: "data",
            columnDefs: [
                {"aTargets": ["_all"], "sClass": "text-center", "bSortable": false},
                {   "aTargets": [0],
                    "mRender": function (data, type, row, meta) {
                        return '<input type="checkbox" id="' + data + '" name="checkbox-group" class="filled-in chk-col-blue-grey" data-id="' + data + '" /><label for="' + data + '"></label>';
                    }
                },
                {
                    "aTargets": [1],
                    "mRender": function (data, type, row, meta) {
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                }
            ]
        }
    }
};


var dataTable = {};
dataTable.table = {
    grid : ''
};

function retrieveData(sSource, aoData, fnCallback) {
    var tco=$.CRUD.settings.dataObject;
    if(tco.hasOwnProperty("postObj")){
        aoData.push(tco.postObj);
    }
    $.ajax({
        url: sSource,//这个就是请求地址对应sAjaxSource
        data: {"aoData": JSON.stringify(aoData)},//这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
        type: 'post',
        dataType: 'json',
        async: true,
        success: function (result) {
            fnCallback(result);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
        },
        error: function (msg) {
            notification('error', msg);
        }
    });
}


var dataTableInit = function (tco) {

    dataTable.table.grid = $('#' + tco.tableId).DataTable({
        "sAjaxSource": tco.ajaxSource,
        "sAjaxDataProp": constants.data_tables.default_option.ajaxDataProp,
        "bServerSide": constants.data_tables.default_option.serverSide,
        "bFilter": constants.data_tables.default_option.searching, // 开启或关闭搜索框
        "bLengthChange": constants.data_tables.default_option.lengthChange, // 选择分页的页数
        "bSort": constants.data_tables.default_option.sort,
        "aLengthMenu": constants.data_tables.default_option.lengthMenu,
        "aaSorting": constants.data_tables.default_option.sorting,
        "oLanguage": constants.data_tables.default_option.language,
         "sPaginationType": "full_numbers", // 分页按钮样式
        "iDisplayLength": constants.data_tables.default_option.displayLength,
        "fnServerData": retrieveData,
        "fnDrawCallback": function () {
            $(this).find('thead input[type=checkbox]').removeAttr('checked');
        },
        "aoColumns": tco.columns,
        "aoColumnDefs": constants.data_tables.default_option.columnDefs.concat(tco.columnDefs)
    });
};

function reload(){
    dataTable.table.grid.draw(false);
}



