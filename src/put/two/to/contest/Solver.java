package put.two.to.contest;

import java.io.InputStream;

import put.two.to.contest.client.ConsoleClient;

public class Solver {

	public Solver() {

	}

	public void solve(InputStream problemInput,
			SolutionAcceptor solutionAcceptor, long timeout, int solutionsLimit) {
		ConsoleClient.process(problemInput,
				solutionAcceptor, solutionsLimit,
				solutionsLimit, true, false);
	}

}
