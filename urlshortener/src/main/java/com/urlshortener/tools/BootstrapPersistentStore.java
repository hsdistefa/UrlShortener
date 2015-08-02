package com.urlshortener.tools;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.DataAccess;


/**
 * TODO: description
 */
public class BootstrapPersistentStore {
    public static void main(String[] args) {
        assert args != null;
        assert args.length == 1;

        // get stage
        String stageArg = args[0];
        Stage stage = null;
        try {
            stage = Stage.valueOf(stageArg);
        } catch (IllegalArgumentException e) {
            System.err.println("bad stage: " + stage);
            System.exit(1);
        }
        System.out.println("stage found: " + stage);

        // bootstrap
        DataAccess dataAccess = new DataAccess(new Config(), stage);
        try {
            dataAccess.bootstrapPersistentStore();
        } catch (Exception e) {
            System.out.println("Could not bootstrap persistent store");
            throw e;
        } finally {
            dataAccess.close();
        }
    }
}
