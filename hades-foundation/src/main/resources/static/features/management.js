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
 * File Name: management.js
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/4/22 上午10:30
 */

$(function () {

    $('#changePasswordForm').validate({
        onsubmit:true,// 是否在提交是验证
        onfocusout:false,// 是否在获取焦点时验证
        onkeyup :false,// 是否在敲击键盘时验证
        rules: {
            newsecuwd: {
                required:true
            },
            newsecuwdAgain: {
                equalTo:"#newsecuwd"
            }
        },
        messages: {
            newsecuwd: {
                required: '请输入新密码'
            },
            newsecuwdAgain: {
                equalTo: '两次密码输入不一致'
            }
        },
        submitHandler: function (form) {
            saveNewPassword();
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
});

function saveNewPassword() {

    console.log("--------------Save Password ==========")

    $.post(context_path + '/system/user/changePassword', {
        personkit: $.base64.encode($('#personkit').val()),
        newsecuwd: $.base64.encode($('#newsecuwd').val())
    }, function (data) {
        notification(data.type, data.msg);
        $('#changePasswordModal').modal('hide');
    }, 'json');
}