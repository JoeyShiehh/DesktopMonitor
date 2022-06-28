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



    //���±���Ϊ��̬�ģ�������HandleClient����ʲ���
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
                    JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ��", "��ʾ",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                else if (teamessage.length!=2){
                    JOptionPane.showMessageDialog(frame, "ip��ʽ����", "��ʾ",JOptionPane.WARNING_MESSAGE);
                    return;
                }else {
                   // JOptionPane.showMessageDialog(frame, "�������󱻼��...", "��ʾ",JOptionPane.WARNING_MESSAGE)
                }

                clientview cltview=new clientview();
                cltview.create();
                final Client client = new Client();
                client.showSystemTray();// ???????
                client.conn(teamessage[0],Integer.parseInt(teamessage[1]));
                client.load();// ???
                client.showSystemTray();// ???????
                JOptionPane.showMessageDialog(frame, "�����ڱ���أ�", "��ʾ",JOptionPane.WARNING_MESSAGE);
                while (client.isLive) {
                    client.sendImage(client.getScreenShot());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ev) {

                    }
                }



            }
        });
        //������̬����½�����

        //���캯���������Լ���ʼ������


            //���ô��ڴ�С

            //���ð������Ͻ�X�ź�ر�
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
        BorderLayout borderLayout=new BorderLayout();
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

    public void create(){

        JFrame frame=new JFrame("Զ����Ļ����ϵͳ");
        Container container=frame.getContentPane();

    }

    public static void main(String[] args) {
        new clientview();
    }

}
