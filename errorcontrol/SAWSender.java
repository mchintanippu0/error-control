
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Random;

public class SAWSender extends Sender {

    public SAWSender(Logger logger, String ip, int port, int percent, String filePath) {
        super(logger, ip, port, percent, filePath);
    }

    @Override
    public void run() {

        try {
            byte index = 0;

            logger.info("Connecting server...");

            socket = new Socket(server, port);
            socket.setSoTimeout(TIMEOUT);
            out = socket.getOutputStream();
            in = socket.getInputStream();

            logger.info("Connected to server...");

            Random rnd = new Random(System.currentTimeMillis());

            fis = new FileInputStream(filePath);
            byte[] rawPacket;
            do {
                // read data from file
                int len = fis.read(buffer, 0, NetworkPacket.BODY_SIZE);
                if (len < 0) {
                    logger.info("File is sent");
                    rawPacket = NetworkPacket.build(NetworkPacket.TYPE_END, index, null, 0);
                    out.write(rawPacket, 0, NetworkPacket.PACKET_SIZE);
                    break;
                } else {
                    while (true) {
                        // generate random number, and decide send rawPacket or simulate to loss
                        int iRandom = rnd.nextInt() % 100;
                        if (iRandom >= lostPercent) {
                            logger.info("Sending packet...sequence=" + index);
                            rawPacket = NetworkPacket.build(NetworkPacket.TYPE_DATA, index, buffer, len);
                            out.write(rawPacket, 0, NetworkPacket.PACKET_SIZE);
                        } else {
                            logger.info("Simulated to loss!");
                        }

                        logger.info("Receiving ACK...");
                        try {
                            // read ack from server
                            in.read(buffer, 0, NetworkPacket.PACKET_SIZE);
                            // if success to read ack, break
                            break;
                        } catch (Exception e) {
                            // timeout, data may be lossed. try to send it again
                            logger.info("Server may not receive data! try send packet again...");
                            continue;
                        }
                    }
                    if (!this.packet.parse(buffer)) {
                        logger.info("Parse packet error! " + this.packet.error);
                        break;
                    }
                    if (this.packet.type != NetworkPacket.TYPE_ACK) {
                        logger.info("Packet type is not ACK!");
                        break;
                    }
                    if (this.packet.sequence != index) {
                        logger.info("Sequence error! expected: " + index + ", got: " + this.packet.sequence);
                        break;
                    }
                    index++;
                }
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (Exception e) {
            }
        }
        logger.info("Stop sending");
    }
}
