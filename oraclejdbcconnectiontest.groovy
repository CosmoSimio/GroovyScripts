/*This script will test a JDBC connection against a remote Oracle server and list PDBs if connected to a CDB*/
import java.sql.*

def host = hostProps.get("system.hostname")
def port = hostProps.get("jdbc.oracle.port") ?: "1521" // Use the provided port or default to 1521
def user = hostProps.get("jdbc.oracle.user")
def pass = hostProps.get("jdbc.oracle.pass")
def url = hostProps.get("oracle.dbname.url") // Fetch the connection string directly

if (url == null || url.isEmpty()) {
    url = "jdbc:oracle:thin:@" + host + ":" + port // Default connection string if not provided
}

try {
    Connection conn = DriverManager.getConnection(url, user, pass)
    Statement stmt = conn.createStatement()

    // Check if the current database is a CDB
    ResultSet rsCDB = stmt.executeQuery("SELECT CDB FROM V$DATABASE")
    String isCDB = ""
    if (rsCDB.next()) {
        isCDB = rsCDB.getString("CDB")
    }

    // Get the name of the current PDB or CDB
    ResultSet rsCurrent = stmt.executeQuery("SELECT SERVICE_NAME FROM V$ACTIVE_SERVICES WHERE UPPER(NAME) = 'SYS$BACKGROUND'")
    String currentPDBorCDB = ""
    if (rsCurrent.next()) {
        currentPDBorCDB = rsCurrent.getString("SERVICE_NAME")
    }

    if ("YES".equalsIgnoreCase(isCDB)) {
        println("The current database is a Container Database (CDB).")
        
        // List all PDBs
        ResultSet rsPDBs = stmt.executeQuery("SELECT NAME FROM V$PDBS")
        println("List of Pluggable Databases (PDBs):")
        while (rsPDBs.next()) {
            println(rsPDBs.getString("NAME"))
        }
        rsPDBs.close()
    } else {
        println("The current database is " + currentPDBorCDB + " and it is not a Container Database (CDB).")
    }

    rsCDB.close()
    rsCurrent.close()
    stmt.close()
    conn.close()
    println("Successfully connected to the Oracle database!")
    println("Connection URL: " + url)

} catch (SQLException e) {
    println("Error connecting to the Oracle database: " + e.getMessage())
    println("SQL state: " + e.getSQLState())
}