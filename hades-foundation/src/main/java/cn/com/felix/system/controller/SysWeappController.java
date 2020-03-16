package cn.com.felix.system.controller;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.system.domain.SysWeapp;
import cn.com.felix.system.service.SysWeappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/system/weapp")
public class SysWeappController extends BaseController<SysWeapp> {

    private final SysWeappService sysWeappService;

    @Autowired
    public SysWeappController(SysWeappService sysWeappService) {
        this.sysWeappService = sysWeappService;
    }

    @Override
    public String getPrefix() {
        return "/system/weapp";
    }

    @Override
    public BaseService<SysWeapp, String> getBaseService() {
        return sysWeappService;
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(SysWeapp sysWeapp) {
        SysWeapp result = sysWeappService.saveOrUpdate(sysWeapp);

        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
}
