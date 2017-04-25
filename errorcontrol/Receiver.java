
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable {

    protected Logger logger;
    protected int port;
    protected NetworkPacket packet;
    protected byte[] buffer;
    protected String saveFolder;

    protected ServerSocket reciever;
    protected Socket client = null;
    protected OutputStream os;
    protected InputStream in;
    protected FileOutputStream fos;

    public Receiver(Logger logger, int port, String savePath) {
        this.logger = logger;
        this.port = port;
        this.saveFolder = savePath;
        packet = new NetworkPacket();
        buffer = new byte[NetworkPacket.PACKET_SIZE];
    }

    public void run() {
        // TODO Auto-generated method stub		
    }
}
