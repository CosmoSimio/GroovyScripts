import com.vmware.vim25.mo.*
import com.vmware.vim25.*

def host = hostProps.get("system.hostname")
def user = hostProps.get("esx.user")
def pass = hostProps.get("esx.pass")
def url = hostProps.get("vcenter.url") ?: "https://${host}/sdk"

ServiceInstance si = null

try {
    si = new ServiceInstance(new URL(url), user, pass, true)
    Folder rootFolder = si.getRootFolder()
    ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem")
    
    if (mes == null || mes.length == 0) {
        println("No ESXi hosts found.")
        return
    }

    for (int i = 0; i < mes.length; i++) {
        HostSystem host = (HostSystem) mes[i]
        println("ESXi Host Name: " + host.getName())
    }

    println("Successfully connected to the ESXi host!")
    println("Connection URL: " + url)

} catch (Exception e) {
    println("Error connecting to the ESXi host: " + e.getMessage())
} finally {
    if (si != null) {
        si.getServerConnection().logout()
    }
}