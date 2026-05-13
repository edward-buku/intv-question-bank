/**
 * System Observability (Datadog Flame Graphs)
 * 
 * BACKGROUND:
 * Imagine we are building the backend aggregation engine for a system observability 
 * tool similar to Datadog. When a complex distributed request comes into our system, 
 * it triggers multiple internal workers (microservices, database calls, file I/O). 
 * To build our visual "Flame Graph" dashboard, we need to analyze the telemetry data 
 * to find our system's maximum concurrent load. An overview of how Datadog handles this
 * can be shown in this link: https://www.datadoghq.com/knowledge-center/distributed-tracing/flame-graph/
 * 
 * THE PROBLEM:
 * You are provided with a list of `FlameBar` objects. Each `FlameBar` represents a 
 * single request's activity during the lifecycle of the request.
 *  - Time is represented discretely as an array (or list) of integers.
 *  - A `1` means the request was active during that time frame.
 *  - A `0` means the request was idle.
 *  - Note: Requests start and finish at different times, so these arrays are not 
 *    guaranteed to be the exact same length.
 * 
 * YOUR TASK:
 * Write a method that takes in this list of `FlameBar` objects and outputs:
 *  1. Peak Concurrency: The maximum number of requests active at the exact same time.
 *  2. Peak Intervals: The continuous time intervals [start_time, end_time] where 
 *     this peak concurrency occurred.
 *  3. Active requests: The exact names of the requests active during each peak interval.
 * 
 * EXAMPLE:
 * Input:
 *  worker_a: [1, 1, 1, 1]
 *  worker_b: [0, 1, 1, 1]
 *  worker_c: [0, 0, 1, 1]
 * 
 * Output: 
 *  Peak Concurrency: 3
 *  Time [2, 3]: [worker_a, worker_b, worker_c]
 * 
 * IMPLEMENTATION DETAILS & CONSTRAINTS:
 * - You are provided with a starter class called `FlameBar`. Please review this 
 *   class and make any modifications necessary to ensure it adheres to proper Java  
 *   Object-Oriented Programming and safety standards before implementing your logic.
 * - You can create additional classes / methods / functions to help you solve the problem.
 * - You must be able to gracefully handle data anomalies & edge cases.
 * - You should write clean, modular code (e.g., extracting helper methods where appropriate).
 * - Your console output format does not need to perfectly match the example string, 
 *   as long as the underlying data is grouped and identified correctly in the correct order.
 * - You can search for internet for Java library references, however you can't rely on AI 
 *   for answers or searching the solution directly (use '-ai' flag when searching to prevent AI-assisted results)
 */

import java.util.*;

class FlameBar {
    private final String name;
    private final List<Integer> duration;
    
    public FlameBar(String name, List<Integer> duration) {
        this.name = name;
        this.duration = duration;
    }
    
    public String getName() { return name; }
    
    public int getDurationStatusAt(int i) { return duration.get(i); }
    
    public int getLengthOfDuration() { return duration.size(); }
}

class ProcessNote {
    private final int time;
    private final List<String> names;

    public ProcessNote(int time, List<String> names) {
        this.time = time;
        this.names = names;
    }
    
    public int getTime() { return time; }

    public int getLengthOfNames() { return names.size(); }

    public void pushToNames(String name) {
        names.add(name);
    }
    
    public String getNamesArray() {
        return Arrays.toString(names.toArray());
    }
}

class FlameGraphAnalyzer {
    
    public static void main(String[] args) {
        analyzeFlameGraph(makeTestCase1());
        analyzeFlameGraph(makeTestCase2());
        analyzeFlameGraph(makeTestCase3());
        analyzeFlameGraph(makeTestCase4());
        analyzeFlameGraph(makeTestCase5());
        analyzeFlameGraph(makeTestCase6());
        analyzeFlameGraph(makeTestCase7());
    }
    
    private static void analyzeFlameGraph(List<FlameBar> graph) {
        if (graph == null || graph.isEmpty()) {
            System.out.println("Peak Concurrency: 0");
            return;
        }
        
        Collections.sort(graph, (a, b) -> {
             if (a.getLengthOfDuration() != a.getLengthOfDuration()) {
                return b.getLengthOfDuration() - a.getLengthOfDuration();
             }
             
             return a.getName().compareTo(b.getName());
         });
        
        int maxDur = graph.get(0).getLengthOfDuration();
        
        Queue<ProcessNote> pq = new PriorityQueue<>((a, b) -> {
            if (b.getLengthOfNames() != a.getLengthOfNames()) {
                return b.getLengthOfNames() - a.getLengthOfNames();
            }
            
            return a.getTime() - b.getTime();
        });
        
        int maxStack = 1;
        
        for (int i = 0; i <= maxDur; ++i) {
            ProcessNote note = new ProcessNote(i, new ArrayList<>());
            for (FlameBar bar : graph) {
                if (i >= bar.getLengthOfDuration()) continue;
                if (bar.getDurationStatusAt(i) == 1) note.pushToNames(bar.getName());
            }
            maxStack = Math.max(note.getLengthOfNames(), maxStack);
            if (maxStack == note.getLengthOfNames()) pq.offer(note);
        }
        
        if (pq.isEmpty()) {
            System.out.println("Peak Concurrency: 0");
            return;
        }
        
        int maxEntries = pq.peek().getLengthOfNames();
        System.out.println("Peak Concurrency: " + maxEntries);
        Map<String, int[]> flameStackMap = new TreeMap<>();
        
        while (pq.peek() != null && pq.peek().getLengthOfNames() == maxEntries) {
            ProcessNote note = pq.poll();
            String key = note.getNamesArray();
            
            if (!flameStackMap.containsKey(key)) {
                flameStackMap.put(key, new int[]{note.getTime(), note.getTime()});
                continue;
            }
            flameStackMap.put(key, new int[]{flameStackMap.get(key)[0], note.getTime()});
        }
        
        List<String> flameMapKeyList = new ArrayList<>(flameStackMap.keySet());
        
        Collections.sort(flameMapKeyList, (a, b) -> {
            int[] durA = flameStackMap.get(a);
            int[] durB = flameStackMap.get(b);
            int diff = (durB[1] - durB[0]) - (durA[1] - durA[0]);
            return  diff == 0 ? durA[0] - durB[0] : diff;
        });
        
        
        for (String key : flameMapKeyList) {
            System.out.println(String.format("Time [%d, %d]: %s", flameStackMap.get(key)[0], flameStackMap.get(key)[1], key));
        }
    }
    
    private static List<FlameBar> makeTestCase1() {
        /*
            Test Case 1:
            Peak Concurrency: 0
        */
        System.out.println("\nTest Case 1:");
        return List.of();
    }

    private static List<FlameBar> makeTestCase2() {
        /*
            Test Case 2:
            Peak Concurrency: 0
        */
        System.out.println("\nTest Case 2:");
        return null;
    }

    private static List<FlameBar> makeTestCase3() {
        /*
            Test Case 3:
            Peak Concurrency: 0
        */
        System.out.println("\nTest Case 3:");
        List<FlameBar> graph = new ArrayList<>();
        graph.add(new FlameBar("test.someRequest", List.of(0, 0, 0, 0)));
        return graph;
    }

    private static List<FlameBar> makeTestCase4() {
        /*
        Test Case 4:
        Peak Concurrency: 1
        Time [2, 2]: [test.someRequest]
        */
        System.out.println("\nTest Case 4:");
        List<FlameBar> graph = new ArrayList<>();
        graph.add(new FlameBar("test.someRequest", List.of(0, 0, 1, 0)));
        return graph;
    }

    private static List<FlameBar> makeTestCase5() {
        /*
        Test Case 5:
        Peak Concurrency: 3
        Time [2, 3]: [test_a.someRequest, test_b.someRequest, test_c.someRequest]
        */
        System.out.println("\nTest Case 5:");
        List<FlameBar> graph = new ArrayList<>();
        graph.add(new FlameBar("test_a.someRequest", List.of(1, 1, 1, 1)));
        graph.add(new FlameBar("test_b.someRequest", List.of(0, 1, 1, 1)));
        graph.add(new FlameBar("test_c.someRequest", List.of(0, 0, 1, 1)));
        return graph;
    }

    private static List<FlameBar> makeTestCase6() {
        /*
        Test Case 6:
        Peak Concurrency: 3
        Time [8, 11]: [test1.someRequest, test4.someRequest, test5.someRequest]
        Time [2, 4]: [test1.someRequest, test2.someRequest, test3.someRequest]
        Time [6, 6]: [test2.someRequest, test4.someRequest, test5.someRequest]
        */
        System.out.println("\nTest Case 6:");
        List<FlameBar> graph = new ArrayList<>();
        graph.add(new FlameBar("test3.someRequest", List.of(0, 0, 1, 1, 1)));
        graph.add(new FlameBar("test5.someRequest", List.of(0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1)));
        graph.add(new FlameBar("test1.someRequest", List.of(1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1)));
        graph.add(new FlameBar("test2.someRequest", List.of(0, 0, 1, 1, 1, 1, 1, 0)));
        graph.add(new FlameBar("test4.someRequest", List.of(0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1)));

        return graph;
    }

    private static List<FlameBar> makeTestCase7() {
        /*
        Test Case 7:
        Peak Concurrency: 5
        Time [2, 3]: [worker_A.someRequest, worker_B.someRequest, worker_C.someRequest, worker_D.someRequest, worker_E.someRequest]
        Time [6, 7]: [worker_A.someRequest, worker_B.someRequest, worker_C.someRequest, worker_D.someRequest, worker_F.someRequest]
        Time [10, 10]: [worker_A.someRequest, worker_B.someRequest, worker_C.someRequest, worker_D.someRequest, worker_G.someRequest]
        */
        System.out.println("\nTest Case 7:");
        List<FlameBar> graph = new ArrayList<>();

        // Worker A: Solid backbone, but takes a break in the middle
        graph.add(new FlameBar("worker_A.someRequest", List.of(0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1)));

        // Worker B: Starts late, takes a break, stays till the end
        graph.add(new FlameBar("worker_B.someRequest", List.of(0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1)));

        // Worker C: Drops out between the peaks
        graph.add(new FlameBar("worker_C.someRequest", List.of(0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1)));

        // Worker D: Early starter, present for peaks, drops near end, comes back for final spike
        graph.add(new FlameBar("worker_D.someRequest", List.of(1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1)));

        // Worker E: Only present for the FIRST peak, then array ends (Out of bounds test)
        graph.add(new FlameBar("worker_E.someRequest", List.of(0, 0, 1, 1)));

        // Worker F: Only present for the SECOND peak, array ends early
        graph.add(new FlameBar("worker_F.someRequest", List.of(0, 0, 0, 0, 0, 0, 1, 1)));

        // Worker G: Only wakes up for the very last frame to trigger a final peak
        graph.add(new FlameBar("worker_G.someRequest", List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1)));

        // Worker H: Distraction - Finishes work before any peaks happen
        graph.add(new FlameBar("worker_H.someRequest", List.of(1, 1)));

        // Edge Cases: Ghost workers
        graph.add(new FlameBar("worker_I.someRequest", List.of())); // Empty
        graph.add(new FlameBar("worker_J.someRequest", List.of(0))); // Immediate 0

        return graph;
    }
}