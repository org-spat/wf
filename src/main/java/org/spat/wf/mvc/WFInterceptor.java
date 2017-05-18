package org.spat.wf.mvc;

public abstract class WFInterceptor {
	
		private int order = 0;

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
		
		
	    public abstract ActionResult before(BeatContext beat) ; //前置拦截

	    public abstract ActionResult after(BeatContext beat, ActionResult actionResult) ;

	    public abstract void complet(BeatContext beat, ActionResult actionResult);

}
