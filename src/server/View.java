package server;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * ��װ�������˵���ͼ��
 *
 * @author Administrator
 */
public class View {
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width;
    private int height;

    //���±���Ϊ��̬�ģ�������HandleClient����ʲ���
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DrawPanel centerPanel;
    static List<String> list = new ArrayList<>();

    public View() {
        width = (int) (screensize.getWidth() * 0.7);
        height = (int) (screensize.getHeight() * 0.8);
    }

    //������ͼ
    public void create() {
        //�õ����ݴ���
        JFrame frame = new JFrame("Զ����Ļ����ϵͳ");
        Container container = frame.getContentPane();

        //���
        JPanel RightPanel = new JPanel();
        RightPanel.setBackground(Color.darkGray);
        container.add(RightPanel, BorderLayout.EAST);
        //��
        root = new DefaultMutableTreeNode("�������ӵı��ض�");
        model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        tree.setBackground(Color.darkGray);

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree) e.getSource();
                DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                try {
                    String nodeName = selectionNode.toString();
                    Server.curKey = nodeName;
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }
        });


//		�������ڵ����ʽ
//		DefaultTreeCellRenderer cr=new DefaultTreeCellRenderer();
//		cr.setBackgroundNonSelectionColor(Color.darkGray);
//		cr.setLeafIcon(new ImageIcon("img/link_success.png"));
//		cr.setTextNonSelectionColor(Color.white);
        tree.setCellRenderer(new MyTreeCellRenderer());
        JScrollPane jsp = new JScrollPane(tree);
        JScrollBar bar = jsp.getHorizontalScrollBar();
        bar.setBackground(Color.darkGray);
        jsp.setBorder(null);
        RightPanel.add(jsp);

        JTextField text = new JTextField("��ǰ��֤��Ϊ��" + Server.checkCode, 100);
        text.setEditable(false);
        JPanel downPanel = new JPanel();
        downPanel.add(text);
        RightPanel.setBackground(Color.darkGray);
        container.add(downPanel, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                Server.serverLive = false;
            }

        });

        centerPanel = new DrawPanel();
        container.add(new JScrollPane(centerPanel));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image image = Toolkit.getDefaultToolkit().getImage("img/tuopan.png");
        final TrayIcon trayIcon = new TrayIcon(image);// ��������ͼ��
        trayIcon.setToolTip("��Ļ���ϵͳ\r\n�ͻ���");// ������ʾ����
        final SystemTray systemTray = SystemTray.getSystemTray();// ���ϵͳ���̶���

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // ���˫��
                    frame.setExtendedState(0);
                    frame.setVisible(true);
                }
            }
        });
        final PopupMenu popupMenu = new PopupMenu(); // ���������˵�
        MenuItem item = new MenuItem("�˳�");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(item);
        trayIcon.setPopupMenu(popupMenu);// Ϊ����ͼ��ӵ����˵�
        try {
            systemTray.add(trayIcon);// Ϊϵͳ���̼�����ͼ��
        } catch (AWTException e) {
            e.printStackTrace();
        }

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Object[] options = {"�˳�", "��С��"};
                int i = JOptionPane.showOptionDialog(null,
                        "ȷ��Ҫ�˳�ϵͳ��",
                        "�˳�ϵͳ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (i == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
        public MyTreeCellRenderer() {
            super();
            this.setBackgroundNonSelectionColor(Color.darkGray);
//            this.setIcon(new ImageIcon("img/database.png"));
//            this.setLeafIcon(new ImageIcon("img/database.png"));
            this.setTextNonSelectionColor(Color.white);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setText(value.toString());
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            //�õ�ÿ���ڵ��text
            String str = node.toString();
            if (str.contains("�Ͽ�")) {
                this.setIcon(new ImageIcon("img/link_failed.png"));
            } else if (str.contains("ע��")) {
                this.setIcon(new ImageIcon("img/link_register.png"));
            } else {
                this.setIcon(new ImageIcon("img/link_success.png"));
            }

            return this;

        }
    }


    /**
     * ������ڵ�
     *
     * @param
     */
    public void setTreeNode(List<String> l) {
        list = l;
        root.removeAllChildren();
        for (int i = 0; i < list.size(); i++) {
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(list.get(i));
            root.add(node1);
        }
        model.reload();
    }

    public List<String> addValue(String key) {
        list.remove(key + "ע��");
        list.remove(key + "�Ͽ�");
        list.add(key);
        return list;
    }

    public List<String> removeValue(String key) {
        list.remove(key);
        list.add(key + "�Ͽ�");
        return list;
    }

    public List<String> registerValue(String key) {
        list.add(key + "ע��");
        return list;
    }

    public void clear() {
        list.clear();
    }

}
