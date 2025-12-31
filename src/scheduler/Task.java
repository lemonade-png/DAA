package scheduler;

/**
 * Represents a CPU task/process with scheduling properties
 * Used in the CPU Task Scheduling system to model real-world operating system processes
 * Each task competes for CPU resources and has properties like execution time, priority, and deadlines
 */
public class Task {
    private int id;
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int deadline;
    private int priority; // Lower number = higher priority
    private int startTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    
    public Task(int id, String name, int arrivalTime, int burstTime) {
        this(id, name, arrivalTime, burstTime, -1, 0);
    }
    
    public Task(int id, String name, int arrivalTime, int burstTime, int deadline, int priority) {
        this.id = id;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.deadline = deadline;
        this.priority = priority;
        this.startTime = -1;
        this.completionTime = -1;
        this.waitingTime = -1;
        this.turnaroundTime = -1;
    }
    
    // Copy constructor
    public Task(Task other) {
        this.id = other.id;
        this.name = other.name;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.remainingTime = other.burstTime;
        this.deadline = other.deadline;
        this.priority = other.priority;
        this.startTime = -1;
        this.completionTime = -1;
        this.waitingTime = -1;
        this.turnaroundTime = -1;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public int getDeadline() { return deadline; }
    public int getPriority() { return priority; }
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public int getCompletionTime() { return completionTime; }
    public void setCompletionTime(int completionTime) { 
        this.completionTime = completionTime;
        this.turnaroundTime = completionTime - arrivalTime;
        this.waitingTime = turnaroundTime - burstTime;
    }
    public int getWaitingTime() { return waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    
    public boolean isCompleted() {
        return remainingTime <= 0;
    }
    
    public void execute(int timeUnits) {
        remainingTime = Math.max(0, remainingTime - timeUnits);
    }
    
    public boolean missedDeadline(int currentTime) {
        return deadline > 0 && currentTime > deadline;
    }
    
    @Override
    public String toString() {
        return String.format("Task[%s: AT=%d, BT=%d, RT=%d, DL=%d, P=%d]", 
            name, arrivalTime, burstTime, remainingTime, deadline, priority);
    }
}

