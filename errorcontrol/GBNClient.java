
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author apple
 */
public class GBNClient extends javax.swing.JFrame {

    /**
     * Creates new form GBNClient
     */
    public GBNClient() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtServer = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtPercent = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogs = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Server:");
        jPanel1.add(jLabel3, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(txtServer, gridBagConstraints);

        jLabel4.setText("Port:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(txtPort, gridBagConstraints);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel1.add(btnSend, gridBagConstraints);

        jLabel5.setText("Percentage of Loss:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(txtPercent, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        txtLogs.setEditable(false);
        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        jScrollPane1.setViewportView(txtLogs);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    final static int TIMEOUT = 5000;
    protected byte[] buffer;
    protected NetworkPacket packet;

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        try {
            Map<Byte, byte[]> packets;
            byte sequence = 0;
            String server = txtServer.getText();
            int port = Integer.parseInt(txtPort.getText());
            int percent = Integer.parseInt(txtPercent.getText());

            info("Connecting server...");
            // set timeout for read ACK.
            try (Socket socket = new Socket(server, port)) {
                // set timeout for read ACK.
                socket.setSoTimeout(TIMEOUT);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                
                Random rnd = new Random(System.currentTimeMillis());
                
                FileInputStream fis = new FileInputStream("COSC635_P2_DataSent.txt");
                byte[] rawData;
                packets = new HashMap<>();
                
                // read unknowledge rawData size
                int iPacketCount = packets.size();
                while (true) {
                    // if size of packets is window size, break
                    if (iPacketCount >= NetworkPacket.GBN_WINDOW_SIZE) {
                        break;
                    }
                    
                    // read data from file, build rawData and add it to packets
                    int iReadLen = fis.read(buffer, 0, NetworkPacket.BODY_SIZE);
                    if (iReadLen < 0) {
                        info("Ended to send file!");
                        rawData = NetworkPacket.build(NetworkPacket.TYPE_END, sequence, null, 0);
                        packets.put(sequence, rawData);
                        break;
                    } else {
                        rawData = NetworkPacket.build(NetworkPacket.TYPE_DATA, sequence, buffer, iReadLen);
                        packets.put(sequence, rawData);
                    }
                    iPacketCount++;
                    sequence++;
                }
                
                Set<Map.Entry<Byte, byte[]>> s = packets.entrySet();
                Iterator<Map.Entry<Byte, byte[]>> iter = s.iterator();
                while (iter.hasNext()) {
                    Map.Entry<Byte, byte[]> entry = iter.next();
                    // generate random number and decide send data or simulate loss.
                    int iRandom = rnd.nextInt() % 100;
                    if (iRandom >= percent) {
                        info("Sending packet...sequence=" + entry.getKey());
                        out.write(entry.getValue(), 0, NetworkPacket.PACKET_SIZE);
                    } else {
                        info("Simulated to loss!");
                    }
                }
                
                // wait ACK
                info("Receiving ACK...");
                while (true) {
                    try {
                        in.read(buffer, 0, NetworkPacket.PACKET_SIZE);
                        
                        if (!this.packet.parse(buffer)) {
                            info("Parse packet error! " + this.packet.error);
                            break;
                        }
                        if (this.packet.type != NetworkPacket.TYPE_ACK) {
                            info("Packet type is not ACK!");
                            break;
                        }
                        info("Received ACK of sequence " + this.packet.sequence);
                        packets.remove(this.packet.sequence);
                        
                        // If received all ACK of packets, break;
                        if (packets.size() < 1) {
                            break;
                        }
                    } catch (Exception e) {
                        // timeout, data may be lossed. send it next time.
                        info("Server may not receive data! try send packet again...");
                        break;
                    }
                }
            }
            info("Stopped!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSendActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GBNClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GBNClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GBNClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GBNClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GBNClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtLogs;
    private javax.swing.JTextField txtPercent;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtServer;
    // End of variables declaration//GEN-END:variables

    private void info(String msg) {
        txtLogs.append(msg + "\n");
    }
}
