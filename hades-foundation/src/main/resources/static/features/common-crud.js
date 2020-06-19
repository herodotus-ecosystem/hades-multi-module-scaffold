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
 * File Name: common-crud.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

$.CRUD = {};
$.CRUD.settings = {};

$.CRUD.init = {
    activate: function (basePath, dataObject, validation) {
        $.CRUD.settings.basePath = basePath;
        $.CRUD.settings.dataObject = dataObject;
        $.CRUD.settings.validation = validation;

        $.CRUD.load.activate();
        $.CRUD.new.activate();
        $.CRUD.back.activate();
        $.CRUD.update.activate();
        $.CRUD.delete.activate();
    }
};

$.CRUD.load = {
    activate: function () {
        loadDataTable();
    }
};

$.CRUD.back = {
    activate: function () {
        $('#core-content').on('click', 'button[data-name=back]', function () {
            $.CRUD.load.activate();
        });
    }
};

$.CRUD.new = {
    activate: function () {
        //新增按钮事件注册
        $('#core-content').on('click', 'button[data-name=show-save]', function () {
            showSaveOrUpdate();
        });
    }
};

$.CRUD.update = {
    activate: function () {
        $('#core-content').on('click', 'button[data-name=show-update]', function () {
            var id = $(this).data('id');
            showSaveOrUpdate(id);
        });
    }
};

$.CRUD.delete = {
    activate: function () {
        $('#core-content').on('click', 'button[data-name=show-delete]', function () {
            var id = $(this).data('id');
            deleteSingle(id, $(this));
        });
    }
};

$.CRUD.validation = {
    activate: function () {
        $('#form').validate({
            onsubmit: true,// 是否在提交是验证
            onfocusout: false,// 是否在获取焦点时验证
            onkeyup: false,// 是否在敲击键盘时验证
            rules: $.CRUD.settings.validation.rules,
            messages: $.CRUD.settings.validation.messages,
            submitHandler: function (form) {
                if ($.CRUD.settings.validation.hasOwnProperty("customize") && $.CRUD.settings.validation.customize) {
                    saveOrUpdateCustomize();
                } else {
                    saveOrUpdate();
                }
            },
            invalidHandler: function (form, validator) {
                return false;
            },
            highlight: function (input) {
                $(input).parents('.form-line').addClass('error');
            },
            unhighlight: function (input) {
                $(input).parents('.form-line').removeClass('error');
            },
            errorPlacement: function (error, element) {
                $(element).parents('.form-group').append(error);
            }
        });
    }
};

//加载数据列表
function loadDataTable() {
    var pageIndex = 0;
    $('#core-content').load(context_path + $.CRUD.settings.basePath + '/page', {
        pageIndex: pageIndex
    }, function () {
        dataTableInit($.CRUD.settings.dataObject);
        checkAllInit();
    });
}

//显示新增界面
function showSaveOrUpdate(id) {
    var url = context_path + $.CRUD.settings.basePath + '/showSaveOrUpdate';
    if (id) {
        url += "/" + id;
    }
    $('#core-content').load(url, function () {
        $.AdminBSB.input.activate($(this));
        $.AdminBSB.select.activate();

        $.CRUD.validation.activate();
    });
}

//数据保存方法
function saveOrUpdate() {
    $.post(context_path + $.CRUD.settings.basePath + '/saveOrUpdate', $('#form').serialize(), function (data) {
        notification(data.type, data.msg);
        $.CRUD.load.activate();
    }, 'json');
}

function deleteSingle(id, $item) {
    swal({
        title: "你确定要删除这条信息么?",
        text: "此条信息删除之后将无法恢复，请慎重操作!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的, 删除!",
        cancelButtonText: "再想想, 取消!",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function (isConfirm) {
        if (isConfirm) {
            $.get(context_path + $.CRUD.settings.basePath + '/delete/' + id, function (data) {
                if (data.type == 'success') {
                    swal({
                        title: data.type,
                        text: "您所选择的信息已经成功删除，此窗口将会在 2 秒钟后自动关闭。",
                        type: "success",
                        timer: 2000,
                        showConfirmButton: false
                    });
                    reload();
                } else {
                    swal(data.type, "请再次尝试，如果还有此问题请联系管理员 :)", "error");
                }
            }, 'json');

        } else {
            swal({
                title: "您已取消!",
                text: "这条信息又安全了 :)，此窗口将会在 2 秒钟后自动关闭。",
                type: "success",
                timer: 2000,
                showConfirmButton: false
            });
        }
    });
}

function checkAllInit() {
    $('#checkAll').on('click', function () {
        if (this.checked) {
            $(this).attr('checked', 'checked')
            $("input[name='checkbox-group']").each(function () {
                this.checked = true;
            });
        } else {
            $(this).removeAttr('checked')
            $("input[name='checkbox-group']").each(function () {
                this.checked = false;
            });
        }
    });
}