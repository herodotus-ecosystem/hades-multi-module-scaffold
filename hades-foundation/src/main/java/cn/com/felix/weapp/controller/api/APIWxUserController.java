/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: WxUserController
 * Author: gengwei.zheng
 * Date: 18-12-29 上午10:17
 * LastModified: 18-12-29 上午10:11
 */

package cn.com.felix.weapp.controller.api;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.com.felix.core.enums.ResultEnum;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.utils.encrypt.SecurityTools;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.hr.service.EmployeeService;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysUserService;
import cn.com.felix.weapp.domain.WxRequest;
import cn.com.felix.weapp.service.WxSecurityService;
import cn.com.felix.weapp.service.WxUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2018/12/29
 */
@RestController
@RequestMapping(value = "/api/wx/user")
public class APIWxUserController {

    private final EmployeeService employeeService;
    private final WxSecurityService wxSecurityService;
    private final WxUserService wxUserService;
    private final SysUserService sysUserService;

    @Autowired
    public APIWxUserController(EmployeeService employeeService, WxSecurityService wxSecurityService, SysUserService sysUserService, WxUserService wxUserService) {
        this.employeeService = employeeService;
        this.wxSecurityService = wxSecurityService;
        this.sysUserService = sysUserService;
        this.wxUserService = wxUserService;
    }

    @PostMapping("/validate")
    public Result validation(String phoneNumber, String openId, String appid) {
        Result result = new Result();

        Employee employee = employeeService.findByPhoneNumber(phoneNumber);
        if (null != employee) {
            if (!wxSecurityService.isWxUserBinding(openId)) {
                sysUserService.createUser(employee.getFullName(), employee.getPkid(), employee.getOrgnizationId(), openId, appid);
            }
            result.put("isWxUserBinding", true);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("您的信息不存在，请重新输入或者与管理员联系！");

        }

        return result;
    }


    @PostMapping("/getPhoneNumber")
    public Result getPhoneNumber(@Valid WxRequest wxRequest) {
        Result result = new Result();

        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxUserService.storePhoneNumberInfo(wxRequest);

        if (null != wxMaPhoneNumberInfo) {
            result = validation(wxMaPhoneNumberInfo.getPhoneNumber(), wxRequest.getOpenId(), wxRequest.getAppId());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("获取手机号码，请选择手工输入或者与管理员联系！");
        }

        return result;
    }

    @PostMapping("/changeReceiver")
    public Result changeReceiver(String employeeid, String appid ) {

        Result result = new Result();

        List<SysUser> sysUsers = sysUserService.changeUserRole(employeeid, "receiver", appid);

        if (CollectionUtils.isNotEmpty(sysUsers)) {
            result.put("changeReceiver", true);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统出错！");
        }

        return result;
    }

    @PostMapping("/setUserRole")
    public Result setUserRole(String employeeid, String roleName, String appid ) {

        Result result = new Result();

        SysUser sysUsers = sysUserService.setUserRole(employeeid, roleName, appid);

        if (null != sysUsers) {
            result.put("isSetUserRoleSuccess", true);
            result.setStatus(ResultEnum.OK.getStatus());
        } else {
            result.put("isSetUserRoleSuccess", false);
            result.setStatus(ResultEnum.UNAUTHORIZED.getStatus());
            result.setMessage("系统出错！");
        }

        return result;
    }
}
