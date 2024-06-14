package Solution;
import java.util.*;

public class Maze{
    public static List<Integer> bfs(Map<Integer, List<Integer>> graph, int start, int end) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> parent = new HashMap<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, node);
                    if (neighbor == end) {
                        List<Integer> path = new ArrayList<>();
                        int current = end;
                        while (current != start) {
                            path.add(current);
                            current = parent.get(current);
                        }
                        path.add(start);
                        Collections.reverse(path);
                        return path;
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of nodes: ");
        int n = scanner.nextInt();
        System.out.print("Enter the number of edges: ");
        int m = scanner.nextInt();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        System.out.println("Enter the edges (format: u v):");
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }
        System.out.print("Enter the start node: ");
        int start = scanner.nextInt();
        System.out.print("Enter the end node: ");
        int end = scanner.nextInt();
        List<Integer> path = bfs(graph, start, end);

        if (path != null) {
            System.out.println("Shortest path from " + start + " to " + end + ": " + path);
        } else {
            System.out.println("There is no path from " + start + " to " + end + ".");
        }

        scanner.close();
    }
}

