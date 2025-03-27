
package UMS;
//import Dixon.DBconnection;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
//import javax.swing.JTable;

/** 
 *
 * @author dixon
 */
public final class Grade extends javax.swing.JFrame {
    private final DBconnection dbConnection;

    // ✅ Constructor
    public Grade() {
        initComponents();
        dbConnection = new DBconnection(); // Use the correct constructor
       
        loadExistingUnits("Select Course", GradeTable); // ✅ Load existing units (initially empty)
        loadCourses(CourseComboBox); // ✅ Load courses into the combo box

       // ✅ Load units when form opens
    String defaultCourse = (String) CourseComboBox.getSelectedItem();
    if (defaultCourse != null && !defaultCourse.equals("Select Course")) {
        loadExistingUnits(defaultCourse, GradeTable);
    }

    // ✅ Reload units when course selection changes
    CourseComboBox.addActionListener(e -> {
        String selectedCourse = (String) CourseComboBox.getSelectedItem();
        if (selectedCourse != null && !selectedCourse.equals("Select Course")) {
            loadExistingUnits(selectedCourse, GradeTable);
        }
    });
    }

    // ✅ Method to populate the course combo box
    public void loadCourses(JComboBox<String> courseComboBox) {
        String query = "SELECT DISTINCT Name FROM course"; 

        try (Connection con = DBconnection.connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            courseComboBox.removeAllItems(); // Clear existing items
            courseComboBox.addItem("Select Course"); // Default option
            while (rs.next()) {
                courseComboBox.addItem(rs.getString("Name")); // Add course names
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading courses: " + ex.getMessage());
        }
    }

    // ✅ Method to load students based on the selected course
    public void loadStudentsForCourse(String selectedCourse, JTable GradeTable) {
        DefaultTableModel model = (DefaultTableModel) GradeTable.getModel();
        model.setRowCount(0); // Clear table before adding new data

        // ✅ Step 1: Retrieve course code using course name
        String getCourseCodeQuery = "SELECT coursecode FROM course WHERE Name = ?";
        String courseCode = null;

        try (Connection con = DBconnection.connect();
             PreparedStatement ps = con.prepareStatement(getCourseCodeQuery)) {

            ps.setString(1, selectedCourse);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                courseCode = rs.getString("coursecode");
            } else {
                JOptionPane.showMessageDialog(null, "Course not found!");
                return;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching course code: " + ex.getMessage());
            return;
        }

        // ✅ Step 2: Fetch students using the retrieved coursecode
        String query = "SELECT e.studentid, g.unit1, g.unit2, g.unit3 " +
                       "FROM enrollment e " +
                       "LEFT JOIN grade g ON e.studentid = g.studentid AND e.coursecode = g.coursecode " +
                       "WHERE e.coursecode = ?";  

        try (Connection con = DBconnection.connect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{ 
                    rs.getString("studentid"),  
                    rs.getString("unit1"), 
                    rs.getString("unit2"), 
                    rs.getString("unit3")
                });
            }

            model.fireTableDataChanged();
            GradeTable.repaint();
            GradeTable.revalidate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading students: " + ex.getMessage());
        }
    }



    public void loadExistingUnits(String selectedCourse, JTable GradeTable) {
    DefaultTableModel model = (DefaultTableModel) GradeTable.getModel();
    model.setRowCount(0); // ✅ Clear existing rows

    if (selectedCourse == null || selectedCourse.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "No course selected!");
        return;
    }

    String query = "SELECT studentid, unit1, unit2, unit3 FROM enrollment " +
                   "WHERE coursecode = (SELECT coursecode FROM course WHERE Name = ?)";

    try (Connection con = DBconnection.connect();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setString(1, selectedCourse);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{ 
                rs.getString("studentid"),
                rs.getString("unit1"), 
                rs.getString("unit2"), 
                rs.getString("unit3")
            });
        }

        model.fireTableDataChanged();
        GradeTable.repaint();
        GradeTable.revalidate();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error loading units: " + ex.getMessage());
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        GradeTable = new javax.swing.JTable();
        CourseComboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtStudentID = new javax.swing.JTextField();
        txtUnit3 = new javax.swing.JTextField();
        txtUnit1 = new javax.swing.JTextField();
        txtUnit2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtInsert = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 255, 255));

        GradeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "StudentID", "Unit1", "Unit2", "Unit3"
            }
        ));
        jScrollPane1.setViewportView(GradeTable);

        CourseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("StudentID");

        txtStudentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStudentIDActionPerformed(evt);
            }
        });

        txtUnit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnit3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Unit1");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Unit2");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Unit3");

        txtInsert.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtInsert.setText("Insert");
        txtInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInsertActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(315, 315, 315)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtInsert)
                                .addGap(104, 104, 104)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUnit3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(txtUnit2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(CourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUnit1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(CourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUnit2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnit3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtInsert)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtStudentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStudentIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStudentIDActionPerformed

    private void txtUnit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnit3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnit3ActionPerformed

    private void txtInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInsertActionPerformed
// Retrieve input values from text fields 
String studentid = txtStudentID.getText().trim();
String unit1Text = txtUnit1.getText().trim();
String unit2Text = txtUnit2.getText().trim();
String unit3Text = txtUnit3.getText().trim();
String selectedCourse = (String) CourseComboBox.getSelectedItem(); 

// Check if any field is empty
if (studentid.isEmpty() || unit1Text.isEmpty() || unit2Text.isEmpty() || unit3Text.isEmpty() || selectedCourse.equals("Select Course")) {
    JOptionPane.showMessageDialog(null, "Please fill all fields and select a course!");
    return;
}

Connection con = null;

try {
    con = DBconnection.connect();
    con.setAutoCommit(false); 

    // Convert text fields to integers
    int unit1 = Integer.parseInt(unit1Text);
    int unit2 = Integer.parseInt(unit2Text);
    int unit3 = Integer.parseInt(unit3Text);

    // Check if student exists in the selected course
    String checkQuery = "SELECT 1 FROM enrollment WHERE studentid = ? AND coursecode = (SELECT coursecode FROM course WHERE Name = ?)";
    
    try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
        checkPs.setString(1, studentid);
        checkPs.setString(2, selectedCourse);
        ResultSet rs = checkPs.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, "Student not found for this course!");
            con.rollback();
            return; 
        }
    }

    // Update units in the database
    String updateQuery = "UPDATE enrollment SET unit1 = ?, unit2 = ?, unit3 = ? " +
                         "WHERE studentid = ? AND coursecode = (SELECT coursecode FROM course WHERE Name = ?)";

    try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
        ps.setInt(1, unit1);  // Use setInt instead of setString
        ps.setInt(2, unit2);
        ps.setInt(3, unit3);
        ps.setString(4, studentid);
        ps.setString(5, selectedCourse);

        int updated = ps.executeUpdate();

        if (updated > 0) {
            con.commit(); 
            JOptionPane.showMessageDialog(null, "Grades updated successfully!");
            
            // 🔥 Refresh table to get latest grades
            loadExistingUnits(selectedCourse, GradeTable);

            // 🔥 Update the JTable row directly
            DefaultTableModel model = (DefaultTableModel) GradeTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(studentid)) { 
                    model.setValueAt(unit1, i, 1); 
                    model.setValueAt(unit2, i, 2); 
                    model.setValueAt(unit3, i, 3); 
                    break; 
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update grades!");
            con.rollback();
        }
        
        // ✅ Clear input fields
        txtStudentID.setText("");
        txtUnit1.setText("");
        txtUnit2.setText("");
        txtUnit3.setText("");

    }
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(null, "Invalid input! Unit grades must be numbers.");
} catch (SQLException ex) {
    if (con != null) {
        try {
            con.rollback();
        } catch (SQLException ex2) {
            ex2.getMessage();
        }
    }
    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
} finally {
    if (con != null) {
        try {
            con.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
}


    }//GEN-LAST:event_txtInsertActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Grade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
          //</editor-fold>
          //</editor-fold>
          
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Grade().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CourseComboBox;
    private javax.swing.JTable GradeTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton txtInsert;
    private javax.swing.JTextField txtStudentID;
    private javax.swing.JTextField txtUnit1;
    private javax.swing.JTextField txtUnit2;
    private javax.swing.JTextField txtUnit3;
    // End of variables declaration//GEN-END:variables
}
//    private void loadStudentsForCourse(String studentid) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody   }

