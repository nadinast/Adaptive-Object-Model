package adaptiveObjects;

import java.util.List;

public abstract class CompositeRule implements Rule {
    private Rule rule1;
    private Rule rule2;

    public CompositeRule(Rule rule1, Rule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    @Override
    public double getResult(List<Property> properties){
        return this.operation(rule1.getResult(properties), rule2.getResult(properties));
    }

    public abstract Double operation(Double value1, Double value2);

    public void setRule1(Rule rule1) {
        this.rule1 = rule1;
    }

    public void setRule2(Rule rule2) {
        this.rule2 = rule2;
    }
}
