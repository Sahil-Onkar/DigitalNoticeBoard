package ui;

import model.Category;
import model.Department;
import model.Notice;
import model.User;
import service.CategoryService;
import service.DepartmentService;
import service.NoticeService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddNoticeFrame extends JFrame {
    private final NoticeManagementFrame managementFrame; private final User currentUser;
    private JTextField titleField,publishDateField,expiryDateField; private JTextArea descriptionArea; private JComboBox<Category> categoryBox; private JComboBox<Department> departmentBox; private JComboBox<String> priorityBox,statusBox;
    private final NoticeService noticeService=new NoticeService(); private final CategoryService categoryService=new CategoryService(); private final DepartmentService departmentService=new DepartmentService();
    public AddNoticeFrame(NoticeManagementFrame frame,User user){managementFrame=frame;currentUser=user;UITheme.styleFrame(this,"Add Notice",720,700);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);createUI();setVisible(true);}
    private void createUI(){
        JPanel root=new JPanel(new BorderLayout(0,15));root.setBackground(UITheme.BG);root.setBorder(BorderFactory.createEmptyBorder(22,28,22,28));
        JPanel head=new JPanel();head.setOpaque(false);head.setLayout(new BoxLayout(head,BoxLayout.Y_AXIS));head.add(UITheme.title("Create New Notice",24));head.add(Box.createVerticalStrut(5));head.add(UITheme.muted("Fill in the notice details below."));root.add(head,BorderLayout.NORTH);
        JPanel form=UITheme.card();form.setLayout(new GridBagLayout());GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(7,8,7,8);c.fill=GridBagConstraints.HORIZONTAL;c.weightx=1;c.anchor=GridBagConstraints.NORTHWEST;
        titleField=new JTextField();UITheme.styleField(titleField);descriptionArea=new JTextArea(5,30);descriptionArea.setFont(UITheme.font(13,Font.PLAIN));descriptionArea.setLineWrap(true);descriptionArea.setWrapStyleWord(true);descriptionArea.setBorder(new EmptyBorder(9,10,9,10));
        categoryBox=new JComboBox<>();for(Category x:categoryService.getAllCategories())categoryBox.addItem(x);UITheme.styleCombo(categoryBox);
        departmentBox=new JComboBox<>();Department all=new Department();all.setDepartmentId(0);all.setDepartmentCode("ALL");all.setDepartmentName("All Departments");departmentBox.addItem(all);for(Department x:departmentService.getAllDepartments())departmentBox.addItem(x);UITheme.styleCombo(departmentBox);
        priorityBox=new JComboBox<>(new String[]{"LOW","MEDIUM","HIGH"});UITheme.styleCombo(priorityBox);statusBox=new JComboBox<>(currentUser.getRole()==1?new String[]{"DRAFT","SUBMITTED","APPROVED","REJECTED","PUBLISHED"}:new String[]{"DRAFT","SUBMITTED"});UITheme.styleCombo(statusBox);
        publishDateField=new JTextField();expiryDateField=new JTextField();UITheme.styleField(publishDateField);UITheme.styleField(expiryDateField);publishDateField.setToolTipText("dd-MM-yyyy HH:mm");expiryDateField.setToolTipText("dd-MM-yyyy HH:mm");
        addRow(form,c,0,"Notice Title",titleField);addRow(form,c,1,"Description",new JScrollPane(descriptionArea));addRow(form,c,2,"Category",categoryBox);addRow(form,c,3,"Department",departmentBox);addRow(form,c,4,"Publish Date",publishDateField);addRow(form,c,5,"Expiry Date",expiryDateField);addRow(form,c,6,"Priority",priorityBox);addRow(form,c,7,"Status",statusBox);
        root.add(form,BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.setOpaque(false);JButton cancel=UITheme.button("Cancel",false),save=UITheme.button("Save Notice",true);cancel.addActionListener(e->dispose());save.addActionListener(e->saveNotice());buttons.add(cancel);buttons.add(save);root.add(buttons,BorderLayout.SOUTH);setContentPane(root);
    }
    private void addRow(JPanel p,GridBagConstraints c,int row,String label,JComponent comp){c.gridx=0;c.gridy=row;c.weightx=0;p.add(UITheme.muted(label),c);c.gridx=1;c.weightx=1;comp.setPreferredSize(new Dimension(400,42));p.add(comp,c);}
    private void saveNotice(){String title=titleField.getText().trim(),description=descriptionArea.getText().trim();if(title.isEmpty()||description.isEmpty()){JOptionPane.showMessageDialog(this,"Title and description are required.","Validation Error",JOptionPane.ERROR_MESSAGE);return;}DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");LocalDateTime pub=null,exp=null;try{if(!publishDateField.getText().trim().isEmpty())pub=LocalDateTime.parse(publishDateField.getText().trim(),f);if(!expiryDateField.getText().trim().isEmpty())exp=LocalDateTime.parse(expiryDateField.getText().trim(),f);}catch(DateTimeParseException e){JOptionPane.showMessageDialog(this,"Invalid date format. Use dd-MM-yyyy HH:mm","Date Format Error",JOptionPane.ERROR_MESSAGE);return;}if(pub!=null&&exp!=null&&!exp.isAfter(pub)){JOptionPane.showMessageDialog(this,"Expiry date must be after publish date.","Date Validation Error",JOptionPane.ERROR_MESSAGE);return;}Category cat=(Category)categoryBox.getSelectedItem();Department dep=(Department)departmentBox.getSelectedItem();if(cat==null)return;Notice n=new Notice();n.setCreatedBy(currentUser.getUserId());n.setTitle(title);n.setDescription(description);n.setCategoryId(cat.getCategoryId());n.setDepartmentId(dep==null||dep.getDepartmentId()==0?null:dep.getDepartmentId());n.setPriority((String)priorityBox.getSelectedItem());n.setStatus((String)statusBox.getSelectedItem());n.setPublishAt(pub);n.setExpiresAt(exp);boolean ok=noticeService.insertNotice(n,currentUser.getRole());JOptionPane.showMessageDialog(this,ok?"Notice added successfully!":"Failed to add notice.",ok?"Success":"Database Error",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);if(ok){managementFrame.loadNotices();dispose();}}
}
