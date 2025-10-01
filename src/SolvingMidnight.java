import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class SolvingMidnight {
    public static final String PATHWAY = "/Users/davidswu/IdeaProjects/SolvingMidnight/src/";
    public static final boolean ABRIDGED_RESULTS = false;
    public static final int LOW_QUALIFIER = 1;
    public static final int HIGH_QUALIFIER = 4;
    public static final int CAPACITY = 46;

    //If I were to redo this, I think I would make ArrayList<ArrayList<HashMap<String, MidnightState>> and
    //use the CONDITION first as the major axis, before the # of live dice as the minor axis
    ArrayList<HashMap<String, MidnightState>> mapsFor1LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor2LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor3LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor4LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor5LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor6LiveDice;

    //The inner ArrayList is like an Array with length 25 for the scores of 0~24
    //Scores of 1, 2, and 3 are impossible so those indices always contain 0
    ArrayList<HashMap<String, long[]>> distributionsFor1LiveDice;
    ArrayList<HashMap<String, long[]>> distributionsFor2LiveDice;
    ArrayList<HashMap<String, long[]>> distributionsFor3LiveDice;
    ArrayList<HashMap<String, long[]>> distributionsFor4LiveDice;
    ArrayList<HashMap<String, long[]>> distributionsFor5LiveDice;
    ArrayList<HashMap<String, long[]>> distributionsFor6LiveDice;

    public SolvingMidnight() {
        mapsFor1LiveDice = new ArrayList<>();
        mapsFor2LiveDice = new ArrayList<>();
        mapsFor3LiveDice = new ArrayList<>();
        mapsFor4LiveDice = new ArrayList<>();
        mapsFor5LiveDice = new ArrayList<>();
        mapsFor6LiveDice = new ArrayList<>();

        for (int i = 0; i < CAPACITY; i++) {
            mapsFor1LiveDice.add(new HashMap<>());
            mapsFor2LiveDice.add(new HashMap<>());
            mapsFor3LiveDice.add(new HashMap<>());
            mapsFor4LiveDice.add(new HashMap<>());
            mapsFor5LiveDice.add(new HashMap<>());
            mapsFor6LiveDice.add(new HashMap<>());
        }

        distributionsFor1LiveDice = new ArrayList<>();
        distributionsFor2LiveDice = new ArrayList<>();
        distributionsFor3LiveDice = new ArrayList<>();
        distributionsFor4LiveDice = new ArrayList<>();
        distributionsFor5LiveDice = new ArrayList<>();
        distributionsFor6LiveDice = new ArrayList<>();

        for (int i = 0; i < CAPACITY; i++) {
            distributionsFor1LiveDice.add(new HashMap<>());
            distributionsFor2LiveDice.add(new HashMap<>());
            distributionsFor3LiveDice.add(new HashMap<>());
            distributionsFor4LiveDice.add(new HashMap<>());
            distributionsFor5LiveDice.add(new HashMap<>());
            distributionsFor6LiveDice.add(new HashMap<>());
        }
    }

    public static void main(String[] args) {
        SolvingMidnight solvingMidnight = new SolvingMidnight();
        solvingMidnight.run();
    }

    public void run() {
        populateWithStates();

        calculateInitialRunThrough();

        calculateAllStateEquitiesRecursively(45);

        //writeToFile(45);

        for (int condition = 0; condition <= 45; condition++) {
            if (condition != 45) {
                continue;
            }

            BigDecimal runningTotal = BigDecimal.ZERO;
            BigDecimal runningEquityGivenSuccess = BigDecimal.ZERO;
            BigDecimal runningEquityGivenFailure = BigDecimal.ZERO;
            for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
                runningTotal = runningTotal.add(BigDecimal.valueOf(midnightState.getSuccessNum()));
                BigDecimal ratio = BigDecimal.valueOf(midnightState.getSuccessNum()).divide(
                        BigDecimal.valueOf(midnightState.getSuccessDenom()), 20, RoundingMode.HALF_UP);
                BigDecimal tempSuccess = BigDecimal.valueOf(midnightState.getEquityGivenSuccess()).multiply(ratio);
                runningEquityGivenSuccess = runningEquityGivenSuccess.add(tempSuccess);
                BigDecimal oppositeRatio = BigDecimal.ONE.subtract(ratio);
                BigDecimal tempFailure = BigDecimal.valueOf(midnightState.getEquityGivenFailure()).multiply(oppositeRatio);
                runningEquityGivenFailure = runningEquityGivenFailure.add(tempFailure);
            }
            BigDecimal exp_6_21 = BigDecimal.valueOf(6).pow(21);
            BigDecimal overallRatio = runningTotal.divide(exp_6_21, 20, RoundingMode.HALF_UP);
            BigDecimal overallOppositeRatio = BigDecimal.ONE.subtract(overallRatio);
            BigDecimal percentage = overallRatio.multiply(BigDecimal.valueOf(100));
            if (condition < 10) {
                System.out.println("Condition: " + condition);
            } else {
                System.out.println("Condition: " + condition);
            }
            System.out.println("RunningTotal: " + runningTotal);
            System.out.println("Percentage: " + percentage);
            System.out.println("EquityGivenSuccess: " + runningEquityGivenSuccess.divide(overallRatio, 10, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(46656.0), 10, RoundingMode.HALF_UP));
            if (overallOppositeRatio.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("EquityGivenFailure: N/A");
            } else {
                System.out.println("EquityGivenFailure: " + runningEquityGivenFailure.divide(overallOppositeRatio, 10, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(46656.0), 10, RoundingMode.HALF_UP));
            }
            System.out.println();
        }

        populateDistributions();
        calculateDistributions();
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
        long[] arr = distributionsFor6LiveDice.get(45).get("00__");
        for (int i = 0; i < arr.length; i++) {
            if (i < 10) {
                System.out.println(" " + i + ": " + arr[i] + " ");
            } else {
                System.out.println(i + ": " + arr[i] + " ");
            }
        }
        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            if (i < 10) {
                System.out.println(" " + i + ": " + arr[i] / Math.pow(6, 21) + " ");
            } else {
                System.out.println(i + ": " + arr[i] / Math.pow(6, 21) + " ");
            }
        }

        System.out.println();
        double cumSum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            cumSum += arr[i];
            if (i < 10) {
                System.out.println(" " + i + ": " + cumSum / Math.pow(6, 21));
            } else {
                System.out.println(i + ": " + cumSum / Math.pow(6, 21));
            }
        }
        System.out.println();
        double cumSum1 = 0.0;
        for (int i = 0; i < arr.length; i++) {
            cumSum1 += arr[i] / 2.0;
            if (i < 10) {
                System.out.println(" " + i + ": " + cumSum1 / Math.pow(6, 21));
            } else {
                System.out.println(i + ": " + cumSum1 / Math.pow(6, 21));
            }
            cumSum1 += arr[i] / 2.0;
        }
    }

    //don't care about unreachable states, since they could be reachable under different qualifiers (such as 2-4-24)
    private void populateWithStates() {
        //1 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "__|" + dice1;
                    int[] liveDice = {dice1};
                    MidnightState midnightState = new MidnightState(condition, 1, liveDice,
                            false, false, pointsBanked);
                    mapsFor1LiveDice.get(condition).put(key, midnightState);
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "_L|" + dice1;
                    int[] liveDice = {dice1};
                    MidnightState midnightState = new MidnightState(condition, 1, liveDice,
                            true, false, pointsBanked);
                    mapsFor1LiveDice.get(condition).put(key, midnightState);
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "H_|" + dice1;
                    int[] liveDice = {dice1};
                    MidnightState midnightState = new MidnightState(condition, 1, liveDice,
                            false, true, pointsBanked);
                    mapsFor1LiveDice.get(condition).put(key, midnightState);
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "HL|" + dice1;
                    int[] liveDice = {dice1};
                    MidnightState midnightState = new MidnightState(condition, 1, liveDice,
                            true, true, pointsBanked);
                    mapsFor1LiveDice.get(condition).put(key, midnightState);
                }
            }
        }

        //2 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "__|" + dice1 + dice2;
                        int[] liveDice = {dice1, dice2};
                        MidnightState midnightState = new MidnightState(condition, 2, liveDice,
                                false, false, pointsBanked);
                        mapsFor2LiveDice.get(condition).put(key, midnightState);
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "_L|" + dice1 + dice2;
                        int[] liveDice = {dice1, dice2};
                        MidnightState midnightState = new MidnightState(condition, 2, liveDice,
                                true, false, pointsBanked);
                        mapsFor2LiveDice.get(condition).put(key, midnightState);
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "H_|" + dice1 + dice2;
                        int[] liveDice = {dice1, dice2};
                        MidnightState midnightState = new MidnightState(condition, 2, liveDice,
                                false, true, pointsBanked);
                        mapsFor2LiveDice.get(condition).put(key, midnightState);
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "HL|" + dice1 + dice2;
                        int[] liveDice = {dice1, dice2};
                        MidnightState midnightState = new MidnightState(condition, 2, liveDice,
                                true, true, pointsBanked);
                        mapsFor2LiveDice.get(condition).put(key, midnightState);
                    }
                }
            }
        }

        //3 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "__|" + dice1 + dice2 + dice3;
                            int[] liveDice = {dice1, dice2, dice3};
                            MidnightState midnightState = new MidnightState(condition, 3, liveDice,
                                    false, false, pointsBanked);
                            mapsFor3LiveDice.get(condition).put(key, midnightState);
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "_L|" + dice1 + dice2 + dice3;
                            int[] liveDice = {dice1, dice2, dice3};
                            MidnightState midnightState = new MidnightState(condition, 3, liveDice,
                                    true, false, pointsBanked);
                            mapsFor3LiveDice.get(condition).put(key, midnightState);
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "H_|" + dice1 + dice2 + dice3;
                            int[] liveDice = {dice1, dice2, dice3};
                            MidnightState midnightState = new MidnightState(condition, 3, liveDice,
                                    false, true, pointsBanked);
                            mapsFor3LiveDice.get(condition).put(key, midnightState);
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String points = "0" + pointsBanked;
                            String key = points + "HL|" + dice1 + dice2 + dice3;
                            int[] liveDice = {dice1, dice2, dice3};
                            MidnightState midnightState = new MidnightState(condition, 3, liveDice,
                                    true, true, pointsBanked);
                            mapsFor3LiveDice.get(condition).put(key, midnightState);
                        }
                    }
                }
            }
        }

        //4 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "__|" + dice1 + dice2 + dice3 + dice4;
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                MidnightState midnightState = new MidnightState(condition, 4, liveDice,
                                        false, false, pointsBanked);
                                mapsFor4LiveDice.get(condition).put(key, midnightState);
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "_L|" + dice1 + dice2 + dice3 + dice4;
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                MidnightState midnightState = new MidnightState(condition, 4, liveDice,
                                        true, false, pointsBanked);
                                mapsFor4LiveDice.get(condition).put(key, midnightState);
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "H_|" + dice1 + dice2 + dice3 + dice4;
                                int[] liveDice = {dice1, dice2, dice3, dice4};
                                MidnightState midnightState = new MidnightState(condition, 4, liveDice,
                                        false, true, pointsBanked);
                                mapsFor4LiveDice.get(condition).put(key, midnightState);
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            String key = "00HL|" + dice1 + dice2 + dice3 + dice4;
                            int[] liveDice = {dice1, dice2, dice3, dice4};
                            MidnightState midnightState = new MidnightState(condition, 4, liveDice,
                                    true, true, 0);
                            mapsFor4LiveDice.get(condition).put(key, midnightState);
                        }
                    }
                }
            }
        }

        //5 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                for (int dice5 = 1; dice5 <= 6; dice5++) {
                                    String points = "0" + pointsBanked;
                                    String key = points + "__|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                    MidnightState midnightState = new MidnightState(condition, 5, liveDice,
                                            false, false, pointsBanked);
                                    mapsFor5LiveDice.get(condition).put(key, midnightState);
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                String key = "00_L|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                MidnightState midnightState = new MidnightState(condition, 5, liveDice,
                                        true, false, 0);
                                mapsFor5LiveDice.get(condition).put(key, midnightState);
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                String key = "00H_|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                int[] liveDice = {dice1, dice2, dice3, dice4, dice5};
                                MidnightState midnightState = new MidnightState(condition, 5, liveDice,
                                        false, true, 0);
                                mapsFor5LiveDice.get(condition).put(key, midnightState);
                            }
                        }
                    }
                }
            }
        }

        //6 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                for (int dice6 = 1; dice6 <= 6; dice6++) {
                                    String key = "00__|" + dice1 + dice2 + dice3 + dice4 + dice5 + dice6;
                                    int[] liveDice = {dice1, dice2, dice3, dice4, dice5, dice6};
                                    MidnightState midnightState = new MidnightState(condition, 6, liveDice,
                                            false, false, 0);
                                    mapsFor6LiveDice.get(condition).put(key, midnightState);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateInitialRunThrough() {
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }

            for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }

            for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }

            for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }

            for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }

            for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
                midnightState.calculateEquityIfKeptAllDice();
            }
        }
    }

    private void calculateAllStateEquitiesRecursively() {
        for (int condition = 0; condition < CAPACITY; condition++) {
            
            System.out.println(condition);

            for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("1 done");

            for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("2 done");

            for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("3 done");

            for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("4 done");

            for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("5 done");

            for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
            }
            System.out.println("6 done");
        }
    }

    private void calculateAllStateEquitiesRecursively(int condition) {
        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("1 done");

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("2 done");

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("3 done");

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("4 done");

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("5 done");

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("6 done");
    }

    private void calculateAllStateEquitiesRecursively(int condition, int maxLiveDice) {
        if (maxLiveDice <= 0 || maxLiveDice > 6) {
            throw new IllegalArgumentException("maxLiveDice: " + maxLiveDice);
        }

        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("1 done");
        if (maxLiveDice == 1) {
            return;
        }

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("2 done");
        if (maxLiveDice == 2) {
            return;
        }

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("3 done");
        if (maxLiveDice == 3) {
            return;
        }

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("4 done");
        if (maxLiveDice == 4) {
            return;
        }

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("5 done");
        if (maxLiveDice == 5) {
            return;
        }

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState, condition <= 3 || condition >= 25);
        }
        System.out.println("6 done");
    }

    //Assumes calculateEquityIfKeptAllDice() has been run for the current state, and
    //solveStateEquityRecursively() has been run for the branching states one level simpler
    private void solveStateEquityRecursively(MidnightState midnightState, boolean mergeSuccessesAndEquity) {
        if (midnightState.getNumLiveDice() <= 1) {  //the base case is already done
            return;
        }

        int optimalPolicyQual = midnightState.getOptimalPolicyQual();
        int optimalPolicyHigh = midnightState.getOptimalPolicyHigh();
        long successDenom = midnightState.getSuccessDenom();
        long successNum = midnightState.getSuccessNum();
        double equityGivenSuccess = midnightState.getEquityGivenSuccess();
        double equityGivenFailure = midnightState.getEquityGivenFailure();

        int maxQual = midnightState.getOptimalPolicyQual();
        int maxHigh = midnightState.getOptimalPolicyHigh();

        for (int qual = maxQual; qual >= 0; qual--) {
            for (int high = maxHigh; high >= 0; high--) {
                if (qual == 0 && high == 0) {
                    break; //would be "continue" but it's the last iteration, because must keep at least 1 dice
                }
                if (qual == maxQual && high == maxHigh) {
                    continue; //already calculated the equity for keeping all dice
                }

                int diceKept = qual + high;
                int newNumLiveDice = midnightState.getNumLiveDice() - diceKept;    //assert it's >= 1
                int[] newDiceArray;
                String keyHead;
                if (qual == 2) {
                    //copy the array with a LOW_QUALIFIER and a HIGH_QUALIFIER removed
                    newDiceArray = new int[midnightState.getNumLiveDice() - 2];
                    boolean lowQualifierFlag = false;
                    boolean highQualifierFlag = false;
                    for (int i = 0, ind = 0; i < midnightState.getNumLiveDice(); i++) {
                        if (!lowQualifierFlag && midnightState.getLiveDice()[i] == LOW_QUALIFIER) {
                            lowQualifierFlag = true;
                            continue;
                        }
                        if (!highQualifierFlag && midnightState.getLiveDice()[i] == HIGH_QUALIFIER) {
                            highQualifierFlag = true;
                            continue;
                        }
                        newDiceArray[ind] = midnightState.getLiveDice()[i];
                        ind++;
                    }
                    keyHead = "HL|";
                } else if (qual == 1) {
                    newDiceArray = new int[midnightState.getNumLiveDice() - 1];
                    if (midnightState.hasLowQualifier() || midnightState.canKeepHighQualifier()) {
                        boolean highQualifierFlag = false;
                        for (int i = 0, ind = 0; i < midnightState.getNumLiveDice(); i++) {
                            if (!highQualifierFlag && midnightState.getLiveDice()[i] == HIGH_QUALIFIER) {
                                highQualifierFlag = true;
                                continue;
                            }
                            newDiceArray[ind] = midnightState.getLiveDice()[i];
                            ind++;
                        }
                        if (midnightState.hasLowQualifier()) {
                            keyHead = "HL|";
                        } else {
                            keyHead = "H_|";
                        }
                    } else {
                        boolean lowQualifierFlag = false;
                        for (int i = 0, ind = 0; i < midnightState.getNumLiveDice(); i++) {
                            if (!lowQualifierFlag && midnightState.getLiveDice()[i] == LOW_QUALIFIER) {
                                lowQualifierFlag = true;
                                continue;
                            }
                            newDiceArray[ind] = midnightState.getLiveDice()[i];
                            ind++;
                        }
                        if (midnightState.hasHighQualifier()) {
                            keyHead = "HL|";
                        } else {
                            keyHead = "_L|";
                        }
                    }
                } else {    //qual == 0
                    newDiceArray = new int[midnightState.getNumLiveDice()];
                    for (int i = 0; i < midnightState.getNumLiveDice(); i++) {
                        newDiceArray[i] = midnightState.getLiveDice()[i];
                    }
                    if (midnightState.hasHighQualifier()) {
                        if (midnightState.hasLowQualifier()) {
                            keyHead = "HL|";
                        } else {
                            keyHead = "H_|";
                        }
                    } else {
                        if (midnightState.hasLowQualifier()) {
                            keyHead = "_L|";
                        } else {
                            keyHead = "__|";
                        }
                    }
                }
                Arrays.sort(newDiceArray);
                int newPointsBanked = midnightState.getPointsBanked();
                for (int i = 0; i < high; i++) {
                    newPointsBanked += newDiceArray[newDiceArray.length - 1 - i];   //hate that Arrays has no rev()
                }
                String points = newPointsBanked < 10 ? "0" + newPointsBanked : "" + newPointsBanked;
                keyHead = points + keyHead;

                long contendingSuccessDenom = 0L;
                long contendingSuccessNum = 0L;
                double contendingEquityGivenSuccess = 0.0; //keep this as a sum and only /= at the end
                double contendingEquityGivenFailure = 0.0; //keep this as a sum and only /= at the end
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    if (newNumLiveDice >= 2) {
                        for (int dice2 = 1; dice2 <= 6; dice2++) {
                            if (newNumLiveDice >= 3) {
                                for (int dice3 = 1; dice3 <= 6; dice3++) {
                                    if (newNumLiveDice >= 4) {
                                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                                            if (newNumLiveDice >= 5) {
                                                for (int dice5 = 1; dice5 <= 6; dice5++) {
                                                    if (newNumLiveDice >= 6) {
                                                        throw new AssertionError("newNumLiveDice: " + newNumLiveDice);
                                                    } else {
                                                        String key = keyHead + dice1 + dice2 + dice3 + dice4 + dice5;
                                                        MidnightState newMidnightState =
                                                                mapsFor5LiveDice.get(midnightState.getCondition()).get(key);
                                                        contendingSuccessDenom += midnightState.getSuccessDenom() / 7776L;
                                                        contendingSuccessNum += newMidnightState.getSuccessNum() * (midnightState.getSuccessDenom() / 470184984576L);
                                                        contendingEquityGivenSuccess += newMidnightState.getSuccessNum() *
                                                                newMidnightState.getEquityGivenSuccess() * midnightState.getSuccessDenom() / 470184984576L;
                                                        contendingEquityGivenFailure +=
                                                                (newMidnightState.getSuccessDenom() - newMidnightState.getSuccessNum()) *
                                                                        newMidnightState.getEquityGivenFailure() * midnightState.getSuccessDenom() / 470184984576L;
                                                    }
                                                }
                                            } else {
                                                String key = keyHead + dice1 + dice2 + dice3 + dice4;
                                                MidnightState newMidnightState =
                                                        mapsFor4LiveDice.get(midnightState.getCondition()).get(key);
                                                contendingSuccessDenom += midnightState.getSuccessDenom() / 1296L;
                                                contendingSuccessNum += newMidnightState.getSuccessNum() * (midnightState.getSuccessDenom() / 60466176L);
                                                contendingEquityGivenSuccess += newMidnightState.getSuccessNum() *
                                                        newMidnightState.getEquityGivenSuccess() * midnightState.getSuccessDenom() / 60466176L;
                                                contendingEquityGivenFailure +=
                                                        (newMidnightState.getSuccessDenom() - newMidnightState.getSuccessNum()) *
                                                                newMidnightState.getEquityGivenFailure() * midnightState.getSuccessDenom() / 60466176L;
                                            }
                                        }
                                    } else {
                                        String key = keyHead + dice1 + dice2 + dice3;
                                        MidnightState newMidnightState =
                                                mapsFor3LiveDice.get(midnightState.getCondition()).get(key);
                                        contendingSuccessDenom += midnightState.getSuccessDenom() / 216L;
                                        contendingSuccessNum += newMidnightState.getSuccessNum() * (midnightState.getSuccessDenom() / 46656L);
                                        contendingEquityGivenSuccess += newMidnightState.getSuccessNum() *
                                                newMidnightState.getEquityGivenSuccess() * midnightState.getSuccessDenom() / 46656L;
                                        contendingEquityGivenFailure +=
                                                (newMidnightState.getSuccessDenom() - newMidnightState.getSuccessNum()) *
                                                        newMidnightState.getEquityGivenFailure() * midnightState.getSuccessDenom() / 46656L;
                                    }
                                }
                            } else {
                                String key = keyHead + dice1 + dice2;
                                MidnightState newMidnightState =
                                        mapsFor2LiveDice.get(midnightState.getCondition()).get(key);
                                contendingSuccessDenom += midnightState.getSuccessDenom() / 36L;
                                contendingSuccessNum += newMidnightState.getSuccessNum() * (midnightState.getSuccessDenom() / 216L);
                                contendingEquityGivenSuccess += newMidnightState.getSuccessNum() *
                                        newMidnightState.getEquityGivenSuccess() * midnightState.getSuccessDenom() / 216L;
                                contendingEquityGivenFailure +=
                                        (newMidnightState.getSuccessDenom() - newMidnightState.getSuccessNum()) *
                                                newMidnightState.getEquityGivenFailure() * midnightState.getSuccessDenom() / 216L;
                            }
                        }
                    } else {
                        String key = keyHead + dice1;
                        MidnightState newMidnightState =
                                mapsFor1LiveDice.get(midnightState.getCondition()).get(key);
                        contendingSuccessDenom += midnightState.getSuccessDenom() / 6L;
                        contendingSuccessNum += newMidnightState.getSuccessNum() * (midnightState.getSuccessDenom() / 6L);
                        contendingEquityGivenSuccess += newMidnightState.getSuccessNum() *
                                newMidnightState.getEquityGivenSuccess() * midnightState.getSuccessDenom() / 6L;
                        contendingEquityGivenFailure +=
                                (newMidnightState.getSuccessDenom() - newMidnightState.getSuccessNum()) *
                                        newMidnightState.getEquityGivenFailure() * midnightState.getSuccessDenom() / 6L;
                    }
                }

                if (contendingSuccessNum == 0) {
                    contendingEquityGivenSuccess = -1.0;
                } else {
                    contendingEquityGivenSuccess /= contendingSuccessNum;
                }
                if (contendingSuccessNum == contendingSuccessDenom) {
                    contendingEquityGivenFailure = -1.0;
                } else {
                    contendingEquityGivenFailure /= contendingSuccessDenom - contendingSuccessNum;
                }
                if (contendingSuccessDenom != successDenom) {
                    throw new AssertionError("contendingSuccessDenom: " + contendingSuccessDenom +
                            ", but successDenom: " + successDenom);
                }

                boolean updateFlag = false;
                if (mergeSuccessesAndEquity) {
                    double mergedEquity = equityGivenSuccess * successNum / successDenom;
                    double contendingMergedEquity = contendingEquityGivenSuccess *
                            contendingSuccessNum / contendingSuccessDenom;
                    if (contendingMergedEquity > mergedEquity) {
                        updateFlag = true;
                    } else if (contendingMergedEquity == mergedEquity) {
                        if (contendingEquityGivenFailure > equityGivenFailure) {
                            updateFlag = true;
                        }
                        //if everything is equal, don't update which means prefer the solution that keeps more dice
                    }
                } else {
                    if (contendingSuccessNum > successNum) {
                        updateFlag = true;
                    } else if (contendingSuccessNum == successNum) {
                        if (contendingEquityGivenSuccess > equityGivenSuccess) {
                            updateFlag = true;
                        } else if (contendingEquityGivenSuccess == equityGivenSuccess) {
                            if (contendingEquityGivenFailure > equityGivenFailure) {
                                updateFlag = true;
                            }
                            //if everything is equal, don't update which means prefer the solution that keeps more dice
                        }
                    }
                }
                if (updateFlag) {
                    optimalPolicyQual = qual;
                    optimalPolicyHigh = high;
                    successNum = contendingSuccessNum;
                    equityGivenSuccess = contendingEquityGivenSuccess;
                    equityGivenFailure = contendingEquityGivenFailure;
                }
            }
        }

        midnightState.setOptimalPolicyQual(optimalPolicyQual);
        midnightState.setOptimalPolicyHigh(optimalPolicyHigh);
        midnightState.setSuccessNum(successNum);
        midnightState.setEquityGivenSuccess(equityGivenSuccess);
        midnightState.setEquityGivenFailure(equityGivenFailure);
    }

    private void printResults() {
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }

            for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }

            for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }

            for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }

            for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }

            for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
                if (ABRIDGED_RESULTS) {
                    System.out.println(midnightState.toStringAbridged());
                } else {
                    System.out.println(midnightState);
                }
            }
        }
    }

    private void printResults(int condition) {
        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
    }

    private void printResults(int condition, int maxLiveDice) {
        if (condition < 0 || condition >= CAPACITY) {
            throw new IllegalArgumentException("condition: " + condition);
        }
        if (maxLiveDice <= 0 || maxLiveDice > 6) {
            throw new IllegalArgumentException("maxLiveDice: " + maxLiveDice);
        }

        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
        if (maxLiveDice == 1) {
            return;
        }

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
        if (maxLiveDice == 2) {
            return;
        }

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
        if (maxLiveDice == 3) {
            return;
        }

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
        if (maxLiveDice == 4) {
            return;
        }

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
        if (maxLiveDice == 5) {
            return;
        }

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            if (ABRIDGED_RESULTS) {
                System.out.println(midnightState.toStringAbridged());
            } else {
                System.out.println(midnightState);
            }
        }
    }

    private void writeToFile() {
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            String modifiedPathway = condition < 10 ? PATHWAY + "0" : PATHWAY;
            try (PrintWriter pw = new PrintWriter(modifiedPathway + condition + ".txt")) {
                //in this case you have no qualifiers and 1 dice left, so you lost for sure
                for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "__|" + dice1;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor1LiveDice.get(condition).get(key));
                        }
                    }
                }

                for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "_L|" + dice1;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor1LiveDice.get(condition).get(key));
                        }
                    }
                }

                for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "H_|" + dice1;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor1LiveDice.get(condition).get(key));
                        }
                    }
                }

                for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "HL|" + dice1;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor1LiveDice.get(condition).get(key));
                        }
                    }
                }

                for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "__|" + dice1 + dice2;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor2LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }

                for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "_L|" + dice1 + dice2;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor2LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }

                for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "H_|" + dice1 + dice2;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor2LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }

                for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "HL|" + dice1 + dice2;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor2LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }

                for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "__|" + dice1 + dice2 + dice3;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "_L|" + dice1 + dice2 + dice3;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "H_|" + dice1 + dice2 + dice3;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                String points = "0" + pointsBanked;
                                String key = points + "HL|" + dice1 + dice2 + dice3;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor3LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                    String key = points + "__|" + dice1 + dice2 + dice3 + dice4;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                    String points = "0" + pointsBanked;
                                    String key = points + "_L|" + dice1 + dice2 + dice3 + dice4;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                    String points = "0" + pointsBanked;
                                    String key = points + "H_|" + dice1 + dice2 + dice3 + dice4;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor4LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String key = "00HL|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }

                for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                    for (int dice1 = 1; dice1 <= 6; dice1++) {
                        for (int dice2 = dice1; dice2 <= 6; dice2++) {
                            for (int dice3 = dice2; dice3 <= 6; dice3++) {
                                for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                    for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                        String points = "0" + pointsBanked;
                                        String key = points + "__|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                        if (ABRIDGED_RESULTS) {
                                            pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                        } else {
                                            pw.println(mapsFor5LiveDice.get(condition).get(key));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                    String key = "00_L|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                    String key = "00H_|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                    for (int dice6 = dice5; dice6 <= 6; dice6++) {
                                        String key = "00__|" + dice1 + dice2 + dice3 + dice4 + dice5 + dice6;
                                        if (ABRIDGED_RESULTS) {
                                            pw.println(mapsFor6LiveDice.get(condition).get(key).toStringAbridged());
                                        } else {
                                            pw.println(mapsFor6LiveDice.get(condition).get(key));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + modifiedPathway + condition + ".txt");
                System.exit(1);
            }
        }
    }

    private void writeToFile(int condition) {
        if (condition < 0 || condition >= CAPACITY) {
            throw new IllegalArgumentException("condition: " + condition);
        }

        String modifiedPathway = condition < 10 ? PATHWAY + "0" : PATHWAY;
        try (PrintWriter pw = new PrintWriter(modifiedPathway + condition + ".txt")) {
            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "__|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "_L|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "H_|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "HL|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "__|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "_L|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "H_|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "HL|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "__|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "_L|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "H_|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = "0" + pointsBanked;
                            String key = points + "HL|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "__|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "_L|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "H_|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            String key = "00HL|" + dice1 + dice2 + dice3 + dice4;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor4LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                    String points = "0" + pointsBanked;
                                    String key = points + "__|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                String key = "00_L|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                String key = "00H_|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                for (int dice6 = dice5; dice6 <= 6; dice6++) {
                                    String key = "00__|" + dice1 + dice2 + dice3 + dice4 + dice5 + dice6;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor6LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor6LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + modifiedPathway + condition + ".txt");
            System.exit(1);
        }
    }
    private void writeToFile(int condition, int maxLiveDice) {
        if (condition < 0 || condition >= CAPACITY) {
            throw new IllegalArgumentException("condition: " + condition);
        }
        if (maxLiveDice <= 0 || maxLiveDice > 6) {
            throw new IllegalArgumentException("maxLiveDice: " + maxLiveDice);
        }

        String modifiedPathway = condition < 10 ? PATHWAY + "0" : PATHWAY;
        try (PrintWriter pw = new PrintWriter(modifiedPathway + condition + ".txt")) {
            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "__|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "_L|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "H_|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                    String key = points + "HL|" + dice1;
                    if (ABRIDGED_RESULTS) {
                        pw.println(mapsFor1LiveDice.get(condition).get(key).toStringAbridged());
                    } else {
                        pw.println(mapsFor1LiveDice.get(condition).get(key));
                    }
                }
            }

            if (maxLiveDice == 1) {
                return;
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "__|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "_L|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "H_|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                        String key = points + "HL|" + dice1 + dice2;
                        if (ABRIDGED_RESULTS) {
                            pw.println(mapsFor2LiveDice.get(condition).get(key).toStringAbridged());
                        } else {
                            pw.println(mapsFor2LiveDice.get(condition).get(key));
                        }
                    }
                }
            }

            if (maxLiveDice == 2) {
                return;
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "__|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "_L|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                            String key = points + "H_|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            String points = "0" + pointsBanked;
                            String key = points + "HL|" + dice1 + dice2 + dice3;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor3LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor3LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            if (maxLiveDice == 3) {
                return;
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                                String key = points + "__|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "_L|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                String points = "0" + pointsBanked;
                                String key = points + "H_|" + dice1 + dice2 + dice3 + dice4;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor4LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            String key = "00HL|" + dice1 + dice2 + dice3 + dice4;
                            if (ABRIDGED_RESULTS) {
                                pw.println(mapsFor4LiveDice.get(condition).get(key).toStringAbridged());
                            } else {
                                pw.println(mapsFor4LiveDice.get(condition).get(key));
                            }
                        }
                    }
                }
            }

            if (maxLiveDice == 4) {
                return;
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = dice1; dice2 <= 6; dice2++) {
                        for (int dice3 = dice2; dice3 <= 6; dice3++) {
                            for (int dice4 = dice3; dice4 <= 6; dice4++) {
                                for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                    String points = "0" + pointsBanked;
                                    String key = points + "__|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor5LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                String key = "00_L|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                String key = "00H_|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                if (ABRIDGED_RESULTS) {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key).toStringAbridged());
                                } else {
                                    pw.println(mapsFor5LiveDice.get(condition).get(key));
                                }
                            }
                        }
                    }
                }
            }

            if (maxLiveDice == 5) {
                return;
            }

            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = dice1; dice2 <= 6; dice2++) {
                    for (int dice3 = dice2; dice3 <= 6; dice3++) {
                        for (int dice4 = dice3; dice4 <= 6; dice4++) {
                            for (int dice5 = dice4; dice5 <= 6; dice5++) {
                                for (int dice6 = dice5; dice6 <= 6; dice6++) {
                                    String key = "00__|" + dice1 + dice2 + dice3 + dice4 + dice5 + dice6;
                                    if (ABRIDGED_RESULTS) {
                                        pw.println(mapsFor6LiveDice.get(condition).get(key).toStringAbridged());
                                    } else {
                                        pw.println(mapsFor6LiveDice.get(condition).get(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + modifiedPathway + condition + ".txt");
            System.exit(1);
        }
    }

    //don't care about unreachable states, since they could be reachable under different qualifiers (such as 2-4-24)
    private void populateDistributions() {
        //1 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                distributionsFor1LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                distributionsFor1LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                distributionsFor1LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                distributionsFor1LiveDice.get(condition).put(distKey, new long[25]);
            }
        }

        //2 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                distributionsFor2LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                distributionsFor2LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                distributionsFor2LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                distributionsFor2LiveDice.get(condition).put(distKey, new long[25]);
            }
        }

        //3 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                distributionsFor3LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                distributionsFor3LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                distributionsFor3LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "HL";
                distributionsFor3LiveDice.get(condition).put(distKey, new long[25]);
            }
        }

        //4 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                distributionsFor4LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "_L";
                distributionsFor4LiveDice.get(condition).put(distKey, new long[25]);
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "H_";
                distributionsFor4LiveDice.get(condition).put(distKey, new long[25]);
            }

            distributionsFor4LiveDice.get(condition).put("00HL", new long[25]);
        }

        //5 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "__";
                distributionsFor5LiveDice.get(condition).put(distKey, new long[25]);
            }

            distributionsFor5LiveDice.get(condition).put("00_L", new long[25]);

            distributionsFor5LiveDice.get(condition).put("00H_", new long[25]);
        }

        //6 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            
            distributionsFor6LiveDice.get(condition).put("00__", new long[25]);
        }
    }

    private void calculateDistributions() {
        //1 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            //in this case you have no qualifiers and 1 dice left, so you lost for sure
            for (int pointsBanked = 5; pointsBanked <= 30; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String key = distKey + "|" + dice1;
                    MidnightState midnightState = mapsFor1LiveDice.get(condition).get(key);
                    int score = midnightState.calculateScoreIfKeptAllDice();
                    distributionsFor1LiveDice.get(condition).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String key = distKey + "|" + dice1;
                    MidnightState midnightState = mapsFor1LiveDice.get(condition).get(key);
                    int score = midnightState.calculateScoreIfKeptAllDice();
                    distributionsFor1LiveDice.get(condition).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String key = distKey + "|" + dice1;
                    MidnightState midnightState = mapsFor1LiveDice.get(condition).get(key);
                    int score = midnightState.calculateScoreIfKeptAllDice();
                    distributionsFor1LiveDice.get(condition).get(distKey)[score] += 1L;
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    String key = distKey + "|" + dice1;
                    MidnightState midnightState = mapsFor1LiveDice.get(condition).get(key);
                    int score = midnightState.calculateScoreIfKeptAllDice();
                    distributionsFor1LiveDice.get(condition).get(distKey)[score] += 1L;
                }
            }
        }

        //2 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 4; pointsBanked <= 24; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String key = distKey + "|" + dice1 + dice2;
                        MidnightState midnightState = mapsFor2LiveDice.get(condition).get(key);
                        if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 2) { // == 2
                            int score = midnightState.calculateScoreIfKeptAllDice();
                            distributionsFor2LiveDice.get(condition).get(distKey)[score] += 6L;
                        } else { // == 1
                            String lowerDistKey = deriveLowerDistKey(key, condition);
                            long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                distributionsFor2LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String key = distKey + "|" + dice1 + dice2;
                        MidnightState midnightState = mapsFor2LiveDice.get(condition).get(key);
                        if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 2) { // == 2
                            int score = midnightState.calculateScoreIfKeptAllDice();
                            distributionsFor2LiveDice.get(condition).get(distKey)[score] += 6L;
                        } else { // == 1
                            String lowerDistKey = deriveLowerDistKey(key, condition);
                            long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                distributionsFor2LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String key = distKey + "|" + dice1 + dice2;
                        MidnightState midnightState = mapsFor2LiveDice.get(condition).get(key);
                        if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 2) { // == 2
                            int score = midnightState.calculateScoreIfKeptAllDice();
                            distributionsFor2LiveDice.get(condition).get(distKey)[score] += 6L;
                        } else { // == 1
                            String lowerDistKey = deriveLowerDistKey(key, condition);
                            long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                distributionsFor2LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "HL";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        String key = distKey + "|" + dice1 + dice2;
                        MidnightState midnightState = mapsFor2LiveDice.get(condition).get(key);
                        if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 2) { // == 2
                            int score = midnightState.calculateScoreIfKeptAllDice();
                            distributionsFor2LiveDice.get(condition).get(distKey)[score] += 6L;
                        } else { // == 1
                            String lowerDistKey = deriveLowerDistKey(key, condition);
                            long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                            for (int i = 0; i < lowerDist.length; i++) {
                                distributionsFor2LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                            }
                        }
                    }
                }
            }
        }

        //3 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 3; pointsBanked <= 18; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String key = distKey + "|" + dice1 + dice2 + dice3;
                            MidnightState midnightState = mapsFor3LiveDice.get(condition).get(key);
                            if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 3) { // == 3
                                int score = midnightState.calculateScoreIfKeptAllDice();
                                distributionsFor3LiveDice.get(condition).get(distKey)[score] += 216L;
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "_L";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String key = distKey + "|" + dice1 + dice2 + dice3;
                            MidnightState midnightState = mapsFor3LiveDice.get(condition).get(key);
                            if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 3) { // == 3
                                int score = midnightState.calculateScoreIfKeptAllDice();
                                distributionsFor3LiveDice.get(condition).get(distKey)[score] += 216L;
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "H_";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String key = distKey + "|" + dice1 + dice2 + dice3;
                            MidnightState midnightState = mapsFor3LiveDice.get(condition).get(key);
                            if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 3) { // == 3
                                int score = midnightState.calculateScoreIfKeptAllDice();
                                distributionsFor3LiveDice.get(condition).get(distKey)[score] += 216L;
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "HL";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            String key = distKey + "|" + dice1 + dice2 + dice3;
                            MidnightState midnightState = mapsFor3LiveDice.get(condition).get(key);
                            if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 3) { // == 3
                                int score = midnightState.calculateScoreIfKeptAllDice();
                                distributionsFor3LiveDice.get(condition).get(distKey)[score] += 216L;
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += 36L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor3LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //4 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 2; pointsBanked <= 12; pointsBanked++) {
                String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
                String distKey = points + "__";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String key = distKey + "|" + dice1 + dice2 + dice3 + dice4;
                                MidnightState midnightState = mapsFor4LiveDice.get(condition).get(key);
                                if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 4) { // == 4
                                    int score = midnightState.calculateScoreIfKeptAllDice();
                                    distributionsFor4LiveDice.get(condition).get(distKey)[score] += 46656L;
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else { // == 1
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "_L";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String key = distKey + "|" + dice1 + dice2 + dice3 + dice4;
                                MidnightState midnightState = mapsFor4LiveDice.get(condition).get(key);
                                if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 4) { // == 4
                                    int score = midnightState.calculateScoreIfKeptAllDice();
                                    distributionsFor4LiveDice.get(condition).get(distKey)[score] += 46656L;
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else { // == 1
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "H_";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                String key = distKey + "|" + dice1 + dice2 + dice3 + dice4;
                                MidnightState midnightState = mapsFor4LiveDice.get(condition).get(key);
                                if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 4) { // == 4
                                    int score = midnightState.calculateScoreIfKeptAllDice();
                                    distributionsFor4LiveDice.get(condition).get(distKey)[score] += 46656L;
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 7776L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += 216L * lowerDist[i];
                                    }
                                } else { // == 1
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor4LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00HL";
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            String key = distKey + "|" + dice1 + dice2 + dice3 + dice4;
                            MidnightState midnightState = mapsFor4LiveDice.get(condition).get(key);
                            if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 4) { // == 4
                                int score = midnightState.calculateScoreIfKeptAllDice();
                                distributionsFor4LiveDice.get(condition).get(distKey)[score] += 46656L;
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor4LiveDice.get(condition).get(distKey)[i] += 7776L * lowerDist[i];
                                }
                            } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor4LiveDice.get(condition).get(distKey)[i] += 216L * lowerDist[i];
                                }
                            } else { // == 1
                                String lowerDistKey = deriveLowerDistKey(key, condition);
                                long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                for (int i = 0; i < lowerDist.length; i++) {
                                    distributionsFor4LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                }
                            }
                        }
                    }
                }
            }
        }

        //5 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            for (int pointsBanked = 1; pointsBanked <= 6; pointsBanked++) {
                String distKey = "0" + pointsBanked + "__";
                for (int dice1 = 1; dice1 <= 6; dice1++) {
                    for (int dice2 = 1; dice2 <= 6; dice2++) {
                        for (int dice3 = 1; dice3 <= 6; dice3++) {
                            for (int dice4 = 1; dice4 <= 6; dice4++) {
                                for (int dice5 = 1; dice5 <= 6; dice5++) {
                                    String key = distKey + "|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                    MidnightState midnightState = mapsFor5LiveDice.get(condition).get(key);
                                    if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 5) { // == 5
                                        int score = midnightState.calculateScoreIfKeptAllDice();
                                        distributionsFor5LiveDice.get(condition).get(distKey)[score] += 60466176L;
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 4) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor5LiveDice.get(condition).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor5LiveDice.get(condition).get(distKey)[i] += 279936L * lowerDist[i];
                                        }
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor5LiveDice.get(condition).get(distKey)[i] += 1296L * lowerDist[i];
                                        }
                                    } else { // == 1
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor4LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor5LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String distKey = "00_L";
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                String key = distKey + "|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                MidnightState midnightState = mapsFor5LiveDice.get(condition).get(key);
                                if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 5) { // == 5
                                    int score = midnightState.calculateScoreIfKeptAllDice();
                                    distributionsFor5LiveDice.get(condition).get(distKey)[score] += 60466176L;
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 4) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else { // == 1
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor4LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            distKey = "00H_";
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                String key = distKey + "|" + dice1 + dice2 + dice3 + dice4 + dice5;
                                MidnightState midnightState = mapsFor5LiveDice.get(condition).get(key);
                                if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 5) { // == 5
                                    int score = midnightState.calculateScoreIfKeptAllDice();
                                    distributionsFor5LiveDice.get(condition).get(distKey)[score] += 60466176L;
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 4) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 10077696L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 279936L * lowerDist[i];
                                    }
                                } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += 1296L * lowerDist[i];
                                    }
                                } else { // == 1
                                    String lowerDistKey = deriveLowerDistKey(key, condition);
                                    long[] lowerDist = distributionsFor4LiveDice.get(condition).get(lowerDistKey);
                                    for (int i = 0; i < lowerDist.length; i++) {
                                        distributionsFor5LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //6 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            

            String distKey = "00__";
            for (int dice1 = 1; dice1 <= 6; dice1++) {
                for (int dice2 = 1; dice2 <= 6; dice2++) {
                    for (int dice3 = 1; dice3 <= 6; dice3++) {
                        for (int dice4 = 1; dice4 <= 6; dice4++) {
                            for (int dice5 = 1; dice5 <= 6; dice5++) {
                                for (int dice6 = 1; dice6 <= 6; dice6++) {
                                    String key = distKey + "|" + dice1 + dice2 + dice3 + dice4 + dice5 + dice6;
                                    MidnightState midnightState = mapsFor6LiveDice.get(condition).get(key);
                                    if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= 6) { // == 6
                                        int score = midnightState.calculateScoreIfKeptAllDice();
                                        distributionsFor6LiveDice.get(condition).get(distKey)[score] += 470184984576L;
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 5) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor1LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor6LiveDice.get(condition).get(distKey)[i] += 78364164096L * lowerDist[i];
                                        }
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 4) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor2LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor6LiveDice.get(condition).get(distKey)[i] += 2176782336L * lowerDist[i];
                                        }
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 3) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor3LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor6LiveDice.get(condition).get(distKey)[i] += 10077696L * lowerDist[i];
                                        }
                                    } else if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() == 2) {
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor4LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor6LiveDice.get(condition).get(distKey)[i] += 7776L * lowerDist[i];
                                        }
                                    } else { // == 1
                                        String lowerDistKey = deriveLowerDistKey(key, condition);
                                        long[] lowerDist = distributionsFor5LiveDice.get(condition).get(lowerDistKey);
                                        for (int i = 0; i < lowerDist.length; i++) {
                                            distributionsFor6LiveDice.get(condition).get(distKey)[i] += lowerDist[i];
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

    private String deriveLowerDistKey(String key, int condition) {
        //return null if all live dice get kept, which is always the case if 1 live dice left
        //if so then it shouldn't have (needed to) called this in the first place
        //ALSO, there's a slight blemish in not being able to distinguish e.g. 12HL with 1 live dice and 2 live dice
        //when it comes to handling originally 3 or more live dice, so the calling method must handle that

        int numLiveDice = key.length() - 5; //00HL|000000
        MidnightState midnightState =
        switch (numLiveDice) {
            case 1 -> mapsFor1LiveDice.get(condition).get(key);
            case 2 -> mapsFor2LiveDice.get(condition).get(key);
            case 3 -> mapsFor3LiveDice.get(condition).get(key);
            case 4 -> mapsFor4LiveDice.get(condition).get(key);
            case 5 -> mapsFor5LiveDice.get(condition).get(key);
            case 6 -> mapsFor6LiveDice.get(condition).get(key);
            default -> throw new IllegalArgumentException("Invalid oldKey: " + key);
        };
        if (midnightState.getOptimalPolicyQual() + midnightState.getOptimalPolicyHigh() >= numLiveDice) { // == num...
            return null;
        }

        int[] newDiceArray = new int[midnightState.getNumLiveDice() - midnightState.getOptimalPolicyQual()];
        boolean keepLowQualifier = false;
        boolean keepHighQualifier = false;
        if (midnightState.getOptimalPolicyQual() == 2) {
            keepLowQualifier = true;
            keepHighQualifier = true;
        } else if (midnightState.getOptimalPolicyQual() == 1) {
            if (midnightState.canKeepHighQualifier()) {
                keepHighQualifier = true;
            } else {
                keepLowQualifier = true;
            }
        } // else qual == 0 and no action needed
        boolean lowQualifierFlag = false;
        boolean highQualifierFlag = false;
        int ind = 0;
        for (int dice : midnightState.getLiveDice()) {
            if (dice == LOW_QUALIFIER && keepLowQualifier && !lowQualifierFlag) {
                lowQualifierFlag = true;
            } else if (dice == HIGH_QUALIFIER && keepHighQualifier && !highQualifierFlag) {
                highQualifierFlag = true;
            } else {
                newDiceArray[ind] = dice;
                ind++;
            }
        }
        Arrays.sort(newDiceArray);
        int newPointsBanked = midnightState.getPointsBanked();
        for (int i = 0; i < midnightState.getOptimalPolicyHigh(); i++) {
            newPointsBanked += newDiceArray[newDiceArray.length - 1 - i];   //hate that Arrays has no rev()
        }
        String points = newPointsBanked < 10 ? "0" + newPointsBanked : "" + newPointsBanked;
        String H = midnightState.hasHighQualifier() || keepHighQualifier ? "H" : "_";
        String L = midnightState.hasLowQualifier() || keepLowQualifier ? "L" : "_";

        return points + H + L;
    }
}
