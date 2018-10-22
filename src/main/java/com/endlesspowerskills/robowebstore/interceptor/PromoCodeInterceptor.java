package com.endlesspowerskills.robowebstore.interceptor;

import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.ParameterValues;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Getter
@Setter
public class PromoCodeInterceptor extends HandlerInterceptorAdapter {

    // -- fields
    private String promoCode;
    private String offerRedirect;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().endsWith(PageMappings.ROBOWEBSTORE + PageMappings.SPECIAL_OFFER)){
            String givenPromoCode = "";
            if(request.getParameterValues(ParameterValues.PROMO)[0] != null){
                givenPromoCode = request.getParameterValues(ParameterValues.PROMO)[0];
            }
            if(givenPromoCode.equals(promoCode)){
                response.sendRedirect(offerRedirect);
            } else {
                response.sendRedirect(PageMappings.ROBOWEBSTORE + PageMappings.INVALID_PROMO_CODE);
            }
            return false;
        }
        return  true;
    }
}
