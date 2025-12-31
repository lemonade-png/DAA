package scheduler;

import java.util.List;

/**
 * Base interface for all greedy-based CPU scheduling algorithms
 * 
 * All implementations follow the greedy paradigm:
 * - At each decision point, select the locally optimal task
 * - Make decisions based on current state without backtracking
 * - Optimize for criteria like shortest time, highest priority, or earliest deadline
 */
public interface Scheduler {
    SchedulingResult schedule(List<Task> tasks);
    String getAlgorithmName();
}

