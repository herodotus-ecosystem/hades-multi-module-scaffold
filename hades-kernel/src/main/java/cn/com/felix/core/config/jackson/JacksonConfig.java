/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: JacksonConfig
 * Author: hades
 * Date: 19-3-20 下午3:05
 * LastModified: 19-3-20 下午3:05
 */

package cn.com.felix.core.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 解决com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class问题
 * @link：https://blog.csdn.net/lvhaoguang0/article/details/86014402
 * </p>
 *
 * @author hades
 * @date 2019/3/20
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
