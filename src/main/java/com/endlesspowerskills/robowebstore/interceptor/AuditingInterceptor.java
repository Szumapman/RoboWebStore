package com.endlesspowerskills.robowebstore.interceptor;

import com.endlesspowerskills.robowebstore.util.FieldNames;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class AuditingInterceptor extends HandlerInterceptorAdapter {

    // -- fields
    private String admin;
    private String productName;


    // -- methods
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().endsWith(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT) && request.getMethod().equals("POST")){
            admin = request.getRemoteUser();
            productName = request.getParameterValues(FieldNames.PRODUCT_NAME)[0];

            log.info("Collection request parameter names: {}",request.getParameterMap().keySet());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(request.getRequestURI().endsWith(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT)&& request.getMethod().equals("POST")
                && response.getStatus() == 200){
            log.info("New product name: {}, added by: {}, added time: {}", productName, admin, getCurrentTime());
        }
    }

    private String getCurrentTime() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'o' hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatter.format(calendar.getTime());
    }
}
