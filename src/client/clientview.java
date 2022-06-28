package client;
import server.DrawPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class clientview {
    public static JFrame frame = new JFrame("登录界面");
    //登陆界面组件
    public static JLabel label1 = new JLabel("用户名");                 //标签
    public static JTextField username = new JTextField(10);            //文本框
    public static JLabel label2 = new JLabel("密   码");
    public static JTextField password = new JPasswordField(10);         //密码文本框

    public static JLabel label3 = new JLabel("老师IP地址");
    public static JTextField teacherip = new JTextField(10);


    public static JButton Signinbtn = new JButton("登录");              //按钮
    public static JButton registerbtn = new JButton("注册");



    //以下必须为静态的，否则在HandleClient里访问不到
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DrawPanel centerPanel;
    static List<String> list=new ArrayList<>();

    public clientview(){

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH );
        registerbtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
               new register();


               frame.setVisible(false);


            }
        });
        Signinbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] teamessage = teacherip.getText().split(":");

                if ("".equals(username.getText())||"".equals(password.getText())||"".equals(teacherip.getText())){
                    JOptionPane.showMessageDialog(frame, "消息不能为空", "提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                else if (teamessage.length!=2){
                    JOptionPane.showMessageDialog(frame, "ip格式不对", "提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }else {
                   // JOptionPane.showMessageDialog(frame, "正在请求被监控...", "提示",JOptionPane.WARNING_MESSAGE)
                }

                clientview cltview=new clientview();
                cltview.create();
                final Client client = new Client();
                client.showSystemTray();// ???????
                client.conn(teamessage[0],Integer.parseInt(teamessage[1]));
                client.load();// ???
                client.showSystemTray();// ???????
                JOptionPane.showMessageDialog(frame, "您正在被监控！", "提示",JOptionPane.WARNING_MESSAGE);
                while (client.isLive) {
                    client.sendImage(client.getScreenShot());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ev) {

                    }
                }



            }
        });
        //公共静态主登陆界面框

        //构造函数，创建以及初始化窗口


            //设置窗口大小

            //设置按下右上角X号后关闭
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
        JPanel panel01 = new JPanel(new FlowLayout(FlowLayout.CENTER));  //居中面板
        panel01.add(label1);
        panel01.add(username);

        JPanel panel02 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel02.add(label2);
        panel02.add(password);

        JPanel panel03 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel03.add(label3);
        panel03.add(teacherip);


        //定义面板封装按钮
        JPanel panel05 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel05.add(Signinbtn);
        panel05.add(registerbtn);

        //箱式布局装入三个面板
        BorderLayout borderLayout=new BorderLayout();
        Box vBox = Box.createVerticalBox();
        vBox.add(panel01);
        vBox.add(panel02);
        vBox.add(panel03);
        vBox.add(panel05);

        //将布局置入窗口
        frame.setContentPane(vBox);
    }
    //


    //

    public void create(){

        JFrame frame=new JFrame("远程屏幕监视系统");
        Container container=frame.getContentPane();

    }

    public static void main(String[] args) {
        new clientview();
    }

}
