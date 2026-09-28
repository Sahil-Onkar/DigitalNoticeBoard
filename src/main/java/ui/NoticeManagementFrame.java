package ui;

import model.Category;
import model.Notice;
import model.User;
import service.CategoryService;
import service.DepartmentService;
import service.NoticeService;
import service.UserService;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoticeManagementFrame extends JFrame {
    private final NoticeService noticeService = new NoticeService();
    private final CategoryService categoryService = new CategoryService();
    private final UserService userService = new UserService();
    private final DepartmentService departmentService = new DepartmentService();
    private final User currentUser;
    private JTable noticeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter, priorityFilter;
    private JComboBox<Category> categoryFilter;
    private dao.NoticeViewDAO noticeViewDAO = new dao.NoticeViewDAO();

    public NoticeManagementFrame(User user) {
        currentUser=user; UITheme.styleFrame(this,"Notice Management - Digital Notice Board",1200,700);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);createUI();loadNotices();setVisible(true);
    }

    private void createUI(){
        setLayout(new BorderLayout());
        JPanel header=new JPanel(new BorderLayout(15,0));header.setBackground(Color.WHITE);header.setBorder(BorderFactory.createEmptyBorder(20,26,18,26));
        JPanel ht=new JPanel();ht.setOpaque(false);ht.setLayout(new BoxLayout(ht,BoxLayout.Y_AXIS));ht.add(UITheme.title("Notice Management",24));ht.add(Box.createVerticalStrut(4));ht.add(UITheme.muted(currentUser.getRole()==1?"Manage all college notices and approvals.":"Create and manage your notices."));header.add(ht,BorderLayout.WEST);
        JButton add=UITheme.button("＋  Add Notice",true);add.addActionListener(e->new AddNoticeFrame(this,currentUser));header.add(add,BorderLayout.EAST);add(header,BorderLayout.NORTH);

        JPanel root=new JPanel(new BorderLayout(0,14));root.setBackground(UITheme.BG);root.setBorder(BorderFactory.createEmptyBorder(16,26,22,26));
        JPanel filters=UITheme.card();filters.setLayout(new GridBagLayout());GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(3,5,3,5);c.fill=GridBagConstraints.HORIZONTAL;c.gridy=0;
        searchField=new JTextField();UITheme.styleField(searchField);searchField.setPreferredSize(new Dimension(230,40));
        statusFilter=new JComboBox<>(new String[]{"ALL","DRAFT","SUBMITTED","APPROVED","REJECTED","PUBLISHED","EXPIRED"});UITheme.styleCombo(statusFilter);statusFilter.setPreferredSize(new Dimension(140,40));
        priorityFilter=new JComboBox<>(new String[]{"ALL","LOW","MEDIUM","HIGH"});UITheme.styleCombo(priorityFilter);priorityFilter.setPreferredSize(new Dimension(120,40));
        categoryFilter=new JComboBox<>();UITheme.styleCombo(categoryFilter);categoryFilter.setPreferredSize(new Dimension(150,40));Category all=new Category();all.setCategoryId(0);all.setCategoryName("ALL");categoryFilter.addItem(all);for(Category x:categoryService.getAllCategories())categoryFilter.addItem(x);
        addFilter(c,filters,"Search",searchField,0,0);addFilter(c,filters,"Status",statusFilter,2,0);addFilter(c,filters,"Priority",priorityFilter,4,0);addFilter(c,filters,"Category",categoryFilter,6,0);
        JButton search=UITheme.button("Search",true);search.addActionListener(e->searchNotices());c.gridx=8;c.weightx=0;filters.add(search,c);root.add(filters,BorderLayout.NORTH);

        String[] cols={"ID","Title","Category","Created By","Department","Priority","Status","Views"};tableModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int col){return false;}};noticeTable=new JTable(tableModel);UITheme.styleTable(noticeTable);UITheme.styleBadgeRenderer(noticeTable,5);UITheme.styleBadgeRenderer(noticeTable,6);noticeTable.getColumnModel().getColumn(0).setPreferredWidth(50);noticeTable.getColumnModel().getColumn(1).setPreferredWidth(260);noticeTable.getColumnModel().getColumn(7).setPreferredWidth(65);
        JScrollPane scroll=new JScrollPane(noticeTable);scroll.setBorder(new LineBorder(UITheme.BORDER));root.add(scroll,BorderLayout.CENTER);

        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));actions.setOpaque(false);JButton view=UITheme.button("View",false);JButton edit=UITheme.button("Edit",false);JButton delete=UITheme.button("Delete",false);JButton approve=UITheme.button("Approve",true);JButton reject=UITheme.button("Reject",false);JButton publish=UITheme.button("Publish",true);JButton refresh=UITheme.button("↻ Refresh",false);
        view.addActionListener(e->viewSelectedNotice());edit.addActionListener(e->editSelectedNotice());delete.addActionListener(e->deleteSelectedNotice());approve.addActionListener(e->updateSelectedNoticeStatus("APPROVED"));reject.addActionListener(e->updateSelectedNoticeStatus("REJECTED"));publish.addActionListener(e->updateSelectedNoticeStatus("PUBLISHED"));refresh.addActionListener(e->loadNotices());
        actions.add(view);actions.add(edit);actions.add(delete);if(currentUser.getRole()==1){actions.add(approve);actions.add(reject);actions.add(publish);}actions.add(Box.createHorizontalStrut(10));actions.add(refresh);root.add(actions,BorderLayout.SOUTH);add(root,BorderLayout.CENTER);
        searchField.addActionListener(e->searchNotices());statusFilter.addActionListener(e->searchNotices());priorityFilter.addActionListener(e->searchNotices());categoryFilter.addActionListener(e->searchNotices());
    }
    private void addFilter(GridBagConstraints c,JPanel p,String label,JComponent comp,int x,int y){c.gridx=x;c.gridy=y;c.weightx=0;p.add(UITheme.muted(label),c);c.gridx=x+1;c.weightx=1;p.add(comp,c);}

    public void loadNotices(){tableModel.setRowCount(0);List<Notice> list=currentUser.getRole()==1?noticeService.getAllNotices():noticeService.getNoticesByCreator(currentUser.getUserId());for(Notice n:list)addNoticeRow(n);}
    private void searchNotices(){String keyword=searchField.getText().trim();String status=(String)statusFilter.getSelectedItem();String priority=(String)priorityFilter.getSelectedItem();Category cat=(Category)categoryFilter.getSelectedItem();int id=cat==null?0:cat.getCategoryId();tableModel.setRowCount(0);List<Notice> list;if(keyword.isEmpty()&&"ALL".equals(status)&&"ALL".equals(priority)&&id==0){list=currentUser.getRole()==1?noticeService.getAllNotices():noticeService.getNoticesByCreator(currentUser.getUserId());}else{list=currentUser.getRole()==1?noticeService.searchNotices(keyword,status,priority,id):noticeService.searchNotices(keyword,status,priority,id,currentUser.getUserId());}for(Notice n:list)addNoticeRow(n);}
    private Notice getSelectedNotice(){int row=noticeTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Please select a notice first.","No Selection",JOptionPane.WARNING_MESSAGE);return null;}int modelRow=noticeTable.convertRowIndexToModel(row);int id=(Integer)tableModel.getValueAt(modelRow,0);for(Notice n:noticeService.getAllNotices())if(n.getNoticeId()==id)return n;return null;}
    private void viewSelectedNotice(){Notice n=getSelectedNotice();if(n!=null)new NoticeDetailsFrame(n,categoryService,userService,departmentService);}
    private void editSelectedNotice(){Notice n=getSelectedNotice();if(n!=null)new EditNoticeFrame(this,n,currentUser);}
    private void deleteSelectedNotice(){Notice n=getSelectedNotice();if(n==null)return;int choice=JOptionPane.showConfirmDialog(this,"Delete this notice?\n\n"+n.getTitle(),"Confirm Delete",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);if(choice!=JOptionPane.YES_OPTION)return;boolean ok=currentUser.getRole()==1?noticeService.deleteNotice(n.getNoticeId()):noticeService.deleteNotice(n.getNoticeId(),currentUser.getUserId());JOptionPane.showMessageDialog(this,ok?"Notice deleted successfully.":"Failed to delete notice.",ok?"Success":"Database Error",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);if(ok)loadNotices();}
    private void updateSelectedNoticeStatus(String status){Notice n=getSelectedNotice();if(n==null)return;if(currentUser.getRole()!=1)return;int choice=JOptionPane.showConfirmDialog(this,"Change status of:\n"+n.getTitle()+"\n\nNew status: "+status,"Confirm Status Change",JOptionPane.YES_NO_OPTION);if(choice!=JOptionPane.YES_OPTION)return;boolean ok=noticeService.updateNoticeStatus(n.getNoticeId(),status);JOptionPane.showMessageDialog(this,ok?"Notice status updated successfully.":"Failed to update notice status.",ok?"Success":"Database Error",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);if(ok)loadNotices();}
    private void addNoticeRow(Notice n){tableModel.addRow(new Object[]{n.getNoticeId(),n.getTitle(),categoryService.getCategoryNameById(n.getCategoryId()),userService.getUserNameById(n.getCreatedBy()),n.getDepartmentId()==null?"All Departments":departmentService.getDepartmentNameById(n.getDepartmentId()),n.getPriority(),n.getStatus(),noticeViewDAO.getViewCount(n.getNoticeId())});}
}
