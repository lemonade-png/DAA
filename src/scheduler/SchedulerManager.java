package scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages multiple schedulers and provides comparison functionality
 */
public class SchedulerManager {
    private List<Scheduler> schedulers;
    private List<Task> tasks;
    
    public SchedulerManager() {
        schedulers = new ArrayList<>();
        tasks = new ArrayList<>();
        
        // Add default schedulers
        schedulers.add(new FirstComeFirstServedScheduler());
        schedulers.add(new ShortestJobFirstScheduler());
        schedulers.add(new ShortestRemainingTimeFirstScheduler());
        schedulers.add(new EarliestDeadlineFirstScheduler());
        schedulers.add(new PriorityScheduler(false));
        schedulers.add(new PriorityScheduler(true));
    }
    
    public void addScheduler(Scheduler scheduler) {
        schedulers.add(scheduler);
    }
    
    public void setTasks(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }
    
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }
    
    public List<SchedulingResult> runAllSchedulers() {
        List<SchedulingResult> results = new ArrayList<>();
        for (Scheduler scheduler : schedulers) {
            results.add(scheduler.schedule(tasks));
        }
        return results;
    }
    
    public SchedulingResult runScheduler(Scheduler scheduler) {
        return scheduler.schedule(tasks);
    }
    
    public List<Scheduler> getSchedulers() {
        return new ArrayList<>(schedulers);
    }
}

