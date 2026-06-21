import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class MidnightNaiveGreedy {
    public static final int LOW_QUALIFIER = 1;
    public static final int HIGH_QUALIFIER = 4;
    public static final int MIN_SCORE = 4;
    public static final int MAX_SCORE = 24;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor1LiveDice;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor2LiveDice;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor3LiveDice;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor4LiveDice;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor5LiveDice;
    private final ArrayList<HashMap<String, long[]>> naiveDistributionsFor6LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor1LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor2LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor3LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor4LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor5LiveDice;
    private final ArrayList<HashMap<String, long[]>> greedyDistributionsFor6LiveDice;

    public MidnightNaiveGreedy() {
        naiveDistributionsFor1LiveDice = new ArrayList<>();
        naiveDistributionsFor2LiveDice = new ArrayList<>();
        naiveDistributionsFor3LiveDice = new ArrayList<>();
        naiveDistributionsFor4LiveDice = new ArrayList<>();
        naiveDistributionsFor5LiveDice = new ArrayList<>();
        naiveDistributionsFor6LiveDice = new ArrayList<>();
        greedyDistributionsFor1LiveDice = new ArrayList<>();
        greedyDistributionsFor2LiveDice = new ArrayList<>();
        greedyDistributionsFor3LiveDice = new ArrayList<>();
        greedyDistributionsFor4LiveDice = new ArrayList<>();
        greedyDistributionsFor5LiveDice = new ArrayList<>();
        greedyDistributionsFor6LiveDice = new ArrayList<>();

        for (int threshold = 0; threshold <= MAX_SCORE; threshold++) {
            if (threshold < MIN_SCORE) {
                naiveDistributionsFor1LiveDice.add(null);
                naiveDistributionsFor2LiveDice.add(null);
                naiveDistributionsFor3LiveDice.add(null);
                naiveDistributionsFor4LiveDice.add(null);
                naiveDistributionsFor5LiveDice.add(null);
                naiveDistributionsFor6LiveDice.add(null);
                greedyDistributionsFor1LiveDice.add(null);
                greedyDistributionsFor2LiveDice.add(null);
                greedyDistributionsFor3LiveDice.add(null);
                greedyDistributionsFor4LiveDice.add(null);
                greedyDistributionsFor5LiveDice.add(null);
                greedyDistributionsFor6LiveDice.add(null);
            } else {
                naiveDistributionsFor1LiveDice.add(new HashMap<>());
                naiveDistributionsFor2LiveDice.add(new HashMap<>());
                naiveDistributionsFor3LiveDice.add(new HashMap<>());
                naiveDistributionsFor4LiveDice.add(new HashMap<>());
                naiveDistributionsFor5LiveDice.add(new HashMap<>());
                naiveDistributionsFor6LiveDice.add(new HashMap<>());
                greedyDistributionsFor1LiveDice.add(new HashMap<>());
                greedyDistributionsFor2LiveDice.add(new HashMap<>());
                greedyDistributionsFor3LiveDice.add(new HashMap<>());
                greedyDistributionsFor4LiveDice.add(new HashMap<>());
                greedyDistributionsFor5LiveDice.add(new HashMap<>());
                greedyDistributionsFor6LiveDice.add(new HashMap<>());
            }
        }
    }

    public static void main(String[] args) {
        MidnightNaiveGreedy midnightNaiveGreedy = new MidnightNaiveGreedy();
        midnightNaiveGreedy.run();
    }

    public void run() {
        populateDistributions();
        calculateNaiveDistributions();
        calculateGreedyDistributions();

        double successDenom = Math.pow(6, 21);
        /*long runningCount = 0L;
        long[] naiveDistribution = naiveDistributionsFor6LiveDice.get(MAX_SCORE).get("00__");
        System.out.println("NAIVE: ");
        for (int i = 0; i < naiveDistribution.length; i++) {
            if (i < 4) {
                System.out.print(i + ": ");
            }
            runningCount += naiveDistribution[i];
            System.out.print(runningCount/successDenom);
            System.out.println(",");
        }

        runningCount = 0L;
        long[] greedyDistribution = greedyDistributionsFor6LiveDice.get(MAX_SCORE).get("00__");
        System.out.println("GREEDY: ");
        for (int i = 0; i < greedyDistribution.length; i++) {
            if (i < 4) {
                System.out.print(i + ": ");
            }
            runningCount += greedyDistribution[i];
            System.out.print(runningCount/successDenom);
            System.out.println(",");
        }*/

        long[][] naiveDistributions = new long[MAX_SCORE+1][];
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            naiveDistributions[score] = naiveDistributionsFor6LiveDice.get(score).get("00__");
        }
        double[] naiveTableA = new double[MAX_SCORE+1];
        System.out.println("NAIVE: ");
        for (int score = MIN_SCORE - 1; score < MAX_SCORE; score++) {
            long runningCount = 0L;
            int threshold = score + 1;
            for (int i = 0; i <= score; i++) {
                runningCount += naiveDistributions[threshold][i];
            }
            if (score == 3) {
                System.out.print("Score " + score + " which is actually 0: ");
            }
            naiveTableA[score] = runningCount/successDenom;
            System.out.print(runningCount/successDenom);
            System.out.println(",");
        }
        naiveTableA[MAX_SCORE] = 1.0;
        System.out.println("Score 24: 1.0");

        long[][] greedyDistributions = new long[MAX_SCORE+1][];
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            greedyDistributions[score] = greedyDistributionsFor6LiveDice.get(score).get("00__");
        }
        double[] greedyTableA = new double[MAX_SCORE+1];
        System.out.println("GREEDY: ");
        for (int score = MIN_SCORE - 1; score < MAX_SCORE; score++) {
            long runningCount = 0L;
            int threshold = score + 1;
            for (int i = 0; i <= score; i++) {
                runningCount += greedyDistributions[threshold][i];
            }
            if (score == 3) {
                System.out.print("Score " + score + " which is actually 0: ");
            }
            greedyTableA[score] = runningCount/successDenom;
            System.out.print(runningCount/successDenom);
            System.out.println(",");
        }
        greedyTableA[MAX_SCORE] = 1.0;
        System.out.println("Score 24: 1.0");

        double[] naiveTableB = new double[MAX_SCORE+1];
        System.out.println("NAIVE UNUSED: ");
        System.out.println("Score 0: 0.0");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            long runningCount = 0L;
            int threshold = score == MAX_SCORE ? MAX_SCORE : score + 1;
            for (int i = 0; i < score; i++) {
                runningCount += naiveDistributions[threshold][i];
            }
            naiveTableB[score] = runningCount/successDenom;
            System.out.print("Score " + score + ": ");
            System.out.print(runningCount/successDenom);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }

        double[] greedyTableB = new double[MAX_SCORE+1];
        System.out.println("GREEDY UNUSED: ");
        System.out.println("Score 0: 0.0");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            long runningCount = 0L;
            int threshold = score == MAX_SCORE ? MAX_SCORE : score + 1;
            for (int i = 0; i < score; i++) {
                runningCount += greedyDistributions[threshold][i];
            }
            greedyTableB[score] = runningCount/successDenom;
            System.out.print("Score " + score + ": ");
            System.out.print(runningCount/successDenom);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }

        System.out.println("NAIVE AVERAGE: ");
        for (int score = MIN_SCORE-1; score <= MAX_SCORE; score++) {
            if (score == 3) {
                System.out.print("Score " + score + " which is actually 0: ");
            }
            System.out.print(naiveTableA[score]/2 + naiveTableB[score]/2);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
        System.out.println("GREEDY AVERAGE: ");
        for (int score = MIN_SCORE-1; score <= MAX_SCORE; score++) {
            if (score == 3) {
                System.out.print("Score " + score + " which is actually 0: ");
            }
            System.out.print(greedyTableA[score]/2 + greedyTableB[score]/2);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }

        double[] naiveTableC = new double[MAX_SCORE+1];
        System.out.println("NAIVE CUCKOO: ");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            long runningCount = 0L;
            for (int i = 0; i <= score; i++) {
                runningCount += naiveDistributions[score][i];
            }
            System.out.print("Score " + score + ": ");
            naiveTableC[score] = runningCount/successDenom;
            System.out.print(runningCount/successDenom);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }

        double[] greedyTableC = new double[MAX_SCORE+1];
        System.out.println("GREEDY CUCKOO: ");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            long runningCount = 0L;
            for (int i = 0; i <= score; i++) {
                runningCount += greedyDistributions[score][i];
            }
            System.out.print("Score " + score + ": ");
            greedyTableC[score] = runningCount/successDenom;
            System.out.print(runningCount/successDenom);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }

        System.out.println("NAIVE SETTLES: ");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            System.out.print(naiveTableA[score-1]/2 + naiveTableC[score]/2);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
        System.out.println("GREEDY SETTLES: ");
        for (int score = MIN_SCORE; score <= MAX_SCORE; score++) {
            System.out.print(greedyTableA[score-1]/2 + greedyTableC[score]/2);
            if (score < MAX_SCORE) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
    }

    //don't care about unreachable states, since they could be reachable under different qualifiers (such as 2-4-24)
    public void populateDistributions() {
        //1 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                naiveDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                naiveDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                naiveDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                naiveDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor1LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }
        }

        //2 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                naiveDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                naiveDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                naiveDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                naiveDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor2LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }
        }

        //3 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                naiveDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                naiveDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                naiveDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "HL";
                naiveDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor3LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }
        }

        //4 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                naiveDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "_L";
                naiveDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "H_";
                naiveDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor4LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            naiveDistributionsFor4LiveDice.get(threshold).put("00HL", new long[MAX_SCORE+1]);
            greedyDistributionsFor4LiveDice.get(threshold).put("00HL", new long[MAX_SCORE+1]);
        }

        //5 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "__";
                naiveDistributionsFor5LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
                greedyDistributionsFor5LiveDice.get(threshold).put(distKey, new long[MAX_SCORE+1]);
            }

            naiveDistributionsFor5LiveDice.get(threshold).put("00_L", new long[MAX_SCORE+1]);
            greedyDistributionsFor5LiveDice.get(threshold).put("00_L", new long[MAX_SCORE+1]);

            naiveDistributionsFor5LiveDice.get(threshold).put("00H_", new long[MAX_SCORE+1]);
            greedyDistributionsFor5LiveDice.get(threshold).put("00H_", new long[MAX_SCORE+1]);
        }

        //6 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            naiveDistributionsFor6LiveDice.get(threshold).put("00__", new long[MAX_SCORE+1]);
            greedyDistributionsFor6LiveDice.get(threshold).put("00__", new long[MAX_SCORE+1]);
        }
    }

    public void calculateNaiveDistributions() {
        //1 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    naiveDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    naiveDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    naiveDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    naiveDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }
        }

        //2 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                naiveDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }
        }

        //3 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //4 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00HL";
            boolean hasHighQualifier = true;
            boolean hasLowQualifier = true;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            int[] liveDice = {dice1, dice2, dice3, dice4};
                            int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                            if (numDiceToKeep == 4) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                            } else if (numDiceToKeep == 3) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                }
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                }
                            } else {    // == 1
                                String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    naiveDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //5 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                for (int dice5 = 1; dice5 <= 6; dice5++) {
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                    int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    if (numDiceToKeep == 5) {
                                        int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                    } else if (numDiceToKeep == 4) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 3) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 2) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                        }
                                    } else {    // == 1
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = naiveDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00_L";
            boolean hasHighQualifier = false;
            boolean hasLowQualifier = true;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                if (numDiceToKeep == 5) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                    naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                } else if (numDiceToKeep == 4) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            distKey = "00H_";
            hasHighQualifier = true;
            hasLowQualifier = false;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                if (numDiceToKeep == 5) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                    naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                } else if (numDiceToKeep == 4) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = naiveDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        naiveDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //6 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            String distKey = "00__";
            boolean hasHighQualifier = false;
            boolean hasLowQualifier = false;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                for (int dice6 = 1; dice6 <= 6; dice6++) {
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5, dice6};
                                    int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    if (numDiceToKeep == 6) {
                                        int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                        naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[score] += 470184984576L;
                                    } else if (numDiceToKeep == 5) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = naiveDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 78364164096L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 4) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = naiveDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 2176782336L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 3) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = naiveDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 2) {
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = naiveDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                        }
                                    } else {    // == 1
                                        String lowerDistKey = deriveNaiveLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = naiveDistributionsFor5LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            naiveDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void calculateGreedyDistributions() {
        //1 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    greedyDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    greedyDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    greedyDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    int[] liveDice = {dice1};
                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                    greedyDistributionsFor1LiveDice.get(threshold).get(distKey)[score] += 1L;
                }
            }
        }

        //2 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        int[] liveDice = {dice1, dice2};
                        int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                        if (numDiceToKeep == 2) {
                            int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                            greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[score] += 6L;
                        } else {    // == 1
                            String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                greedyDistributionsFor2LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }
        }

        //3 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "HL";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            int[] liveDice = {dice1, dice2, dice3};
                            int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                            if (numDiceToKeep == 3) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[score] += 216L;
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor3LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //4 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "_L";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = true;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "H_";
                boolean hasHighQualifier = true;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                if (numDiceToKeep == 4) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00HL";
            boolean hasHighQualifier = true;
            boolean hasLowQualifier = true;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            int[] liveDice = {dice1, dice2, dice3, dice4};
                            int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                            if (numDiceToKeep == 4) {
                                int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[score] += 46656L;
                            } else if (numDiceToKeep == 3) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                }
                            } else if (numDiceToKeep == 2) {
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += 216L * lowerDist[i];
                                }
                            } else {    // == 1
                                String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    greedyDistributionsFor4LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //5 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "__";
                boolean hasHighQualifier = false;
                boolean hasLowQualifier = false;
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                for (int dice5 = 1; dice5 <= 6; dice5++) {
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                    int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                    if (numDiceToKeep == 5) {
                                        int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked);
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                    } else if (numDiceToKeep == 4) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 3) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 2) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                        }
                                    } else {    // == 1
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
                                        long[] lowerDist = greedyDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00_L";
            boolean hasHighQualifier = false;
            boolean hasLowQualifier = true;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                if (numDiceToKeep == 5) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                    greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                } else if (numDiceToKeep == 4) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            distKey = "00H_";
            hasHighQualifier = true;
            hasLowQualifier = false;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                if (numDiceToKeep == 5) {
                                    int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                    greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[score] += 60466176L;
                                } else if (numDiceToKeep == 4) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 3) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (numDiceToKeep == 2) {
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else {    // == 1
                                    String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    long[] lowerDist = greedyDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        greedyDistributionsFor5LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //6 dice
        for (int threshold = MIN_SCORE; threshold <= MAX_SCORE; threshold++) {
            String distKey = "00__";
            boolean hasHighQualifier = false;
            boolean hasLowQualifier = false;
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                for (int dice6 = 1; dice6 <= 6; dice6++) {
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5, dice6};
                                    int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                    if (numDiceToKeep == 6) {
                                        int score = calculateScoreIfKeptAllDice(liveDice, hasLowQualifier, hasHighQualifier, 0);
                                        greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[score] += 470184984576L;
                                    } else if (numDiceToKeep == 5) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = greedyDistributionsFor1LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 78364164096L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 4) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = greedyDistributionsFor2LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 2176782336L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 3) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = greedyDistributionsFor3LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (numDiceToKeep == 2) {
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = greedyDistributionsFor4LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += 7776L * lowerDist[i];
                                        }
                                    } else {    // == 1
                                        String lowerDistKey = deriveGreedyLowerDistKey(liveDice, hasLowQualifier, hasHighQualifier, 0, threshold);
                                        long[] lowerDist = greedyDistributionsFor5LiveDice.get(threshold).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            greedyDistributionsFor6LiveDice.get(threshold).get(distKey)[i] += lowerDist[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Given the current state, derive the key for the lower distribution after using the naive strategy on this state
    //ALSO, there's a slight blemish in not being able to distinguish e.g. 12HL with 1 live dice and 2 live dice
    //when it comes to handling originally 3 or more live dice, so the calling method must handle that
    public String deriveNaiveLowerDistKey(int[] liveDice, boolean hasLowQualifier, boolean hasHighQualifier,
                                          int pointsBanked, int threshold) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }
        int numLiveDice = liveDice.length;
        if (numLiveDice < 1 || numLiveDice > 6) {
            throw new IllegalArgumentException("Invalid numLiveDice: " + numLiveDice);
        }
        for (int dice : liveDice) {
            if (dice < 1 || dice > 6) {
                throw new IllegalArgumentException("Invalid liveDice: " + Arrays.toString(liveDice));
            }
        }
        int numBankedDice = hasHighQualifier ? hasLowQualifier ? 4 - numLiveDice : 5 - numLiveDice :
                hasLowQualifier ? 5 - numLiveDice : 6 - numLiveDice;
        if (numBankedDice < 0) {
            throw new IllegalArgumentException("Invalid qualifiers: " + hasLowQualifier + ", " + hasHighQualifier);
        }
        if (pointsBanked < numBankedDice || pointsBanked > 6 * numBankedDice) {
            throw new IllegalArgumentException("Invalid pointsBanked: " + pointsBanked);
        }
        if (threshold < 4 || threshold > 24) {
            throw new IllegalArgumentException("Invalid threshold: " + threshold);
        }

        int numDiceToKeep = naiveStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
        //you can't derive a lower distKey if numDiceToKeep is equal to numLiveDice, or an unexpected invalid value
        if (numDiceToKeep <= 0 || numDiceToKeep > numLiveDice) {
            throw new IllegalStateException("unexpected value for numDiceToKeep: " + numDiceToKeep);
        } else if (numDiceToKeep == numLiveDice) {
            throw new IllegalStateException("cannot derive lower distKey since naive strategy says to keep all dice: "
                    + numDiceToKeep);
        }
        sortHighToLowWithQualifiersFirst(liveDice, !hasLowQualifier, !hasHighQualifier);

        boolean canKeepLowQualifier = false;
        boolean canKeepHighQualifier = false;
        int newPointsBanked = pointsBanked;
        for (int i = 0; i < numDiceToKeep; i++) {
            if (!hasHighQualifier && liveDice[i] == HIGH_QUALIFIER) {
                canKeepHighQualifier = true;
            } else if (!hasLowQualifier && liveDice[i] == LOW_QUALIFIER) {
                canKeepLowQualifier = true;
            } else {
                newPointsBanked += liveDice[i];
            }
        }
        String points = newPointsBanked < 10 ? "0" + newPointsBanked : "" + newPointsBanked;
        String H = hasHighQualifier || canKeepHighQualifier ? "H" : "_";
        String L = hasLowQualifier || canKeepLowQualifier ? "L" : "_";
        return points + H + L;
    }

    //Given the current state, derive the key for the lower distribution after using the naive strategy on this state
    //ALSO, there's a slight blemish in not being able to distinguish e.g. 12HL with 1 live dice and 2 live dice
    //when it comes to handling originally 3 or more live dice, so the calling method must handle that
    public String deriveGreedyLowerDistKey(int[] liveDice, boolean hasLowQualifier, boolean hasHighQualifier,
                                          int pointsBanked, int threshold) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }
        int numLiveDice = liveDice.length;
        if (numLiveDice < 1 || numLiveDice > 6) {
            throw new IllegalArgumentException("Invalid numLiveDice: " + numLiveDice);
        }
        for (int dice : liveDice) {
            if (dice < 1 || dice > 6) {
                throw new IllegalArgumentException("Invalid liveDice: " + Arrays.toString(liveDice));
            }
        }
        int numBankedDice = hasHighQualifier ? hasLowQualifier ? 4 - numLiveDice : 5 - numLiveDice :
                hasLowQualifier ? 5 - numLiveDice : 6 - numLiveDice;
        if (numBankedDice < 0) {
            throw new IllegalArgumentException("Invalid qualifiers: " + hasLowQualifier + ", " + hasHighQualifier);
        }
        if (pointsBanked < numBankedDice || pointsBanked > 6 * numBankedDice) {
            throw new IllegalArgumentException("Invalid pointsBanked: " + pointsBanked);
        }
        if (threshold < 4 || threshold > 24) {
            throw new IllegalArgumentException("Invalid threshold: " + threshold);
        }

        int numDiceToKeep = greedyStrategy(liveDice, hasLowQualifier, hasHighQualifier, pointsBanked, threshold);
        //you can't derive a lower distKey if numDiceToKeep is equal to numLiveDice, or an unexpected invalid value
        if (numDiceToKeep <= 0 || numDiceToKeep > numLiveDice) {
            throw new IllegalStateException("unexpected value for numDiceToKeep: " + numDiceToKeep);
        } else if (numDiceToKeep == numLiveDice) {
            throw new IllegalStateException("cannot derive lower distKey since greedy strategy says to keep all dice: "
                    + numDiceToKeep);
        }
        sortHighToLowWithQualifiersFirst(liveDice, !hasLowQualifier, !hasHighQualifier);

        boolean canKeepLowQualifier = false;
        boolean canKeepHighQualifier = false;
        int newPointsBanked = pointsBanked;
        for (int i = 0; i < numDiceToKeep; i++) {
            if (!hasHighQualifier && liveDice[i] == HIGH_QUALIFIER) {
                canKeepHighQualifier = true;
            } else if (!hasLowQualifier && liveDice[i] == LOW_QUALIFIER) {
                canKeepLowQualifier = true;
            } else {
                newPointsBanked += liveDice[i];
            }
        }
        String points = newPointsBanked < 10 ? "0" + newPointsBanked : "" + newPointsBanked;
        String H = hasHighQualifier || canKeepHighQualifier ? "H" : "_";
        String L = hasLowQualifier || canKeepLowQualifier ? "L" : "_";
        return points + H + L;
    }

    //helper method for determining priority of dice to keep by the caller of naiveStrategy() or greedyStrategy()
    public void sortHighToLowWithQualifiersFirst(int[] liveDice, boolean needLowQualifier, boolean needHighQualifier) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }

        Arrays.sort(liveDice);
        //move just one of the needed low qualifiers to the end of the array
        if (needLowQualifier && contains(liveDice, LOW_QUALIFIER)) {
            int oldIndex = -1;
            for (int i = 0; i < liveDice.length; i++) {
                if (liveDice[i] == LOW_QUALIFIER) {
                    oldIndex = i;
                    break;
                }
            }
            for (int i = oldIndex; i < liveDice.length - 1; i++) {
                liveDice[i] = liveDice[i+1];
            }
            liveDice[liveDice.length - 1] = LOW_QUALIFIER;
        }
        //then, so high qualifier sorts above low qualifier, do the same for the high qualifier afterward
        if (needHighQualifier && contains(liveDice, HIGH_QUALIFIER)) {
            int oldIndex = -1;
            for (int i = 0; i < liveDice.length; i++) {
                if (liveDice[i] == HIGH_QUALIFIER) {
                    oldIndex = i;
                    break;
                }
            }
            for (int i = oldIndex; i < liveDice.length - 1; i++) {
                liveDice[i] = liveDice[i+1];
            }
            liveDice[liveDice.length - 1] = HIGH_QUALIFIER;
        }
        //finally, reverse the array
        for (int i = 0; i < liveDice.length / 2; i++) {
            int temp = liveDice[i];
            liveDice[i] = liveDice[liveDice.length - 1 - i];
            liveDice[liveDice.length - 1 - i] = temp;
        }
    }

    //use threshold = 24 for the unenhanced version, i.e. will only bank all if guarantees 24, the maximum score
    //both naive and greedy always prioritize missing qualifiers, so only need to return the total number of dice kept
    //the calling method will figure out the dice kept with the following priority:
    //Missing High Qualifier > Missing Low Qualifier > 6 > 5 > 4 > 3 > 2 > 1
    public int naiveStrategy(int[] liveDice, boolean hasLowQualifier, boolean hasHighQualifier, int pointsBanked,
                             int threshold) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }
        int numLiveDice = liveDice.length;
        if (numLiveDice < 1 || numLiveDice > 6) {
            throw new IllegalArgumentException("Invalid numLiveDice: " + numLiveDice);
        }
        for (int dice : liveDice) {
            if (dice < 1 || dice > 6) {
                throw new IllegalArgumentException("Invalid liveDice: " + Arrays.toString(liveDice));
            }
        }
        int numBankedDice = hasHighQualifier ? hasLowQualifier ? 4 - numLiveDice : 5 - numLiveDice :
                                               hasLowQualifier ? 5 - numLiveDice : 6 - numLiveDice;
        if (numBankedDice < 0) {
            throw new IllegalArgumentException("Invalid qualifiers: " + hasLowQualifier + ", " + hasHighQualifier);
        }
        if (pointsBanked < numBankedDice || pointsBanked > 6 * numBankedDice) {
            throw new IllegalArgumentException("Invalid pointsBanked: " + pointsBanked);
        }
        if (threshold < 4 || threshold > 24) {
            throw new IllegalArgumentException("Invalid threshold: " + threshold);
        }

        if (numLiveDice == 1) {
            return 1;
        }

        if (hasHighQualifier) {
            if (hasLowQualifier) {
                if (calculateScoreIfKeptAllDice(liveDice, true, true, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, 6)) {
                    return containsHowMany(liveDice, 6);
                } else {
                    return 1;
                }
            } else {
                if (calculateScoreIfKeptAllDice(liveDice, false, true, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, LOW_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 1;    //+1 for the low qualifier
                } else {
                    return 1;
                }
            }
        } else {
            if (hasLowQualifier) {
                if (calculateScoreIfKeptAllDice(liveDice, true, false, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, HIGH_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 1;    //+1 for the high qualifier
                } else {
                    return 1;
                }
            } else {
                if (calculateScoreIfKeptAllDice(liveDice, false, false, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, LOW_QUALIFIER) && contains(liveDice, HIGH_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 2;    //+2 for both qualifiers
                } else {
                    return 1;   //only keep the high qualifier, else only the low qualifier, else the single highest
                }
            }
        }
    }

    public int greedyStrategy(int[] liveDice, boolean hasLowQualifier, boolean hasHighQualifier, int pointsBanked,
                             int threshold) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }
        int numLiveDice = liveDice.length;
        if (numLiveDice < 1 || numLiveDice > 6) {
            throw new IllegalArgumentException("Invalid numLiveDice: " + numLiveDice);
        }
        for (int dice : liveDice) {
            if (dice < 1 || dice > 6) {
                throw new IllegalArgumentException("Invalid liveDice: " + Arrays.toString(liveDice));
            }
        }
        int numBankedDice = hasHighQualifier ? hasLowQualifier ? 4 - numLiveDice : 5 - numLiveDice :
                                               hasLowQualifier ? 5 - numLiveDice : 6 - numLiveDice;
        if (numBankedDice < 0) {
            throw new IllegalArgumentException("Invalid qualifiers: " + hasLowQualifier + ", " + hasHighQualifier);
        }
        if (pointsBanked < numBankedDice || pointsBanked > 6 * numBankedDice) {
            throw new IllegalArgumentException("Invalid pointsBanked: " + pointsBanked);
        }
        if (threshold < 4 || threshold > 24) {
            throw new IllegalArgumentException("Invalid threshold: " + threshold);
        }

        if (numLiveDice == 1) {
            return 1;
        }

        if (hasHighQualifier) {
            if (hasLowQualifier) {
                if (calculateScoreIfKeptAllDice(liveDice, true, true, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, 6)) {
                    return containsHowMany(liveDice, 6);
                } else {
                    return 1;
                }
            } else {
                if (calculateScoreIfKeptAllDice(liveDice, false, true, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, LOW_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 1;    //+1 for the low qualifier
                } else if (contains(liveDice, 6)) {
                    return Math.min(containsHowMany(liveDice, 6), numLiveDice - 1); //do not keep all 6s and lose
                } else {
                    return 1;
                }
            }
        } else {
            if (hasLowQualifier) {
                if (calculateScoreIfKeptAllDice(liveDice, true, false, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, HIGH_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 1;    //+1 for the high qualifier
                } else if (contains(liveDice, 6)) {
                    return Math.min(containsHowMany(liveDice, 6), numLiveDice - 1); //do not keep all 6s and lose
                } else {
                    return 1;
                }
            } else {
                if (calculateScoreIfKeptAllDice(liveDice, false, false, pointsBanked) >= threshold) {
                    return numLiveDice;
                } else if (contains(liveDice, LOW_QUALIFIER) && contains(liveDice, HIGH_QUALIFIER)) {
                    return containsHowMany(liveDice, 6) + 2;    //+2 for both qualifiers
                } else if (contains(liveDice, LOW_QUALIFIER) || contains(liveDice, HIGH_QUALIFIER)) {
                    return Math.min(containsHowMany(liveDice, 6) + 1, numLiveDice - 1); //+1 for the qualifier, and do not keep too many 6s and lose
                } else if (contains(liveDice, 6) && numLiveDice > 2) {  //&& numLiveDice > 2 only required on [2, 6], [3, 6], [5, 6], and [6, 6] to prevent illegally keeping 0 dice
                    return Math.min(containsHowMany(liveDice, 6), numLiveDice - 2); //do not keep too many 6s and lose
                } else {
                    return 1;
                }
            }
        }
    }

    public int calculateScoreIfKeptAllDice(int[] liveDice,
                                           boolean hasLowQualifier, boolean hasHighQualifier, int pointsBanked) {
        if (liveDice == null) {
            throw new IllegalArgumentException("liveDice is null");
        }
        int numLiveDice = liveDice.length;
        if (numLiveDice < 1 || numLiveDice > 6) {
            throw new IllegalArgumentException("Invalid numLiveDice: " + numLiveDice);
        }
        for (int dice : liveDice) {
            if (dice < 1 || dice > 6) {
                throw new IllegalArgumentException("Invalid liveDice: " + Arrays.toString(liveDice));
            }
        }
        int numBankedDice = hasHighQualifier ? hasLowQualifier ? 4 - numLiveDice : 5 - numLiveDice :
                hasLowQualifier ? 5 - numLiveDice : 6 - numLiveDice;
        if (numBankedDice < 0) {
            throw new IllegalArgumentException("Invalid qualifiers: " + hasLowQualifier + ", " + hasHighQualifier);
        }
        if (pointsBanked < numBankedDice || pointsBanked > 6 * numBankedDice) {
            throw new IllegalArgumentException("Invalid pointsBanked: " + pointsBanked);
        }

        boolean canKeepLowQualifier = !hasLowQualifier && contains(liveDice, LOW_QUALIFIER);
        boolean canKeepHighQualifier = !hasHighQualifier && contains(liveDice, HIGH_QUALIFIER);
        if ((hasLowQualifier || canKeepLowQualifier) && (hasHighQualifier || canKeepHighQualifier)) {   //qualification
            int score = pointsBanked;
            for (int j : liveDice) {
                score += j;
            }
            if (canKeepLowQualifier) {
                score -= LOW_QUALIFIER;
            }
            if (canKeepHighQualifier) {
                score -= HIGH_QUALIFIER;
            }
            return score;
        } else {
            return 0;
        }
    }

    public boolean contains(int[] array, int diceNum) {
        for (int num : array) {
            if (num == diceNum) {
                return true;
            }
        }
        return false;
    }

    public int containsHowMany(int[] array, int diceNum) {
        int count = 0;
        for (int num : array) {
            if (num == diceNum) {
                count++;
            }
        }
        return count;
    }
}