package com.ryan.loginwithcookie2;

import com.ryan.loginwithcookie2.controller.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoginWithCookie2Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LoginWithCookie2Application.class, args);
    }

    @Bean
    public TomcatConnectorCustomizer tomcatConnectorCustomizer() {
        return connector -> connector.setMaxLocaleCount(200);
    }

    @Bean
    public ServletRegistrationBean<LoginController> loginServletRegistration() {
        ServletRegistrationBean<LoginController> reg = new ServletRegistrationBean<>(new LoginController(), "/login");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<RegisterController> registerServletRegistration() {
        ServletRegistrationBean<RegisterController> reg = new ServletRegistrationBean<>(new RegisterController(), "/register");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<WaitingController> waitingServletRegistration() {
        ServletRegistrationBean<WaitingController> reg = new ServletRegistrationBean<>(new WaitingController(), "/waiting");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<LogoutController> logoutServletRegistration() {
        ServletRegistrationBean<LogoutController> reg = new ServletRegistrationBean<>(new LogoutController(), "/logout");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<HomeController> homeServletRegistration() {
        ServletRegistrationBean<HomeController> reg = new ServletRegistrationBean<>(new HomeController(), "/", "/home");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<AdminHomeController> adminHomeServletRegistration() {
        ServletRegistrationBean<AdminHomeController> reg = new ServletRegistrationBean<>(new AdminHomeController(), "/admin/home");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<ManagerHomeController> managerHomeServletRegistration() {
        ServletRegistrationBean<ManagerHomeController> reg = new ServletRegistrationBean<>(new ManagerHomeController(), "/manager/home");
        reg.setLoadOnStartup(1);
        return reg;
    }
}

