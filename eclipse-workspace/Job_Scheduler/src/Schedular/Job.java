package Schedular;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Job {
    private String name;
    private int executionTime;
    private List<String> requiredResources;
    private List<String> dependencies;
    private int importance;

    private long startTime; // Start time of execution
    private AtomicBoolean interrupted; // Flag to track interruption

    public Job(String name, int executionTime, List<String> requiredResources,
               List<String> dependencies, int importance) {
        this.name = name;
        this.executionTime = executionTime;
        this.requiredResources = requiredResources;
        this.dependencies = dependencies;
        this.importance = importance;

        this.startTime = -1; 
        this.interrupted = new AtomicBoolean(false);
    }

    public String getName() {
        return name;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public List<String> getRequiredResources() {
        return requiredResources;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public int getImportance() {
        return importance;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isInterrupted() {
        return interrupted.get();
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted.set(interrupted);
    }

    public void execute() {
        System.out.println("Executing job: " + name + " at time: " + startTime +
                " using resources: " + requiredResources);
        try {
            Thread.sleep(executionTime * 1000);
        } catch (InterruptedException e) {
            System.out.println("Job " + name + " interrupted.");
            interrupted.set(true);
        }
        System.out.println("Job " + name + " completed.");
    }
}
