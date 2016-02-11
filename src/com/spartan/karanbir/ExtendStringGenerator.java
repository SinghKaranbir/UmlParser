package com.spartan.karanbir;

/**
 * Created by karanbir on 22/10/15.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtendStringGenerator extends StringGenerator {

    private StringBuilder extendStringBuilder = new StringBuilder();
    private List<String> extendStringList;
    private List<HashMap<String,String>> extendItemMap;

    public ExtendStringGenerator(List<HashMap<String,String>> extendItemMap){

        this.extendItemMap = extendItemMap;
        extendStringList = new ArrayList<>();
    }

    @Override
    public void generate() {

        for(HashMap<String,String> extendItem : extendItemMap){
            extendStringBuilder.append(extendItem.get("SuperClass"));
            extendStringBuilder.append("<|--");
            extendStringBuilder.append(extendItem.get("SubClass"));
            extendStringBuilder.append("\n");
        }
    }

    @Override
    public List<String> getStringList() {
        extendStringList.add(extendStringBuilder.toString());
        return extendStringList;
    }
}
