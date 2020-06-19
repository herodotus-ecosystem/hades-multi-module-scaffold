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
 * File Name: DepartmentEmployeeDTO.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.dto;

import cn.com.felix.common.basic.dto.BaseDTO;
import cn.com.felix.hr.domain.Department;
import cn.com.felix.hr.domain.Employee;
import cn.com.felix.weapp.dto.component.WuxSelectOptionDTO;

import java.util.*;

import static java.util.Comparator.comparing;

/**
 * <p>Description: </p>
 *
 * @author hades
 * @date 2019/3/8
 */
public class DepartmentEmployeeDTO extends BaseDTO {

    private List<Employee> employees;
    private List<DepartmentDTO> departmentDTOS = new ArrayList<>();
    private List<WuxSelectOptionDTO> employeeOptionDTOS = new ArrayList<>();
    private Map<String, List<Integer>> indexMap = new HashMap<>();

    public DepartmentEmployeeDTO(List<Employee> employees) {
        this.employees = employees;
        init();
    }

    private void init() {
        for (Employee employee : employees) {
            Department department = employee.getDepartment();
            if (null != department) {

                DepartmentDTO departmentDTO = toDepartmentDTO(department);

                if (!departmentDTOS.contains(departmentDTO)) {
                    if (departmentDTO.getRanking() != 0) {
                        departmentDTOS.add(departmentDTO);
                    }
                }

                if (!indexMap.containsKey(department.getPkid())) {
                    List<Integer> indexs = new ArrayList<>();
                    employeeOptionDTOS.add(employeeOptionDTOS.size(), toWuxSelectOptionDTO(employee));
                    indexs.add(indexs.size(),employeeOptionDTOS.size() - 1);
                    indexMap.put(department.getPkid(), indexs);
                } else {
                    List<Integer> indexs = indexMap.get(department.getPkid());
                    employeeOptionDTOS.add(employeeOptionDTOS.size(), toWuxSelectOptionDTO(employee));
                    indexs.add(indexs.size(),employeeOptionDTOS.size() - 1);
                }
            }
        }
    }

    private WuxSelectOptionDTO toWuxSelectOptionDTO(Employee employee) {
        WuxSelectOptionDTO wuxSelectOptionDTO = new WuxSelectOptionDTO();
        wuxSelectOptionDTO.setTitle(employee.getFullName());
        wuxSelectOptionDTO.setValue(employee.getPkid());
        return wuxSelectOptionDTO;
    }

    private DepartmentDTO toDepartmentDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentid(department.getPkid());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        departmentDTO.setOrgnizationid(department.getOrgnizationId());
        departmentDTO.setRanking(department.getRanking());

        return departmentDTO;
    }

    public Map<String, Object> getDatas() {

        Map<String, Object> result = new HashMap<>();

        departmentDTOS.sort(Comparator.comparingInt(DepartmentDTO::getRanking));

        result.put("departments", departmentDTOS);
        result.put("employeeOptions", employeeOptionDTOS);
        result.put("indexMap", indexMap);
        return result;
    }

}
