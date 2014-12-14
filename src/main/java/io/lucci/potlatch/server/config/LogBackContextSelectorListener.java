package io.lucci.potlatch.server.config;

import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;


public class LogBackContextSelectorListener  implements ServletContextListener {


	public void contextInitialized( ServletContextEvent servletContextEvent ) {
        try {
        	
        	String logConfigLocation = servletContextEvent.getServletContext().getInitParameter("logConfigLocation"); 
        	String webAppRootKey = servletContextEvent.getServletContext().getInitParameter("webAppRootKey");

        	System.out.println("LOGBACK:  logConfigLocation: " + logConfigLocation +" webAppRootKey: " + webAppRootKey);
        	LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        	loggerContext.setName(webAppRootKey);
        	ContextInitializer contextInitializer = new ContextInitializer(loggerContext);
        	contextInitializer.configureByResource( new URL( resolveLogConfigLocation( logConfigLocation ) ) );
        	System.out.println("LOGBACK configured.");
			
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    protected String resolveLogConfigLocation( final String input ) {
        final Properties properties = System.getProperties();
        StringBuffer buf = new StringBuffer( input );
        for ( int startIndex = buf.indexOf( "${" ); startIndex != -1; ) {
            int endIndex = buf.indexOf( "}", startIndex + "${".length() );
            if ( endIndex != -1 ) {
                String placeholder = buf.substring( startIndex + "${".length(), endIndex );
                int nextIndex = endIndex + "}".length();
                String propVal = ( String ) properties.get( placeholder );
                if ( propVal == null ) {
                    throw new RuntimeException( "Unable to resolve placeholder [" + placeholder + "]" );
                }
                if ( propVal != null ) {
                    buf.replace( startIndex, endIndex + "}".length(), propVal );
                    nextIndex = startIndex + propVal.length();
                }
                startIndex = buf.indexOf( "${", nextIndex );
            } else {
                startIndex = -1;
            }
        }
        final String result = buf.toString();
        System.out.println( "Resolved placeholder: [" + result + "]" );
        return result;
    }

    public void contextDestroyed( ServletContextEvent servletContextEvent ) {
    }
	
}
