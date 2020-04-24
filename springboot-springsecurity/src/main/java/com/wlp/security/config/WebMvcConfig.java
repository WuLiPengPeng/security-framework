package com.wlp.security.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.protobuf.ServiceException;
import com.wlp.security.exception.ExceptionJsonMsg;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

    }

    /**
     * 在springboot的静态资源映射中，其中默认配置的/**映射到/static（或/public、/resources、/META-INF/resources）
     * 其中默认配置的/webjars/**映射到classpath:/META-INF/resources/webjars/。
     * 而抛出NoHandlerFoundException的条件我们可以从DispatcherServlet的源码中发现：
     *
     * mappedHandler = getHandler(processedRequest);
     * if (mappedHandler == null || mappedHandler.getHandler() == null) {
     *     noHandlerFound(processedRequest, response);
     *     return;
     * }
     * 在路径不会被匹配的时候才会抛出NoHandlerFoundException异常，但是由于默认的匹配路径有/**，所以即使你的地址错误，仍然会匹配到 /**这个静态资源映射地址，就不会进入noHandlerFound方法，自然不会抛出NoHandlerFoundException了。
     *
     * 由于我们上面配置关闭了静态资源映射，所以这时候我们项目的静态资源没法访问了，
     * 我们需要手动定义静态资源的映射，比如我们集成了swagger，会发现/swagger-ui.html页面没法访问了，
     * 我们需要手动定义静态资源映射：
     * @param registry
     */

    /**
     * 定义静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/", "/static", "/public");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     *使用阿里 FastJson 作为JSON MessageConverter
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero);//Number null -> 0
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }

    /**
     * 在springboot中，404异常是进行了默认资源映射的，
     * 如果没有做特殊配置，我们的异常处理系统就会处理不到404请求
     * 这样就不能统一返回值的格式，不能友好提示。
     *
     * 需要添加配置
     * #出现错误时, 直接抛出异常
     * spring.mvc.throw-exception-if-no-handler-found=true
     * #关闭工程中的资源文件建立映射
     * spring.resources.add-mappings=false
     *
     * 统一异常处理
     *
     * @param resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

        resolvers.add((request, response, handler, e) -> {
            ExceptionJsonMsg result;
            if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                result = new ExceptionJsonMsg("501", "业务层出错：" + e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                result = new ExceptionJsonMsg("404", "接口 [" + request.getRequestURI() + "] 不存在");
            } else {
                result = new ExceptionJsonMsg("500", "接口 [" + request.getRequestURI() + "] 错误，请联系管理员！");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                logger.error(message);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }


    private void responseResult(HttpServletResponse response, ExceptionJsonMsg result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

    }

}
