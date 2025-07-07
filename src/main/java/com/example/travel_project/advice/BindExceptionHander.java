package com.example.travel_project.advice;

import com.example.travel_project.common.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
@RestControllerAdvice
它用于处理 BindException 异常，这通常发生在表单绑定（如 HTTP 请求参数和 Java 对象的绑定）时出现验证失败的情况
@RestControllerAdvice 是 Spring 5 引入的注解，它结合了 @ControllerAdvice 和 @ResponseBody，意味着该类是一个全局的异常处理器，能够处理控制器中的异常
@RestControllerAdvice 会拦截整个应用中的所有 @RequestMapping 方法中抛出的异常，并且能够返回一个响应，通常以 JSON 格式返回
*/


/*
@ExceptionHandler(BindException.class)
public void handleBindException(BindException ex, HttpServletResponse response)
@ExceptionHandler(BindException.class) 表示这个方法专门处理 BindException 异常。当一个方法参数绑定过程中出现问题时，
Spring 会抛出 BindException，比如请求中的字段与目标对象的属性不匹配，或者字段验证失败

ex 参数表示捕获的 BindException 实例，response 参数是 HttpServletResponse，用于设置返回给客户端的 HTTP 响应。
*/


@RestControllerAdvice
public class BindExceptionHander {
    @ExceptionHandler(BindException.class)
    public void handleBindException(BindException ex, HttpServletResponse response) throws IOException{
        //ex.getFieldErrors() 获取所有的字段验证错误，这些错误是 FieldError 对象。
        List<FieldError> fieldErrors = ex.getFieldErrors();
        //对于每一个 FieldError，我们使用 getDefaultMessage() 方法获取错误信息（通常是绑定失败的字段及其具体错误信息），然后将错误信息添加到 errors 列表中
        List<String> errors = new ArrayList<>();
        fieldErrors.stream().forEach(s->{
            errors.add(s.getDefaultMessage());
        });

        ResponseData responseData = new ResponseData();
        responseData.setCode(400);
        responseData.setMessage(errors.stream().collect(Collectors.joining(",")));
        String josn = new ObjectMapper().writeValueAsString(responseData);
        //设置响应的状态码为 400
        response.setStatus(400);
        //设置响应的内容类型为 JSON
        response.setContentType("application/json");
        // 将 JSON 字符串写入 HTTP 响应体
        response.getWriter().println(josn);

    }
}
