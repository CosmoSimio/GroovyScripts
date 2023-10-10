import com.logicmonitor.common.sse.utils.GroovyScriptHelper
import com.logicmonitor.mod.Snippets

def host = hostProps.get("system.hostname")
def user = hostProps.get('vcenter.user') ?: hostProps.get("vcsa.user") ?: hostProps.get("esx.user")
def pass = hostProps.get('vcenter.pass') ?: hostProps.get("vcsa.pass") ?: hostProps.get("esx.pass")

try {
    // Establish a connection to the vCenter/ESXi host
    def url = new URL("https://${host}/") //edit endpoint to test and query
    def connection = url.openConnection()
    connection.setRequestMethod("GET")
    connection.setDoOutput(true)
    connection.setRequestProperty("Authorization", "Basic " + "${user}:${pass}".bytes.encodeBase64().toString())

    // Check the response code
    if (connection.getResponseCode() == 200) {
        println("Connection successful!")
    } else {
        println("Failed to connect. Response code: " + connection.getResponseCode())
    }
} catch (Exception e) {
    println("Error: " + e.getMessage())
}

return 0