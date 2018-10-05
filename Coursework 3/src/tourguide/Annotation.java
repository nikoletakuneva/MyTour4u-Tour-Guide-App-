package tourguide; /**
 * 
 */

/**
 * @author pbj
 *
 */
public class Annotation {
    private String text;
    
    public static final Annotation DEFAULT = getDefault();
    
    public String toString() { return text; }
    
    public Annotation(String s) { text = s; }
    
    /*
     * Introduce a default Annotation object for when have no annotation.
     * 
     * Seems cleaner than relying on null reference value for no annotation.
     */
    public static Annotation getDefault() {
        return new Annotation("DEFAULT");
    }
    
    public boolean isDefault() {
        return text.equals("DEFAULT");
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof Annotation)) return false;
        Annotation oA = (Annotation) o;
        return text.equals(oA.text);
    }
    
}
