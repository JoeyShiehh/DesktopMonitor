package client;

import server.DrawPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class clientview {
    public static JFrame frame = new JFrame("��¼����");
    //��½�������
    public static JLabel label1 = new JLabel("�û���");                 //��ǩ
    public static JTextField username = new JTextField(10);            //�ı���
    public static JLabel label2 = new JLabel("��   ��");
    public static JTextField password = new JPasswordField(10);         //�����ı���

    public static JLabel label3 = new JLabel("��ʦIP��ַ");
    public static JTextField teacherip = new JTextField(10);


    public static JButton Signinbtn = new JButton("��¼");              //��ť
    public static JButton registerbtn = new JButton("ע��");
    private TrayIcon trayIcon;//����ͼ��
    private SystemTray systemTray;//ϵͳ����


    //���±���Ϊ��̬�ģ�������HandleClient����ʲ���
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DrawPanel centerPanel;
    static List<String> list = new ArrayList<>();

    public clientview() {
        systemTray = SystemTray.getSystemTray();//���ϵͳ���̵�ʵ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400,400);


        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowIconified(WindowEvent e) {
                        try {
                            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
                            systemTray.add(trayIcon);//�������̵�ͼ��
                        } catch (AWTException e2) {
                            e2.printStackTrace();
                        }
                        frame.setVisible(false);

                        trayIcon.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent e) {
                                int clt = e.getClickCount();
                                if (clt == 2) {
                                    frame.setExtendedState(Frame.NORMAL);

                                }
                                systemTray.remove(trayIcon);
                                frame.setVisible(true);
                                frame.toFront();
                            }
                        });
                    }
                });


        registerbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new register();
                frame.setVisible(false);
            }
        });
        Signinbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] teamessage = teacherip.getText().split(":");

                if ("".equals(username.getText()) || "".equals(password.getText()) || "".equals(teacherip.getText())) {
                    JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ��", "��ʾ", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (teamessage.length != 2) {
                    JOptionPane.showMessageDialog(frame, "ip��ʽ����", "��ʾ", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    // JOptionPane.showMessageDialog(frame, "�������󱻼��...", "��ʾ",JOptionPane.WARNING_MESSAGE)
                }

                clientview cltview = new clientview();
                cltview.create();
                final Client client = new Client();
                client.showSystemTray();// ???????
                client.conn(teamessage[0], Integer.parseInt(teamessage[1]));
                client.load();// ???
                client.showSystemTray();// ???????
                JOptionPane.showMessageDialog(frame, "�����ڱ���أ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
                while (client.isLive) {
                    client.sendImage(client.getScreenShot());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ev) {

                    }
                }


            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //���ú�����ʼ����������
        initFrame();
        //���ھ���
        frame.setLocationRelativeTo(null);
        //���ڿɼ�
        frame.setVisible(true);


    }


    public void initFrame() {
        //��������װ�ı���ͱ�ǩ
        JPanel panel01 = new JPanel(new FlowLayout(FlowLayout.CENTER));  //�������
        panel01.add(label1);
        panel01.add(username);

        JPanel panel02 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel02.add(label2);
        panel02.add(password);

        JPanel panel03 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel03.add(label3);
        panel03.add(teacherip);


        //��������װ��ť
        JPanel panel05 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel05.add(Signinbtn);
        panel05.add(registerbtn);

        //��ʽ����װ���������
        BorderLayout borderLayout = new BorderLayout();
        Box vBox = Box.createVerticalBox();
        vBox.add(panel01);
        vBox.add(panel02);
        vBox.add(panel03);
        vBox.add(panel05);

        //���������봰��
        frame.setContentPane(vBox);
    }
    //


    //

    public void create() {

        JFrame frame = new JFrame("Զ����Ļ����ϵͳ");
        Container container = frame.getContentPane();

    }

    public static void main(String[] args) {
        new clientview();
    }

}
