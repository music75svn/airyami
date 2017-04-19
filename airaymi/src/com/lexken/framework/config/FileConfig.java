package com.lexken.framework.config;

import java.io.IOException;

// Referenced classes of package com.newmulticampus.front.common.config:
//            ConfigProperties

public class FileConfig extends ConfigProperties
{

    public FileConfig()
    {
        setProperties();
    }

    public static FileConfig getInstance()
    {
        return fileInstance;
    }

    public static void reset()
        throws IOException
    {
        fileInstance = new FileConfig();
    }

    public static void loadProperties()
    {
        setProperties();
    }

    private static void setProperties()
    {
        String fs = System.getProperty("file.separator");
        boolean loaded = false;
        java.io.InputStream props = com.lexken.framework.config.FileConfig.class.getResourceAsStream("file.properties");
        if(props != null)
            try
            {
                properties.load(props);
                WEB_ROOT_PATH = properties.getProperty("WEB_ROOT_PATH");
                loaded = true;
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        if(!loaded)
            System.err.println("kr.co.lexken.FileConfig Load Error!");
    }

    public static String WEB_ROOT_PATH = "";
    private static final String CONFIG_FILE_NAME = "file.properties";
    private static FileConfig fileInstance = new FileConfig();
}
