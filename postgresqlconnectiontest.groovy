/*This script will test a JDBC connection against a remote PostgreSQL server*/
import java.sql.*;

def host = hostProps.get("system.hostname")
def user = hostProps.get("jdbc.postgres.user")
def pass = hostProps.get("jdbc.postgres.pass")
def database = hostProps.get("dbname")

String url = "jdbc:postgresql://" + host + ":5432/" + database
String query = "select dataname from pg_database where datallowconn = true"

try {
    Connection conn = DriverManager.getConnection(url, user, pass)
    Statement stmt = conn.createStatement()
    ResultSet rs = stmt.executeQuery(query)
    while (rs.next()) {
        println(rs.getString(1))
    }
    rs.close()
    stmt.close()
    conn.close()
    println("Successfully connected to the PostgreSQL database!")
    println("Connection URL: " + url)

} catch (SQLException e) {
    println("Error connecting to the PostgreSQL database: " + e.getMessage())
    println("SQL state: " + e.getSQLState())
}