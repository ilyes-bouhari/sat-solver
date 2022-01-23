package solvers.HeuristicSearch;

import java.util.ArrayList;

class Solution extends solvers.MetaheuristicSearch.Solution {

    public Solution(int size) {
        super(size);
    }

    public Solution(solvers.MetaheuristicSearch.Solution solution) {
        super(solution);
    }

    public void update(ArrayList<Integer> nodesValue) {
        for (Integer nodeValue : nodesValue) {
            solution.set(Math.abs(nodeValue) - 1, nodeValue);
        }
    }
}