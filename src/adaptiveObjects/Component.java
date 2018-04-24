package adaptiveObjects;

import java.util.ArrayList;

public class Component {
    private ArrayList<Property> properties = new ArrayList<>();
    private ComponentType type;

    public Component(ComponentType type){
        this.type = type;
        type.addNewInstance(this);
    }

    public String getTypeName(){
        return type.getName();
    }

    public String getPropertyTypeNames(){
        return type.getPropertyTypes();
    }

    public void addProperty(PropertyType propertyType, Double value) throws PropertyTypeException{
        String componentPropertyTypes = type.getPropertyTypes();
        if(componentPropertyTypes.contains(propertyType.getName())) {
            Property prop = new Property(propertyType, value);
            properties.add(prop);
            propertyType.addInstance(prop);
        }
        else
            throw new PropertyTypeException("The property " + propertyType.getName() + " is not defined for the type " +
                                             type.getName());
    }

    public double applyRule(String ruleName){
        System.out.println(type.getName());
        Rule rule = type.getRule(ruleName);
        return rule.getResult(properties);
    }

}
