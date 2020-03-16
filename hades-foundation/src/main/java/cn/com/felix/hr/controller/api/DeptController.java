package cn.com.felix.hr.controller.api;

import cn.com.felix.common.basic.controller.BaseController;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.extend.json.ResultUtils;
import cn.com.felix.core.utils.JacksonUtils;
import cn.com.felix.core.utils.datatables.DataTableResult;
import cn.com.felix.core.utils.datatables.DataTableUtils;
import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.dto.DeptVo;
import cn.com.felix.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/hr/dept")
public class DeptController extends BaseController<Department> {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public String getPrefix() {
        return "/hr/dept";
    }

    @Override
    public BaseService<Department, String> getBaseService() {
        return this.departmentService;
    }

    @Override
    public String getList(String aoData) {
        DataTableResult dataTableResult = DataTableUtils.parseDataTableParameter(aoData);
        Page<DeptVo> pages = departmentService.findByPageVo(dataTableResult.getPageNumber(), dataTableResult.getPageSize());

        Map<String, Object> map = super.initPageResult(dataTableResult, pages.getTotalElements(), pages.getTotalPages());
        map.put("data", pages.getContent());

        return JacksonUtils.toJson(map);
    }


    @PostMapping(value = "/dropdownlistdept")
    @ResponseBody
    public String getDeopDownListDept(String org) {
        List<DeptVo> list = departmentService.findAllByOrgID(org);
        Result result = ResultUtils.success();
        result.setData(list);
        return JacksonUtils.toJson(result);
    }

    @RequestMapping(value = "/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(Department department) {
        Department result = departmentService.saveOrUpdate(department);

        if (result != null) {
            return ajaxSuccess("保存成功！");
        } else {
            return ajaxError("保存失败！");
        }
    }


}
