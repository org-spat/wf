package org.spat.wf.mvc.toolbox.monitor;


/**
 * @author Service Platform Architecture Team (spat@58.com)
 *用于处理一个请求时间
 */
public interface ActionTimeMonitor {

    public static final ActionTimeMonitor NULL = new ActionTimeMonitor() {

        @Override
        public void post() {
        }
    };


    public void post();

    public abstract class Factory {
        private static Factory factory = new Factory() {

            @Override
            public ActionTimeMonitor build() {
                return new DefaultActionTimeMonitor();
            }
        };

        public static void set(Factory factory) {
            if (factory == null)
                throw new NullPointerException();

            Factory.factory = factory;
        }

        public static ActionTimeMonitor create() {
            return factory.build();
        }

        public abstract ActionTimeMonitor build();
    }
}
