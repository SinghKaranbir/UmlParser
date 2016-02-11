package com.spartan.karanbir;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

/**
 * Created by karanbir on 21/10/15.
 */
public class Parser{


    public static  List<String> classNames;
    public static List<String> interfaceNames;
    static List<HashMap<String,Object>> associationMapList;
    static List<HashMap<String,String>> extendMapList;
    static Set<HashMap<String,String>> usesInterfaceMapList;
    static List<HashMap<String,String>> implementInterfaceMapList;
    static  String className;
    static boolean isInterface;
    static boolean isAbstract  = false;
    static int classModifiers;
    static List<ClassOrInterfaceType> classExtendsList;
    static  List<ClassOrInterfaceType> classImplementsList;
    static List<String> fieldName;
    static List<Integer> fieldModifier;
    static List<String> fieldType;
    static List<String> methodNames;
    static List<Integer> methodModifiers;
    static List<String> methodTypeList;
    static  List<List<Parameter>> methodParametersList;
    static  List<String> constructorNames;
    static List<Integer> constructorModifiers;
    static List<List<Parameter>> constructorParametersList;
    static ArrayList<String> innerAttributeTypes ;
    static ArrayList<String> setGetterSetterNames;
    static ArrayList<String> setGetterSetterFieldNames;
    static ArrayList<Boolean> isGetter;
    static ArrayList<Boolean> isSetter;






    public Parser() {
        classNames = new ArrayList<>();
        interfaceNames = new ArrayList<>();
        associationMapList = new ArrayList<>();
        extendMapList = new ArrayList<>();
        usesInterfaceMapList = new LinkedHashSet<>();
        implementInterfaceMapList = new ArrayList<>();
        classExtendsList = new ArrayList<>();
        classImplementsList = new ArrayList<>();
        methodNames = new ArrayList<>();
        methodModifiers = new ArrayList<>();
        methodTypeList = new ArrayList<>();
        methodParametersList = new ArrayList<>();
        fieldName = new ArrayList<>();
        fieldModifier = new ArrayList<>();
        fieldType = new ArrayList<>();
        constructorNames = new ArrayList<>();
        constructorModifiers = new ArrayList<>();
        constructorParametersList = new ArrayList<>();
        innerAttributeTypes = new ArrayList<>();

        setGetterSetterNames = new ArrayList<>();
        setGetterSetterFieldNames = new ArrayList<>();
        isGetter = new ArrayList<>();
        isSetter = new ArrayList<>();

    }


    //1. Visit Class and Interface.
    public static class ClassVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            className = n.getName();
            isInterface = n.isInterface();
            classExtendsList = n.getExtends();
            classImplementsList = n.getImplements();
            classModifiers = n.getModifiers();
            if(ModifierSet.isAbstract(classModifiers)) {
                isAbstract = true;
            }

        }

    }

    //2. Visit Variables in the class

    public static class FieldVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(FieldDeclaration n, Object arg) {

            fieldType.add(n.getType().toString());
            String varName=n.getVariables().get(0).toString();
            if (varName.contains("=")) {
                varName=varName.substring(0, varName.indexOf("="));
            }
            fieldName.add(varName);
            fieldModifier.add(n.getModifiers());
        }
    }

    //3. visit method in the class
    public static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            methodModifiers.add(n.getModifiers());
            methodNames.add(n.getName());
            methodTypeList.add(n.getType().toString());
            methodParametersList.add(n.getParameters());



            // check if method is getter or setter
            if(n.getName().toUpperCase().contains("SET"))
            {

                for(String nameItem: fieldName) {
                    if(n.getName().toUpperCase().equals(("set" + nameItem).toUpperCase())) {
                        setGetterSetterNames.add(n.getName());
                        setGetterSetterFieldNames.add(nameItem);
                        isGetter.add(false);
                        isSetter.add(true);
                    }
                }

            }
            else if(n.getName().toUpperCase().contains("GET"))
            {

                for(String nameItem: fieldName) {
                    if(n.getName().toUpperCase().equals(("get"+nameItem).toUpperCase())) {
                        setGetterSetterNames.add(n.getName());
                        setGetterSetterFieldNames.add(nameItem);
                        isGetter.add(true);
                        isSetter.add(false);
                    }
                }
            }



        }

    }


    //3. visit attribute


    //4. visit constructor
    public static class ConstructorVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
            constructorModifiers.add(n.getModifiers());
            constructorNames.add(n.getName());
            constructorParametersList.add(n.getParameters());


        }
    }

    //5. visit inner attributes in methods
    public static class VariableDecVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(VariableDeclarationExpr n, Object arg) {
            innerAttributeTypes.add(n.getType().toString());
        }
    }

    public static void reset() {

        methodNames.clear();
        methodModifiers.clear();
        methodTypeList.clear();
        methodParametersList.clear();
        fieldName.clear();
        fieldModifier.clear();
        fieldType.clear();
        constructorNames.clear();
        constructorModifiers.clear();
        constructorParametersList.clear();
        innerAttributeTypes.clear();
        setGetterSetterNames.clear();
        setGetterSetterFieldNames.clear();
        isGetter.clear();
        isSetter.clear();



    }


    public static boolean isFieldHasGetterSetter (String fieldName) {
        boolean hasSetter=false;
        boolean hasGetter=false;
        for( int i =0; i< setGetterSetterFieldNames.size();i++) {

            if(setGetterSetterFieldNames.get(i).equals(fieldName)){
                if(isSetter.get(i)) hasSetter=true;
                if(isGetter.get(i)) hasGetter=true;
            }
        }
        if(hasSetter && hasGetter)
            return true;

        return false;
    }

    public static boolean isMethodGetterSetter (String methodName) {

        for(String methodItem: setGetterSetterNames){
            if(methodItem.equals(methodName)) {
                int index= setGetterSetterNames.indexOf(methodItem);
                if(isSetter.get(index)|| isGetter.get(index))
                    return true;
            }
        }

        return false;
    }



}
