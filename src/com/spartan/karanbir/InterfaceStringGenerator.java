package com.spartan.karanbir;



/**
 * Created by karanbir on 22/10/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class InterfaceStringGenerator extends StringGenerator {

    private  StringBuilder interfaceStringBuilder;
    private List<HashMap<String,String>> implementInterfaceItemMapList;
    private List<String> interfaceStringList;
    private Set<HashMap<String,String>> usesInterfaceMapList;
    public InterfaceStringGenerator(List<HashMap<String,String>> implementInterfaceItemMapList, Set<HashMap<String,String>> usesInterfaceMapList){

        interfaceStringBuilder = new StringBuilder();
        interfaceStringList = new ArrayList<>();
        this.implementInterfaceItemMapList = implementInterfaceItemMapList;
        this.usesInterfaceMapList = usesInterfaceMapList;

    }

    @Override
    public void generate() {

        for(HashMap<String,String> implementInterfaceItem : implementInterfaceItemMapList ) {

            interfaceStringBuilder.append(implementInterfaceItem.get("InterfaceName"));
            interfaceStringBuilder.append("<|..");
            interfaceStringBuilder.append(implementInterfaceItem.get("ImplementName"));
            interfaceStringBuilder.append("\n");


        }

        for(HashMap<String,String> useInterfaceItem : usesInterfaceMapList) {

            interfaceStringBuilder.append(useInterfaceItem.get("UseName"));
            interfaceStringBuilder.append("..>");
            interfaceStringBuilder.append(useInterfaceItem.get("InterfaceName"));
            interfaceStringBuilder.append(": use\n");

        }

    }

    @Override
    public List<String> getStringList() {

        interfaceStringList.add(interfaceStringBuilder.toString());
        return interfaceStringList;
    }
}
