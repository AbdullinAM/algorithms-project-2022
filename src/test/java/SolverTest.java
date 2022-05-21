import app.game15.logic.fifteen.State;
import app.game15.logic.solver.Solver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    private final int[][] solved = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,15,0}};
    private final State finalState = new State(solved, null);

    @Test
    void isSolvable() {
        int[][] combination = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,0,15}};
        State state = new State(combination, null);
        assertTrue(Solver.isSolvable(state));
    }

    @Test
    void isNotSolvable() {
        int[][] combination = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,15,14,0}};
        State state = new State(combination, null);
        assertFalse(Solver.isSolvable(state));
    }

    @Test
    void solutionSample1() {
        int[][] combination = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,0,15}};
        State state = new State(combination, null);
        Solver solver = new Solver(state);
        solver.solve();
        ArrayList<State> result = solver.getResult();
        assertEquals(finalState, result.get(0));
    }

    @Test
    void solutionSample2() {
        int[][] combination = {{1,2,3,4}, {5,6,0,8}, {9,10,7,11}, {13,14,15,12}};
        State state = new State(combination, null);
        Solver solver = new Solver(state);
        solver.solve();
        ArrayList<State> result = solver.getResult();
        assertEquals(finalState, result.get(0));
    }

    @Test
    void solutionSample3() {
        int[][] combination = {{0,1,2,4}, {5,6,3,8}, {9,10,7,11}, {13,14,15,12}};
        State state = new State(combination, null);
        Solver solver = new Solver(state);
        solver.solve();
        ArrayList<State> result = solver.getResult();
        assertEquals(finalState, result.get(0));
    }

    @Test
    void solutionSample4() {
        int[][] combination = {{5,1,2,4}, {0,6,3,8}, {9,10,7,11}, {13,14,15,12}};
        State state = new State(combination, null);
        Solver solver = new Solver(state);
        solver.solve();
        ArrayList<State> result = solver.getResult();
        assertEquals(finalState, result.get(0));
    }

    @Test
    void solutionSample5() {
        int[][] combination = {{5,1,2,4}, {9,6,3,8}, {13,10,7,11}, {0,14,15,12}};
        State state = new State(combination, null);
        Solver solver = new Solver(state);
        solver.solve();
        ArrayList<State> result = solver.getResult();
        assertEquals(finalState, result.get(0));
    }
}