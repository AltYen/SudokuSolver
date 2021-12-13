package yenigun.io.sudoku;

import sac.State;
import sac.StateFunction;

public class MyHeuristic extends StateFunction {
	
	@Override
	public double calculate(State state) {
		Sudoku s=(Sudoku) state;
		return s.zeros;
	}
	

}
