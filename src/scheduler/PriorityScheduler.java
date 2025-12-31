package scheduler;

import java.util.*;

/**
 * Priority Scheduling - Greedy algorithm
 * Always selects the task with highest priority (lower number = higher priority)
 */
public class PriorityScheduler implements Scheduler {
    private boolean preemptive;
    
    public PriorityScheduler(boolean preemptive) {
        this.preemptive = preemptive;
    }
    
    @Override
    public SchedulingResult schedule(List<Task> tasks) {
        SchedulingResult result = new SchedulingResult(getAlgorithmName());
        List<Task> taskList = new ArrayList<>();
        for (Task t : tasks) {
            taskList.add(new Task(t)); // Create copies
        }
        result.setTasks(taskList);
        
        // Sort by arrival time
        taskList.sort(Comparator.comparingInt(Task::getArrivalTime));
        
        PriorityQueue<Task> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt(Task::getPriority)
                .thenComparingInt(Task::getArrivalTime)
        );
        
        int currentTime = 0;
        int taskIndex = 0;
        Task currentTask = null;
        int lastSwitchTime = 0;
        
        while (taskIndex < taskList.size() || !readyQueue.isEmpty() || currentTask != null) {
            // Add all tasks that have arrived
            while (taskIndex < taskList.size() && 
                   taskList.get(taskIndex).getArrivalTime() <= currentTime) {
                Task arriving = taskList.get(taskIndex);
                readyQueue.offer(arriving);
                taskIndex++;
                
                // Preempt if new task has higher priority (if preemptive)
                if (preemptive && currentTask != null && 
                    arriving.getPriority() < currentTask.getPriority()) {
                    // Save current task progress
                    if (currentTime > lastSwitchTime) {
                        result.addTimeSlot(new TimeSlot(currentTask, lastSwitchTime, currentTime));
                        currentTask.execute(currentTime - lastSwitchTime);
                    }
                    readyQueue.offer(currentTask);
                    currentTask = readyQueue.poll();
                    lastSwitchTime = currentTime;
                }
            }
            
            // Start new task if CPU is idle
            if (currentTask == null && !readyQueue.isEmpty()) {
                currentTask = readyQueue.poll();
                if (currentTask.getStartTime() < 0) {
                    currentTask.setStartTime(currentTime);
                }
                lastSwitchTime = currentTime;
            }
            
            if (currentTask == null) {
                // No tasks ready, advance time
                if (taskIndex < taskList.size()) {
                    currentTime = taskList.get(taskIndex).getArrivalTime();
                } else {
                    break;
                }
                continue;
            }
            
            if (preemptive) {
                // Execute for 1 time unit (preemptive)
                currentTime++;
                currentTask.execute(1);
                
                // Check if task completed
                if (currentTask.isCompleted()) {
                    result.addTimeSlot(new TimeSlot(currentTask, lastSwitchTime, currentTime));
                    currentTask.setCompletionTime(currentTime);
                    currentTask = null;
                    lastSwitchTime = currentTime;
                }
            } else {
                // Execute until completion (non-preemptive)
                int executionTime = currentTask.getRemainingTime();
                result.addTimeSlot(new TimeSlot(currentTask, currentTime, currentTime + executionTime));
                currentTime += executionTime;
                currentTask.setCompletionTime(currentTime);
                currentTask = null;
                lastSwitchTime = currentTime;
            }
        }
        
        result.calculateStatistics();
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return preemptive ? "Priority Scheduling (Preemptive)" : "Priority Scheduling (Non-Preemptive)";
    }
}

