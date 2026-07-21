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
}

class FlameGraphAnalyzer {
    
    public static void main(String[] args) {
        // Uncomment test cases as you build your solution
        //analyzeFlameGraph(makeTestCase1());
        //analyzeFlameGraph(makeTestCase2());
        //analyzeFlameGraph(makeTestCase3());
        // analyzeFlameGraph(makeTestCase4());
        // analyzeFlameGraph(makeTestCase5());
        // analyzeFlameGraph(makeTestCase6());
        // analyzeFlameGraph(makeTestCase7());
    }
    
    private static void analyzeFlameGraph(List<FlameBar> graph) {
        
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
