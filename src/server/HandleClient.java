package server;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.tree.DefaultMutableTreeNode;

import myutil.Protocol;
import myutil.Result;


public class HandleClient implements Runnable {
    private Socket socket;
    private DataInputStream dis = null;
    private String key = null;
    private boolean isLive = true;

    public HandleClient(Socket socket) {
        this.socket = socket;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            Server.view = new View();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (isLive) {
            Result result = null;
            result = Protocol.getResult(dis);

            if (result != null) {
                handleType(result.getType(), result.getData());

                //д���ļ�
                ByteArrayInputStream in = new ByteArrayInputStream(result.getData());
                try {
                    BufferedImage image = ImageIO.read(in);
                     writeImageFile(image);
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                    System.out.println("Save Filed");
                }
            }
        }
    }

    //    �洢ͼ���ļ�
    public void writeImageFile(BufferedImage bi) throws IOException {
        String fileName= "saveImg/"+System.currentTimeMillis()+".png";
        File outputfile = new File(fileName);
        ImageIO.write(bi, "png", outputfile);
    }


    //��������type����Ϣ
    private void handleType(int type, byte[] data) {
        System.out.println(type);
        try {
            switch (type) {
                case 1:
                    if (Server.curKey != key) break;
                    ByteArrayInputStream bai = new ByteArrayInputStream(data);
                    BufferedImage buff = ImageIO.read(bai);
//                    double scale = 540 / (double)buff.getHeight();
                    double scale = 1;
                    System.out.println(buff.getHeight());
                    buff = scale(buff, scale);
                    Server.view.centerPanel.setBufferedImage(buff);//Ϊ��Ļ�����ͼ����BufferedImage
                    Server.view.centerPanel.repaint();
                    bai.close();
                    break;
                case 2:
                    String msg = new String(data);
                    if (msg.equals("client")) {
                        key = socket.getInetAddress().getHostAddress();
                        Server.client.put(key, socket);
                        Server.view.setTreeNode(Server.view.addValue(key));
                        if (Server.curKey == null) Server.curKey = key;
                    }
                    break;
                case 3:
                    Server.view.setTreeNode(Server.view.removeValue(key));
                    Server.client.remove(key);
                    Server.view.centerPanel.setBufferedImage(null);
                    Server.view.centerPanel.repaint();
                    Server.curKey = null;
                    isLive = false;
                    break;
                default:
                    break;
            }
        } catch (IOException exception) {
            try {
                if (key != null && key.indexOf("client") != -1) Server.client.remove(key);
                if (socket != null) socket.close();
                exception.printStackTrace();
            } catch (IOException ez) {
                ez.printStackTrace();
            }
        }
    }

    /**
     * ͼƬ����
     *
     * @param bfImage
     * @param scale
     * @return
     */
    public BufferedImage scale(BufferedImage bfImage, double scale) {
        //��ͼѹ��
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        Image image = bfImage.getScaledInstance((int) (width * scale), (int) (height * scale), Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tag.createGraphics();
        g.drawImage(image, 0, 0, null); // ������С���ͼ   
        g.dispose();
        return tag;
    }
}
