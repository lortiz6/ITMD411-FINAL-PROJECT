package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

    // class level member objects
    Dao dao = new Dao(); // for CRUD operations
    Boolean chkIfAdmin = null;

    // Main menu object items
    private JMenu mnuFile = new JMenu("File");
    private JMenu mnuAdmin = new JMenu("Admin");
    private JMenu mnuTickets = new JMenu("Tickets");

    // Sub menu item objects for all Main menu item objects
    JMenuItem mnuItemOpenTicket;
    JMenuItem mnuItemViewTicket;
    JMenuItem mnuItemRefresh;
    JMenuItem mnuItemUpdate;
    JMenuItem mnuItemDelete;
    JMenuItem mnuItemExit;


    public Tickets(Boolean isAdmin) {

        chkIfAdmin = isAdmin;
        createMenu();
        prepareGUI();
    }

    private void createMenu() {
        // Initialize sub-menu items

        // initialize sub-menu item for File main menu
        mnuItemExit = new JMenuItem("Exit");
        // add to File main menu item
        mnuFile.add(mnuItemExit);
        // initialize sub-menu item for File main menu
        mnuItemRefresh = new JMenuItem("Refresh");
        // add to File main menu item
        mnuFile.add(mnuItemRefresh);

        //Admin only Tab
        if (chkIfAdmin) {
            // initialize first sub-menu items for Admin main menu
            mnuItemUpdate = new JMenuItem("Update Ticket");
            // add to Admin main menu item
            mnuAdmin.add(mnuItemUpdate);
            // initialize second sub menu items for Admin main menu
            mnuItemDelete = new JMenuItem("Delete Ticket");
            // add to Admin main menu item
            mnuAdmin.add(mnuItemDelete);
        }
        // initialize first sub menu item for Tickets main menu
        mnuItemOpenTicket = new JMenuItem("Open Ticket");
        // add to Ticket Main menu item
        mnuTickets.add(mnuItemOpenTicket);
        // initialize second sub menu item for Tickets main menu
        mnuItemViewTicket = new JMenuItem("View Ticket");
        // add to Ticket Main menu item
        mnuTickets.add(mnuItemViewTicket);

        /* Add action listeners for each desired menu item */
        mnuItemExit.addActionListener(this);
        mnuItemRefresh.addActionListener(this);
        if (chkIfAdmin) {
            mnuItemUpdate.addActionListener(this);
            mnuItemDelete.addActionListener(this);
        }
        mnuItemOpenTicket.addActionListener(this);
        mnuItemViewTicket.addActionListener(this);
    }

    private void prepareGUI() {
        // create JMenu bar
        UIManager.put("MenuBar.background", Color.LIGHT_GRAY);
        JMenuBar bar = new JMenuBar();
        bar.add(mnuFile); // add main menu items in order, to JMenuBar
        if (chkIfAdmin) {
            bar.add(mnuAdmin); // 3rd row
        }
        bar.add(mnuTickets);
        // add menu bar components to frame
        setJMenuBar(bar);

        addWindowListener(new WindowAdapter() {
            // define a window close operation
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });
        // set frame options
        setSize(400, 400);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setForeground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // implement actions for sub menu items
        //Exit the program
        if (e.getSource() == mnuItemExit) {
            System.out.println("Ticket System Exit Successful!");
            System.exit(0);
        }
        //Add a Ticket
        else if (e.getSource() == mnuItemOpenTicket) {

            // get ticket information
            String ticketName = JOptionPane.showInputDialog(null, "Enter Your Name: ");
            String ticketDesc = JOptionPane.showInputDialog(null, "Enter A Ticket Description: ");
            // insert ticket information to database
            int id = dao.insertRecords(ticketName, ticketDesc);

            // display results if successful or not to console / dialog box
            if (id != 0) {
                System.out.println("Ticket ID " + id + " Created Successfully!!!");
                JOptionPane.showMessageDialog(null, "Ticket ID " + id + " Was Created");
            } else
                System.out.println("Ticket cannot be created!!!");
        }
        //View and Refresh the Ticket
        else if (e.getSource() == mnuItemViewTicket || e.getSource() == mnuItemRefresh) {
            // retrieve all tickets details for viewing in JTable
            try {
                // Use JTable built in functionality to build a table model and
                // display the table model off your result set!!!
                JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
                jt.setBounds(30, 40, 200, 400);
                jt.setBackground(Color.LIGHT_GRAY);
                jt.setForeground(Color.BLACK);
                jt.getTableHeader().setBackground(Color.BLACK);
                jt.getTableHeader().setForeground(Color.WHITE);
                JScrollPane sp = new JScrollPane(jt);
                add(sp);
                setVisible(true); // refreshes or repaints frame on screen
                System.out.println("Ticket Viewed Successully!");

            } catch (SQLException e1) {
                System.out.println("Ticket Viewing Failed!");
                e1.printStackTrace();
            }
        }
        // Delete a Ticket
        else if (e.getSource() == mnuItemDelete) {
            try {
                String ticketID = JOptionPane.showInputDialog(null, "Enter the Ticket ID To Be Delete: ");

                int answer = JOptionPane.showConfirmDialog(null,
                        "Are you Sure You Want To Delete Ticket " + ticketID + "?", "Delete Ticket",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    dao.deleteRecords(ticketID);
                    JOptionPane.showMessageDialog(null, "You Deleted Ticket ID: " + ticketID);
                    System.out.println("A Record Has Been Deleted Successfully!");
                } else {
                    dispose();
                }
                JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
                jt.setBounds(30, 40, 200, 400);
                jt.setBackground(Color.CYAN);
                jt.setForeground(Color.BLACK);
                jt.getTableHeader().setBackground(Color.BLACK);
                jt.getTableHeader().setForeground(Color.WHITE);
                JScrollPane sp = new JScrollPane(jt);
                add(sp);
                setVisible(true); // refreshes or repaints frame on screen
                System.out.println("Ticket Viewed Successully!");
            } catch (Exception se) {
                se.printStackTrace();
            }
        }
        // Update a Ticket
        else if (e.getSource() == mnuItemUpdate) {
            try {
                String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID You Want To Update: ");
                String[] option = { "Update Name", "Update Description" };
                String answer = (String) JOptionPane.showInputDialog(null, "Which Would You Like To Update?",
                        "Update Ticket", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

                String oldinfo;
                String newinfo;

                if (answer.equals("Update Name")) {
                    oldinfo = "ticket_issuer";
                    newinfo = JOptionPane.showInputDialog(null, "Enter New Name: ");
                } else {
                    oldinfo = "ticket_description";
                    newinfo = JOptionPane.showInputDialog(null, "Enter New Description: ");
                }

                dao.updateRecords(ticketID, oldinfo, newinfo);
                System.out.println("Ticket ID " + ticketID + " Has Changes Made to It.");
                JOptionPane.showMessageDialog(null, "Ticket ID " + ticketID + " Was Successfully Updated!");

                JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
                jt.setBounds(30, 40, 200, 400);
                jt.setBackground(Color.CYAN);
                jt.setForeground(Color.BLACK);
                jt.getTableHeader().setBackground(Color.BLACK);
                jt.getTableHeader().setForeground(Color.WHITE);
                JScrollPane sp = new JScrollPane(jt);
                add(sp);
                setVisible(true); // refreshes or repaints frame on screen
                System.out.println("Ticket Viewed Successully!");
            } catch (Exception se) {
                se.printStackTrace();
            }
        }
    }
}