package com.ryan.loginwithcookie2;

import com.ryan.loginwithcookie2.config.SiteMeshFilter;
import com.ryan.loginwithcookie2.controller.*;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.EnumSet;

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
    public ServletRegistrationBean<VerifyOtpController> verifyOtpServletRegistration() {
        ServletRegistrationBean<VerifyOtpController> reg = new ServletRegistrationBean<>(new VerifyOtpController(), "/verify-otp", "/resend-otp");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<ForgotPasswordController> forgotPasswordServletRegistration() {
        ServletRegistrationBean<ForgotPasswordController> reg = new ServletRegistrationBean<>(new ForgotPasswordController(), "/forgot-password");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<ResetPasswordController> resetPasswordServletRegistration() {
        ServletRegistrationBean<ResetPasswordController> reg = new ServletRegistrationBean<>(new ResetPasswordController(), "/reset-password");
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
    public ServletRegistrationBean<VideoController> videoServletRegistration() {
        ServletRegistrationBean<VideoController> reg = new ServletRegistrationBean<>(new VideoController(), "/video");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<VideoDetailController> videoDetailServletRegistration() {
        ServletRegistrationBean<VideoDetailController> reg = new ServletRegistrationBean<>(new VideoDetailController(), "/video/detail");
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean<VideoManageController> videoManageServletRegistration() {
        ServletRegistrationBean<VideoManageController> reg = new ServletRegistrationBean<>(new VideoManageController(), "/video/manage", "/video/create", "/video/edit", "/video/delete");
        reg.setLoadOnStartup(1);
        reg.setMultipartConfig(new MultipartConfigElement("uploads", 1024 * 1024 * 50, 1024 * 1024 * 100, 1024 * 1024 * 2));
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

    @Bean
    public ServletRegistrationBean<ProfileController> profileServletRegistration() {
        ServletRegistrationBean<ProfileController> reg = new ServletRegistrationBean<>(new ProfileController(), "/profile", "/profile/verify-email");
        reg.setLoadOnStartup(1);
        reg.setMultipartConfig(new MultipartConfigElement("uploads", 1024 * 1024 * 10, 1024 * 1024 * 20, 1024 * 1024 * 2));
        return reg;
    }

    @Bean
    public FilterRegistrationBean<SiteMeshFilter> siteMeshFilterRegistration() {
        FilterRegistrationBean<SiteMeshFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new SiteMeshFilter());
        filter.addUrlPatterns("/views/*");
        filter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD));
        filter.setOrder(1);
        return filter;
    }
}
