package client;
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
        public static JFrame frame2 = new JFrame("ע�����");
        public static JLabel label4 = new JLabel("ѧ��");
        public static JTextField usrname = new JTextField(8);
    public static JLabel label5 = new JLabel("����");
    public static JTextField pass = new JPasswordField(8);
    public static JLabel label6 = new JLabel("ȷ������");
    public static JTextField conpass = new JPasswordField(8);
    public static JLabel label7 = new JLabel("ѧ��ip(����192.36.25.153��30000)");

    public static JTextField stuip = new JTextField(8);
    public static JLabel label8 = new JLabel("��ʦip(����192.36.25.153��30000)");
    public static JTextField teaip = new JTextField(8);
    public static JLabel label9 = new JLabel("��ʦ��֤��");
    public static JTextField teacode = new JTextField(18);
        public static JButton rregisterbtn = new JButton("ע��");

        public register() {


            frame2.setExtendedState(JFrame.MAXIMIZED_BOTH );
            rregisterbtn.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) {
                    try {

                        String stunum = usrname.getText();
                        String pass1 = pass.getText();
                        String conpass1 = conpass.getText();
                        String stuip1 = stuip.getText();
                        String teaip1 = teaip.getText();
                        System.out.println(teaip1);
                        String teachercode1=teacode.getText();
                        String[] teamessage = teaip1.split(":");
                        String[] stumessage = stuip1.split(":");
                        if ("".equals(stunum)||"".equals(pass1)||"".equals(stuip1)||"".equals(teaip1)||"".equals(teachercode1)){
                            JOptionPane.showMessageDialog(frame2, "��Ϣ����Ϊ��", "��ʾ",JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        else if (!pass1.equals(conpass1)){
                            JOptionPane.showMessageDialog(frame2, "�����������벻һ��", "��ʾ",JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        else if (teamessage.length!=2||stumessage.length!=2){
                            JOptionPane.showMessageDialog(frame2, "ip��ʽ����", "��ʾ",JOptionPane.WARNING_MESSAGE);
                            return;
                        }else {

                        }


                        for (String i : teamessage) {
                            System.out.println(i);
                        }
                                 String reg = "1,"+stunum+","+","+pass1+","+","+stuip1+","+teaip1+","+teachercode1+","+teamessage[0]+","+teamessage[1];//ע���ʶ��ѧ�ţ����룬ѧ��IP����ʦ��֤�룬��ʦip����ʦ�˿�
                                 System.out.println(reg);
                                 socket = new Socket(teamessage[0], Integer.parseInt(teamessage[1]));
                                 dos = new DataOutputStream(socket.getOutputStream());
                                 dos.write(reg.getBytes(StandardCharsets.UTF_8));
                                 dos.flush();
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
            //���ð������Ͻ�X�ź�ر�
            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //���ú�����ʼ����������
            initFrame2();
            //�´�����ɴ��ڴ�50���ء�
            //���ڿɼ�
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
        public static void register() {
            new register();
        }
    }

