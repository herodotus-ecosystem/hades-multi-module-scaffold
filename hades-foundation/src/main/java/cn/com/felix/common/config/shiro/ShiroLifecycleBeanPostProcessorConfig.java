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
