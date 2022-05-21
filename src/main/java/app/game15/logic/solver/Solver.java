package app.game15.logic.solver;

import app.game15.logic.fifteen.State;
import java.util.*;

public class Solver {

    private final ArrayList<State> result = new ArrayList<>();
    private State currentState;

    private final PriorityQueue<State> priorityQueue = new PriorityQueue<>(100, new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return Integer.compare(measure(o1), measure(o2));
        }
    });

    public Solver(State initial) {
        priorityQueue.add(initial);
        currentState = initial;
    }

    public void solve() {

        if(!isSolvable(currentState)) return;

        while (!currentState.isGoal()) {

            currentState = priorityQueue.poll();
            System.out.println(currentState);
            if (currentState.isGoal()) {
                stateToList(currentState);
                return;
            }
            currentState.setChildren();
            ArrayList<State> children = currentState.getChildren();
            for (State board1 : children) {
                if (board1 != null && !containsInPath(currentState, board1)) {
                    priorityQueue.add(board1);
                }
            }

        }
    }


    private static int measure(State currentState){
        State currentState2 = currentState;
        int c = 0;   // g(x)
        int measure = currentState.h();  // h(x)
        while (true){
            c++;
            currentState2 = currentState2.getParent();
            if(currentState2 == null) {
                // g(x) + h(x)
                return measure + c;
            }
        }
    }

    private void stateToList(State currentState){
        State currentState2 = currentState;
        result.add(currentState2);
        while (true) {
            currentState2 = currentState2.getParent();
            if(currentState2 == null) {
                return;
            }
            result.add(currentState2);
        }
    }

    private boolean containsInPath(State currentState, State State){
        State currentState2 = currentState;
        while (true) {
            if(currentState2.equals(State)) return true;
            currentState2 = currentState2.getParent();
            if(currentState2 == null) return false;
        }
    }

    public static boolean isSolvable(State state) {
        int[][] arr = state.blocks;
        int count = 0;
        int index = 0;
        ArrayList<Integer> arrList = toArrayList(arr);
        for (int i = 0; i < 16; i++) {
            for (int j = i + 1; j < 15; j++) {
                if (arrList.get(j) < arrList.get(i)) {
                    count++;
                }
                if (arrList.get(i) == 0) {
                    index = (i / 4);
                }
            }
        }
        return ((count + index) % 2) == 0;

    }

    private static ArrayList<Integer> toArrayList(int[][] arr) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.add(arr[i][j]);
            }
        }
        return result;
    }

    public ArrayList<State> getResult() {
        return result;
    }

    public static int[][] convertToArray(ArrayList<Integer> list) {
        int[][] result = new int[4][4];
        for (int i = 0; i < list.size(); i++) {
            result[i / 4][i % 4] = list.get(i);
        }
        return result;
    }
}
