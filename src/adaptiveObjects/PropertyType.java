package adaptiveObjects;

import java.util.ArrayList;

public class PropertyType {
    private String name;
    private ArrayList<Property> instances = new ArrayList<>();

    public PropertyType(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public String getName(){
        return name;
    }

    public void addInstance(Property prop){
        instances.add(prop);
    }
}
