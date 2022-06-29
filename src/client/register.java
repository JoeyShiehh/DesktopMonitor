package client;

import myutil.Protocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import static client.clientview.frame;


public class register {
    private DataOutputStream dos = null;
    private Socket socket;
    public static JFrame frame2 = new JFrame("注册界面");
    public static JLabel label4 = new JLabel("学号");
    public static JTextField usrname = new JTextField(8);
    public static JLabel label5 = new JLabel("密码");
    public static JTextField pass = new JPasswordField(8);
    public static JLabel label6 = new JLabel("确认密码");
    public static JTextField conpass = new JPasswordField(8);
    public static JLabel label7 = new JLabel("学生ip(例：192.36.25.153：30000)");

    public static JTextField stuip = new JTextField(8);
    public static JLabel label8 = new JLabel("老师ip(例：192.36.25.153：30000)");
    public static JTextField teaip = new JTextField(8);
    public static JLabel label9 = new JLabel("老师验证码");
    public static JTextField teacode = new JTextField(18);
    public static JButton rregisterbtn = new JButton("注册");

    public register() {


        frame2.setSize(400,400);
        frame2.setLocationRelativeTo(null);
        rregisterbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {

                    String stunum = usrname.getText();
                    String pass1 = pass.getText();
                    String conpass1 = conpass.getText();
                    String stuip1 = stuip.getText();
                    String teaip1 = teaip.getText();
                    System.out.println(teaip1);
                    String teachercode1 = teacode.getText();
                    String[] teamessage = teaip1.split(":");
                    String teacher_ip = teamessage[0];
                    String[] stumessage = stuip1.split(":");
                    String student_ip = stumessage[0];
                    String ip_re = "(0\\d{2}|0\\d|\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(0\\d{2}|0\\d|\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
                    if ("".equals(stunum) || "".equals(pass1) || "".equals(stuip1) || "".equals(teaip1) || "".equals(teachercode1)) {
                        JOptionPane.showMessageDialog(frame2, "消息不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (!pass1.equals(conpass1)) {
                        JOptionPane.showMessageDialog(frame2, "两次密码输入不一致", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (teamessage.length != 2 || stumessage.length != 2) {
                        JOptionPane.showMessageDialog(frame2, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (teacher_ip.length() < 7 || teacher_ip.length() > 15 || student_ip.length() < 7 || student_ip.length() > 15) {
                        JOptionPane.showMessageDialog(frame2, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (!(teacher_ip.matches(ip_re) && student_ip.matches(ip_re))) {
                        JOptionPane.showMessageDialog(frame2, "ip格式不对", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else {

                    }


                    for (String i : teamessage) {
                        System.out.println(i);
                    }
                    socket = new Socket(teamessage[0], Integer.parseInt(teamessage[1]));
                    dos = new DataOutputStream(socket.getOutputStream());
                    String reg = stunum + "," + pass1 + "," + stuip1 + "," + teaip1 + "," + teachercode1 + "," + teamessage[0] + "," + teamessage[1];//注册标识，学号，密码，学生IP，老师验证码，老师ip，老师端口
                    Protocol.send(0, reg.getBytes(StandardCharsets.UTF_8), dos);
                } catch (UnknownHostException unknownHostException) {
                    unknownHostException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } finally {

                }
                frame2.setVisible(false);
                frame.setVisible(true);

            }
        });
        //设置按下右上角X号后关闭
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //调用函数初始化窗体的组件
        initFrame2();
        //新窗口与旧窗口错开50像素。
        //窗口可见
        frame2.setVisible(true);
    }

    public void initFrame2() {

        JPanel panel11 = new JPanel();
        panel11.add(label4);
        panel11.add(usrname);

        JPanel panel12 = new JPanel();
        panel12.add(label5);
        panel12.add(pass);

        JPanel panel13 = new JPanel();
        panel13.add(label6);
        panel13.add(conpass);

        JPanel panel14 = new JPanel();
        panel14.add(label7);
        panel14.add(stuip);
        JPanel panel15 = new JPanel();
        panel15.add(label8);
        panel15.add(teaip);
        JPanel panel16 = new JPanel();
        panel16.add(label9);
        panel16.add(teacode);

        JPanel panel17 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel17.add(rregisterbtn);


        Box vBox = Box.createVerticalBox();
        vBox.add(panel11);
        vBox.add(panel12);
        vBox.add(panel13);
        vBox.add(panel14);
        vBox.add(panel15);
        vBox.add(panel16);
        vBox.add(panel17);

        frame2.setContentPane(vBox);
    }

    public static void register2() {
        new register();
    }
}

