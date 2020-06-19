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
 * File Name: OrgController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

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
