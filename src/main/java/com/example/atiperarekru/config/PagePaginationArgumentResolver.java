package com.example.atiperarekru.config;

import static java.lang.Integer.parseInt;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.atiperarekru.dto.PagePagination;

public class PagePaginationArgumentResolver implements HandlerMethodArgumentResolver {

    private final static String PAGE = "page";
    private final static String PAGE_LIMIT = "size";

    private final static int DEFAULT_PAGE = 1;
    private final static int DEFAULT_PAGE_LIMIT = 20;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PagePagination.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter param,
            ModelAndViewContainer container,
            NativeWebRequest request,
            WebDataBinderFactory binderFactory) {
        String offset = request.getParameter(PAGE);
        String limit = request.getParameter(PAGE_LIMIT);
        return new PagePagination(
                null == offset ? DEFAULT_PAGE : parseInt(offset),
                null == limit ? DEFAULT_PAGE_LIMIT : parseInt(limit));
    }

}
