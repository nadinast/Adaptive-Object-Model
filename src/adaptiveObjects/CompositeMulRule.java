package adaptiveObjects;

public class CompositeMulRule extends CompositeRule {

    public CompositeMulRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
    }

    @Override
    public Double operation(Double value1, Double value2) {
        return value1 * value2;
    }
}
