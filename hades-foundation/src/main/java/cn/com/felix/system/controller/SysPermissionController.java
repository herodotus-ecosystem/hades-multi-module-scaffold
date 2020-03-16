package cn.com.felix.system.controller;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.dto.SysPermissionDTO;
import cn.com.felix.system.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/system/permission")
public class SysPermissionController extends BaseController<SysPermission> {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public String getPrefix() {
        return "/system/permission";
    }

    @Override
    public BaseService<SysPermission, String> getBaseService() {
        return this.sysPermissionService;
    }

    @ModelAttribute(value = "permissions")
    public List<SysPermission> getAllPermissions() {
        return sysPermissionService.findAll();
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(SysPermissionDTO sysPermissionDTO) {

        SysPermission result = sysPermissionService.saveOrUpdate(sysPermissionDTO);
        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
}
