package com.spartan.karanbir;

import com.github.javaparser.ast.body.ModifierSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.body.Parameter;

/**
 * Created by karanbir on 27/10/15.
 */
public class ClassStringGenerator extends StringGenerator{

    private StringBuilder classStringBuilder;
    private ArrayList<String> classStringList;


    public ClassStringGenerator(){
        classStringBuilder = new StringBuilder();
        classStringList = new ArrayList<>();

    }

    @Override
    public void generate() {





        if(Parser.isInterface){
            classStringBuilder.append("interface ")
                    .append(Parser.className)
                    .append(" {\n");


        }
        else {
            if(ModifierSet.isAbstract(Parser.classModifiers)) {
                classStringBuilder.append("abstract class ")
                        .append(Parser.className)
                        .append(" {\n");


            }
            else {

                classStringBuilder.append("class ")
                        .append(Parser.className)
                        .append(" {\n");


            }
        }

        //A. Making UML FIELD string
        for (String field: Parser.fieldName)
        {
            //1. create field string of class UML
            int index = Parser.fieldName.indexOf(field);

            // if field has associations with other classes, then it will not be printed in the class UML, but put into associationItemMap
            String checkAssociation="";
            if (Parser.fieldType.get(index).indexOf('[')>=0) {
                checkAssociation += Parser.fieldType.get(index).substring(0,Parser.fieldType.get(index).indexOf('['));
            }
            else if(Parser.fieldType.get(index).contains("Collection") || Parser.fieldType.get(index).contains("List") || Parser.fieldType.get(index).contains("Map") || Parser.fieldType.get(index).contains("Set")){
                checkAssociation += Parser.fieldType.get(index).substring(Parser.fieldType.get(index).indexOf('<')+1,Parser.fieldType.get(index).indexOf('>'));
            }

            if(Parser.classNames.indexOf(Parser.fieldType.get(index))>=0 || Parser.classNames.indexOf(checkAssociation)>=0
                    ||Parser.interfaceNames.indexOf(Parser.fieldType.get(index))>=0 || Parser.interfaceNames.indexOf(checkAssociation)>=0){

                HashMap<String, Object> associationMap = new HashMap<>();
                associationMap.put("StartName",Parser.className);


                if(checkAssociation!="") {

                    associationMap.put("EndName", checkAssociation);
                }
                else {

                    associationMap.put("EndName",Parser.fieldType.get(index));
                }
                associationMap.put("AttributeName",field);


                if(checkAssociation!="") {
                    associationMap.put("isMultiple", true);

                }
                else {
                    associationMap.put("isMultiple",false);

                }
                System.out.println(associationMap.toString());
                Parser.associationMapList.add(associationMap);


            }
            else{

                StringBuilder typeStringBuilder = new StringBuilder();

                if (Parser.fieldType.get(index).indexOf('[')>=0) {

                    typeStringBuilder.append(Parser.fieldType.get(index).substring(0, Parser.fieldType.get(index).indexOf('[')))
                            .append("(*)");


                }
                else if(Parser.fieldType.get(index).contains("Collection") || Parser.fieldType.get(index).contains("List") || Parser.fieldType.get(index).contains("Map") || Parser.fieldType.get(index).contains("Set")){

                    typeStringBuilder.append(Parser.fieldType.get(index).substring(Parser.fieldType.get(index).indexOf('<')+1, Parser.fieldType.get(index).indexOf('>')))
                            .append("(*)");

                }
                else {

                    typeStringBuilder.append(Parser.fieldType.get(index));

                }

                if (ModifierSet.isPublic(Parser.fieldModifier.get(index))){

                    classStringBuilder.append("+")
                            .append(field)
                            .append(":")
                            .append(typeStringBuilder.toString())
                            .append("\n");


                }
                else if(Parser.isFieldHasGetterSetter(field)) {
                    classStringBuilder.append("+")
                            .append(field)
                            .append(":")
                            .append(typeStringBuilder.toString())
                            .append("\n");


                }
                else if (ModifierSet.isPrivate(Parser.fieldModifier.get(index))) {
                    classStringBuilder.append("-")
                            .append(field)
                            .append(":")
                            .append(typeStringBuilder.toString())
                            .append("\n");



                }
            }

        }
        classStringBuilder.append("__\n");

        //B. making constructor UML String
        for(String methodName: Parser.constructorNames) {
            int index = Parser.constructorNames.indexOf(methodName);
            if (ModifierSet.isPublic(Parser.constructorModifiers.get(index))) {

                StringBuilder paramStringBuilder = new StringBuilder();

                for (Parameter parameterSingle : Parser.constructorParametersList.get(index)) {
                    String[] parts = parameterSingle.toString().split(" ");
                    paramStringBuilder.append(parts[1])
                            .append(":")
                            .append(parameterSingle.getType());


                    if (Parser.constructorParametersList.get(index).indexOf(parameterSingle) + 1 != Parser.constructorParametersList.get(index).size())
                        paramStringBuilder.append(",");
                }
                classStringBuilder.append("+")
                        .append(methodName)
                        .append("(")
                        .append(paramStringBuilder.toString())
                        .append(")")
                        .append("\n");


            }


            //find if any use of interface in parameters, save to useInterfaceList
            for (Parameter parameterSingle : Parser.constructorParametersList.get(index)) {
                String CheckIterfaceUse = "";
                String paramtertype = parameterSingle.getType().toString();

                if (paramtertype.indexOf('[') >= 0) {
                    CheckIterfaceUse += paramtertype.substring(0, paramtertype.indexOf('['));
                } else if (paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") || paramtertype.contains("Set")) {
                    CheckIterfaceUse += paramtertype.substring(paramtertype.indexOf('<') + 1, paramtertype.indexOf('>'));
                } else
                    CheckIterfaceUse += paramtertype;


                for (String interfaceName : Parser.interfaceNames) {

                    if (interfaceName.equals(CheckIterfaceUse)) {
                        HashMap<String, String> usesInterfaceItem = new HashMap<>();
                        usesInterfaceItem.put("InterfaceName", interfaceName);
                        usesInterfaceItem.put("UseName", Parser.className);

                        //if use is a class, added to useInterfaceList, ignore used by a interface
                        if (Parser.classNames.contains(Parser.className))
                            Parser.usesInterfaceMapList.add(usesInterfaceItem);
                    }
                }
            }
        }
        //C. making method UML String
        for(String methodName: Parser.methodNames)
        {
            int index = Parser.methodNames.indexOf(methodName);
            if((ModifierSet.isPublic(Parser.methodModifiers.get(index)) || Parser.interfaceNames.contains(Parser.className))
                    && !Parser.isMethodGetterSetter(methodName)) {
                String parameterStr="";

                for(Parameter parameterSingle: Parser.methodParametersList.get(index)) {
                    String[] parts=parameterSingle.toString().split(" ");
                    parameterStr += parts[1] + ":"+parameterSingle.getType();
                    if(Parser.methodParametersList.get(index).indexOf(parameterSingle)+1!= Parser.methodParametersList.get(index).size())
                        parameterStr +=",";
                }
                classStringBuilder.append("+")
                        .append(methodName)
                        .append("(")
                        .append(parameterStr)
                        .append("):")
                        .append(Parser.methodTypeList.get(index))
                        .append("\n");

            }


            //find if any use of interface in parameters, save to useInterfaceList
            for(Parameter parameterSingle: Parser.methodParametersList.get(index)) {
                String checkInterfaceUse="";
                String paramtertype = parameterSingle.getType().toString();

                if(paramtertype.indexOf('[')>=0) {
                    checkInterfaceUse += paramtertype.substring(0, paramtertype.indexOf('['));
                }
                else if(paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") ||paramtertype.contains("Set") ) {
                    checkInterfaceUse += paramtertype.substring(paramtertype.indexOf('<')+1,paramtertype.indexOf('>'));
                }
                else
                    checkInterfaceUse +=paramtertype;


                for(String interfaceName: Parser.interfaceNames) {
                    if (interfaceName.equals(checkInterfaceUse)) {

                        HashMap<String, String> usesInterfaceItem = new HashMap<>();
                        usesInterfaceItem.put("InterfaceName", interfaceName);
                        usesInterfaceItem.put("UseName", Parser.className);


                        //if use is a class, added to useInterfaceList, ignore used by a interface
                        if(Parser.classNames.contains(Parser.className))
                            Parser.usesInterfaceMapList.add(usesInterfaceItem);
                    }
                }
            }


            //find if any use of interface in return type, save to useInterfaceList
            String checkInterfaceUse="";
            String returntype= Parser.methodTypeList.get(index);
            if(returntype.indexOf('[')>=0) {
                checkInterfaceUse += returntype.substring(0, returntype.indexOf('['));
            }
            else if(returntype.contains("Collection") || returntype.contains("List") || returntype.contains("Map") ||returntype.contains("Set") ) {
                checkInterfaceUse += returntype.substring(returntype.indexOf('<')+1,returntype.indexOf('>'));
            }
            else
                checkInterfaceUse +=returntype;

            for(String interfaceName:Parser.interfaceNames) {
                if (interfaceName.equals(checkInterfaceUse)) {

                    HashMap<String, String> usesInterfaceItem = new HashMap<>();
                    usesInterfaceItem.put("InterfaceName", interfaceName);
                    usesInterfaceItem.put("UseName", Parser.className);

                    //if use is a class, added to useInterfaceList, ignore use by a interface
                    if(Parser.classNames.contains(Parser.className))
                        Parser.usesInterfaceMapList.add(usesInterfaceItem);
                }
            }

        }

        classStringBuilder.append("}\n");

        //D. find if any use of interface inside a method
        for(String innervarType: Parser.innerAttributeTypes){
            for(String interfaceName:Parser.interfaceNames) {
                if (interfaceName.equals(innervarType)) {
                    HashMap<String, String> usesInterfaceItem = new HashMap<>();
                    usesInterfaceItem.put("InterfaceName", interfaceName);
                    usesInterfaceItem.put("UseName", Parser.className);

                    //if use is a class, added to useInterfaceList, ignore use by a interface
                    if(Parser.classNames.contains(Parser.className))
                        Parser.usesInterfaceMapList.add(usesInterfaceItem);

                }
            }
        }

    }
    @Override
    public List<String> getStringList(){
        classStringList.add(classStringBuilder.toString());
        return classStringList;
    }


}

