package adaptiveObjects;

public class Property {
    private Double value;
    private PropertyType propertyType;

    public Property(PropertyType propertyType, Double value){
        this.propertyType = propertyType;
        this.value = value;
    }

    public void setProperty(Double value){
        this.value = value;
    }

    public Object getPropertyValue(){
        return value;
    }

    public String getPropertyName(){
        return propertyType.getName();
    }

}
