package com.sk.pp.core;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App 
{
	private static final Logger log = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	try
    	{
    		String appName = System.getProperty("appName");
    		String contextName = appName + "-app.xml";
    		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(contextName);
    		ShutdownHook shutdownhook = new ShutdownHook();
    		shutdownhook.setApplicationContext(context);
    	
    		Runtime.getRuntime().addShutdownHook(new Thread(shutdownhook));
    		context.start();
  
    		while(context.isRunning())
    		{
    			synchronized(shutdownhook)
    			{
    				try
    				{
    					shutdownhook.wait();
    				}
    				catch(InterruptedException ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		}
    		
    	}
    	catch(Exception ex)
    	{
    		log.error("Failed to start:", ex);
    	}
		
    }
    
    
    static class ShutdownHook implements Runnable
    {

    	private ConfigurableApplicationContext ctx;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ctx.close();
			synchronized(this)
			{
				notify();
			}
		}
		
		public void setApplicationContext(ApplicationContext ctx)
		{
			this.ctx = (ConfigurableApplicationContext)ctx;
		}
    	
    }
}
