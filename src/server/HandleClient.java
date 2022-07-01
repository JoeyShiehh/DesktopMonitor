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
    private String username = null;


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
            System.out.println("\nRun " + "Type:" + result.getType());
            switch (result.getType()) {
                case 0:
                    try {
                        a = new String(result.getData(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    datas = a.split(",");
                    System.out.println("data[4]:" + datas[4]);
                    handleType(result.getType(), result.getData());
                    break;
                case 1://写入文件
                    ByteArrayInputStream in = new ByteArrayInputStream(result.getData());
                    try {
                        BufferedImage image = ImageIO.read(in);
                        writeImageFile(image);
                    } catch (IOException | IllegalArgumentException e) {
                        e.printStackTrace();
                        System.out.println("Save Filed");
                    }
                    handleType(result.getType(), result.getData());
                    break;
                case 2://登录请求
                    try {
                        a = new String(result.getData(), "utf-8");
                        System.out.println(a);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    datas = a.split(",");
                    handleType(result.getType(), result.getData());
                    break;
                default:
                    handleType(result.getType(), result.getData());
            }
        }
    }

    //    存储图像文件
    public void writeImageFile(BufferedImage bi) throws IOException {
        String path = "saveImg/" + key;
        System.out.println(path);
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
        System.out.println("type:" + type + "-key:" + key);
        try {
            switch (type) {
                case 0:
                    dos = new DataOutputStream(socket.getOutputStream());
                    if (Integer.parseInt(datas[4]) == Server.checkCode) {
                        reg = "200";
                        Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                        String address = socket.getInetAddress().getHostAddress();
                        Server.sqLitejdbc.insert(datas[0], datas[1], address);
                        System.out.println("exe insert");
                        Server.register_client.add(address);
                        Server.view.setTreeNode(Server.view.registerValue(address));
                    } else {
                        reg = "504";// 验证码错误
                        Protocol.send(Protocol.TYPE_CONNECT_FAILED, reg.getBytes(StandardCharsets.UTF_8), dos);
                    }
                    break;
                case 1:
                    if (Server.curKey != key) break;
                    ByteArrayInputStream bai = new ByteArrayInputStream(data);
                    BufferedImage buff = ImageIO.read(bai);
                    double scale = 1;
//                    System.out.println(buff.getHeight());
                    buff = scale(buff, scale);
                    Server.view.centerPanel.setBufferedImage(buff);//为屏幕监控视图设置BufferedImage
                    Server.view.centerPanel.repaint();
                    bai.close();
                    break;
                case 2:
                    key = socket.getInetAddress().getHostAddress();
                    if (!Server.register_client.contains(key)) {
                        dos = new DataOutputStream(socket.getOutputStream());
                        reg = "500";
                        Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                        System.out.println("未注册");
                        isLive = false;
                        break;
                    }
                    System.out.println(datas[0]+"::"+datas[1]);
                    if (!Server.sqLitejdbc.select(datas[0], datas[1])) {
                        dos = new DataOutputStream(socket.getOutputStream());
                        reg = "505";//密码错误
                        System.out.println("密码错误");
                        Protocol.send(4, reg.getBytes(StandardCharsets.UTF_8), dos);
                        isLive = false;
                        break;
                    }
                    Server.client.put(key, socket);
                    Server.view.setTreeNode(Server.view.addValue(key));
                    if (Server.curKey == null) Server.curKey = key;
                    break;
                case 3:
                    Server.view.setTreeNode(Server.view.removeValue(key));
                    Server.client.remove(key);
                    Server.view.centerPanel.setBufferedImage(null);
                    Server.view.centerPanel.repaint();
                    Server.curKey = key + "断开";
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
