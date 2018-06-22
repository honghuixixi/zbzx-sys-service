package com.aek.ebey.sys.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aek.common.core.serurity.JwtAuthenticationEntryPoint;
import com.aek.common.core.serurity.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private JwtAuthenticationTokenFilter tokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
      return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf()
                .disable()
                
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                
                // don't create session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/zbzxsys/excel/export/templt/tenant")
                .permitAll()
                .antMatchers("/zbzxsys/excel/export/error/tenant/*")
                .permitAll()
                .antMatchers("/zbzxsys/area/**")
                .permitAll()
                .antMatchers("/zbzxsys/tenant/all/manageTenant")
                .permitAll()
                .antMatchers("/zbzxsys/tenant/createTrialSubTenant")
                .permitAll()
                .antMatchers("/zbzxsys/tenant/createTrialSubTenantForZjzx")
                .permitAll()
                .antMatchers("/zbzxsys/index/sendRstCode")
                .permitAll()
                .antMatchers("/zbzxsys/index/sendCode")
                .permitAll()
                .antMatchers("/zbzxsys/user/register")
                .permitAll()
                .antMatchers("/zbzxsys/user/websiteLogin")
                .permitAll()
                .antMatchers("/zbzxsys/user/getTenantName")
                .permitAll()
                .antMatchers("/zbzxsys/user/checkLoginName")
                .permitAll()
                .antMatchers("/zbzxsys/user/checkMobile")
                .permitAll()
                .antMatchers("/zbzxsys/user/checkTenantName")
                .permitAll()
                .antMatchers("/zbzxsys/user/isLoginNameExist")
                .permitAll()
                .antMatchers("/zbzxsys/index/resetPassword")
                .permitAll()
                .antMatchers("/zbzxsys/user/*/modify/email")  // 邮箱验证
                .permitAll()
                .antMatchers("/zbzxsys/tenant/tenantNameCheck")  
                .permitAll()
                .antMatchers("/zbzxsys/user/getUserByMobile")  
                .permitAll()
                .antMatchers("/zbzxsys/tenant/mobileCheck")
                .permitAll()
                .antMatchers("/zbzxsys/index/validateCode")
                .permitAll()
                .antMatchers("/webjars/**")
                .permitAll()
                .antMatchers("/swagger-resources/**")
                .permitAll()
                .antMatchers("/v2/api-docs")
                .permitAll()
                .antMatchers("/images/*.jpg")
                .permitAll()
                .antMatchers("/images/*.png")
                .permitAll()
                .antMatchers("/**/*.js")
                .permitAll()
                .antMatchers("/**/*.css")
                .permitAll()
                .antMatchers("/**/*.woff")
                .permitAll()
                .antMatchers("/**/*.woff2")
                .permitAll()
                .antMatchers("/**/*.jsp")
                .permitAll()
                .antMatchers("/**/*.html")
                .permitAll()
                .antMatchers("/favicon.ico")
                .permitAll()
                .antMatchers("/auth/**")
                .permitAll()
        		.anyRequest()
                .authenticated();
        
        // Custom JWT based security filter
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}