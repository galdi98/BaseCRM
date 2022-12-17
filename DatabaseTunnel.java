import com.jcraft.jsch.*;
import java.io.*;
import java.nio.file.*;

public class DatabaseTunnel implements AutoCloseable {

    private static final String DB_HOST = "cs.westminstercollege.edu";
    private static final int DB_SSH_PORT = 2322;                        // ssh port
    private static final String DB_SSH_USER = "student";                // ssh user
    private static final int DB_PORT = 3306;                            // Remote port (MySQL standard port number)

    // ssh fingerprint for database server
    private static final String KNOWN_HOST_ENTRY = "[cs.westminstercollege.edu]:2322 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDUK8Sdd11vwtHRf0VRFop4g5Mxirk710JtGsphi75TU6o/VJU3gsnHkZjpH0lx4XkRxOkNQ8+87MqkQ6erQac/UKBVRbwTS5CCdp45a7KYRg23by/fjDRwPXzGmP8Er+e5cZbcEQyevFyyAwM9b7OuAdy/XDzu7pOpnLxeBKiaq/EvGrqX2auJkTyZPVh8rfkV3sXXW9Bb/RXbFMb+pPEM4s9qJDibGNQiXJBRjGCedtzZ0AmSyMPxhXn9HLCPHcLN8bHySnr7omDYCtKVBxwOcGoASgUv+czh04dcBUr72Eq6JV7qoe7OVvRdF3qEgx6uH1yTYl9xaUrtqwKWGDZr";
    
    // Private RSA key used for the ssh connection
    private static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n"
        + "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\n"
        + "-----END RSA PRIVATE KEY-----\n\n";

    private Session jschSession;
    private int forwardedPort;
    
    public DatabaseTunnel() throws IOException {
        open();
    }
    
    private void open() throws IOException {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(getKeyfile());
            jsch.setKnownHosts(new ByteArrayInputStream(KNOWN_HOST_ENTRY.getBytes()));
            jschSession = jsch.getSession(DB_SSH_USER, DB_HOST, DB_SSH_PORT);
            jschSession.connect();
            forwardedPort = jschSession.setPortForwardingL(0, "localhost", DB_PORT);
        } catch (JSchException ex) {
            throw new IOException("Unable to open tunnel", ex);
        }
    }
    
    public int getForwardedPort() {
        return forwardedPort;
    }
    
    @Override
    public void close() {
        jschSession.disconnect();
    }
    
    // Creates a file containing the RSA key from PRIVATE_KEY if it doesn't exist. Returns the name of the file.
    private String getKeyfile() throws IOException {
        final String keyfileName = "id_rsa.cmpt307.tunnel";
        Path keyfilePath = Path.of(keyfileName);
        if (!Files.exists(keyfilePath))
            Files.writeString(keyfilePath, PRIVATE_KEY, java.nio.charset.StandardCharsets.UTF_8);
        return keyfileName;
    }
}
