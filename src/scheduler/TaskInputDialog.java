package scheduler;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for inputting CPU task/process details
 * Used in the CPU Task Scheduling system to add new processes
 */
public class TaskInputDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextField deadlineField;
    private JTextField priorityField;
    private boolean confirmed = false;
    private Task task;
    private static int nextId = 1;
    
    public TaskInputDialog(JFrame parent) {
        super(parent, "Add CPU Task", true);
        initializeDialog();
    }
    
    private void initializeDialog() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID field
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setText(String.valueOf(nextId));
        add(idField, gbc);
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Process Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setText("P" + nextId);
        add(nameField, gbc);
        
        // Arrival Time field
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Arrival Time (ms):"), gbc);
        gbc.gridx = 1;
        arrivalTimeField = new JTextField(15);
        arrivalTimeField.setText("0");
        add(arrivalTimeField, gbc);
        
        // Burst Time field
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Burst Time (ms):"), gbc);
        gbc.gridx = 1;
        burstTimeField = new JTextField(15);
        burstTimeField.setText("5");
        add(burstTimeField, gbc);
        
        // Deadline field
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Deadline (ms, -1 for none):"), gbc);
        gbc.gridx = 1;
        deadlineField = new JTextField(15);
        deadlineField.setText("-1");
        add(deadlineField, gbc);
        
        // Priority field
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Priority (lower = higher priority):"), gbc);
        gbc.gridx = 1;
        priorityField = new JTextField(15);
        priorityField.setText("0");
        add(priorityField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(e -> {
            if (validateAndCreateTask()) {
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);
        
        pack();
        setLocationRelativeTo(getParent());
    }
    
    private boolean validateAndCreateTask() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return false;
            }
            int arrivalTime = Integer.parseInt(arrivalTimeField.getText().trim());
            int burstTime = Integer.parseInt(burstTimeField.getText().trim());
            if (burstTime <= 0) {
                JOptionPane.showMessageDialog(this, "Burst time must be positive!");
                return false;
            }
            int deadline = Integer.parseInt(deadlineField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());
            
            task = new Task(id, name, arrivalTime, burstTime, deadline, priority);
            nextId = Math.max(nextId, id + 1);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
            return false;
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Task getTask() {
        return task;
    }
}

