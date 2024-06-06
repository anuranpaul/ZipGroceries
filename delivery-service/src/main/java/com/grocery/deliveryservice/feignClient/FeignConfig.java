//package com.grocery.deliveryservice.feignClient;
//
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Configuration
//public class FeignConfig {
//
//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            // Retrieve the token from the current request
//            String token = getTokenFromRequest();
//            if (token != null) {
//                // Add the Authorization header with the token
//                requestTemplate.header("Authorization", "Bearer " + token);
//            }
//        };
//    }
//
//    private String getTokenFromRequest() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes instanceof ServletRequestAttributes) {
//            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//            // Get the Authorization header
//            String authorizationHeader = request.getHeader("Authorization");
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                return authorizationHeader.substring(7); // Remove "Bearer " prefix
//            }
//        }
//        return null;
//    }
//}
