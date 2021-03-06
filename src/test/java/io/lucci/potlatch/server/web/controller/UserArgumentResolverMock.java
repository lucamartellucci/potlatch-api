package io.lucci.potlatch.server.web.controller;

import io.lucci.potlatch.server.web.controller.resolver.CurrentUser;
import io.lucci.potlatch.server.web.model.User;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolverMock implements HandlerMethodArgumentResolver {

    private static User user;

    public boolean supportsParameter( MethodParameter methodParameter ) {
        return methodParameter.getParameterAnnotation( CurrentUser.class ) != null;
    }

    public Object resolveArgument( MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory ) throws Exception {
        return user;
    }


    public static void setUser( final User user ) {
        UserArgumentResolverMock.user = user;
    }

}
