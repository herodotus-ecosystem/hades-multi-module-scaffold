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
 * Module Name: hades-application
 * File Name: HadesWebApplication.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:40
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.flowable.ui.modeler.conf.ApplicationConfiguration;
import org.flowable.ui.modeler.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableAsync
@EnableEncryptableProperties
public class HadesWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HadesWebApplication.class, args);
    }

}

