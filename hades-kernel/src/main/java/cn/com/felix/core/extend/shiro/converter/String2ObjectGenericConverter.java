/*
 * Copyright (c) 2018. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: String2ObjectGenericConverter
 * Author: hades
 * Date: 18-12-28 下午5:18
 * LastModified: 18-10-30 下午8:18
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
