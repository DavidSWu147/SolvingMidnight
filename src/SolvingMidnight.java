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
    public static final double[] EQUITIES = {0.0,
            1.0-0.95672706359889976662, 1.0-0.95672706359889976662, 1.0-0.95672706359889976662,
            1.0-0.95672706358836411817, 1.0-0.95672706297209642347, 1.0-0.95672704552161571283,
            1.0-0.95672678335709949177, 1.0-0.95672446724621963774, 1.0-0.95670993238007934962,
            1.0-0.95663444259125802254, 1.0-0.95631638082948018693, 1.0-0.95520070366519759297,
            1.0-0.95182675145489243582, 1.0-0.94299012541309841426, 1.0-0.92268563306789560691,
            1.0-0.88397221121369336300, 1.0-0.82337700330052564038, 1.0-0.73781575661001199616,
            1.0-0.62452394435622684523, 1.0-0.48529044623072225198, 1.0-0.34240811630351624107,
            1.0-0.21713087590733940749, 1.0-0.09998965917946808615, 1.0};

    ArrayList<HashMap<String, MidnightState>> mapsFor1LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor2LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor3LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor4LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor5LiveDice;
    ArrayList<HashMap<String, MidnightState>> mapsFor6LiveDice;
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
    }

    public static void main(String[] args) {
        SolvingMidnight solvingMidnight = new SolvingMidnight();
        solvingMidnight.run();
    }

    public void run() {
        populateWithStates();

        calculateInitialRunThrough();

        calculateAllStateEquitiesRecursively();

        writeToFile();

        for (int condition = 0; condition <= 25; condition++) {
            if (condition == 1 || condition == 2) {
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
                System.out.println("Condition: 0" + condition);
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
    }

    //don't care too much about unreachable states for now
    private void populateWithStates() {
        //1 dice
        for (int condition = 0; condition < CAPACITY; condition++) {
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
        for (int condition = 0; condition <= CAPACITY; condition++) {
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
        for (int condition = 0; condition <= CAPACITY; condition++) {
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }
            System.out.println(condition);

            for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("1 done");

            for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("2 done");

            for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("3 done");

            for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("4 done");

            for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("5 done");

            for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
                solveStateEquityRecursively(midnightState);
            }
            System.out.println("6 done");
        }
    }

    private void calculateAllStateEquitiesRecursively(int condition) {
        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("1 done");

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("2 done");

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("3 done");

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("4 done");

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("5 done");

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("6 done");
    }

    private void calculateAllStateEquitiesRecursively(int condition, int maxLiveDice) {
        if (maxLiveDice <= 0 || maxLiveDice > 6) {
            throw new IllegalArgumentException("maxLiveDice: " + maxLiveDice);
        }

        for (MidnightState midnightState : mapsFor1LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("1 done");
        if (maxLiveDice == 1) {
            return;
        }

        for (MidnightState midnightState : mapsFor2LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("2 done");
        if (maxLiveDice == 2) {
            return;
        }

        for (MidnightState midnightState : mapsFor3LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("3 done");
        if (maxLiveDice == 3) {
            return;
        }

        for (MidnightState midnightState : mapsFor4LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("4 done");
        if (maxLiveDice == 4) {
            return;
        }

        for (MidnightState midnightState : mapsFor5LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("5 done");
        if (maxLiveDice == 5) {
            return;
        }

        for (MidnightState midnightState : mapsFor6LiveDice.get(condition).values()) {
            solveStateEquityRecursively(midnightState);
        }
        System.out.println("6 done");
    }

    //Assumes calculateEquityIfKeptAllDice() has been run for the current state, and
    //solveStateEquityRecursively() has been run for the branching states one level simpler
    private void solveStateEquityRecursively(MidnightState midnightState) {
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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
            }

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
            if (condition == 1 || condition == 2 || condition > 25) {
                continue;
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
}
