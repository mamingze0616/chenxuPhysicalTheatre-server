package com.chenxu.physical.theatre.configure;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author mamingze
 * @version 1.0
 * @title CustomizeSpringMvcConfigure
 * @description
 * @create 2025/3/28 19:22
 */
@Configurable
public class CustomizeSpringMvcConfigure {
    /**
     * # 代码解释
     * 该代码定义了一个Spring Bean，用于自定义Jackson的ObjectMapper行为。具体功能如下：
     * 1. `@Bean`注解表明该方法返回的对象将被注册为Spring容器中的一个Bean。
     * 2. 方法`customizer`返回一个`Jackson2ObjectMapperBuilderCustomizer`实例，通过Lambda表达式配置序列化功能。
     * 3. 启用`SerializationFeature.WRITE_ENUMS_USING_TO_STRING`特性，使枚举类型在序列化时使用其`toString`方法的值。
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }
}
