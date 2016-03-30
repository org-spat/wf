package org.spat.wf.log;

public interface ILogger {
	
	void debug(String msg);
    
    void debug(String format, Object arg);
    
    void debug(String msg, Throwable t);
    
    void info(String msg);
    
    public void info(String format, Object arg);

    public void info(String msg, Throwable t);
    
    public void warn(String msg);
    
    public void warn(String format, Object arg);
    
	public void warn(String msg, Throwable t) ;
 
    public void error(String msg) ;
    
    public void error(String msg, Throwable t) ;
    
    public void error(String format, Object arg) ;
    
	public boolean isDebugEnabled() ;
	
	public boolean isInfoEnabled() ;
	
	public boolean isWarnEnabled();

	public boolean isErrorEnabled();
	
	public String getName();
	
	public String toString() ;
}
