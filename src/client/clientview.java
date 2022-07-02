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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;

public class clientview {
    public static JFrame frame = new JFrame("登录界面");
    //登陆界面组件
    public static JLabel label1 = new JLabel("用户名");                 //标签
    public static JTextField username = new JTextField(10);            //文本框
    public static JLabel label2 = new JLabel("密   码");
    public static JTextField password = new JPasswordField(10);         //密码文本框

    public static JLabel label3 = new JLabel("老师IP地址");
    public static JTextField teacherip = new JTextField(10);
    public static JLabel label4 = new JLabel("监控频率");
    public static JTextField frequency = new JTextField(10);


    public static JButton Signinbtn = new JButton("登录");              //按钮
    public static JButton registerbtn = new JButton("注册");
    private TrayIcon trayIcon;//托盘图标
    private SystemTray systemTray;//系统托盘


    //以下必须为静态的，否则在HandleClient里访问不到
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DrawPanel centerPanel;
    static List<String> list = new ArrayList<>();

    public clientview() {
        systemTray = SystemTray.getSystemTray();//获得系统托盘的实
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400, 400);


        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowIconified(WindowEvent e) {
                        try {
                            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
                            systemTray.add(trayIcon);//设置托盘的图标
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
                String ip_re = "(0\\d{2}|0\\d|\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(0\\d{2}|0\\d|\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
                if ("".equals(username.getText()) || "".equals(password.getText()) || "".equals(teacherip.getText())) {
                    JOptionPane.showMessageDialog(frame, "消息不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (teamessage.length != 2) {
                    JOptionPane.showMessageDialog(frame, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if(tea_ip.length()<7||tea_ip.length()>15){
                    JOptionPane.showMessageDialog(frame, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if(!(tea_ip.matches(ip_re))){
                    JOptionPane.showMessageDialog(frame, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    // JOptionPane.showMessageDialog(frame, "正在请求被监控...", "提示",JOptionPane.WARNING_MESSAGE)
                }

                final Client client = new Client();
                client.conn(teamessage[0], Integer.parseInt(teamessage[1]));
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(password.getText().getBytes());
                    String md_pass = new BigInteger(1,md.digest()).toString(16);
                    System.out.println(md_pass);
                }catch (Exception e){
                    String md_pass = password.getText();
                    e.printStackTrace();
                }
                int i = client.load((username.getText() + "," + /*password.getText()*/ md_pass).getBytes(StandardCharsets.UTF_8));
                System.out.println(i);
                if (i == -1) {
                    JOptionPane.showMessageDialog(frame, "用户名或密码错误", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                clientview cltView = new clientview();
                cltView.create();
                client.showSystemTray();
                JOptionPane.showMessageDialog(frame, "您正在被监控！", "提示", JOptionPane.WARNING_MESSAGE);
                while (client.isLive) {
                    client.sendImage(client.getScreenShot());
                    try {       //默认频率1000ms
                        if("".equals(frequency.getText())) {
                            Thread.sleep(1000);
                        } else{
                            String frequency_str = frequency.getText();
                            try{
                                int fre = Integer.parseInt(frequency_str);
                                Thread.sleep(fre);
                            } catch(NumberFormatException nfe){
                                Thread.sleep(1000);
                            }
                        }
                    } catch (InterruptedException ev) {

                    }
                }


            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //调用函数初始化窗体的组件
        initFrame();
        //窗口居中
        frame.setLocationRelativeTo(null);
        //窗口可见
        frame.setVisible(true);


    }


    public void initFrame() {
        //定义面板封装文本框和标签
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));  //居中面板
        panel1.add(label1);
        panel1.add(username);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel2.add(label2);
        panel2.add(password);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel3.add(label3);
        panel3.add(teacherip);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel4.add(label4);
        panel4.add(frequency);


        //定义面板封装按钮
        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel5.add(Signinbtn);
        panel5.add(registerbtn);

        //箱式布局装入三个面板
        BorderLayout borderLayout = new BorderLayout();
        Box vBox = Box.createVerticalBox();
        vBox.add(panel1);
        vBox.add(panel2);
        vBox.add(panel3);
        vBox.add(panel4);
        vBox.add(panel5);

        //将布局置入窗口
        frame.setContentPane(vBox);
    }
    //


    //

    public void create() {

        JFrame frame = new JFrame("远程屏幕监视系统");
        Container container = frame.getContentPane();

    }

    public static void main(String[] args) {
        new clientview();
    }

}
