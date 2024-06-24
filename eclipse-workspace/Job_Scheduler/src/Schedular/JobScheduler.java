package Schedular;
import java.util.*;
import java.util.concurrent.*;

public class JobScheduler {
    private List<Job> jobs;
    private Map<String, Future<?>> jobFutures;
    private Map<String, Semaphore> resourceLocks;
    private ExecutorService executor;

    public JobScheduler(List<Job> jobs) {
        this.jobs = jobs;
        this.jobFutures = new HashMap<>();
        this.resourceLocks = new HashMap<>();
        this.executor = Executors.newCachedThreadPool(); 
        for (Job job : jobs) {
            Semaphore sem = new Semaphore(1); // Each job's resources are protected by a semaphore
            for (String resource : job.getRequiredResources()) {
                resourceLocks.putIfAbsent(resource, new Semaphore(1));
            }
        }
    }

    public void job_scheduler() {
        PriorityQueue<Job> jobQueue = new PriorityQueue<>(jobs.size(),
                (j1, j2) -> Integer.compare(j2.getImportance(), j1.getImportance())); // Higher importance first

        jobQueue.addAll(jobs);

        long currentTime = System.currentTimeMillis() / 1000; // Start time in seconds
        while (!jobQueue.isEmpty()) {
            Job job = jobQueue.poll();

            // Check dependencies
            boolean dependenciesMet = true;
            for (String dependency : job.getDependencies()) {
                if (jobFutures.containsKey(dependency)) {
                    Future<?> depFuture = jobFutures.get(dependency);
                    if (!depFuture.isDone()) {
                        dependenciesMet = false;
                        break;
                    }
                }
            }

            if (!dependenciesMet) {
                jobQueue.offer(job); // Put back into the queue
                continue;
            }

            // Acquire required resources
            List<Semaphore> locks = new ArrayList<>();
            for (String resource : job.getRequiredResources()) {
                locks.add(resourceLocks.get(resource));
            }
            try {
                acquireResources(locks);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            // Execute job
      
            Future<?> future = executor.submit(() -> {
                job.execute();
                releaseResources(locks); // Release resources after execution
            });
            long endtime= System.currentTimeMillis() / 1000;
            job.setStartTime(endtime-currentTime);
            jobFutures.put(job.getName(), future);
            // Update current time for next job start
            currentTime = System.currentTimeMillis() / 1000;
        }
        // Shutdown executor
        executor.shutdown();
    }

    private void acquireResources(List<Semaphore> locks) throws InterruptedException {
        for (Semaphore lock : locks) {
            lock.acquire();
        }
    }

    private void releaseResources(List<Semaphore> locks) {
        for (Semaphore lock : locks) {
            lock.release();
        }
    }

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Job1", 4, Arrays.asList("CPU"), Collections.emptyList(), 3));
        jobs.add(new Job("Job2", 2, Arrays.asList("GPU"), Collections.emptyList(), 2));
        jobs.add(new Job("Job3", 3, Arrays.asList("CPU","GPU"), Collections.emptyList(), 4));
        jobs.add(new Job("Job4", 1, Arrays.asList("CPU"), Arrays.asList("Job1"), 1));
        jobs.add(new Job("Job5", 3, Arrays.asList("GPU"), Arrays.asList("Job2"), 1));
        jobs.add(new Job("Job6", 2, Arrays.asList("CPU","GPU"), Arrays.asList("Job4"), 1));
        
        JobScheduler scheduler = new JobScheduler(jobs);
        scheduler.job_scheduler();
    }
}
