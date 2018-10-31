package com.endlesspowerskills.robowebstore.config;

import com.endlesspowerskills.robowebstore.interceptor.AuditingInterceptor;
import com.endlesspowerskills.robowebstore.interceptor.PromoCodeInterceptor;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.ViewNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    // -- methods
    @Bean
    public LocaleResolver localeResolver(){
        return new SessionLocaleResolver();
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(PageMappings.ROBOWEBSTORE + PageMappings.LOGIN).setViewName(ViewNames.LOGIN);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new AuditingInterceptor());

        PromoCodeInterceptor promoCodeInterceptor = new PromoCodeInterceptor();
        promoCodeInterceptor.setPromoCode("pr0m0");
        // Dla uproszczenia przekierowanie do standardowej strony produktów
        promoCodeInterceptor.setOfferRedirect(PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS);
        registry.addInterceptor(promoCodeInterceptor);
    }
}
