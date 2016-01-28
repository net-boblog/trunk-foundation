package com.tumbleweed.platform.trunk.base.constants;

import java.io.File;

/**
 * Created by mylover on 1/22/16.
 */
public interface Constants {

    /**
     *  Script目录
     */
    String SCRIPT_DIR_MATCH = File.separator + "script" + File.separator;


    public static final String SCRIPT_DIR_NAME = "scriptDirName";
    public static final String LOCAL_DIR_NAME = "localDirName";
    public static final String ERROR_FILE_NAME = "ErrorCode";
    public static final String GENERAL_FILE_NAME = "GeneralConfig";
    public static final String UNCHECKRIGHT_NAME = "UnCheckRight";
    public static final String MDN_BLACK_LIST_NAME = "NumberBlackList";
    public static final String ERROR_REFLECT_FILE_NAME = "ErrorCodeReflect";

    public static final String SERVER_SERIAL = "SERVER_SERIAL";
}
