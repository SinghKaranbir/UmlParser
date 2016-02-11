package com.spartan.karanbir.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by karanbir on 20/10/15.
 */
public  class FileGetter {

    private String dirName;

    public FileGetter(String dirName){
        this.dirName = dirName;
    }

    public File[] getJavaFiles(){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".java");
            }
        });


    }
}
