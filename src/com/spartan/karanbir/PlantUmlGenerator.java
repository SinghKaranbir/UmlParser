package com.spartan.karanbir;

import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;


/**
 * Created by karanbir on 21/10/15.
 */
public class PlantUmlGenerator {


    public static void plantUmlGenerator(String outputFileName, Collection<String> classString, Collection<String> associationString, Collection<String> extendString, Collection<String> interfaceString) throws Exception {
        OutputStream png = new FileOutputStream(outputFileName);
        StringBuilder plantUmlInput = new StringBuilder();
        plantUmlInput.append("@startuml\n")
                .append("title UML PARSER SJSU ID 010276305\n")
                .append("skinparam classAttributeIconSize 0\n")
                .append("skinparam classArrowColor #ee130e\n")
                .append("skinparam BackgroundColor #f2a553\n")
                .append("skinparam classBackgroundColor #feffd3\n")
                .append("skinparam classBorderColor Brown\n")
                .append("skinparam usecaseFontSize 1\n")
                .append("skinparam classFontColor Black\n");


        for(String item:classString)
        {
            plantUmlInput.append(item);

        }

        for(String item:associationString)
        {
            plantUmlInput.append(item);

        }

        for(String item:extendString)
        {
            plantUmlInput.append(item);

        }

        for(String item: interfaceString)
        {
            plantUmlInput.append(item);

        }

        plantUmlInput.append("@enduml\n");



         System.out.print(plantUmlInput.toString());




        SourceStringReader reader = new SourceStringReader(plantUmlInput.toString());

        reader.generateImage(png);

    }

}
