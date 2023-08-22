package application.config;

import application.entities.ServerResponse;
import application.security.AuthenticationProviderImpl;
import application.security.AuthenticationSuccessHandlerImpl;
import application.security.LogoutSuccessHandlerImpl;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProviderImpl authenticationProvider;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/register", "/login", "/logout")
            .permitAll().anyRequest().authenticated().and().formLogin().loginProcessingUrl("/login")
            .successHandler(authenticationSuccessHandler)
            .failureHandler((request, response, exception) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(ServerResponse
                    .error(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage()));
                response.getWriter().flush();
            }).and().logout().deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler)
            .and().exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(ServerResponse
                    .error(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                response.getWriter().flush();
            });
    }
}
