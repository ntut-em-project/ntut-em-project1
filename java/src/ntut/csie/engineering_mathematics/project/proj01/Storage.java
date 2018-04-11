package ntut.csie.engineering_mathematics.project.proj01;

import ntut.csie.engineering_mathematics.project.proj01.config.App;
import ntut.csie.engineering_mathematics.project.proj01.config.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Storage {
    private static Connection _connection = null;

    public static Connection getConnection() {
        try {
            if (_connection != null && !_connection.isClosed()) {
                return _connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String connectString =
                    String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&characterEncoding=%s",
                            DB.getHost(), DB.getPort(), DB.getDbName(), App.ENCODING
                    );

            Class.forName("com.mysql.jdbc.Driver");
            _connection = DriverManager.getConnection(connectString, DB.getUser(), DB.getPass());

        } catch (SQLException | ClassNotFoundException e) {
            _connection = null;
            e.printStackTrace();
        }
        return _connection;
    }
}
