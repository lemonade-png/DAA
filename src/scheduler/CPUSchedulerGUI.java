package scheduler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CPU Task Scheduling Using Greedy Algorithms: A Real-World Operating System Approach
 * DAA Assignment: Design and Analysis of Algorithms
 * * This application demonstrates how greedy algorithms are applied in real-world
 * operating systems for efficient CPU task scheduling.
 */
public class CPUSchedulerGUI extends JFrame {
    private SchedulerManager schedulerManager;
    private DefaultTableModel taskTableModel;
    private DefaultTableModel resultTableModel;
    private JTable taskTable;
    private JTable resultTable;
    private JTextArea scheduleArea;
    private JComboBox<Scheduler> schedulerComboBox;
    private JLabel avgWaitingLabel;
    private JLabel avgTurnaroundLabel;
    private JLabel completionTimeLabel;
    private JLabel cpuUtilLabel;
    private JLabel missedDeadlinesLabel;
    private JPanel ganttChartPanel;
    
    public CPUSchedulerGUI() {
        schedulerManager = new SchedulerManager();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("CPU Task Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add gaps between regions
        
        // --- UPDATED MENU BAR (About Only) ---
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // About Menu
        JMenu aboutMenu = new JMenu("About"); // Renamed from Help
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        aboutMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        // -------------------------------------
        
        // Top panel - Task input
        JPanel topPanel = createTaskInputPanel();
        
        // Center panel - Results and schedule
        JPanel centerPanel = createResultsPanel();
        
        // Bottom panel - Statistics
        JPanel bottomPanel = createStatisticsPanel();
        
        // Add main padding
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainContainer);
        
        // Fixed window size
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Auto-populate sample tasks for demonstration
        SwingUtilities.invokeLater(() -> loadSampleTasks());
    }
    
    // --- SPECIFIC HELP METHODS ---
    
    private void showTaskHelp() {
        String info = 
            "TASK MANAGEMENT GUIDE\n\n" +
            "• Add Task: Opens a dialog to create a new process manually.\n" +
            "• Burst Time: The duration (in ms) the CPU needs to complete the task.\n" +
            "• Arrival Time: The time (in ms) when the task enters the ready queue.\n" +
            "• Priority: Determines order in Priority Scheduling. Lower number = Higher priority.\n" +
            "• Deadline: The time by which the task should ideally finish.\n" +
            "• Load Sample Tasks: Quickly fills the table with realistic demo data.";
        showInfoDialog(info, "Task Help");
    }

    private void showAlgoHelp() {
        String info = 
            "ALGORITHM GUIDE\n\n" +
            "1. Run Selected:\n" +
            "   Visualizes the single algorithm selected in the dropdown.\n\n" +
            "2. Compare All:\n" +
            "   Runs ALL algorithms on the current tasks and ranks them in the table.\n\n" +
            "Available Algorithms:\n" +
            "• FCFS: First Come First Served (Simple baseline)\n" +
            "• SJF: Shortest Job First (Minimizes waiting time)\n" +
            "• SRTF: Shortest Remaining Time First (Preemptive SJF)\n" +
            "• EDF: Earliest Deadline First (Prioritizes urgent deadlines)\n" +
            "• Priority: Executes highest priority tasks first";
        showInfoDialog(info, "Algorithm Help");
    }

    private void showResultHelp() {
        String info = 
            "READING RESULTS & CHARTS\n\n" +
            "• Gantt Chart (Bottom):\n" +
            "   The colored bars represent the CPU timeline. Each block shows exactly which task is controlling the CPU at that time.\n\n" +
            "• Schedule Log (Top):\n" +
            "   A text log showing start times, stop times, and context switches.\n\n" +
            "• Performance Metrics:\n" +
            "   - Avg Waiting Time: Time spent waiting in queue (Lower is better).\n" +
            "   - Turnaround Time: Total time from arrival to completion.\n" +
            "   - CPU Utilization: % of time CPU was busy (Higher is better).";
        showInfoDialog(info, "Results Guide");
    }

    private void showInfoDialog(String text, String title) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setMargin(new Insets(15, 15, 15, 15));
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBackground(new Color(250, 250, 250));
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scroll, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this, 
            "CPU Task Scheduling Simulator\n\n" +
            "Demonstrating Greedy Algorithms in OS Design\n\n" +
            "Made by Sarfaraz Ahmed Soomro\n" +
            "For DAA Course", 
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // -------------------------------
    
    private JPanel createTaskInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("CPU Task Management"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        // Task table setup...
        String[] columns = {"ID", "Process Name", "Arrival", "Burst", "Deadline", "Priority"};
        taskTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Integer.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        taskTable = new JTable(taskTableModel);
        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        TableColumnModel columnModel = taskTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(60);
        
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setPreferredSize(new Dimension(0, 140));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Selected");
        JButton clearButton = new JButton("Clear All");
        JButton loadSampleButton = new JButton("Load Sample Tasks");
        JButton helpButton = new JButton("?"); // Specific Help Button
        
        Dimension btnSize = new Dimension(140, 30);
        addButton.setPreferredSize(btnSize);
        removeButton.setPreferredSize(btnSize);
        clearButton.setPreferredSize(btnSize);
        loadSampleButton.setPreferredSize(btnSize);
        helpButton.setPreferredSize(new Dimension(40, 30));
        helpButton.setToolTipText("Task Management Guide");
        
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeSelectedTask());
        clearButton.addActionListener(e -> clearAllTasks());
        loadSampleButton.addActionListener(e -> loadSampleTasks());
        helpButton.addActionListener(e -> showTaskHelp());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loadSampleButton);
        buttonPanel.add(helpButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // --- Left Panel: Controls & Results Table ---
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Greedy Scheduling Algorithms"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        // Algorithm Control Panel
        JPanel algoControlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: Label and ComboBox
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        algoControlPanel.add(new JLabel("Select Algorithm:"), gbc);
        
        gbc.gridx = 1; 
        gbc.weightx = 1.0;
        schedulerComboBox = new JComboBox<>();
        for (Scheduler s : schedulerManager.getSchedulers()) {
            schedulerComboBox.addItem(s);
        }
        algoControlPanel.add(schedulerComboBox, gbc);
        
        // Row 2: Buttons
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton runButton = new JButton("Run Selected");
        JButton compareButton = new JButton("Compare All");
        JButton algoHelpBtn = new JButton("?"); // Specific Help Button
        
        runButton.setFont(runButton.getFont().deriveFont(Font.BOLD));
        compareButton.setFont(compareButton.getFont().deriveFont(Font.BOLD));
        algoHelpBtn.setPreferredSize(new Dimension(40, 25));
        algoHelpBtn.setToolTipText("Algorithm Guide");
        
        runButton.addActionListener(e -> runSelectedScheduler());
        compareButton.addActionListener(e -> compareAllSchedulers());
        algoHelpBtn.addActionListener(e -> showAlgoHelp());
        
        actionButtonPanel.add(runButton);
        actionButtonPanel.add(compareButton);
        actionButtonPanel.add(algoHelpBtn);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2; 
        algoControlPanel.add(actionButtonPanel, gbc);
        
        // Results Table
        String[] resultColumns = {"Algorithm", "Wait", "Turnaround", "Complete", "Util %", "Missed"};
        resultTableModel = new DefaultTableModel(resultColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(resultTableModel);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Column Sizing
        TableColumnModel resColModel = resultTable.getColumnModel();
        resColModel.getColumn(0).setPreferredWidth(200); 
        resColModel.getColumn(1).setPreferredWidth(60);  
        resColModel.getColumn(2).setPreferredWidth(80);  
        resColModel.getColumn(3).setPreferredWidth(70);  
        resColModel.getColumn(4).setPreferredWidth(60);  
        resColModel.getColumn(5).setPreferredWidth(60);  
        
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        
        leftPanel.add(algoControlPanel, BorderLayout.NORTH);
        leftPanel.add(resultScrollPane, BorderLayout.CENTER);
        
        // --- Right Panel: Text Schedule & Gantt Chart ---
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Execution Schedule & Gantt Chart"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        // Add a small header for the Right Panel to hold the Help Button
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton resultHelpBtn = new JButton("?");
        resultHelpBtn.setPreferredSize(new Dimension(40, 20));
        resultHelpBtn.setToolTipText("Results Guide");
        resultHelpBtn.addActionListener(e -> showResultHelp());
        rightHeader.add(resultHelpBtn);
        
        scheduleArea = new JTextArea();
        scheduleArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scheduleArea.setEditable(false);
        scheduleArea.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleArea);
        
        // Custom Gantt Panel
        ganttChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        ganttChartPanel.setLayout(new BorderLayout());
        ganttChartPanel.setBackground(Color.WHITE);
        ganttChartPanel.setPreferredSize(new Dimension(0, 250));
        
        JPanel ganttContainer = new JPanel(new BorderLayout());
        ganttContainer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        ganttContainer.add(ganttChartPanel, BorderLayout.CENTER);
        
        rightPanel.add(rightHeader, BorderLayout.NORTH); // Add help button row
        rightPanel.add(scheduleScrollPane, BorderLayout.CENTER);
        rightPanel.add(ganttContainer, BorderLayout.SOUTH);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(520);
        splitPane.setResizeWeight(0.4);
        splitPane.setContinuousLayout(true);
        
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0)); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Performance Metrics"),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        avgWaitingLabel = createStatLabel("Avg Waiting Time: -");
        avgTurnaroundLabel = createStatLabel("Avg Turnaround: -");
        completionTimeLabel = createStatLabel("Total Time: -");
        cpuUtilLabel = createStatLabel("CPU Utilization: -");
        missedDeadlinesLabel = createStatLabel("Missed Deadlines: -");
        
        panel.add(avgWaitingLabel);
        panel.add(avgTurnaroundLabel);
        panel.add(completionTimeLabel);
        panel.add(cpuUtilLabel);
        panel.add(missedDeadlinesLabel);
        
        return panel;
    }
    
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }
    
    private void addTask() {
        TaskInputDialog dialog = new TaskInputDialog(this);
        dialog.setVisible(true); 
        
        if (dialog.isConfirmed()) {
            Task task = dialog.getTask();
            Object[] row = {
                task.getId(),
                task.getName(),
                task.getArrivalTime(),
                task.getBurstTime(),
                task.getDeadline() > 0 ? task.getDeadline() : "-",
                task.getPriority()
            };
            taskTableModel.addRow(row);
            updateTasksFromTable();
        }
    }
    
    private void removeSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            taskTableModel.removeRow(selectedRow);
            updateTasksFromTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to remove.");
        }
    }
    
    private void clearAllTasks() {
        taskTableModel.setRowCount(0);
        updateTasksFromTable();
    }
    
    private void loadSampleTasks() {
        taskTableModel.setRowCount(0);
        List<Task> sampleTasks = createSampleTasks();
        for (Task task : sampleTasks) {
            Object[] row = {
                task.getId(),
                task.getName(),
                task.getArrivalTime(),
                task.getBurstTime(),
                task.getDeadline() > 0 ? task.getDeadline() : "-",
                task.getPriority()
            };
            taskTableModel.addRow(row);
        }
        updateTasksFromTable();
    }
    
    private List<Task> createSampleTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Browser", 0, 5, 10, 3));
        tasks.add(new Task(2, "System", 1, 3, 8, 1));
        tasks.add(new Task(3, "Media", 2, 8, 15, 4));
        tasks.add(new Task(4, "Editor", 3, 6, 12, 2));
        tasks.add(new Task(5, "Backup", 4, 4, 9, 5));
        return tasks;
    }
    
    private void updateTasksFromTable() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskTableModel.getRowCount(); i++) {
            int id = (Integer) taskTableModel.getValueAt(i, 0);
            String name = (String) taskTableModel.getValueAt(i, 1);
            int arrivalTime = Integer.parseInt(taskTableModel.getValueAt(i, 2).toString());
            int burstTime = Integer.parseInt(taskTableModel.getValueAt(i, 3).toString());
            Object deadlineObj = taskTableModel.getValueAt(i, 4);
            int deadline = deadlineObj.toString().equals("-") ? -1 : Integer.parseInt(deadlineObj.toString());
            int priority = Integer.parseInt(taskTableModel.getValueAt(i, 5).toString());
            
            tasks.add(new Task(id, name, arrivalTime, burstTime, deadline, priority));
        }
        schedulerManager.setTasks(tasks);
    }
    
    private void runSelectedScheduler() {
        updateTasksFromTable();
        if (schedulerManager.getTasks().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add tasks first!");
            return;
        }
        
        Scheduler selected = (Scheduler) schedulerComboBox.getSelectedItem();
        SchedulingResult result = schedulerManager.runScheduler(selected);
        displayResult(result);
    }
    
    private void compareAllSchedulers() {
        updateTasksFromTable();
        if (schedulerManager.getTasks().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add tasks first!");
            return;
        }
        
        List<SchedulingResult> results = schedulerManager.runAllSchedulers();
        resultTableModel.setRowCount(0);
        
        for (SchedulingResult result : results) {
            Object[] row = {
                result.getAlgorithmName(),
                String.format("%.2f", result.getAverageWaitingTime()),
                String.format("%.2f", result.getAverageTurnaroundTime()),
                result.getTotalCompletionTime(),
                String.format("%.2f", result.getCpuUtilization()),
                result.getMissedDeadlines()
            };
            resultTableModel.addRow(row);
        }
        
        if (!results.isEmpty()) {
            displayResult(results.get(0));
        }
    }
    
    private void displayResult(SchedulingResult result) {
        avgWaitingLabel.setText(String.format("Avg Waiting: %.2f", result.getAverageWaitingTime()));
        avgTurnaroundLabel.setText(String.format("Avg Turnaround: %.2f", result.getAverageTurnaroundTime()));
        completionTimeLabel.setText("Total Time: " + result.getTotalCompletionTime());
        cpuUtilLabel.setText(String.format("CPU Utilization: %.2f%%", result.getCpuUtilization()));
        missedDeadlinesLabel.setText("Missed Deadlines: " + result.getMissedDeadlines());
        
        StringBuilder scheduleText = new StringBuilder();
        scheduleText.append("Algorithm: ").append(result.getAlgorithmName()).append("\n");
        scheduleText.append("===========================================\n\n");
        scheduleText.append("Execution Order (Time Slots):\n");
        scheduleText.append("----------------------------\n");
        for (TimeSlot slot : result.getSchedule()) {
            scheduleText.append(slot.toString()).append("\n");
        }
        
        scheduleText.append("\nDetailed Task Metrics:\n");
        scheduleText.append("----------------------------------------------------------------\n");
        scheduleText.append(String.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", 
            "Task", "Start", "Finish", "Wait", "Turnaround", "Deadline"));
        scheduleText.append("----------------------------------------------------------------\n");
        
        for (Task task : result.getTasks()) {
            scheduleText.append(String.format("%-10s %-10d %-10d %-10d %-10d %-10s\n",
                task.getName(),
                task.getStartTime() >= 0 ? task.getStartTime() : -1,
                task.getCompletionTime() >= 0 ? task.getCompletionTime() : -1,
                task.getWaitingTime() >= 0 ? task.getWaitingTime() : -1,
                task.getTurnaroundTime() >= 0 ? task.getTurnaroundTime() : -1,
                task.getDeadline() > 0 ? String.valueOf(task.getDeadline()) : "-"));
        }
        
        scheduleArea.setText(scheduleText.toString());
        scheduleArea.setCaretPosition(0); 
        drawGanttChart(result);
    }
    
    private void drawGanttChart(SchedulingResult result) {
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(new BorderLayout());
        
        if (result.getSchedule().isEmpty()) {
            ganttChartPanel.repaint();
            ganttChartPanel.revalidate();
            return;
        }
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int maxTime = result.getTotalCompletionTime();
                int width = getWidth() - 40;
                int height = getHeight() - 40;
                int startX = 20;
                int startY = 20;
                
                if (maxTime == 0) return;
                
                // Draw Axis
                g2d.setColor(Color.GRAY);
                g2d.drawLine(startX, startY + height, startX + width, startY + height);
                g2d.setColor(Color.BLACK);
                
                // Draw Time Markers
                for (int i = 0; i <= maxTime; i += Math.max(1, maxTime / 10)) {
                    int x = startX + (i * width / maxTime);
                    g2d.drawLine(x, startY + height, x, startY + height + 5);
                    g2d.drawString(String.valueOf(i), x - 5, startY + height + 20);
                }
                
                // Draw Bars
                Color[] colors = {
                    new Color(66, 133, 244),  // Google Blue
                    new Color(15, 157, 88),   // Google Green
                    new Color(219, 68, 55),   // Google Red
                    new Color(244, 180, 0),   // Google Yellow
                    new Color(171, 71, 188),  // Purple
                    new Color(0, 172, 193),   // Cyan
                    new Color(255, 112, 67),  // Deep Orange
                    new Color(158, 158, 158)  // Grey
                };
                
                int colorIndex = 0;
                int barHeight = Math.max(25, height / result.getSchedule().size());
                int yPos = startY;
                
                for (TimeSlot slot : result.getSchedule()) {
                    int x1 = startX + (slot.getStartTime() * width / maxTime);
                    int x2 = startX + (slot.getEndTime() * width / maxTime);
                    int barWidth = Math.max(x2 - x1, 1); 
                    
                    Color color = colors[colorIndex % colors.length];
                    
                    // Shadow effect
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRect(x1 + 2, yPos + 2, barWidth, barHeight - 5);
                    
                    // Main Bar
                    g2d.setColor(color);
                    g2d.fillRect(x1, yPos, barWidth, barHeight - 5);
                    
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(x1, yPos, barWidth, barHeight - 5);
                    
                    // Label
                    if (barWidth > 25) {
                        g2d.setColor(Color.WHITE);
                        FontMetrics fm = g2d.getFontMetrics();
                        String label = slot.getTask().getName();
                        
                        if (fm.stringWidth(label) > barWidth - 4) {
                            label = label.substring(0, Math.min(label.length(), 3)) + ".";
                        }
                        
                        int textX = x1 + (barWidth - fm.stringWidth(label)) / 2;
                        int textY = yPos + (barHeight - 5) / 2 + fm.getAscent() / 2 - 2;
                        g2d.drawString(label, textX, textY);
                    }
                    
                    yPos += barHeight;
                    colorIndex++;
                }
            }
        };
        
        // Use parent container width since window is fixed size
        int panelWidth = ganttChartPanel.getWidth() > 0 ? ganttChartPanel.getWidth() - 20 : 600;
        chartPanel.setPreferredSize(new Dimension(panelWidth, Math.max(180, result.getSchedule().size() * 35 + 50)));
        chartPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null); 
        
        ganttChartPanel.add(scrollPane, BorderLayout.CENTER);
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CPUSchedulerGUI().setVisible(true);
        });
    }
}