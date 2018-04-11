package ntut.csie.engineering_mathematics.project.proj01.config;


import java.io.InputStreamReader;
import java.util.Properties;

public class DB {

    private static final String CONFIGURE_FILE = "db.properties";

    private static String HOST = "localhost";
    private static String PORT = "3306";
    private static String USER = "root";
    private static String PASS = "";
    private static String DBNAME = "db_name";

    private static Boolean initial = false;

    public static final String getHost() {
        initialize();
        return HOST;
    }

    public static final String getPort() {
        initialize();
        return PORT;
    }

    public static final String getUser() {
        initialize();
        return USER;
    }

    public static final String getPass() {
        initialize();
        return PASS;
    }

    public static final String getDbName() {
        initialize();
        return DBNAME;
    }

    private static void initialize() {
        if (!initial) {
            Properties p = new Properties();
            try {
                p.load(new InputStreamReader(DB.class.getResourceAsStream(CONFIGURE_FILE), App.ENCODING));

                HOST = p.getProperty("DB_HOST", HOST);
                PORT = p.getProperty("DB_PORT", PORT);
                USER = p.getProperty("DB_USER", USER);
                PASS = p.getProperty("DB_PASS", PASS);
                DBNAME = p.getProperty("DB_NAME", DBNAME);

                initial = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
