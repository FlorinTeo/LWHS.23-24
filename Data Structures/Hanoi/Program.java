package Hanoi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Program {

    private static final int _height = 7;
    private static Queue<int[]> _moves;
    private static ArrayList<Stack<Integer>> _towers;

    public static void setup() {
        _moves = new LinkedList<int[]>();
        _towers = new ArrayList<Stack<Integer>>();
        for(int i = 0; i < 3; i++) {
            _towers.add(new Stack<Integer>());
        }

        for(int h = _height; h>=1; h--) {
            _towers.get(0).push(h);
        }
    }

    public static void print() {
        for(Stack<Integer> tower : _towers) {
            System.out.println(tower);
        }
        System.out.println("---------------");
    }

    public static void move(int height, int from, int to) {
        int other = 3 - (to + from);
        Stack<Integer> fromTower = _towers.get(from);
        Stack<Integer> toTower = _towers.get(to);
        if (height == 1) {
            toTower.push(fromTower.pop());
            _moves.add(new int[] {from, to});
        } else {
            move(height-1, from, other);
            toTower.push(fromTower.pop());
            _moves.add(new int[] {from, to});
            move(height-1, other, to);
        }
    }

    public static void save(String fileName) throws IOException {
        Path file = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(file);
        for(int[] move : _moves) {
            bw.write(move[0]+">"+ move[1] + "\n");
        }
        bw.close();

    }

    public static void main(String[] args) throws IOException {
        setup();
        print();
        move(_height, 0, 1);
        print();
        save("Hanoi/hanoi_" + _height + ".txt");
    }
}
