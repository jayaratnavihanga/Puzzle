import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            IcePuzzleSolver solver = new IcePuzzleSolver("src/test3.txt"); // Change the filename as needed
            solver.displayMaze(); // Display the maze before solving
            List<int[]> path = solver.findShortestPath();
            solver.printSolution(path);
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
    }
}

class IcePuzzleSolver {
    private static final char EMPTY = '.';
    private static final char ROCK = '0';
    private static final char START = 'S';
    private static final char FINISH = 'F';
    private static final char PLAYER = '@';
    private static final char VISITED = 'x';

    private static final int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    private char[][] map;
    private int width;
    private int height;
    private int[] start;
    private int[] finish;

    public IcePuzzleSolver(String filePath) throws FileNotFoundException {
        parseMap(filePath);
    }

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
                    start = new int[]{i, j};
                } else if (ch == FINISH) {
                    finish = new int[]{i, j};
                }
            }
        }
    }

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

    public List<int[]> findShortestPath() {
        Queue<int[]> queue = new LinkedList<>();
        Map<int[], int[]> parentMap = new HashMap<>();
        queue.add(start);
        parentMap.put(start, null);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == finish[0] && current[1] == finish[1]) {
                return reconstructPath(parentMap, current);
            }
            for (int[] dir : directions) {
                int[] next = new int[]{current[0] + dir[0], current[1] + dir[1]};
                if (isValidMove(next)) {
                    queue.add(next);
                    map[next[0]][next[1]] = VISITED;
                    parentMap.put(next, current);
                }
            }
        }
        return null; // No path found
    }

    private boolean isValidMove(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        return x >= 0 && x < height && y >= 0 && y < width && (map[x][y] == EMPTY || map[x][y] == FINISH);
    }

    private List<int[]> reconstructPath(Map<int[], int[]> parentMap, int[] current) {
        List<int[]> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

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
            System.out.println(step + ". Move to Finish (" + (finish[1] + 1) + "," + (finish[0] + 1) + ")");
            System.out.println("Done!");
        }
    }
}
