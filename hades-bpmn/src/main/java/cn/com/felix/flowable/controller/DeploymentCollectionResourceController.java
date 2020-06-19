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
 * Module Name: hades-bpmn
 * File Name: DeploymentCollectionResourceController.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.flowable.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.rest.util.RestUrlBuilder;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.rest.service.api.BpmnRestApiInterceptor;
import org.flowable.rest.service.api.RestUrls;
import org.flowable.rest.service.api.repository.DeploymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author : gengwei.zheng
 * @description : TODO
 * @date : 2019/11/14 11:30
 */
@RestController
@RequestMapping(value = "/flowable-task/process-api")
public class DeploymentCollectionResourceController {

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired(required=false)
    protected BpmnRestApiInterceptor restApiInterceptor;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", dataType = "file", paramType = "form", required = true)
    })
    @PostMapping(value = "/app-repository/deployments", produces = "application/json", consumes = "multipart/form-data")
    public DeploymentResponse uploadDeployment(@ApiParam(name = "deploymentKey") @RequestParam(value = "deploymentKey", required = false) String deploymentKey,
                                               @ApiParam(name = "deploymentName") @RequestParam(value = "deploymentName", required = false) String deploymentName,
                                               @ApiParam(name = "tenantId") @RequestParam(value = "tenantId", required = false) String tenantId,
                                               HttpServletRequest request, HttpServletResponse response) {

        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new FlowableIllegalArgumentException("Multipart request is required");
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.executeNewDeploymentForTenantId(tenantId);
        }

        String queryString = request.getQueryString();
        Map<String, String> decodedQueryStrings = splitQueryString(queryString);

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        if (multipartRequest.getFileMap().size() == 0) {
            throw new FlowableIllegalArgumentException("Multipart request with file content is required");
        }

        MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

        try {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName) || !(fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn") || fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip"))) {

                fileName = file.getName();
            }

            if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
                deploymentBuilder.addInputStream(fileName, file.getInputStream());
            } else if (fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip")) {
                deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
            } else {
                throw new FlowableIllegalArgumentException("File must be of type .bpmn20.xml, .bpmn, .bar or .zip");
            }

            if (!decodedQueryStrings.containsKey("deploymentName") || StringUtils.isEmpty(decodedQueryStrings.get("deploymentName"))) {
                String fileNameWithoutExtension = fileName.split("\\.")[0];

                if (StringUtils.isNotEmpty(fileNameWithoutExtension)) {
                    fileName = fileNameWithoutExtension;
                }

                deploymentBuilder.name(fileName);
            } else {
                deploymentBuilder.name(decodedQueryStrings.get("deploymentName"));
            }

            if (decodedQueryStrings.containsKey("deploymentKey") && StringUtils.isNotEmpty(decodedQueryStrings.get("deploymentKey"))) {
                deploymentBuilder.key(decodedQueryStrings.get("deploymentKey"));
            }

            if (tenantId != null) {
                deploymentBuilder.tenantId(tenantId);
            }

            if (restApiInterceptor != null) {
                restApiInterceptor.enhanceDeployment(deploymentBuilder);
            }

            Deployment deployment = deploymentBuilder.deploy();

            response.setStatus(HttpStatus.CREATED.value());

            return createDeploymentResponse(deployment);

        } catch (Exception e) {
            if (e instanceof FlowableException) {
                throw (FlowableException) e;
            }
            throw new FlowableException(e.getMessage(), e);
        }
    }

    public Map<String, String> splitQueryString(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return Collections.emptyMap();
        }
        Map<String, String> queryMap = new HashMap<>();
        for (String param : queryString.split("&")) {
            queryMap.put(StringUtils.substringBefore(param, "="), decode(StringUtils.substringAfter(param, "=")));
        }
        return queryMap;
    }

    protected String decode(String string) {
        if (string != null) {
            try {
                return URLDecoder.decode(string, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                throw new IllegalStateException("JVM does not support UTF-8 encoding.", uee);
            }
        }
        return null;
    }

    public List<DeploymentResponse> createDeploymentResponseList(List<Deployment> deployments) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<DeploymentResponse> responseList = new ArrayList<>();
        for (Deployment instance : deployments) {
            responseList.add(createDeploymentResponse(instance, urlBuilder));
        }
        return responseList;
    }

    protected RestUrlBuilder createUrlBuilder() {
        return RestUrlBuilder.fromCurrentRequest();
    }

    public DeploymentResponse createDeploymentResponse(Deployment deployment) {
        return createDeploymentResponse(deployment, createUrlBuilder());
    }

    public DeploymentResponse createDeploymentResponse(Deployment deployment, RestUrlBuilder urlBuilder) {
        return new DeploymentResponse(deployment, urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, deployment.getId()));
    }
}
