package com.agrotech.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jdbc.JdbcComponent;
import org.apache.camel.support.SimpleRegistry;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConfig {
    public static DataSource createH2DataSource() {
        return new DataSource() {
            @Override public Connection getConnection() {
                try {
                    String url = "jdbc:h2:file:./database/agrotech;AUTO_SERVER=TRUE";
                    return DriverManager.getConnection(url, "sa", "");
                } catch (Exception e) { throw new RuntimeException(e); }
            }
            @Override public Connection getConnection(String username, String password) {
                try {
                    String url = "jdbc:h2:file:./database/agrotech;AUTO_SERVER=TRUE";
                    return DriverManager.getConnection(url, username, password);
                } catch (Exception e) { throw new RuntimeException(e); }
            }
            @Override public <T> T unwrap(Class<T> iface){throw new UnsupportedOperationException();}
            @Override public boolean isWrapperFor(Class<?> iface){return false;}
            @Override public java.io.PrintWriter getLogWriter(){return null;}
            @Override public void setLogWriter(java.io.PrintWriter out){}
            @Override public void setLoginTimeout(int seconds){}
            @Override public int getLoginTimeout(){return 0;}
            @Override public java.util.logging.Logger getParentLogger(){return java.util.logging.Logger.getGlobal();}
        };
    }

    public static void bindJdbc(CamelContext camel) throws Exception {
        var ds = createH2DataSource();
        var registry = (SimpleRegistry) camel.getRegistry();
        registry.bind("myDataSource", ds);
        JdbcComponent jdbc = new JdbcComponent();
        jdbc.setDataSource(ds);
        camel.addComponent("jdbc", jdbc);
        try (var conn = ds.getConnection(); var st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS lecturas (
                  id_sensor VARCHAR(10),
                  fecha VARCHAR(30),
                  humedad DOUBLE,
                  temperatura DOUBLE
                )
            """);
        }
    }
}
