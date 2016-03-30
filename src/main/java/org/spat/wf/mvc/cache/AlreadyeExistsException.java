package org.spat.wf.mvc.cache;

public class AlreadyeExistsException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;

	public AlreadyeExistsException(){
		
	}
	
	@Override
	public String getMessage(){
		return "Data is Alreadye Exists！";
	}
	

}
