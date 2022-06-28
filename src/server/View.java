package server;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 * ��װ�������˵���ͼ��
 * @author Administrator
 *
 */
public class View {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;
	
	//���±���Ϊ��̬�ģ�������HandleClient����ʲ���
	static DefaultTreeModel model;
	static DefaultMutableTreeNode root;
	static DrawPanel centerPanel;
	static List<String> list=new ArrayList<>();

	public View(){
		width = (int) (screensize.getWidth()*0.7);
		height = (int) (screensize.getHeight()*0.8);
	}
	
	//������ͼ
	public void create(){
		//�õ����ݴ���
		JFrame frame=new JFrame("Զ����Ļ����ϵͳ");
		Container container=frame.getContentPane();
		
		//���
		JPanel leftPanel=new JPanel();
		leftPanel.setBackground(Color.darkGray);
		container.add(leftPanel,BorderLayout.WEST);
		//��
		root=new DefaultMutableTreeNode("�������ӵı��ض�");
		model=new DefaultTreeModel(root);
		JTree tree=new JTree(model);
		tree.setBackground(Color.darkGray);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				JTree tree=(JTree) e.getSource();
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				try {
					String nodeName = selectionNode.toString();
					Server.curKey = nodeName;
				} catch (NullPointerException n){
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
		JScrollPane jsp=new JScrollPane(tree);
		JScrollBar bar=jsp.getHorizontalScrollBar();
		bar.setBackground(Color.darkGray);
		jsp.setBorder(null);
		leftPanel.add(jsp);
		
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				Server.serverLive=false;
			}
			
		});
		centerPanel=new DrawPanel();
		container.add(new JScrollPane(centerPanel));
		frame.setSize(width,height);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public class MyTreeCellRenderer extends DefaultTreeCellRenderer{
		public MyTreeCellRenderer() {
			super();
			this.setBackgroundNonSelectionColor(Color.darkGray);
			this.setLeafIcon(new ImageIcon("img/link_success.png"));
			this.setTextNonSelectionColor(Color.white);
		}

//		@Override
//		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//
//			return this;
//		}
	}


	/**
	 * ������ڵ�
	 * @param node
	 */
	public void setTreeNode(List<String> l){
		list=l;
		root.removeAllChildren();
		for(int i=0;i<list.size();i++){
			DefaultMutableTreeNode node1=new DefaultMutableTreeNode(list.get(i));
			root.add(node1);
		}
		model.reload();
	}
	
	public List<String> addValue(String key){
		list.add(key);
		return list;
	}
	
	public List<String> removeValue(String key){
		list.remove(key);
		return list;
	}

	public List<String> disableValue(String Key){

		return list;
	}

	public void disableNode(List<String> l){

	}

	public void clear(){
		list.clear();
	}
	
}
