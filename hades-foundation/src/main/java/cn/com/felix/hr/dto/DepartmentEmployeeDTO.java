/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: DepartmentEmployeeDTO
 * Author: hades
 * Date: 19-3-8 上午10:45
 * LastModified: 19-3-8 上午10:45
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
