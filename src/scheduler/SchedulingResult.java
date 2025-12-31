package scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the results of a scheduling algorithm execution
 */
public class SchedulingResult {
    private String algorithmName;
    private List<Task> tasks;
    private List<TimeSlot> schedule;
    private double averageWaitingTime;
    private double averageTurnaroundTime;
    private int totalCompletionTime;
    private int missedDeadlines;
    private double cpuUtilization;
    
    public SchedulingResult(String algorithmName) {
        this.algorithmName = algorithmName;
        this.schedule = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    
    public void addTimeSlot(TimeSlot slot) {
        schedule.add(slot);
    }
    
    public void calculateStatistics() {
        if (tasks.isEmpty()) return;
        
        int totalWaiting = 0;
        int totalTurnaround = 0;
        int completed = 0;
        
        for (Task task : tasks) {
            if (task.getCompletionTime() >= 0) {
                totalWaiting += task.getWaitingTime();
                totalTurnaround += task.getTurnaroundTime();
                completed++;
            }
        }
        
        if (completed > 0) {
            averageWaitingTime = (double) totalWaiting / completed;
            averageTurnaroundTime = (double) totalTurnaround / completed;
        }
        
        // Calculate total completion time
        totalCompletionTime = schedule.isEmpty() ? 0 : 
            schedule.get(schedule.size() - 1).getEndTime();
        
        // Count missed deadlines
        missedDeadlines = 0;
        for (Task task : tasks) {
            if (task.getDeadline() > 0 && task.getCompletionTime() > task.getDeadline()) {
                missedDeadlines++;
            }
        }
        
        // Calculate CPU utilization
        int totalBusyTime = 0;
        for (TimeSlot slot : schedule) {
            totalBusyTime += slot.getDuration();
        }
        cpuUtilization = totalCompletionTime > 0 ? 
            (double) totalBusyTime / totalCompletionTime * 100 : 0;
    }
    
    // Getters
    public String getAlgorithmName() { return algorithmName; }
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    public List<TimeSlot> getSchedule() { return schedule; }
    public double getAverageWaitingTime() { return averageWaitingTime; }
    public double getAverageTurnaroundTime() { return averageTurnaroundTime; }
    public int getTotalCompletionTime() { return totalCompletionTime; }
    public int getMissedDeadlines() { return missedDeadlines; }
    public double getCpuUtilization() { return cpuUtilization; }
}

