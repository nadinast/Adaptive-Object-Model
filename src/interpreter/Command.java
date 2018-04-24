package interpreter;

import adaptiveObjects.*;

import java.io.*;
import java.util.HashMap;

public class Command {

    private static HashMap<String, Component> components = new HashMap<>();
    private static HashMap<String, ComponentType> componentTypes = new HashMap<>();
    private static HashMap<String, PropertyType> propertyTypes = new HashMap<>();

    public static boolean parseCommand(String commandName, String[] commandParams, int line)throws InvalidCommandException{
        switch(commandName){
            case "NewComponentType":
                if(commandParams.length == 1)
                    if(checkValidParamName(commandParams[0]))
                        return true;
                return false;
            case "ApplyRule":
            case "GetPropertyValue":
            case "NewComponent":
            case "AddPropType":
            case "NewPropertyType" :
                if (commandParams.length == 2)
                    if (checkValidParamName(commandParams[0]))
                        if (checkValidParamName(commandParams[1]))
                            return true;
                return false;
            case "AddRule":
            case "AddNewProperty":
                if(commandParams.length == 3)
                    if(checkValidParamName(commandParams[0]))
                        if(checkValidParamName(commandParams[1]))
                            if(checkValidParamName(commandParams[2]))
                                return true;
                return false;
            default:
                throw new InvalidCommandException("The command parameters at line " + line + " do not have valid names");
        }
    }

    private static boolean checkValidParamName(String paramName){
        if (Character.isAlphabetic(paramName.charAt(0)) || (paramName.charAt(0) == '_'))
            if(paramName.length() <= 30)
                return true;
        return false;
    }

    public static void execute(String command, int line) throws InvalidCommandException{
        String commandName =  command.split(" ")[0];
        String[] commandParams =  command.substring(commandName.length()).split(",");
        for(int i = 0; i < commandParams.length; i++)
            commandParams[i] = commandParams[i].trim();
            parseCommand(commandName, commandParams, line);
            switch (commandName) {
                case "NewComponentType":
                    componentTypes.put(commandParams[0], new ComponentType(commandParams[0]));
                    break;
                case "NewPropertyType":
                    if (!commandParams[1].equals("Double"))
                        throw new InvalidCommandException("The type of the " + commandParams[0] + " property was not 'Double'");
                    propertyTypes.put(commandParams[0], new PropertyType(commandParams[0]));
                    break;
                case "AddRule":
                    ComponentType compType = componentTypes.get(commandParams[0]);
                    if (compType == null)
                        throw new InvalidCommandException("The rule " + commandParams[1] + " cannot be added to "
                                + commandParams[0] + " because the component type does not exist");

                    compType.addRule(commandParams[1], commandParams[2]);
                    break;
                case "AddPropType":
                    ComponentType componentType = componentTypes.get(commandParams[0]);
                    PropertyType propertyType = propertyTypes.get(commandParams[1]);
                    if (componentType == null)
                        throw new InvalidCommandException("The property type " + commandParams[1] + " cannot be added to "
                                + commandParams[0] + " because the component type does not exist");
                    if (propertyType == null)
                        throw new InvalidCommandException("The property type " + commandParams[1] + " cannot be added to "
                                + commandParams[0] + " because the property type does not exist");
                    componentType.addPropertyType(propertyType);
                    break;

                case "NewComponent":
                    ComponentType componentType2 = componentTypes.get(commandParams[1]);
                    if (componentType2 == null)
                        throw new InvalidCommandException("The component " + commandParams[0] + " cannot be created " +
                                "because the component type " + commandParams[1] + " does not exist");
                    Component newComponent = new Component(componentType2);
                    components.put(commandParams[0], newComponent);
                    componentType2.addNewInstance(newComponent);
                    break;
                case "ApplyRule":
                    Component component = components.get(commandParams[0]);
                    System.out.println(component.applyRule(commandParams[1]));
                    break;
                case "AddNewProperty":
                    Component component1 = components.get(commandParams[0]);
                    if (component1 == null) {
                        throw new InvalidCommandException("The property " + commandParams[1] + " cannot be added to " +
                                commandParams[0] + " because the component does not exist");
                    }
                    PropertyType pType = propertyTypes.get(commandParams[1]);
                    if (pType == null) {
                        throw new InvalidCommandException("The property " + commandParams[1] + " cannot be added to "
                                + commandParams[0] + " because the property type does not exist");
                    }
                    Double propVal;
                    try {
                        propVal = Double.valueOf(commandParams[2]);
                    } catch (NumberFormatException e) {
                        throw new InvalidCommandException("The property " + commandParams[1] + " cannot be added to "
                                + commandParams[0] + " because the value of the property is not a double");
                    }
                    try {
                        component1.addProperty(pType, propVal);
                    } catch (PropertyTypeException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    break;
                default:
                    throw new InvalidCommandException("The command semantics on line " + line + " were not valid");
            }

    }

    public static void main(String[] args) {
        try {

            if(!args[0].contains(".dsl")){
                System.out.println("The file does not have a .dsl extension");
                System.exit(-2);
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            String line;
            int lineCount  = 1;
            while((line = buf.readLine()) != null){
                System.out.println(line);
                try{
                    Command.execute(line, lineCount);
                    lineCount++;
                }
                catch(InvalidCommandException e){
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Exception with file handling");;
        }
    }

}
