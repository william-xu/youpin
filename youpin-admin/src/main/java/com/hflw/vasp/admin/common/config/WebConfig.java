package com.hflw.vasp.admin.common.config;

import com.hflw.vasp.framework.components.UploadProperties;
import com.hflw.vasp.framework.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liuyf
 * @Title WebConfig.java
 * @Package com.hflw.vasp.credit.sp.config
 * @Description 系统的配置相关文件，比如拦截器，过滤器，跨域等
 * @date 2019年7月18日 下午4:45:06
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UploadProperties uploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("/dist/").addResourceLocations();

        String path = uploadProperties.getBasePath();
        registry.addResourceHandler("/files/**").addResourceLocations("file://" + path);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new CorsFilter());
        frBean.addUrlPatterns("/*");
        return frBean;
    }

    /**
     * 有问题
     * https://blog.csdn.net/weixin_33958585/article/details/88678712
     *
     * @param registry
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(origins.split(","))
//                .allowedHeaders("Authorization, Content-Type, X-Requested-With")
//                .allowedMethods("POST, PUT, GET, OPTIONS, DELETE")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureContentNegotiation(
//            ContentNegotiationConfigurer configurer) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureDefaultServletHandling(
//            DefaultServletHandlerConfigurer configurer) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void addArgumentResolvers(
//            List<HandlerMethodArgumentResolver> resolvers) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void addReturnValueHandlers(
//            List<HandlerMethodReturnValueHandler> handlers) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureMessageConverters(
//            List<HttpMessageConverter<?>> converters) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void configureHandlerExceptionResolvers(
//            List<HandlerExceptionResolver> resolvers) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void extendHandlerExceptionResolvers(
//            List<HandlerExceptionResolver> resolvers) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public Validator getValidator() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public MessageCodesResolver getMessageCodesResolver() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
