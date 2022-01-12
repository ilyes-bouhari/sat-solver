package solvers.HeuristicSearch;

import common.BaseSolution;

import java.util.ArrayList;

public class Solution extends BaseSolution {

    public Solution(int size) {
        super(size);
    }

    public Solution(BaseSolution solution) {
        super(solution);
    }

    public void update(ArrayList<Integer> nodesValue) {
        for (Integer nodeValue : nodesValue) {
            this.solution.get(Math.abs(nodeValue) - 1)
                .setValue(nodeValue);
        }
    }
}
