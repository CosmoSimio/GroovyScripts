import java.sql.*

def host = hostProps.get("system.hostname")
def user = hostProps.get("jdbc.mysql.user")
def pass = hostProps.get("jdbc.mysql.pass")
def dbName = hostProps.get("dbname") ?: ""

// Construct the MySQL connection URL with SSL enabled, public key retrieval allowed, and server certificate verification disabled
def url = "jdbc:mysql://" + host + ":3306/" + dbName + "?useSSL=true&allowPublicKeyRetrieval=true&verifyServerCertificate=false"

if (url == null || url.isEmpty()) {
    println("No valid MySQL connection URL found.")
    return
}

def query = "SHOW DATABASES"

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
    println("Successfully connected to the MySQL database!")
    println("Connection URL: " + url)

} catch (SQLException e) {
    println("Error connecting to the MySQL database: " + e.getMessage())
    println("SQL state: " + e.getSQLState())
}