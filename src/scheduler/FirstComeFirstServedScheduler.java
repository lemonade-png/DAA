package scheduler;

import java.util.*;

/**
 * First Come First Served (FCFS) - Baseline algorithm
 * Processes tasks in order of arrival
 */
public class FirstComeFirstServedScheduler implements Scheduler {
    
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
        
        Queue<Task> readyQueue = new LinkedList<>();
        
        int currentTime = 0;
        int taskIndex = 0;
        
        while (taskIndex < taskList.size() || !readyQueue.isEmpty()) {
            // Add all tasks that have arrived
            while (taskIndex < taskList.size() && 
                   taskList.get(taskIndex).getArrivalTime() <= currentTime) {
                readyQueue.offer(taskList.get(taskIndex));
                taskIndex++;
            }
            
            if (readyQueue.isEmpty()) {
                // No tasks ready, advance time
                if (taskIndex < taskList.size()) {
                    currentTime = taskList.get(taskIndex).getArrivalTime();
                }
                continue;
            }
            
            // Process first task in queue
            Task current = readyQueue.poll();
            
            if (current.getStartTime() < 0) {
                current.setStartTime(currentTime);
            }
            
            // Execute until completion (non-preemptive)
            int executionTime = current.getBurstTime();
            result.addTimeSlot(new TimeSlot(current, currentTime, currentTime + executionTime));
            currentTime += executionTime;
            current.setCompletionTime(currentTime);
        }
        
        result.calculateStatistics();
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return "First Come First Served (FCFS)";
    }
}

