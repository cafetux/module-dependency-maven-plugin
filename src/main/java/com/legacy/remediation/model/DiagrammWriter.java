package com.legacy.remediation.model;

import com.legacy.remediation.model.module.Modules;

import java.io.File;

public interface DiagrammWriter {

    /**
     * generate string format of the modules dependencies and write it on file.
     * @param modules modules and dependencies
     * @param filename the name of the file to write (without extension)
     * @return the saved file
     */
    File write(Modules modules, String filename);

}
