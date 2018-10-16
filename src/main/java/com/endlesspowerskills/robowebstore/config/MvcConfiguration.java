package com.endlesspowerskills.robowebstore.config;

import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.ViewNames;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(PageMappings.ROBOWEBSTORE + PageMappings.LOGIN).setViewName(ViewNames.LOGIN);
    }
}
