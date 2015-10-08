package mysql.hu.edu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlantDAO {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public PlantDAO() {
        super();
        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");

            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://isteiner-e90.c7qccqw6h0ez.us-east-1.rds.amazonaws.com:3306/PlantDB?" + "user=root&password=rootpassword");

            PreparedStatement preparedStatement = connect.prepareStatement("CREATE TABLE IF NOT EXISTS PlantDB.Flowers (id INTEGER PRIMARY KEY AUTO_INCREMENT,name VARCHAR(255),description VARCHAR(255),height DECIMAL(6,2))");

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeRow(String name, String description, Double height) throws Exception {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
                .getConnection("jdbc:mysql://isteiner-e90.c7qccqw6h0ez.us-east-1.rds.amazonaws.com:3306/PlantDB?" + "user=root&password=rootpassword");
        // Statements allow to issue SQL queries to the database
        PreparedStatement preparedStatement = connect.prepareStatement("insert into PlantDB.Flowers (name,description,height) VALUES(?,?,?)");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, height);
        preparedStatement.executeUpdate();
    }

    public void deleteRow(int id){
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://isteiner-e90.c7qccqw6h0ez.us-east-1.rds.amazonaws.com:3306/PlantDB?" + "user=root&password=rootpassword");

            PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM PlantDB.Flowers where id = ?");

            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://isteiner-e90.c7qccqw6h0ez.us-east-1.rds.amazonaws.com:3306/PlantDB?" + "user=root&password=rootpassword");
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from PlantDB.Flowers");
            writeResultSet(resultSet);
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.println(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i));
            }
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        }
    }
} 