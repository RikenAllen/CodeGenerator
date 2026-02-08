import java.util.ArrayList;

public class UMLClass {
    private String id = null;
    private String value = null;
    private boolean ab = false;
    private String superClassId = null;
    private ArrayList<String> contains = new ArrayList<>();

    public UMLClass(String id, String value, boolean ab) {
        this.id = id;
        this.value = value;
        this.ab = ab;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getSuperClassId() {
        return superClassId;
    }

    public boolean isAb() {
        return ab;
    }

    public ArrayList<String> getContains() {
        return contains;
    }

    public void setSuperClassId(String superClassId) {
        this.superClassId = superClassId;
    }

    public void addComposition(String subclassID) {
        contains.add(subclassID);
    }
}
