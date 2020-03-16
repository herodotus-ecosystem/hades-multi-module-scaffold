package cn.com.felix.system.controller;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.common.config.shiro.PasswordHelper;
import cn.com.felix.core.utils.encrypt.Base64Utils;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysRoleService;
import cn.com.felix.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/system/user")
public class SysUserController extends BaseController<SysUser> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public String getPrefix() {
        return "/system/user";
    }

    @Override
    public BaseService<SysUser, String> getBaseService() {
        return this.sysUserService;
    }

    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public String changePassword(String personkit, String newsecuwd) {

        String sysUserId = Base64Utils.decodeData(personkit);
        String newPassword = Base64Utils.decodeData(newsecuwd);

        SysUser sysUser = sysUserService.findById(sysUserId);
        if (null != sysUser) {
            sysUser = passwordHelper.encryptPassword(sysUser, newPassword);
            sysUserService.saveOrUpdate(sysUser);
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
}
