package server;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private DataOutputStream dos = null;
    private String reg = null;
    private String[] datas = null;

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
            String a = null;
            if (result.getType() == 0) {
                try {
                    a = new String(result.getData(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                datas = a.split(",");
                System.out.println("data[4]:" + datas[4]);
            }

            if (result != null) {
                handleType(result.getType(), result.getData());

                //写入文件
                if (result.getType() == 1) {
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
    }

    //    存储图像文件
    public void writeImageFile(BufferedImage bi) throws IOException {
        String path = "saveImg/" + key;
        File dir = new File(path);
        dir.mkdir();
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_-HH_mm_ss_SSS");
        Date date = new Date(currentTime);
        String fileName = path + "/" + formatter.format(date) + ".png";
        File outputFile = new File(fileName);
        ImageIO.write(bi, "png", outputFile);
    }


    //处理类型type的消息
    private void handleType(int type, byte[] data) {
        System.out.println(type);
        try {
            switch (type) {
                case 0:
                    dos = new DataOutputStream(socket.getOutputStream());

                    if (Integer.parseInt(datas[4]) == Server.checkCode) {
                        reg = "200";
                        Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                        String address = socket.getInetAddress().getHostAddress();
//                        String address = code[3];
                        Server.register_client.add(address);
                        Server.view.setTreeNode(Server.view.registerValue(address));
                    } else {
                        reg = "503";// 验证码错误
                        Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                    }
                    break;
                case 1:
                    if (Server.curKey != key) break;
                    ByteArrayInputStream bai = new ByteArrayInputStream(data);
                    BufferedImage buff = ImageIO.read(bai);
//                    double scale = 540 / (double)buff.getHeight();
                    double scale = 1;
                    System.out.println(buff.getHeight());
                    buff = scale(buff, scale);
                    Server.view.centerPanel.setBufferedImage(buff);//为屏幕监控视图设置BufferedImage
                    Server.view.centerPanel.repaint();
                    bai.close();
                    break;
                case 2:
                    String msg = new String(data);
                    if (msg.equals("client")) {
                        key = socket.getInetAddress().getHostAddress();
                        if (!Server.register_client.contains(key)) {
                            dos = new DataOutputStream(socket.getOutputStream());
                            reg = "500";
                            Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                            break;
                        }
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
                    Server.curKey = key + "已断开";
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
     * 图片缩放
     *
     * @param bfImage
     * @param scale
     * @return
     */
    public BufferedImage scale(BufferedImage bfImage, double scale) {
        //截图压缩
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        Image image = bfImage.getScaledInstance((int) (width * scale), (int) (height * scale), Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tag.createGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图   
        g.dispose();
        return tag;
    }
}
