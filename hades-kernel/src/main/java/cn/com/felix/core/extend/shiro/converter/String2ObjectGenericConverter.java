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
 * File Name: String2ObjectGenericConverter.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.extend.shiro.converter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@ConfigurationPropertiesBinding
public class String2ObjectGenericConverter implements GenericConverter {

    private static final Logger logger = LoggerFactory.getLogger(String2ObjectGenericConverter.class);

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;

    static {
        Set<ConvertiblePair> convertiblePairs = new HashSet<>();
        convertiblePairs.add(new ConvertiblePair(String.class, CredentialsMatcher.class));
        convertiblePairs.add(new ConvertiblePair(String.class, Filter.class));
        convertiblePairs.add(new ConvertiblePair(String.class, Realm.class));
        convertiblePairs.add(new ConvertiblePair(String.class, byte[].class));
        CONVERTIBLE_TYPES = Collections.unmodifiableSet(convertiblePairs);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONVERTIBLE_TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            if (targetType.getObjectType().isAssignableFrom(byte[].class)) return source.toString().getBytes();
            return BeanUtils.instantiateClass((Class<Filter>) Class.forName(ObjectUtils.nullSafeToString(source)));
        } catch (ClassNotFoundException e) {
            logger.error("[Shiro] |- Can't convert {} from {} to {}", source, sourceType.getName(), targetType.getName(), e);
        }
        return null;
    }
}
