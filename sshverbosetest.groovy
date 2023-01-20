import com.jcraft.jsch.*
import java.io.*

def sshUsername = "username"
def sshPassword = "password"
def remoteHost = "remotehost"

def jsch = new JSch()

def session = jsch.getSession(sshUsername, remoteHost)
session.setPassword(sshPassword)
session.setConfig("StrictHostKeyChecking", "no")
session.setConfig("PreferredAuthentications", "password")
session.setConfig("LogLevel", "VERBOSE")
session.setConfig("compression.s2c", "zlib,none")
session.setConfig("compression.c2s", "zlib,none")

// create a redirecting output stream
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
PrintStream printStream = new PrintStream(byteArrayOutputStream)

// redirect the log output to the stream
session.setOutputStream(printStream)

session.connect()

if (session.isConnected()) {
    println "SSH login successful!"
} else {
    println "SSH login failed."
}

// print the log
println byteArrayOutputStream.toString()

session.disconnect()
println "SSH disconnected"