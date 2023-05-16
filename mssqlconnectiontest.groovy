import java.sql.*;

def host = hostProps.get("system.hostname")
def user = hostProps.get("jdbc.mssql.user")
def pass = hostProps.get("jdbc.mssql.pass")

// Check for the presence of auto.mssqlserver.mssql_url
def url = hostProps.get("auto.mssqlserver.mssql_url")
if (url == null || url.isEmpty()) {
    // Fallback to mssql.mssqlserver.mssql_url
    url = hostProps.get("mssql.mssqlserver.mssql_url")
    println("Property auto.mssqlserver.mssql_url is null. Falling back to mssql.mssqlserver.mssql_url")
}

if (url == null || url.isEmpty()) {
    println("No mssql.mssqlserver.mssql_url property found.")
    return
}

def query = "SELECT name FROM sys.databases"

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
    println("Successfully connected to the MSSQL database!")
    println("Connection URL: " + url)

} catch (SQLException e) {
    println("Error connecting to the MSSQL database: " + e.getMessage())
    println("SQL state: " + e.getSQLState())
}