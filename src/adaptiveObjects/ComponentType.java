package adaptiveObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class ComponentType {
    private String name;
    private ArrayList<PropertyType> propertyTypes = new ArrayList<>();
    private ArrayList<Component> instances = new ArrayList<>();
    private HashMap<String, Rule> rules = new HashMap<String, Rule>();

    public ComponentType(String type){
        name = type;
    }

    public Rule getRule(String name){
        System.out.println(name);
        return rules.get(name);
    }

    public void addPropertyType(PropertyType propType){
        propertyTypes.add(propType);
    }

    public String getPropertyTypes(){
        return propertyTypes.toString();
    }

    public void addNewInstance(Component component){
        instances.add(component);
    }

    public boolean addRule(String name, String rule){
        try {
            Rule parsedRule = parseRule(rule);
            rules.put(name, parsedRule);
            return true;
        }
        catch(InvalidRuleException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    public String getName(){
        return this.name;
    }

    private Rule parseRule(String rule) throws InvalidRuleException{
        System.out.println(rule);
        String[] operands = rule.split("[*+/-]");
        System.out.println(operands[1] +" " +operands.length);
        String[] operators =  rule.split("[^+*/-]+");
        System.out.println(operators[0] +" " +operators.length);
        Rule parsedRule = null;
        ArrayList<Rule> simpleRules = new ArrayList<>();
        for(String operand : operands){
            try {
                isNumber(operand);
            }
            catch(NumberFormatException e){
                if(!getPropertyTypes().contains(operand))
                    throw new InvalidRuleException("The property type " + operand + " does not match the property types of " + this.name);
            }
            finally{
                simpleRules.add(new SimpleRule(operand));
            }
        }

        if(operators.length == 0) {
            if (operands.length == 1)
                return new SimpleRule(operands[0]);
            else
                throw new InvalidRuleException("The number of operators and operands do not match");
        }

        for(int i  = operators.length - 1; i >= 0; i--)
            switch(operators[i]){
                case "+":
                    if(parsedRule == null)
                        parsedRule = new CompositeAddRule(simpleRules.get(i-1), simpleRules.get(i));
                    else
                        parsedRule = new CompositeAddRule(simpleRules.get(i-1), parsedRule);
                    break;
                case "-":
                    if(parsedRule == null)
                        parsedRule = new CompositeSubRule(simpleRules.get(i-1), simpleRules.get(i));
                    else
                        parsedRule = new CompositeSubRule(simpleRules.get(i-1), parsedRule);
                    break;
                case "*":
                    if(parsedRule == null)
                        parsedRule = new CompositeMulRule(simpleRules.get(i-1), simpleRules.get(i));
                    else
                        parsedRule = new CompositeMulRule(simpleRules.get(i-1), parsedRule);
                    break;
                case "/":
                    if(parsedRule == null)
                        parsedRule = new CompositeDivRule(simpleRules.get(i-1), simpleRules.get(i));
                    else
                        parsedRule = new CompositeDivRule(simpleRules.get(i-1), parsedRule);
                    break;
                default:
                    throw new InvalidRuleException("The operator " + operators[i] + " is not a valid operator for th type ");
            }
        return parsedRule;
    }

    private boolean isNumber(String operand) throws NumberFormatException{
        Double.valueOf(operand);
        return true;
    }

}
