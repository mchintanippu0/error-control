
/**
 * NetworkPacket data class
 * first 4 bytes is header, 28 bytes is body
 * header:
 * byte1: Signature, value is 0x55
 * byte2: Sequence Number
 * byte3: Data Length, if packet is not data, it may be 0
 * byte4: NetworkPacket Type, data or ack or end
 *
 *
 */
public class NetworkPacket {

    protected final static byte SIGN = 0x55;

    public final static int PACKET_SIZE = 32;
    public final static int HEADER_SIZE = 4;
    public final static int BODY_SIZE = PACKET_SIZE - HEADER_SIZE;

    public final static int GBN_WINDOW_SIZE = 3;

    public final static byte TYPE_DATA = 0x4F;
    public final static byte TYPE_ACK = 0x49;
    public final static byte TYPE_END = 0x40;

    byte signature;
    byte sequence;
    byte size;
    byte type;
    byte[] data;

    public String error;

    public NetworkPacket() {
        this.signature = SIGN;
        this.sequence = 0;
        this.size = 0;
        this.type = 0;
        this.data = null;
        this.error = "";
    }

    public byte getSignature() {
        return signature;
    }

    public void setSignature(byte signature) {
        this.signature = signature;
    }

    public byte getSequence() {
        return sequence;
    }

    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static byte[] build(byte bType, byte bSeq, byte[] sendData, int iDataLength) {
        try {
            byte[] packet = new byte[PACKET_SIZE];
            packet[0] = SIGN;
            packet[1] = bSeq;
            packet[2] = (byte) iDataLength;
            packet[3] = bType;

            if (bType == TYPE_DATA) {
                if (iDataLength < 1 || iDataLength > BODY_SIZE) {
                    return null;
                }
                for (int i = 0; i < iDataLength; i++) {
                    packet[HEADER_SIZE + i] = sendData[i];
                }
            }

            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public NetworkPacket copy() {
        NetworkPacket pac = new NetworkPacket();
        pac.signature = this.signature;
        pac.sequence = this.sequence;
        pac.size = this.size;
        pac.type = this.type;
        pac.data = this.data.clone();
        return pac;
    }

    public boolean parse(byte[] recv) {
        this.error = "";
        try {
            if (recv[0] != SIGN) {
                this.error = "Signature error!";
                return false;
            }
            this.sequence = recv[1];
            this.size = recv[2];
            this.type = recv[3];

            if (this.type == TYPE_DATA) {
                if (this.size < 1 || this.size > BODY_SIZE) {
                    this.error = "Data length  is invalid!";
                    return false;
                }
                this.data = new byte[this.size];
                for (int i = 0; i < this.size; i++) {
                    this.data[i] = recv[HEADER_SIZE + i];
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
