package application.config;

import java.lang.reflect.Field;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BeanConfiguration {
    /**
     * Return a logger named corresponding to the class where this logger is being injected.
     *
     * @param injectionPoint {@link InjectionPoint}
     * @return {@link Logger} named corresponding to the class injected
     */
    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(
            Optional.ofNullable(injectionPoint.getMethodParameter()).<Class<?>>map(
                MethodParameter::getDeclaringClass).orElseGet(() ->
                Optional.ofNullable(injectionPoint.getField()).map(Field::getDeclaringClass)
                    .orElseThrow(IllegalArgumentException::new)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }
}
