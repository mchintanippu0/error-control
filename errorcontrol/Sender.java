
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Sender implements Runnable {

    protected final static int TIMEOUT = 5000;

    protected String server;
    protected int port;
    protected int lostPercent;
    protected String filePath;
    protected Socket socket;
    protected OutputStream out;
    protected InputStream in;
    protected Logger logger;

    protected FileInputStream fis;

    protected byte[] buffer;
    protected NetworkPacket packet;

    public Sender(Logger logger, String server, int port, int percent, String filePath) {
        this.logger = logger;
        this.server = server;
        this.port = port;
        this.lostPercent = percent;
        this.filePath = filePath;
        this.buffer = new byte[NetworkPacket.PACKET_SIZE];
        this.packet = new NetworkPacket();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

}
