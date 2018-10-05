package tourguide; /**
 * 
 */

/**
 * @author pbj
 * 
 *   type Status := OK | Error String
 * 
 * Usage:
 * 
 *   Status.OK
 *   new Status.Error("message")
 *   
 *   Static reference used for OK object, since only ever need one.
 */
public abstract class Status {
    
    public static final Status OK = new OK();
    
    public static class OK extends Status {
        private OK() {}
        
        public boolean equals(Object o) {
            return o instanceof OK;
        }
        
        public String toString() { return "Status.OK"; }
    }
    
    public static class Error extends Status {
        private String message;
        
        public Error(String message) {
            this.message = message;
        }
        
        public boolean equals(Object o) {
            if (o instanceof Error) {
                Error oE = (Error) o;
                return message.equals(oE.message);
            } else {
                return false;
            }
        }
        
        public String toString() {return "Status.Error(" + message + ")"; }
    }

}
