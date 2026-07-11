package bg.softuni.cinevault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final SessionInterceptor sessionInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public WebMvcConfiguration(SessionInterceptor sessionInterceptor, AdminInterceptor adminInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/login",
                        "/register",
                        "/css/**",
                        "/movies",
                        "/movies/{id}"
                );
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns(
                        "/movies/add/**",
                        "/movies/edit/**",
                        "/movies/delete/**"
                );
    }
}
