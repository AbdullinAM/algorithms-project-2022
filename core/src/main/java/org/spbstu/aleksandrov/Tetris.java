package org.spbstu.aleksandrov;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.spbstu.aleksandrov.controller.Controller;
import org.spbstu.aleksandrov.controller.Player;
import org.spbstu.aleksandrov.controller.Robot;
import org.spbstu.aleksandrov.controller.RobotInput;
import org.spbstu.aleksandrov.model.GameSession;
import org.spbstu.aleksandrov.solver.Solver;
import org.spbstu.aleksandrov.view.GameRenderer;

@SuppressWarnings({"ConstantConditions", "PointlessBooleanExpression"})
public class Tetris extends Game {

	private Controller controller;
	private GameSession game;
	private GameRenderer renderer;
	private Solver solver;
	private RobotInput player;
	private Input original;

	public final static int INITIAL_LEVEL = 1; // lvl numeration starts from 0
	public final static boolean DEBUG = false;

	// Tactic type for autoplay
	// Survival tactic: increase playtime
	// Non-survival (risky) tactic: increase score
	public final static boolean SURVIVAL = false;

	// ROBOT has access to gameSession, interacts with the game through the GameSession methods
	public final static boolean ROBOT = true;

	// PLAYER has no access to gameSession, interacts with the game when the Controller.processInput() called
	public final static boolean PLAYER = false;

	public final static boolean HINTS = true || ROBOT || PLAYER; // Enable Solver
	public final static boolean AUTOPLAY = ROBOT || PLAYER;

	int frames = 1;

	@Override
	public void create() {
		game = new GameSession();
		game.setLevel(INITIAL_LEVEL);

		if (HINTS || AUTOPLAY) {
			solver = new Solver(game);
			solver.setSolving(HINTS || AUTOPLAY);
			solverThread = new SolverThread(solver);
			solverThread.start();
		}
		if (AUTOPLAY) {
			if (ROBOT) player = new Robot(solver, game);
			else player = new Player(solver, game);
			original = Gdx.input;
			Gdx.input = player;
		}

		renderer = new GameRenderer(game, solver);
		setScreen(renderer);
		controller = new Controller(game, PLAYER);
	}

	private SolverThread solverThread;

	public static class SolverThread extends Thread {

		Solver solver;

		public SolverThread(Solver solver) {
			this.solver = solver;
		}

		public void run() {
			solver.setSolving(HINTS);
			solver.startSolving();
		}

		public void exit() {
			solver.setSolving(false);
		}
	}

	@Override
	public void dispose () {
		solverThread.exit();
		super.dispose();
	}

	@Override
	public void render() {
		// FPS = 60
		renderer.render(Gdx.graphics.getDeltaTime());
		if (game.isGameOver()) {
			if (Gdx.input == player) Gdx.input = original;
			if (solverThread.isAlive()) solverThread.exit();
			if (controller.waitForSpace()) {
				game = new GameSession();
				controller.update(game);
				renderer.update(game);
				if (HINTS || AUTOPLAY) {
					solver.update(game);
					solverThread = new SolverThread(solver);
					solverThread.start();
				}
			}
		} else controller.processInput();

		if (!DEBUG && PLAYER) {
			if (game.getLevel() > 29) game.step();
			else if (frames >= GameSession.FRAMES_PER_STEP[game.getLevel() - 1]) {
				game.step();
				frames = 1;
			}
		}
		frames++;
	}
}