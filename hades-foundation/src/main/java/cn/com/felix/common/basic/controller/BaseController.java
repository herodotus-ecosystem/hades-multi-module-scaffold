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
 * File Name: BaseController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.common.basic.controller;

import cn.com.felix.common.basic.domain.BaseDomain;
import cn.com.felix.common.basic.service.BaseService;
import cn.com.felix.core.utils.JacksonUtils;
import cn.com.felix.core.utils.datatables.DataTableResult;
import cn.com.felix.core.utils.datatables.DataTableUtils;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController<D extends BaseDomain> extends AbstractController {

    @RequestMapping(value = "/index")
    public String index() {
        return getPrefix() + "/index";
    }

    @RequestMapping(value = "/page")
    public String page() {
        return getPrefix() + "/page";
    }

    @RequestMapping(value = "/showSaveOrUpdate")
    public String showSaveOrUpdate(Model model) {
        model.addAttribute("isEdit", false);
        return getPrefix() + "/save-or-update";
    }

    @RequestMapping(value = "/showSaveOrUpdate/{id}")
    public String showSaveOrUpdate(@PathVariable String id, Model model) {
        D bean = getBaseService().findById(id);
        model.addAttribute("bean", bean);
        model.addAttribute("isEdit", true);
        return getPrefix() + "/save-or-update";
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable String id) {
        getBaseService().deleteById(id);
        return ajaxSuccess("删除成功");
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public String getList(@RequestParam(defaultValue = "") String aoData) {
        DataTableResult dataTableResult = DataTableUtils.parseDataTableParameter(aoData);
        Page<D> pages = getBaseService().findByPage(dataTableResult.getPageNumber(), dataTableResult.getPageSize());

        Map<String, Object> map = initPageResult(dataTableResult, pages.getTotalElements(),pages.getTotalPages());
        map.put("data", pages.getContent());

        return JacksonUtils.toJson(map);
    }

    public Map<String, Object> initPageResult(DataTableResult dataTableResult,long totalCount,int totalPages) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", dataTableResult.getSEcho());
        map.put("iTotalRecords", totalCount);//总数
        map.put("iTotalDisplayRecords", totalCount);  ////显示的行数,这个要和上面写的一样
        map.put("iTotalPages",totalPages);//总数 pages.getTotalPages());//多少页
        return map;
    }

    public abstract String getPrefix();

    public abstract BaseService<D, String> getBaseService();
}
