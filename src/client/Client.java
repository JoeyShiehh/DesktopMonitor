package client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import myutil.Protocol;
import myutil.Result;

/**
 * ��װ���ض˵ķ���
 *
 * @author Administrator
 */
public class Client {

    Socket socket;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screensize.getWidth();
    double height = screensize.getHeight();
    Robot robot;
    static boolean isLive = true;
    JButton button;

    public Client() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ӷ�����
     */
    public void conn(String address, int port) {
        try {
            socket = new Socket(address, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            // dos.writeUTF("client");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ��Ļ��ͼ������
     *
     * @return
     */
    public BufferedImage getScreenShot() {
        BufferedImage bfImage = robot.createScreenCapture(new Rectangle(0, 0, (int) width, (int) height));
        return bfImage;
    }

    public int load(byte[] aa) {

        Protocol.send(Protocol.TYPE_LOAD, aa, dos);
        Result result = Protocol.getResult(dis);
        String a = null;
        try {
            a = new String(result.getData(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(a);
        if (a.equals("505")) {
            return -1;
        } else return 1;
    }


    public void sendImage(BufferedImage buff) {
        if (buff == null)
            return;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buff, "png", baos);
            Protocol.send(Protocol.TYPE_IMAGE, baos.toByteArray(), dos);
            baos.close();
            System.out.println("send file successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // �رտͻ��ˣ��ͷŵ���Դ
    public void close() {
        //�������������Ϣ
        Protocol.send(Protocol.TYPE_LOGOUT, new String("logout").getBytes(), dos);
        // �ر���Դ
        try {
            if (dos != null)
                dos.close();
            if (socket != null)
                socket.close();
            if (dis != null)
                dis.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        // ��ͼѹ��
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        Image image = bfImage.getScaledInstance((int) (width * scale), (int) (height * scale), Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tag.createGraphics();
        g.drawImage(image, 0, 0, null); // ������С���ͼ
        g.dispose();
        return tag;
    }

    /**
     * ��ʾϵͳ����
     */
    public void showSystemTray() {
        Image image = Toolkit.getDefaultToolkit().getImage("img/icon.png");
        final TrayIcon trayIcon = new TrayIcon(image);// ��������ͼ��
        trayIcon.setToolTip("��Ļ���ϵͳ\r\n�ͻ���");// ������ʾ����
        final SystemTray systemTray = SystemTray.getSystemTray();// ���ϵͳ���̶���

        final PopupMenu popupMenu = new PopupMenu(); // ���������˵�
        MenuItem item = new MenuItem("�˳�");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLive = false;
                close();
            }
        });
        popupMenu.add(item);
        trayIcon.setPopupMenu(popupMenu);// Ϊ����ͼ��ӵ����˵�
        try {
            systemTray.add(trayIcon);// Ϊϵͳ���̼�����ͼ��
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Client client = new Client();
        client.showSystemTray();// ��ʾ����
        client.conn("127.0.0.1", 33000);
//        client.load();
        client.showSystemTray();// ��ʾ����
        while (client.isLive) {
            client.sendImage(client.getScreenShot());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ev) {

            }
        }
    }
}
