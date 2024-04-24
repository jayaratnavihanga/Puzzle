//w1810568-20191129-Vihanga Jayaratna

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            IcePuzzleSolver solver = new IcePuzzleSolver("src/test.txt");
            solver.displayMaze();
            List<int[]> path = solver.findShortestPath();
            solver.printSolution(path);
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
    }
}

class IcePuzzleSolver {
    // Constants for maze elements
    private static final char EMPTY = '.';
    private static final char ROCK = '0';
    private static final char START = 'S';
    private static final char FINISH = 'F';
    private static final char VISITED = 'x';

    // Directions for movement
    private static final int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    private char[][] map; // Maze representation
    private int width; // Width of the maze
    private int height; // Height of the maze
    private int[] start; // Start position
    private int[] finish; // Finish position

    // Constructor to initialize the IcePuzzleSolver
    public IcePuzzleSolver(String filePath) throws FileNotFoundException {
        parseMap(filePath); // Parse the maze from the file
    }

    // Method to parse the maze from the file
    private void parseMap(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        height = lines.size();
        width = lines.get(0).length();
        map = new char[height][width];

        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) {
                char ch = line.charAt(j);
                map[i][j] = ch;
                if (ch == START) {
                    start = new int[]{i, j}; // Store the start position
                } else if (ch == FINISH) {
                    finish = new int[]{i, j}; // Store the finish position
                }
            }
        }
    }

    // Method to display the maze
    public void displayMaze() {
        System.out.println("Input Maze:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method to find the shortest path using BFS
    public List<int[]> findShortestPath() {
        Queue<int[]> queue = new LinkedList<>(); // Queue for BFS traversal
        Map<int[], int[]> parentMap = new HashMap<>(); // Map to store parent-child relationships
        queue.add(start); // Add start position to the queue
        parentMap.put(start, null); // Start position has no parent

        while (!queue.isEmpty()) {
            int[] current = queue.poll(); // Retrieve current position from the queue
            if (current[0] == finish[0] && current[1] == finish[1]) {
                return reconstructPath(parentMap, current); // If finish position reached, reconstruct path
            }
            for (int[] dir : directions) {
                int[] next = new int[]{current[0] + dir[0], current[1] + dir[1]}; // Calculate next position
                if (isValidMove(next)) {
                    queue.add(next); // Add valid next position to the queue
                    map[next[0]][next[1]] = VISITED; // Mark next position as visited
                    parentMap.put(next, current); // Update parent-child relationship
                }
            }
        }
        return null; // No path found
    }

    // Method to check if a move is valid
    private boolean isValidMove(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        return x >= 0 && x < height && y >= 0 && y < width && (map[x][y] == EMPTY || map[x][y] == FINISH);
    }

    // Method to reconstruct the path from finish to start
    private List<int[]> reconstructPath(Map<int[], int[]> parentMap, int[] current) {
        List<int[]> path = new ArrayList<>();
        while (current != null) {
            path.add(current); // Add current position to the path
            current = parentMap.get(current); // Move to the parent position
        }
        Collections.reverse(path); // Reverse the path to get start to finish
        return path;
    }

    // Method to print the solution path
    public void printSolution(List<int[]> path) {
        if (path == null) {
            System.out.println("No path found.");
        } else {
            System.out.println("Solution:");
            int step = 1;
            for (int i = 0; i < path.size() - 1; i++) {
                int[] current = path.get(i);
                int[] next = path.get(i + 1);
                int dx = next[1] - current[1];
                int dy = next[0] - current[0];
                String direction = "";
                if (dx == 1 && dy == 0) {
                    direction = "RIGHT";
                } else if (dx == -1 && dy == 0) {
                    direction = "LEFT";
                } else if (dx == 0 && dy == 1) {
                    direction = "DOWN";
                } else if (dx == 0 && dy == -1) {
                    direction = "UP";
                }
                System.out.println(step + ". Move " + direction + " to (" + (next[1] + 1) + "," + (next[0] + 1) + ")");
                step++;
            }
            System.out.println("Done! ");
        }
    }
}

