package io.tlf.sqltest5;

import com.impossibl.postgres.jdbc.PGDataSource;
import com.impossibl.postgres.jdbc.PGDriver;
import com.impossibl.postgres.tools.UDTGenerator;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tlfal
 */
public class Main {

    private static PGDataSource ds = new PGDataSource();

    public static void main(String[] args) throws SQLException, InterruptedException {
        System.out.println("**** Running Test5");
        test4();
    }

    public static void test4() throws SQLException, InterruptedException {

        //Create DB connection
        try {
            Class.forName("com.impossibl.postgres.jdbc.PGConnectionPoolDataSource");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        System.err.println("Connecting to db...");
        Connection con = null;
        try {
            ds.setServerName("localhost");
            ds.setPort(5432);
            ds.setDatabaseName("test5");
            ds.setUser("test5");
            ds.setPassword("test5");
            con = ds.getConnection();
            //Create mappings
            Map map = con.getTypeMap();
            map.put("TEST_OBJ", TestObj.class);
            con.setTypeMap(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (con == null) {
            System.err.println("Connection null");
            return;
        }

        try {
            System.err.println("Reading test data");
            PreparedStatement pstmt1 = con.prepareStatement("SELECT * FROM data LIMIT 1");
            ResultSet result = pstmt1.executeQuery();
            TestObj data = null;
            if (result.next()) {
                data = result.getObject("obj", TestObj.class);
                result.close();
            }
            pstmt1.close();
            if (data == null) {
                System.err.println("DID NOT GET DATA");
                //return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            TestObj data = new TestObj();
            data.setStrings(null);
            System.err.println("Writing test data: null");
            PreparedStatement pstmt2 = con.prepareStatement("UPDATE data SET obj=?");
            pstmt2.setObject(1, data);
            pstmt2.executeUpdate();
            pstmt2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            TestObj data = new TestObj();
            data.setStrings(new String[] {"STR5", "STR6", "STR7"});
            System.err.println("Writing test data: string[]");
            PreparedStatement pstmt2 = con.prepareStatement("UPDATE data SET obj=?");
            pstmt2.setObject(1, data);
            pstmt2.executeUpdate();
            pstmt2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Build udt
        /*
        System.err.println("Generating UDTs");
        try {
            executeGenerator(con, Arrays.asList(new String[]{"TEST_OBJ"}));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         */

        //Done
        System.err.println("Done");
        con.close();
        PGDriver.cleanup();
    }

    public static void executeGenerator(Connection connection, List<String> typeNames) {
        new UDTGenerator(connection, "io.tlf.sqltest5", typeNames).generate(new File("src/main/java"));
    }
}
