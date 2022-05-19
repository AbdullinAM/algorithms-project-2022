package logic.solver;

import logic.fifteen.State;
import java.util.*;

public class Solver {

    private final List<State> result = new ArrayList<>();

    private static class CurrentState {
        private final CurrentState parent;
        private final State State;

        private CurrentState(CurrentState parent, State State) {
            this.parent = parent;
            this.State = State;
        }

        public State getBoard() {
            return State;
        }
    }

    public Solver(State initial) {

        if(!isSolvable()) return;

        PriorityQueue<CurrentState> priorityQueue = new PriorityQueue<>(10, new Comparator<CurrentState>() {
            @Override
            public int compare(CurrentState o1, CurrentState o2) {
                return Integer.compare(measure(o1), measure(o2));
            }
        });

        priorityQueue.add(new CurrentState(null, initial));

        while (true) {
            CurrentState State2 = priorityQueue.poll();

            assert State2 != null;
            if(State2.State.isGoal()) {
                stateToList(new CurrentState(State2, State2.State));
                return;
            }

            for (State board1 : State2.State.neighbors()) {
                if (board1 != null && !containsInPath(State2, board1))
                    priorityQueue.add(new CurrentState(State2, board1));
            }
        }
    }

    private static int measure(CurrentState currentState){
        CurrentState currentState2 = currentState;
        int c = 0;   // g(x)
        int measure = currentState.getBoard().h();  // h(x)
        while (true){
            c++;
            currentState2 = currentState2.parent;
            if(currentState2 == null) {
                // g(x) + h(x)
                return measure + c;
            }
        }
    }

    private void stateToList(CurrentState currentState){
        CurrentState currentState2 = currentState;
        while (true) {
            currentState2 = currentState2.parent;
            if(currentState2 == null) {
                Collections.reverse(result);
                return;
            }
            result.add(currentState2.State);
        }
    }

    private boolean containsInPath(CurrentState currentState, State State){
        CurrentState currentState2 = currentState;
        while (true){
            if(currentState2.State.equals(State)) return true;
            currentState2 = currentState2.parent;
            if(currentState2 == null) return false;
        }
    }

    public boolean isSolvable() {
        return true; //to do
    }


    public Iterable<State> solution() {
        return result;
    }

}
