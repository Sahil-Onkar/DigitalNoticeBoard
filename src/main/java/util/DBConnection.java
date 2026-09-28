package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties props = new Properties();

    static {
        boolean loaded = false;

        // 1. Try the classpath (works after Maven copies resources)
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
                loaded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Fall back to reading the file straight from disk
        if (!loaded) {
            String[] paths = {
                    "src/main/resources/db.properties",
                    "db.properties"
            };
            for (String path : paths) {
                File file = new File(path);
                if (file.exists()) {
                    try (InputStream in = new FileInputStream(file)) {
                        props.load(in);
                        loaded = true;
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!loaded) {
            System.err.println("db.properties NOT FOUND. Working directory: "
                    + new File(".").getAbsolutePath());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");

        if (url == null) {
            throw new SQLException(
                    "db.url missing. Check db.properties exists and has "
                            + "db.url, db.user and db.password.");
        }

        return DriverManager.getConnection(
                url,
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}