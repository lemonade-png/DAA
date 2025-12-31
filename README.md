# CPU Task Scheduling Using Greedy Algorithms: A Real-World Operating System Approach

**DAA Assignment: Design and Analysis of Algorithms**

A comprehensive Java application demonstrating CPU task scheduling using various greedy algorithms. This project provides both a graphical user interface (GUI) and a console interface for visualizing and comparing different scheduling algorithms.

## Project Proposal

### Introduction

Modern computer systems execute multiple processes simultaneously, all competing for limited CPU resources. Efficient CPU scheduling is essential to ensure system responsiveness, reduced waiting time, and timely task execution. Operating systems rely on scheduling algorithms to determine the order in which tasks are executed by the CPU.

This project focuses on CPU scheduling as a real-world optimization problem and applies Greedy algorithms, which are widely used in practical operating systems due to their efficiency and real-time decision-making capability.

### Problem Statement

The CPU can execute only one task at a time while multiple tasks with different execution times, priorities, and deadlines compete for CPU access. The challenge is to schedule these tasks in an optimal order to improve system performance by minimizing waiting time and meeting task deadlines.

### Proposed Solution / Methodology

The proposed solution uses the Greedy paradigm to schedule CPU tasks. The system implements and analyzes common greedy-based CPU scheduling algorithms such as:

- **Shortest Job First (SJF)** - Greedily selects the task with shortest execution time
- **Priority Scheduling** - Greedily selects the task with highest priority
- **Earliest Deadline First (EDF)** - Greedily selects the task with nearest deadline

At each scheduling decision, the algorithm selects the task that appears optimal at that moment based on predefined criteria such as shortest execution time, highest priority, or nearest deadline.

### Tools & Technologies

- **Programming Language:** Java
- **Data Structures:** Arrays, Priority Queues, Lists
- **Paradigm:** Greedy Algorithms
- **GUI Framework:** Java Swing

### Expected Outcomes

- Improved CPU utilization
- Reduced average waiting time
- Better understanding of real-world scheduling strategies
- Demonstration of how greedy algorithms are applied in operating systems

### Real-World Relevance

CPU scheduling algorithms are directly used in operating systems, cloud computing platforms, mobile devices, and real-time embedded systems. This project demonstrates how greedy strategies enable fast and efficient scheduling decisions in real-world computing environments.

### Conclusion

This project highlights the importance of greedy algorithms in solving real-world CPU scheduling problems. By analyzing and implementing different scheduling strategies, the project provides insight into how operating systems efficiently manage CPU resources under heavy workloads.

## Features

- **Multiple Greedy Scheduling Algorithms:**
  - Shortest Job First (SJF) - Non-preemptive
  - Shortest Remaining Time First (SRTF) - Preemptive
  - Earliest Deadline First (EDF) - Preemptive
  - Priority Scheduling - Both preemptive and non-preemptive
  - First Come First Served (FCFS) - Baseline algorithm

- **Visualization:**
  - Interactive Gantt chart visualization
  - Detailed schedule timeline
  - Task execution visualization

- **Performance Metrics:**
  - Average waiting time
  - Average turnaround time
  - Total completion time
  - CPU utilization percentage
  - Missed deadline count

- **Comparison Tools:**
  - Side-by-side algorithm comparison
  - Best algorithm identification for each metric
  - Detailed statistics for each scheduler

## Project Structure

```
CPUScheduler/
├── src/
│   └── scheduler/
│       ├── Task.java                          # Task/Process representation
│       ├── TimeSlot.java                      # Time slot in schedule
│       ├── SchedulingResult.java              # Results container
│       ├── Scheduler.java                     # Base scheduler interface
│       ├── ShortestJobFirstScheduler.java      # SJF implementation
│       ├── ShortestRemainingTimeFirstScheduler.java  # SRTF implementation
│       ├── EarliestDeadlineFirstScheduler.java       # EDF implementation
│       ├── PriorityScheduler.java             # Priority scheduling
│       ├── FirstComeFirstServedScheduler.java # FCFS baseline
│       ├── SchedulerManager.java              # Scheduler management
│       ├── CPUSchedulerGUI.java               # GUI application
│       ├── CPUSchedulerConsole.java           # Console application
│       └── TaskInputDialog.java               # Task input dialog
└── README.md
```

## Algorithms Explained

### 1. Shortest Job First (SJF)
- **Type:** Non-preemptive greedy algorithm
- **Strategy:** Always selects the task with the shortest burst time
- **Advantage:** Minimizes average waiting time
- **Disadvantage:** May cause starvation for long tasks

### 2. Shortest Remaining Time First (SRTF)
- **Type:** Preemptive greedy algorithm
- **Strategy:** Always selects the task with the shortest remaining time
- **Advantage:** Optimal for minimizing average waiting time
- **Disadvantage:** High context switching overhead

### 3. Earliest Deadline First (EDF)
- **Type:** Preemptive greedy algorithm
- **Strategy:** Always selects the task with the earliest deadline
- **Advantage:** Good for real-time systems with deadlines
- **Disadvantage:** Requires deadline information

### 4. Priority Scheduling
- **Type:** Both preemptive and non-preemptive
- **Strategy:** Always selects the task with highest priority (lower number = higher priority)
- **Advantage:** Flexible priority-based scheduling
- **Disadvantage:** May cause starvation for low-priority tasks

### 5. First Come First Served (FCFS)
- **Type:** Non-preemptive baseline
- **Strategy:** Processes tasks in order of arrival
- **Advantage:** Simple and fair
- **Disadvantage:** Poor performance with varying burst times

## How to Run

### GUI Application
```bash
cd CPUScheduler/src
javac scheduler/*.java
java scheduler.CPUSchedulerGUI
```

### Console Application
```bash
cd CPUScheduler/src
javac scheduler/*.java
java scheduler.CPUSchedulerConsole
```

## Usage Guide

### GUI Application

1. **Adding Tasks:**
   - Click "Add Task" to add a new task
   - Fill in the task details (ID, Name, Arrival Time, Burst Time, Deadline, Priority)
   - Or click "Load Sample Tasks" to load predefined examples

2. **Running Algorithms:**
   - Select an algorithm from the dropdown
   - Click "Run Selected Algorithm" to see results
   - Click "Compare All Algorithms" to see side-by-side comparison

3. **Viewing Results:**
   - Check the statistics panel for performance metrics
   - View the Gantt chart for visual schedule representation
   - Review the schedule text area for detailed timeline

### Console Application

1. **Main Menu Options:**
   - `1` - Add a new task
   - `2` - View all current tasks
   - `3` - Run a single scheduler
   - `4` - Compare all schedulers
   - `5` - Load sample tasks
   - `6` - Clear all tasks
   - `7` - Exit

2. **Example Session:**
   ```
   === CPU Task Scheduler - Greedy Algorithms ===
   
   --- Main Menu ---
   1. Add Task
   2. View Tasks
   3. Run Single Scheduler
   4. Compare All Schedulers
   5. Load Sample Tasks
   6. Clear All Tasks
   7. Exit
   
   Enter your choice: 5
   ```

## Task Properties

Each task has the following properties:

- **ID:** Unique identifier
- **Name:** Task name (e.g., P1, P2)
- **Arrival Time:** When the task arrives in the system
- **Burst Time:** CPU time required to complete the task
- **Deadline:** Optional deadline (use -1 for no deadline)
- **Priority:** Priority level (lower number = higher priority)

## Performance Metrics

The application calculates and displays:

- **Average Waiting Time:** Average time tasks spend waiting in ready queue
- **Average Turnaround Time:** Average time from arrival to completion
- **Total Completion Time:** Time when all tasks complete
- **CPU Utilization:** Percentage of time CPU is busy
- **Missed Deadlines:** Number of tasks that missed their deadlines

## Example Output

### Sample Tasks (Real-World CPU Processes)
```
ID  Name      Arrival    Burst      Deadline   Priority
1   Browser   0          5          10         3
2   System    1          3          8          1
3   Media     2          8          15         4
4   Editor    3          6          12         2
5   Backup    4          4          9          5
```

These represent realistic operating system processes:
- **Browser:** Web browser process (moderate priority)
- **System:** System process (high priority)
- **Media:** Media player process (low priority)
- **Editor:** Text editor process (medium-high priority)
- **Backup:** Background backup process (lowest priority)

### Comparison Results
```
Algorithm                          Avg Waiting    Avg Turnaround  Completion     CPU Util %     Missed DL
First Come First Served (FCFS)    7.20           12.20          20             100.00         0
Shortest Job First (SJF)           4.60           9.60           20             100.00         0
Shortest Remaining Time First      3.40           8.40           20             100.00         0
Earliest Deadline First (EDF)      4.20           9.20           20             100.00         0
Priority Scheduling (Non-Preemptive) 5.00         10.00          20             100.00         0
Priority Scheduling (Preemptive)  3.40           8.40           20             100.00         0
```

## Greedy Algorithm Characteristics

All implemented algorithms follow the greedy paradigm:

1. **Greedy Choice Property:** At each step, make the locally optimal choice
2. **Optimal Substructure:** Optimal solution contains optimal solutions to subproblems
3. **No Backtracking:** Once a choice is made, it's never reconsidered

### Why These Algorithms Are Greedy:

- **SJF/SRTF:** Greedily choose shortest job at each decision point
- **EDF:** Greedily choose earliest deadline at each decision point
- **Priority:** Greedily choose highest priority at each decision point
- **FCFS:** Greedily choose first arrived task (baseline)

## Technical Details

- **Language:** Java
- **GUI Framework:** Java Swing
- **Architecture:** Object-oriented design with strategy pattern
- **Time Complexity:** O(n log n) for most algorithms due to priority queue operations
- **Space Complexity:** O(n) for storing tasks and schedule

## Extending the Project

To add a new scheduling algorithm:

1. Create a class implementing the `Scheduler` interface
2. Implement the `schedule()` method
3. Implement the `getAlgorithmName()` method
4. Add the scheduler to `SchedulerManager`

Example:
```java
public class MyCustomScheduler implements Scheduler {
    @Override
    public SchedulingResult schedule(List<Task> tasks) {
        // Your implementation
    }
    
    @Override
    public String getAlgorithmName() {
        return "My Custom Algorithm";
    }
}
```

## License

This project is provided as-is for educational purposes.

## Author

Created as a comprehensive demonstration of greedy algorithms in CPU task scheduling.

