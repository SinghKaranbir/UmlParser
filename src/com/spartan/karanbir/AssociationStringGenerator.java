package com.spartan.karanbir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karanbir on 22/10/15.
 */
public class AssociationStringGenerator extends StringGenerator {

    private StringBuilder associationStringBuilder;
    private List<HashMap<String,Object>> associationMapList;
    private ArrayList associationStringList;

    public AssociationStringGenerator(List<HashMap<String,Object>> associationMapList) {

        associationStringBuilder = new StringBuilder();
        this.associationMapList = associationMapList;
        associationStringList = new ArrayList();
    }

    @Override
    public void generate() {

        while(!associationMapList.isEmpty()){
            String startClass = (String)associationMapList.get(0).get("StartName");
            String endClass = (String)associationMapList.get(0).get("EndName");

            int i=0;
            for( ; i<associationMapList.size(); i++) {
                if(associationMapList.get(i).get("StartName").equals(endClass) && associationMapList.get(i).get("EndName").equals(startClass)) {
                    break;
                }
            }
            if(i<associationMapList.size()) {
                if((boolean)associationMapList.get(0).get("isMultiple") && (boolean)associationMapList.get(i).get("isMultiple")) {
                    associationStringBuilder.append(startClass)
                            .append(" \"*\"")
                            .append("--")
                            .append("\"*\" ")
                            .append(endClass)
                            .append("\n");

                }
                else if((boolean)associationMapList.get(0).get("isMultiple")) {
                    associationStringBuilder.append(startClass)
                            .append(" \"1\"")
                            .append("--")
                            .append("\"*\" ")
                            .append(endClass)
                            .append("\n");

                }
                else if((boolean)associationMapList.get(i).get("isMultiple")) {
                    associationStringBuilder.append(startClass)
                            .append(" \"*\"")
                            .append("--")
                            .append("\"1\" ")
                            .append(endClass)
                            .append("\n");
                }
                else {
                    associationStringBuilder.append(startClass)
                            .append(" \"1\"")
                            .append("--")
                            .append("\"1\" ")
                            .append(endClass)
                            .append("\n");

                }
                associationMapList.remove(i);
                associationMapList.remove(0);
            }
            else {
                if((boolean)associationMapList.get(0).get("isMultiple")) {
                    if(((String)associationMapList.get(0).get("EndName")).toUpperCase().equals(((String)associationMapList.get(0).get("AttributeName")).toUpperCase())){
                        associationStringBuilder.append(startClass)
                                .append("--")
                                .append("\"*\" ")
                                .append(endClass)
                                .append("\n");

                    }
                    else {
                        associationStringBuilder.append(startClass)
                                .append("--")
                                .append("\"*\" ")
                                .append(endClass)
                                .append("\n");
                    }

                }
                else {
                    associationStringBuilder.append(startClass)
                            .append("--")
                            .append("\"1\" ")
                            .append(endClass)
                            .append("\n");
                }
                associationMapList.remove(0);
            }


        }
        associationStringList.add(associationStringBuilder.toString());


    }
    @Override
    public List<String> getStringList() {
         return associationStringList;
    }





}




