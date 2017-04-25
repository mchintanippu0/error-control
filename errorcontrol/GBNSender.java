
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class GBNSender extends Sender {

    protected Map<Byte, byte[]> mPackets;

    public GBNSender(Logger client, String strIP, int iPort, int iPercent, String strSendFilePath) {
        super(client, strIP, iPort, iPercent, strSendFilePath);
    }

    @Override
    public void run() {

        try {
            byte sequence = 0;
            // connect to server
            logger.info("Connecting server...");
            socket = new Socket(server, port);
            // set timeout for read ACK.
            socket.setSoTimeout(TIMEOUT);
            out = socket.getOutputStream();
            in = socket.getInputStream();

            Random rnd = new Random(System.currentTimeMillis());

            fis = new FileInputStream(filePath);
            byte[] rawData;
            mPackets = new HashMap<Byte, byte[]>();

            // read unknowledge rawData size
            int iPacketCount = mPackets.size();
            while (true) {
                // if size of packets is window size, break
                if (iPacketCount >= NetworkPacket.GBN_WINDOW_SIZE) {
                    break;
                }

                // read data from file, build rawData and add it to packets
                int iReadLen = fis.read(buffer, 0, NetworkPacket.BODY_SIZE);
                if (iReadLen < 0) {
                    logger.info("Ended to send file!");
                    rawData = NetworkPacket.build(NetworkPacket.TYPE_END, sequence, null, 0);
                    mPackets.put(sequence, rawData);
                    break;
                } else {
                    rawData = NetworkPacket.build(NetworkPacket.TYPE_DATA, sequence, buffer, iReadLen);
                    mPackets.put(sequence, rawData);
                }
                iPacketCount++;
                sequence++;
            }

            Set<Entry<Byte, byte[]>> s = mPackets.entrySet();
            Iterator<Entry<Byte, byte[]>> iter = s.iterator();
            while (iter.hasNext()) {
                Entry<Byte, byte[]> entry = iter.next();
                // generate random number and decide send data or simulate loss.
                int iRandom = rnd.nextInt() % 100;
                if (iRandom >= lostPercent) {
                    logger.info("Sending packet...sequence=" + entry.getKey());
                    out.write(entry.getValue(), 0, NetworkPacket.PACKET_SIZE);
                } else {
                    logger.info("Simulated to loss!");
                }
            }

            // wait ACK				
            logger.info("Receiving ACK...");
            while (true) {
                try {
                    in.read(buffer, 0, NetworkPacket.PACKET_SIZE);

                    if (!this.packet.parse(buffer)) {
                        logger.info("Parse packet error! " + this.packet.error);
                        break;
                    }
                    if (this.packet.type != NetworkPacket.TYPE_ACK) {
                        logger.info("Packet type is not ACK!");
                        break;
                    }
                    logger.info("Received ACK of sequence " + this.packet.sequence);
                    mPackets.remove(this.packet.sequence);

                    // If received all ACK of packets, break; 
                    if (mPackets.size() < 1) {
                        break;
                    }
                } catch (Exception e) {
                    // timeout, data may be lossed. send it next time.
                    logger.info("Server may not receive data! try send packet again...");
                    break;
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Stopped!");
    }
}
