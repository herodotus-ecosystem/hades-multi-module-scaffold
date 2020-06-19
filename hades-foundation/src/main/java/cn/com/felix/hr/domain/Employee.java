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
 * File Name: Employee.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.hr.domain;

import cn.com.felix.common.basic.domain.BaseAppDomain;
import cn.com.felix.core.enums.Gender;
import cn.com.felix.core.enums.Hierarchy;
import cn.com.felix.core.enums.Identity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_employee", indexes = {
        @Index(name = "sys_employee_pkid_idx", columnList = "pkid"),
        @Index(name = "sys_employee_phonenumber_idx", columnList = "phone_number")})
@NamedEntityGraph(name = "Employee.Graph", attributeNodes = {@NamedAttributeNode("department")})
public class Employee extends BaseAppDomain {

    @Column(name = "full_name", length = 512)
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    /**
     * 民族
     */
    @Column(name = "nation", length = 64)
    private String nation;
    /**
     * 出生年月
     */
    @Column(name = "birthday")
    private Date birthday;
    /**
     * 工作时间
     */
    @Column(name = "entry_time")
    private Date entryTime;
    /**
     * 入党时间
     */
    @Column(name = "party_time")
    private Date partyTime;
    /**
     * 文化程度
     */
    @Column(name = "education_degree", length = 64)
    private String educationDegree;
    /**
     * 技术职称
     */
    @Column(name = "technical_title")
    private String technicalTitle;

    @Column(name = "orgnizationid", length = 64)
    private String orgnizationId;

    /**
     * 层级。指代高层、中层等
     */
    @Column(name = "hierarchy")
    @Enumerated(EnumType.ORDINAL)
    private Hierarchy hierarchy = Hierarchy.GRASS_ROOTS;

    /**
     * 身份。指代领导、员工之类的内容
     */
    @Column(name = "identity")
    @Enumerated(EnumType.ORDINAL)
    private Identity identity = Identity.STAFF;

    /**
     * Hibernate技巧：cascade选项通常位于mappedBy一侧
     * @ref： Hibernate实战（第二版）Page：153
     */
    @ManyToOne
    @JoinColumn(name = "departmentid", referencedColumnName = "pkid")
    private Department department;

    @Column(name = "phone_number", length = 512)
    private String phoneNumber;


    @Column(name = "telephone", length = 512)
    private String telephone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getPartyTime() {
        return partyTime;
    }

    public void setPartyTime(Date partyTime) {
        this.partyTime = partyTime;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getTechnicalTitle() {
        return technicalTitle;
    }

    public void setTechnicalTitle(String technicalTitle) {
        this.technicalTitle = technicalTitle;
    }

    public String getOrgnizationId() {
        return orgnizationId;
    }

    public void setOrgnizationId(String orgnizationId) {
        this.orgnizationId = orgnizationId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName)
                .append("gender", gender)
                .append("nation", nation)
                .append("birthday", birthday)
                .append("entryTime", entryTime)
                .append("partyTime", partyTime)
                .append("educationDegree", educationDegree)
                .append("technicalTitle", technicalTitle)
                .append("orgnizationId", orgnizationId)
                .append("hierarchy", hierarchy)
                .append("identity", identity)
                .append("department", department)
                .append("phoneNumber", phoneNumber)
                .append("telephone", telephone)
                .toString();
    }
}
