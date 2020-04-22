/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: management.js
 * Author: hades
 * Date: 18-12-30 上午9:20
 * LastModified: 18-12-30 上午9:20
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