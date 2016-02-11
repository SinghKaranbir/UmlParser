package com.spartan.karanbir;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.spartan.karanbir.util.FileGetter;
import java.io.*;
import java.util.HashMap;



public class Main {

    private static Parser parser = new Parser();
    private static ClassStringGenerator classStringGenerator = new ClassStringGenerator();
    public static void main(String[] args) throws IOException, ParseException {
        String dirName, outputFileName;

            dirName = args[0];
            outputFileName = args[1];
        //2. Get All Files using File Getter

        FileGetter getter = new FileGetter(dirName);

        for( File file: getter.getJavaFiles()){

            CompilationUnit unit = JavaParser.parse(file);
            new Parser.ClassVisitor().visit(unit, null);

            if(Parser.isInterface)
                Parser.interfaceNames.add(Parser.className);
            else
                Parser.classNames.add(Parser.className);


            if(Parser.classExtendsList !=null) {
                for (ClassOrInterfaceType item : Parser.classExtendsList)
                {
                    HashMap<String, String> extendItem = new HashMap<>();
                    extendItem.put("SuperClass",item.getName());
                    extendItem.put("SubClass", Parser.className);
                    Parser.extendMapList.add(extendItem);

                }
            }

            if(Parser.classImplementsList !=null) {
                for (ClassOrInterfaceType item : Parser.classImplementsList)
                {
                    HashMap<String,String> implementItem = new HashMap<>();
                    implementItem.put("ImplementName",Parser.className);
                    implementItem.put("InterfaceName",item.getName());
                    Parser.implementInterfaceMapList.add(implementItem);
                }
            }

        }



        //2.create class UML string for each .java file
        for( File file: getter.getJavaFiles()){

            CompilationUnit unit = JavaParser.parse(file);
            new Parser.ClassVisitor().visit(unit, null);
            new Parser.FieldVisitor().visit(unit, null);
            new Parser.MethodVisitor().visit(unit, null);
            new Parser.ConstructorVisitor().visit(unit, null);
            new Parser.VariableDecVisitor().visit(unit, null);

            //a. create UML string for both interface and normal class
            classStringGenerator.generate();
            Parser.reset();
        }

        //4.create extend relation string between java classes
        ExtendStringGenerator extendStringGenerator = new ExtendStringGenerator(Parser.extendMapList);
        extendStringGenerator.generate();
        //5.Create Interface String
        InterfaceStringGenerator interfaceStringGenerator = new InterfaceStringGenerator(Parser.implementInterfaceMapList,Parser.usesInterfaceMapList);
        interfaceStringGenerator.generate();

        //3.create association  string for java classes
        AssociationStringGenerator associationStringGenerator = new AssociationStringGenerator(Parser.associationMapList);
        associationStringGenerator.generate();


        // Send the Strings to Uml generator
        try {
            PlantUmlGenerator.plantUmlGenerator(outputFileName,
                                                classStringGenerator.getStringList(),
                                                associationStringGenerator.getStringList(),
                                                extendStringGenerator.getStringList(),
                                                interfaceStringGenerator.getStringList());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
