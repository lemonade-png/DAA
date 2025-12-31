package scheduler;

import java.util.*;

/**
 * Shortest Remaining Time First (SRTF) - Preemptive greedy algorithm
 * Always selects the task with the shortest remaining time
 */
public class ShortestRemainingTimeFirstScheduler implements Scheduler {
    
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
            Comparator.comparingInt(Task::getRemainingTime)
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
                
                // Preempt if new task has shorter remaining time
                if (currentTask != null && 
                    arriving.getRemainingTime() < currentTask.getRemainingTime()) {
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
        }
        
        result.calculateStatistics();
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Shortest Remaining Time First (SRTF)";
    }
}

