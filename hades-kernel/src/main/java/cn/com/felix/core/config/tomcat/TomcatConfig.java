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
 * Module Name: hades-kernel
 * File Name: TomcatConfig.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.config.tomcat;

import cn.com.felix.core.properties.TomcatProperties;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class TomcatConfig {

    private final TomcatProperties tomcatProperties;

    @Autowired
    public TomcatConfig(TomcatProperties tomcatProperties) {
        this.tomcatProperties = tomcatProperties;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(new GwsTomcatConnectionCustomizer());
        return tomcat;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(DataSize.parse(tomcatProperties.getMaxFileSize())); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.parse(tomcatProperties.getMaxRequestSize()));
        return factory.createMultipartConfig();
    }

    /**
     * 默认http连接
     */
    public class GwsTomcatConnectionCustomizer implements TomcatConnectorCustomizer {

        public GwsTomcatConnectionCustomizer() {
        }

        @Override
        public void customize(Connector connector) {
            connector.setPort(Integer.valueOf(tomcatProperties.getPort()));
            connector.setAttribute("connectionTimeout", tomcatProperties.getConnectionTimeout());
            connector.setAttribute("acceptorThreadCount", tomcatProperties.getAcceptorThreadCount());
            connector.setAttribute("minSpareThreads", tomcatProperties.getMinSpareThreads());
            connector.setAttribute("maxSpareThreads", tomcatProperties.getMaxSpareThreads());
            connector.setAttribute("maxThreads", tomcatProperties.getMaxThreads());
            connector.setAttribute("maxConnections", tomcatProperties.getMaxConnections());
            connector.setAttribute("protocol", tomcatProperties.getProtocol());
            connector.setAttribute("redirectPort", tomcatProperties.getRedirectPort());
            connector.setAttribute("compression", tomcatProperties.getCompression());
        }
    }
}
