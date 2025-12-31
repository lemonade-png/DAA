package scheduler;

/**
 * Represents a time slot in the CPU schedule
 */
public class TimeSlot {
    private Task task;
    private int startTime;
    private int endTime;
    
    public TimeSlot(Task task, int startTime, int endTime) {
        this.task = task;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public Task getTask() { return task; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }
    public int getDuration() { return endTime - startTime; }
    
    @Override
    public String toString() {
        return String.format("[%d-%d: %s]", startTime, endTime, task.getName());
    }
}

