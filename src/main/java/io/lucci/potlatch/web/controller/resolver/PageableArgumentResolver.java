package io.lucci.potlatch.web.controller.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class PageableArgumentResolver implements HandlerMethodArgumentResolver {
	
    private static Logger logger = LoggerFactory.getLogger( PageableArgumentResolver.class );

    @Override
    public boolean supportsParameter( MethodParameter methodParameter ) {
        return methodParameter.getParameterAnnotation( PageReq.class ) != null;
    }

    @Override
    public Object resolveArgument( MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory ) throws Exception {
        logger.debug( "resolving argument of methodParameter: {}", methodParameter );
        
        Integer page = null;
        if ( nativeWebRequest.getParameter( "page" ) != null ) {
            page = Integer.valueOf( nativeWebRequest.getParameter( "page" ) );
        }
        Integer size = null;
        if ( nativeWebRequest.getParameter( "size" ) != null ) {
            size = Integer.valueOf( nativeWebRequest.getParameter( "size" ) );
        }
        PageRequest pageRequest = null;
        if (size != null && page != null) {
        	pageRequest = new PageRequest( page, size );
        }
        logger.debug( "argument resolved as {}", pageRequest );
        return pageRequest;
    }

}
