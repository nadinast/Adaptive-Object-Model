package adaptiveObjects;

import java.util.List;

public class SimpleRule implements Rule{
    private String propertyName;

    public SimpleRule(String propertyName){
        this.propertyName = propertyName;
    }

    @Override
    public double getResult(List<Property> properties){
        for(Property p : properties)
            if(p.getPropertyName() == propertyName){
                String className = p.getPropertyValue().getClass().getName();
                if(className == "Double")
                    return ((Double)p.getPropertyValue()).doubleValue();
            }
        return 0;
    }
}
