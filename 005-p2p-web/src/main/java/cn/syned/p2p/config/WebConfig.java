package cn.syned.p2p.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //遍历转换器列表
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //获取配置
        FastJsonConfig config = new FastJsonConfig();
        //设置配置
        config.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        //设置支持的类型
        ArrayList<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        //设置转换器
        converter.setFastJsonConfig(config);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(converter);
    }
}
