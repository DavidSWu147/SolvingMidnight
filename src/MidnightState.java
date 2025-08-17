public class MidnightState {
    public static final int LOW_QUALIFIER = 1;
    public static final int HIGH_QUALIFIER = 4;
    public static final double[] EQUITIES = {0.0,
            1.0-0.95672706359889976662, 1.0-0.95672706359889976662, 1.0-0.95672706359889976662, //indices 1, 2, 3 same
            1.0-0.95672706358836411817, 1.0-0.95672706297209642347, 1.0-0.95672704552161571283,
            1.0-0.95672678335709949177, 1.0-0.95672446724621963774, 1.0-0.95670993238007934962,
            1.0-0.95663444259125802254, 1.0-0.95631638082948018693, 1.0-0.95520070366519759297,
            1.0-0.95182675145489243582, 1.0-0.94299012541309841426, 1.0-0.92268563306789560691,
            1.0-0.88397221121369336300, 1.0-0.82337700330052564038, 1.0-0.73781575661001199616,
            1.0-0.62452394435622684523, 1.0-0.48529044623072225198, 1.0-0.34240811630351624107,
            1.0-0.21713087590733940749, 1.0-0.09998965917946808615, 1.0};

    public static final double[] EQUITIES_3P = {0.0,
            (1.0-0.86268835220042000468) * (1.0-0.95672706359889976662),
            (1.0-0.86268835220042000468) * (1.0-0.95672706359889976662),
            (1.0-0.86268835220042000468) * (1.0-0.95672706359889976662), //indices 1, 2, 3 same
            (1.0-0.86268835071417975486) * (1.0-0.95672706358836411817),
            (1.0-0.86268831221763956365) * (1.0-0.95672706297209642347),
            (1.0-0.86268788831016379610) * (1.0-0.95672704552161571283), //6
            (1.0-0.86268506644510539280) * (1.0-0.95672678335709949177),
            (1.0-0.86267148962566865682) * (1.0-0.95672446724621963774),
            (1.0-0.86261689541029746468) * (1.0-0.95670993238007934962), //9
            (1.0-0.86237614746976460346) * (1.0-0.95663444259125802254),
            (1.0-0.86166449109411739562) * (1.0-0.95631638082948018693),
            (1.0-0.85982675140964671027) * (1.0-0.95520070366519759297), //12
            (1.0-0.85595255186261362680) * (1.0-0.95182675145489243582),
            (1.0-0.84737798717512180824) * (1.0-0.94299012541309841426),
            (1.0-0.83065111778411748713) * (1.0-0.92268563306789560691), //15
            (1.0-0.80229688332902217392) * (1.0-0.88397221121369336300),
            (1.0-0.75021579019446316548) * (1.0-0.82337700330052564038),
            (1.0-0.67817031905897643374) * (1.0-0.73781575661001199616), //18
            (1.0-0.61378025927909483806) * (1.0-0.62452394435622684523),
            (1.0-0.48373137478600613734) * (1.0-0.48529044623072225198),
            (1.0-0.34240577779594055386) * (1.0-0.34240811630351624107), //21
            (1.0-0.21701318861990429608) * (1.0-0.21713087590733940749),
            (1.0-0.09998965917946808615) * (1.0-0.09998965917946808615),
            1.0};

    //0 means highest average score, with failure to qualify counting as 0 (but everything considered "pass" here)
    //btw it seems that condition 0 is equivalent to condition 4
    //1 means highest average equity for P1 vs a most conservative P2 (condition 4+) but P1 loses ties
    //2 means highest average equity for P1 vs a most conservative P2 (condition 4+) and P1 wins ties
    //3 means highest average equity for P1 against an optimal P2
    //4~24 is an N+ condition
    //25 means highest average equity for P2 against optimal P3, assuming P1 has scored 0 (so P2 0 never ever wins)
    //The above also works for P1 against an optimal P2, but where P2 wins 0-0 ties
    //26~44 means highest average equity for P2 against optimal P3, assuming P1 has scored 22+N
    //(46 means P1 has scored 24 which cannot be beaten, and 45 means P1 has scored 23 which can only be beaten by 24,
    // so use the same strategy as the 24 condition)
    //45 means highest average equity for P1 against optimal P2 and P3
    private final int condition;
    private final int numLiveDice; //1~6

    private final int[] liveDice; //array of length 1~6
    private final boolean hasLowQualifier;
    private final boolean hasHighQualifier;
    private final int pointsBanked;

    private int optimalPolicyQual; //how many qualifier dice to keep (4 > 1)
    private int optimalPolicyHigh; //how many high dice to keep (6 > 5 > 4 > 3 > 2 > 1)
    private long successDenom; //this should always be 6^[[number of live dice - 1]th triangular number]
    private long successNum; //number of times the condition is fulfilled
    private double equityGivenSuccess; //average score (which can differ from Midnight score) given condition fulfilled
    private double equityGivenFailure; //average score given condition not fulfilled

    public MidnightState(int condition, int numLiveDice, int[] liveDice, boolean hasLowQualifier,
                         boolean hasHighQualifier, int pointsBanked) {
        this.condition = condition;
        this.numLiveDice = numLiveDice;
        this.liveDice = liveDice;
        this.hasLowQualifier = hasLowQualifier;
        this.hasHighQualifier = hasHighQualifier;
        this.pointsBanked = pointsBanked;
        this.optimalPolicyQual = -1;
        this.optimalPolicyHigh = -1;
        this.successDenom = -1L;
        this.successNum = -1L;
        this.equityGivenSuccess = -1.0;
        this.equityGivenFailure = -1.0;
    }

    public void calculateEquityIfKeptAllDice() {
        switch (condition) {
            case 1: case 2:
                throw new UnsupportedOperationException("Not yet: " + condition);
            case 0:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() == 0) {
                    successNum = 0;
                    equityGivenFailure = calculateScoreIfKeptAllDice(); //0
                } else {
                    successNum = successDenom;
                    equityGivenSuccess = calculateScoreIfKeptAllDice();
                }

                break;
            case 4: case 5: case 6: case 7: case 8: case 9: case 10:
            case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18:
            case 19: case 20: case 21: case 22: case 23: case 24:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() >= condition) {
                    successNum = successDenom;
                    equityGivenSuccess = calculateScoreIfKeptAllDice();
                } else {
                    successNum = 0;
                    equityGivenFailure = calculateScoreIfKeptAllDice();
                }

                break;
            case 3:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() == 0) {
                    successNum = 0;
                    equityGivenFailure = EQUITIES[3];
                } else {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                }

                break;
            case 25:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() == 0) {
                    successNum = 0;
                    equityGivenFailure = EQUITIES[calculateScoreIfKeptAllDice()]; //0.0
                } else {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                }

                break;
            case 26: case 27: case 28: case 29: case 30: case 31: case 32:
            case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40:
            case 41: case 42: case 43: case 44:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > condition - 22) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                } else {
                    successNum = 0;
                    equityGivenFailure = EQUITIES[calculateScoreIfKeptAllDice()];
                }

                break;
            case 45:
                optimalPolicyQual = 0;
                if (canKeepLowQualifier()) {
                    optimalPolicyQual++;
                }
                if (canKeepHighQualifier()) {
                    optimalPolicyQual++;
                }
                optimalPolicyHigh = numLiveDice - optimalPolicyQual;

                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() == 0) {
                    successNum = 0;
                    equityGivenFailure = EQUITIES_3P[3];
                } else {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_3P[calculateScoreIfKeptAllDice()];
                }

                break;
            default:
                throw new IllegalArgumentException("Invalid condition: " + condition);
        }
    }

    public int calculateScoreIfKeptAllDice() {
        if ((hasLowQualifier || canKeepLowQualifier()) && (hasHighQualifier || canKeepHighQualifier())) {
            int score = pointsBanked;
            for (int i = 0; i < numLiveDice; i++) {
                score += liveDice[i];
            }
            if (canKeepLowQualifier()) {
                score -= LOW_QUALIFIER;
            }
            if (canKeepHighQualifier()) {
                score -= HIGH_QUALIFIER;
            }
            return score;
        } else {
            return 0;
        }
    }

    public boolean canKeepLowQualifier() {
        if (!hasLowQualifier) {
            for (int i = 0; i < numLiveDice; i++) {
                if (liveDice[i] == LOW_QUALIFIER) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canKeepHighQualifier() {
        if (!hasHighQualifier) {
            for (int i = 0; i < numLiveDice; i++) {
                if (liveDice[i] == HIGH_QUALIFIER) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getCondition() {
        return condition;
    }

    public int getNumLiveDice() {
        return numLiveDice;
    }

    public int[] getLiveDice() {
        return liveDice;
    }

    public boolean hasLowQualifier() {
        return hasLowQualifier;
    }

    public boolean hasHighQualifier() {
        return hasHighQualifier;
    }

    public int getPointsBanked() {
        return pointsBanked;
    }

    public int getOptimalPolicyQual() {
        return optimalPolicyQual;
    }

    public void setOptimalPolicyQual(int optimalPolicyQual) {
        this.optimalPolicyQual = optimalPolicyQual;
    }

    public int getOptimalPolicyHigh() {
        return optimalPolicyHigh;
    }

    public void setOptimalPolicyHigh(int optimalPolicyHigh) {
        this.optimalPolicyHigh = optimalPolicyHigh;
    }

    public long getSuccessDenom() {
        return successDenom;
    }

    public void setSuccessDenom(long successDenom) {
        this.successDenom = successDenom;
    }

    public long getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(long successNum) {
        this.successNum = successNum;
    }

    public double getEquityGivenSuccess() {
        return equityGivenSuccess;
    }

    public void setEquityGivenSuccess(double equityGivenSuccess) {
        this.equityGivenSuccess = equityGivenSuccess;
    }

    public double getEquityGivenFailure() {
        return equityGivenFailure;
    }

    public void setEquityGivenFailure(double equityGivenFailure) {
        this.equityGivenFailure = equityGivenFailure;
    }

    public boolean equals(Object o) {
        if (o instanceof MidnightState) {
            MidnightState other = (MidnightState)(o);
            return condition == other.condition && getKey().equals(other.getKey());
        } else {
            return false;
        }
    }

    public String toString() {
        String string = condition < 10 ? "Condition: 0" : "Condition: ";
        string += condition + "\n";
        string += getKey() + "\n";
        string += "optimalPolicyQual: " + optimalPolicyQual + "\n";
        string += "optimalPolicyHigh: " + optimalPolicyHigh + "\n";
        string += "successDenom: " + successDenom + "\n";
        string += "successNum: " + successNum + "\n";
        string += "equityGivenSuccess: " + equityGivenSuccess + "\n";
        string += "equityGivenFailure: " + equityGivenFailure + "\n";
        return string;
    }

    public String toStringAbridged() {
        String string = condition < 10 ? "Cond: 0" : "Cond: ";
        string += condition + ", ";
        string += getKey() + "\n";
        string += "Qual: " + optimalPolicyQual + ", ";
        string += "High: " + optimalPolicyHigh + "\n";
        string += successNum + "/" + successDenom + "\n";
        string += "EqGS: " + equityGivenSuccess + "\n";
        string += "EqGF: " + equityGivenFailure + "\n";
        return string;
    }

    public String getKey() {
        String points = pointsBanked < 10 ? "0" + pointsBanked : "" + pointsBanked;
        String highQualifier = hasHighQualifier ? "H" : "_";
        String lowQualifier = hasLowQualifier ? "L" : "_";
        StringBuilder key = new StringBuilder(points + highQualifier + lowQualifier + "|");
        for (int i = 0; i < numLiveDice; i++) {
            key.append(liveDice[i]);
        }
        return key.toString();
    }
}
