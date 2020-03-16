package cn.com.felix.common.basic.controller;

import cn.com.felix.common.config.shiro.CurrentUser;
import cn.com.felix.system.domain.SysPermission;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysUserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractController {

    @Autowired
    private SysUserService sysUserService;

    public enum Message {
        success, info, warning, error
    }

    protected String ajaxSuccess(String message) {
        return ajaxMessage(message, Message.success.name());
    }

    protected String ajaxInfo(String message) {
        return ajaxMessage(message, Message.info.name());
    }

    protected String ajaxWarning(String message) {
        return ajaxMessage(message, Message.warning.name());
    }

    protected String ajaxError(String message) {
        return ajaxMessage(message, Message.error.name());
    }

    protected String ajaxDefault(String message) {
        return ajaxMessage(message, "default");
    }

    private String ajaxMessage(String message, Message messageType) {
        return ajaxMessage(message, messageType.name());
    }

    private String ajaxMessage(String message, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("msg", message);
        return JSON.toJSONString(map);
    }

    @ModelAttribute(value = "menus")
    public List<SysPermission> getMenus() {
        return sysUserService.getMenu();
    }

    @ModelAttribute(value = "current_user_name")
    public String getCurrentUserName() {
        return CurrentUser.getUserName();
    }

    @ModelAttribute(value = "current_user_uid")
    public String getCurrentUserId() {
        return CurrentUser.getUserID();
    }
}
