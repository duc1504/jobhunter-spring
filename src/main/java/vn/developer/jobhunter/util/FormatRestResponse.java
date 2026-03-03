package vn.developer.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.developer.jobhunter.controller.HelloController;
import vn.developer.jobhunter.domain.RestResponse;
import vn.developer.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest resquest,
            ServerHttpResponse response) {
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
        HttpServletResponse httpServletResponse = servletResponse.getServletResponse();
        int statusCode = httpServletResponse.getStatus();
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(statusCode);
        if (statusCode >= 400) {
            return body;
        } else if (statusCode == 204 || body == null) {
            return body;
        } else if (body instanceof String) {
            return body;
        } else {
            res.setData(body);
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
           
            res.setMessage(apiMessage != null ? apiMessage.value() : "Call successfully");
            return res;
        }

    }

}
