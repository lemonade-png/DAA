package scheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CPU Task Scheduling Using Greedy Algorithms: A Real-World Operating System Approach
 * DAA Assignment: Design and Analysis of Algorithms
 * 
 * This application demonstrates how greedy algorithms are applied in real-world
 * operating systems for efficient CPU task scheduling. It implements and analyzes
 * common greedy-based scheduling algorithms including Shortest Job First (SJF),
 * Priority Scheduling, and Earliest Deadline First (EDF).
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
        setTitle("CPU Task Scheduling Using Greedy Algorithms - DAA Assignment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Top panel - Task input
        JPanel topPanel = createTaskInputPanel();
        
        // Center panel - Results and schedule
        JPanel centerPanel = createResultsPanel();
        
        // Bottom panel - Statistics
        JPanel bottomPanel = createStatisticsPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Auto-populate sample tasks for demonstration
        SwingUtilities.invokeLater(() -> loadSampleTasks());
    }
    
    private JPanel createTaskInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("CPU Task Management"));
        
        // Task table
        String[] columns = {"Task ID", "Process Name", "Arrival Time", "Burst Time (ms)", "Deadline", "Priority"};
        taskTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Integer.class : String.class;
            }
        };
        taskTable = new JTable(taskTableModel);
        taskTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Selected");
        JButton clearButton = new JButton("Clear All");
        JButton loadSampleButton = new JButton("Load Sample Tasks");
        
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeSelectedTask());
        clearButton.addActionListener(e -> clearAllTasks());
        loadSampleButton.addActionListener(e -> loadSampleTasks());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loadSampleButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Left side - Algorithm selection and results table
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Greedy Scheduling Algorithms"));
        
        // Algorithm selection
        JPanel algoPanel = new JPanel(new FlowLayout());
        algoPanel.add(new JLabel("Algorithm:"));
        schedulerComboBox = new JComboBox<>();
        for (Scheduler s : schedulerManager.getSchedulers()) {
            schedulerComboBox.addItem(s);
        }
        JButton runButton = new JButton("Run Selected Algorithm");
        JButton compareButton = new JButton("Compare All Algorithms");
        runButton.addActionListener(e -> runSelectedScheduler());
        compareButton.addActionListener(e -> compareAllSchedulers());
        
        algoPanel.add(schedulerComboBox);
        algoPanel.add(runButton);
        algoPanel.add(compareButton);
        
        // Results table
        String[] resultColumns = {"Algorithm", "Avg Waiting Time", "Avg Turnaround Time", 
                                  "Completion Time", "CPU Utilization %", "Missed Deadlines"};
        resultTableModel = new DefaultTableModel(resultColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(resultTableModel);
        resultTable.setRowHeight(25);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        
        leftPanel.add(algoPanel, BorderLayout.NORTH);
        leftPanel.add(resultScrollPane, BorderLayout.CENTER);
        
        // Right side - Gantt chart and schedule
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart & Execution Schedule"));
        
        scheduleArea = new JTextArea(15, 40);
        scheduleArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scheduleArea.setEditable(false);
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleArea);
        
        ganttChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gantt chart will be drawn here
            }
        };
        ganttChartPanel.setPreferredSize(new Dimension(800, 200));
        ganttChartPanel.setBackground(Color.WHITE);
        
        rightPanel.add(scheduleScrollPane, BorderLayout.CENTER);
        rightPanel.add(ganttChartPanel, BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Performance Metrics"));
        
        avgWaitingLabel = new JLabel("Avg Waiting Time: -");
        avgTurnaroundLabel = new JLabel("Avg Turnaround Time: -");
        completionTimeLabel = new JLabel("Completion Time: -");
        cpuUtilLabel = new JLabel("CPU Utilization: -");
        missedDeadlinesLabel = new JLabel("Missed Deadlines: -");
        
        panel.add(avgWaitingLabel);
        panel.add(avgTurnaroundLabel);
        panel.add(completionTimeLabel);
        panel.add(cpuUtilLabel);
        panel.add(missedDeadlinesLabel);
        
        return panel;
    }
    
    private void addTask() {
        TaskInputDialog dialog = new TaskInputDialog(this);
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
        // Realistic CPU tasks representing different processes
        tasks.add(new Task(1, "Browser", 0, 5, 10, 3));      // Web browser process
        tasks.add(new Task(2, "System", 1, 3, 8, 1));        // System process (high priority)
        tasks.add(new Task(3, "Media", 2, 8, 15, 4));        // Media player process
        tasks.add(new Task(4, "Editor", 3, 6, 12, 2));       // Text editor process
        tasks.add(new Task(5, "Backup", 4, 4, 9, 5));        // Background backup process
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
        
        // Display first result's details
        if (!results.isEmpty()) {
            displayResult(results.get(0));
        }
    }
    
    private void displayResult(SchedulingResult result) {
        // Update statistics labels
        avgWaitingLabel.setText(String.format("Avg Waiting: %.2f", result.getAverageWaitingTime()));
        avgTurnaroundLabel.setText(String.format("Avg Turnaround: %.2f", result.getAverageTurnaroundTime()));
        completionTimeLabel.setText("Completion Time: " + result.getTotalCompletionTime());
        cpuUtilLabel.setText(String.format("CPU Utilization: %.2f%%", result.getCpuUtilization()));
        missedDeadlinesLabel.setText("Missed Deadlines: " + result.getMissedDeadlines());
        
        // Display schedule
        StringBuilder scheduleText = new StringBuilder();
        scheduleText.append("Schedule for: ").append(result.getAlgorithmName()).append("\n\n");
        scheduleText.append("Time Slots:\n");
        for (TimeSlot slot : result.getSchedule()) {
            scheduleText.append(slot.toString()).append("\n");
        }
        
        scheduleText.append("\nTask Details:\n");
        scheduleText.append(String.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", 
            "Task", "Start", "Complete", "Waiting", "Turnaround", "Deadline"));
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
        
        // Draw Gantt chart
        drawGanttChart(result);
    }
    
    private void drawGanttChart(SchedulingResult result) {
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(new BorderLayout());
        
        if (result.getSchedule().isEmpty()) {
            ganttChartPanel.repaint();
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
                
                // Draw time scale
                g2d.setColor(Color.BLACK);
                g2d.drawLine(startX, startY + height, startX + width, startY + height);
                for (int i = 0; i <= maxTime; i += Math.max(1, maxTime / 10)) {
                    int x = startX + (i * width / maxTime);
                    g2d.drawLine(x, startY + height, x, startY + height + 5);
                    g2d.drawString(String.valueOf(i), x - 5, startY + height + 20);
                }
                
                // Draw Gantt bars
                Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, 
                                 Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
                int colorIndex = 0;
                
                int barHeight = Math.max(20, height / result.getSchedule().size());
                int yPos = startY;
                
                for (TimeSlot slot : result.getSchedule()) {
                    int x1 = startX + (slot.getStartTime() * width / maxTime);
                    int x2 = startX + (slot.getEndTime() * width / maxTime);
                    int barWidth = x2 - x1;
                    
                    Color color = colors[colorIndex % colors.length];
                    g2d.setColor(color);
                    g2d.fillRect(x1, yPos, barWidth, barHeight - 2);
                    
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x1, yPos, barWidth, barHeight - 2);
                    
                    // Draw task name
                    if (barWidth > 20) {
                        g2d.setColor(Color.WHITE);
                        FontMetrics fm = g2d.getFontMetrics();
                        String label = slot.getTask().getName();
                        int textX = x1 + (barWidth - fm.stringWidth(label)) / 2;
                        int textY = yPos + barHeight / 2 + fm.getAscent() / 2 - 2;
                        g2d.drawString(label, textX, textY);
                    }
                    
                    yPos += barHeight;
                    colorIndex++;
                }
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(800, Math.max(200, result.getSchedule().size() * 30 + 60)));
        ganttChartPanel.add(new JScrollPane(chartPanel), BorderLayout.CENTER);
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

