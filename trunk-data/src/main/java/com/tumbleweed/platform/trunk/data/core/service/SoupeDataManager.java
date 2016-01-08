package com.tumbleweed.platform.trunk.data.core.service;

public interface SoupeDataManager {

    public void exportSchema(
            String outputFileAbsolutePath, String delimiter, boolean toConsole,
            boolean toDatabase, boolean justDrop, boolean justCreate);

}
