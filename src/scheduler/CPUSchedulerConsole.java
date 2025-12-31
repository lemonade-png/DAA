package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based interface for CPU Task Scheduling
 */
public class CPUSchedulerConsole {
    private SchedulerManager schedulerManager;
    private Scanner scanner;
    
    public CPUSchedulerConsole() {
        schedulerManager = new SchedulerManager();
        scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("=== CPU Task Scheduler - Greedy Algorithms ===\n");
        
        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    runSingleScheduler();
                    break;
                case 4:
                    compareAllSchedulers();
                    break;
                case 5:
                    loadSampleTasks();
                    break;
                case 6:
                    clearTasks();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Run Single Scheduler");
        System.out.println("4. Compare All Schedulers");
        System.out.println("5. Load Sample Tasks");
        System.out.println("6. Clear All Tasks");
        System.out.println("7. Exit");
        System.out.println();
    }
    
    private void addTask() {
        System.out.println("\n--- Add Task ---");
        int id = getIntInput("Task ID: ");
        System.out.print("Task Name: ");
        String name = scanner.nextLine().trim();
        int arrivalTime = getIntInput("Arrival Time: ");
        int burstTime = getIntInput("Burst Time: ");
        int deadline = getIntInput("Deadline (-1 for none): ");
        int priority = getIntInput("Priority (lower = higher): ");
        
        Task task = new Task(id, name, arrivalTime, burstTime, deadline, priority);
        List<Task> tasks = schedulerManager.getTasks();
        tasks.add(task);
        schedulerManager.setTasks(tasks);
        System.out.println("Task added successfully!");
    }
    
    private void viewTasks() {
        List<Task> tasks = schedulerManager.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks added yet.");
            return;
        }
        
        System.out.println("\n--- Current Tasks ---");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n", 
            "ID", "Name", "Arrival", "Burst", "Deadline", "Priority");
        for (int i = 0; i < 65; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (Task task : tasks) {
            System.out.printf("%-5d %-10s %-10d %-10d %-10s %-10d\n",
                task.getId(),
                task.getName(),
                task.getArrivalTime(),
                task.getBurstTime(),
                task.getDeadline() > 0 ? String.valueOf(task.getDeadline()) : "-",
                task.getPriority());
        }
    }
    
    private void runSingleScheduler() {
        List<Task> tasks = schedulerManager.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available! Please add tasks first.");
            return;
        }
        
        System.out.println("\n--- Available Schedulers ---");
        List<Scheduler> schedulers = schedulerManager.getSchedulers();
        for (int i = 0; i < schedulers.size(); i++) {
            System.out.println((i + 1) + ". " + schedulers.get(i).getAlgorithmName());
        }
        
        int choice = getIntInput("\nSelect scheduler (1-" + schedulers.size() + "): ");
        if (choice < 1 || choice > schedulers.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        
        Scheduler selected = schedulers.get(choice - 1);
        SchedulingResult result = schedulerManager.runScheduler(selected);
        displayResult(result);
    }
    
    private void compareAllSchedulers() {
        List<Task> tasks = schedulerManager.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available! Please add tasks first.");
            return;
        }
        
        System.out.println("\n--- Comparing All Schedulers ---\n");
        List<SchedulingResult> results = schedulerManager.runAllSchedulers();
        
        // Print comparison table
        System.out.printf("%-35s %-15s %-15s %-15s %-15s %-15s\n",
            "Algorithm", "Avg Waiting", "Avg Turnaround", "Completion", "CPU Util %", "Missed DL");
        System.out.println(repeatString("-", 110));
        
        for (SchedulingResult result : results) {
            System.out.printf("%-35s %-15.2f %-15.2f %-15d %-15.2f %-15d\n",
                result.getAlgorithmName(),
                result.getAverageWaitingTime(),
                result.getAverageTurnaroundTime(),
                result.getTotalCompletionTime(),
                result.getCpuUtilization(),
                result.getMissedDeadlines());
        }
        
        // Find best algorithm for each metric
        System.out.println("\n--- Best Algorithms ---");
        SchedulingResult bestWaiting = results.stream()
            .min((a, b) -> Double.compare(a.getAverageWaitingTime(), b.getAverageWaitingTime()))
            .orElse(null);
        SchedulingResult bestTurnaround = results.stream()
            .min((a, b) -> Double.compare(a.getAverageTurnaroundTime(), b.getAverageTurnaroundTime()))
            .orElse(null);
        SchedulingResult bestUtilization = results.stream()
            .max((a, b) -> Double.compare(a.getCpuUtilization(), b.getCpuUtilization()))
            .orElse(null);
        
        if (bestWaiting != null) {
            System.out.println("Best Average Waiting Time: " + bestWaiting.getAlgorithmName() + 
                " (" + String.format("%.2f", bestWaiting.getAverageWaitingTime()) + ")");
        }
        if (bestTurnaround != null) {
            System.out.println("Best Average Turnaround Time: " + bestTurnaround.getAlgorithmName() + 
                " (" + String.format("%.2f", bestTurnaround.getAverageTurnaroundTime()) + ")");
        }
        if (bestUtilization != null) {
            System.out.println("Best CPU Utilization: " + bestUtilization.getAlgorithmName() + 
                " (" + String.format("%.2f", bestUtilization.getCpuUtilization()) + "%)");
        }
    }
    
    private void displayResult(SchedulingResult result) {
        System.out.println("\n=== " + result.getAlgorithmName() + " ===\n");
        
        // Print schedule
        System.out.println("Schedule:");
        for (TimeSlot slot : result.getSchedule()) {
            System.out.println("  " + slot.toString());
        }
        
        // Print task details
        System.out.println("\nTask Details:");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n",
            "Task", "Start", "Complete", "Waiting", "Turnaround", "Deadline");
        System.out.println(repeatString("-", 70));
        for (Task task : result.getTasks()) {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n",
                task.getName(),
                task.getStartTime() >= 0 ? String.valueOf(task.getStartTime()) : "-",
                task.getCompletionTime() >= 0 ? String.valueOf(task.getCompletionTime()) : "-",
                task.getWaitingTime() >= 0 ? String.valueOf(task.getWaitingTime()) : "-",
                task.getTurnaroundTime() >= 0 ? String.valueOf(task.getTurnaroundTime()) : "-",
                task.getDeadline() > 0 ? String.valueOf(task.getDeadline()) : "-");
        }
        
        // Print statistics
        System.out.println("\nStatistics:");
        System.out.println("  Average Waiting Time: " + String.format("%.2f", result.getAverageWaitingTime()));
        System.out.println("  Average Turnaround Time: " + String.format("%.2f", result.getAverageTurnaroundTime()));
        System.out.println("  Total Completion Time: " + result.getTotalCompletionTime());
        System.out.println("  CPU Utilization: " + String.format("%.2f", result.getCpuUtilization()) + "%");
        System.out.println("  Missed Deadlines: " + result.getMissedDeadlines());
    }
    
    private void loadSampleTasks() {
        List<Task> sampleTasks = new ArrayList<>();
        sampleTasks.add(new Task(1, "P1", 0, 5, 10, 3));
        sampleTasks.add(new Task(2, "P2", 1, 3, 8, 1));
        sampleTasks.add(new Task(3, "P3", 2, 8, 15, 4));
        sampleTasks.add(new Task(4, "P4", 3, 6, 12, 2));
        sampleTasks.add(new Task(5, "P5", 4, 4, 9, 5));
        
        schedulerManager.setTasks(sampleTasks);
        System.out.println("\nSample tasks loaded successfully!");
        viewTasks();
    }
    
    private void clearTasks() {
        schedulerManager.setTasks(new ArrayList<>());
        System.out.println("\nAll tasks cleared!");
    }
    
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }
    
    /**
     * Helper method to repeat a string a specified number of times.
     * Compatible with Java 8+ (String.repeat() requires Java 11+).
     */
    private String repeatString(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        new CPUSchedulerConsole().run();
    }
}

