package logic.solver;

import loading.MainWindow;
import logic.fifteen.State;
import java.util.*;

public class Solver {

    private final List<State> result = new ArrayList<>();
    private final State currentState;

    private PriorityQueue<State> priorityQueue = new PriorityQueue<>(20, new Comparator<State>() {
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

        while (true) {

            State state2 = priorityQueue.poll();
            assert state2 != null;
            MainWindow.initButtons(toArrayList(state2.blocks));
            if(state2.isGoal()) {
                stateToList(new State(state2.getBlocks(), state2));
                return;
            }

            for (State board1 : state2.neighbors()) {
                if (board1 != null && !containsInPath(state2, board1)) {
                    priorityQueue.add(new State(state2.getBlocks(), board1));
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
        while (true) {
            currentState2 = currentState2.getParent();
            if(currentState2 == null) {
                Collections.reverse(result);
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
                    index = (i % 4) + 1;
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

    public Iterable<State> solution() {
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
