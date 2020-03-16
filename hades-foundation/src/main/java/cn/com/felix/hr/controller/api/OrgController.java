package cn.com.felix.hr.controller.api;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.extend.json.ResultUtils;
import cn.com.felix.core.utils.JacksonUtils;
import cn.com.felix.hr.domain.Orgnization;
import cn.com.felix.hr.service.OrgnizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/hr/org")
public class OrgController extends BaseController<Orgnization> {

    @Autowired
    private OrgnizationService orgnizationService;


    @Override
    public String getPrefix() {
        return "/hr/org";
    }

    @Override
    public BaseService<Orgnization, String> getBaseService() {
        return this.orgnizationService;
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(Orgnization orgnization) {
        Orgnization result = orgnizationService.saveOrUpdate(orgnization);

        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }
    @PostMapping(value = "/dropdownlistorg")
    @ResponseBody
    public String getDropDownListOrg() {
        List<Orgnization> list = orgnizationService.findAll();
        Result result = ResultUtils.success();
        result.setData(list);
        return JacksonUtils.toJson(result);
    }


}
