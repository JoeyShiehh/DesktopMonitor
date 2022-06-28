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
 * 封装服务器端的视图层
 * @author Administrator
 *
 */
public class View {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;
	
	//以下必须为静态的，否则在HandleClient里访问不到
	static DefaultTreeModel model;
	static DefaultMutableTreeNode root;
	static DrawPanel centerPanel;
	static List<String> list=new ArrayList<>();

	public View(){
		width = (int) (screensize.getWidth()*0.7);
		height = (int) (screensize.getHeight()*0.8);
	}
	
	//创建视图
	public void create(){
		//得到内容窗格
		JFrame frame=new JFrame("远程屏幕监视系统");
		Container container=frame.getContentPane();
		
		//左侧
		JPanel leftPanel=new JPanel();
		leftPanel.setBackground(Color.darkGray);
		container.add(leftPanel,BorderLayout.WEST);
		//树
		root=new DefaultMutableTreeNode("所有连接的被控端");
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
		
		
//		设置树节点的样式
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
	 * 添加树节点
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
