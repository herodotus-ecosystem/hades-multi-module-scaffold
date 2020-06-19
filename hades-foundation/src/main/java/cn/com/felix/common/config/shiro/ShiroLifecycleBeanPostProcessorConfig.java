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
 * File Name: ShiroLifecycleBeanPostProcessorConfig.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.common.config.shiro;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro 生命周期处理器，该类Bean会导致类的注入失效，所以单独分离开。
 * LifecycleBeanPostProcessor， 是DestructionAwareBeanPostProcessor的子类，
 * 负责org.apache.shiro.util.Initializable类型bean的生命周期，初始化和销毁
 * 主要涉及AuthorizingRealm， CacheManager。
 * 如果将Bean写入ShiroConfig，会导致CacheManager以及Sys*Service等的注入失败。
 */
@Configuration
public class ShiroLifecycleBeanPostProcessorConfig {

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
