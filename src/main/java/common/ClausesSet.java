package common;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class ClausesSet {
    private ArrayList<Clause> clauses = new ArrayList<Clause>();
    private int clauseSize = 0;
    private int numberVariables;

    public ClausesSet(InputStream inputStream) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception exception) {
            System.out.println("Exception while reading file :" + exception);
        }

        String line;
        String[] actualClause;

        try {
            while(!(line = Objects.requireNonNull(br).readLine()).equalsIgnoreCase("%")){
                switch((line = line.trim()).charAt(0)) {
                    case 'c':
                        break;
                    case 'p':{
                        this.numberVariables = Integer.parseInt(line.replaceAll("[^0-9 ]", "").replaceAll("  ", " ").trim().split(" ")[0]);
                        break;
                    }
                    default:{
                        actualClause = line.replaceAll(" 0$", "").split(" ");

                        if (this.clauseSize < actualClause.length)
                            this.clauseSize = actualClause.length;

                        Clause clause = new Clause();

                        for (int i = 0; i < this.clauseSize; i++)
                            clause.addLiteral(Integer.parseInt(actualClause[i]));

                        this.clauses.add(clause);
                    }
                }
            }
        } catch (NumberFormatException | IOException ignore) {}

        try { Objects.requireNonNull(br).close(); } catch (IOException ignore) {}
    }

    public int getNumberOfVariables() {
        return numberVariables;
    }

    public int getNumberOfClause() {
        return this.clauses.size();
    }

    public int getClauseSize(){
        return this.clauseSize;
    }

    public Clause getClause(int position) {
        return this.clauses.get(position);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("The list of clauses is : \n");

        for (int i = 0; i < this.clauses.size(); i++)
            string.append(i + 1).append(". ").append(this.clauses.get(i)).append("\n");

        return string.toString();
    }
}