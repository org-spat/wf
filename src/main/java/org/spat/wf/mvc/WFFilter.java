package org.spat.wf.mvc;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;

public abstract class WFFilter {
	
	protected ILogger log = LoggerFactory.getLogger(this.getClass());
	
	public abstract Boolean doFilter(BeatContext beat) throws Exception;
}