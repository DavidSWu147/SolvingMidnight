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

    public static final double[] EQUITIES_SAFEST = {0.0, 0.0, 0.0, 0.043272936401100234, //index 3 is score of 0
            0.043272941642608725, 0.043273081510634650, 0.043274668006369706,
            0.043285507491790180, 0.043338179784174110, 0.043543638153175045,
            0.044229007314734500, 0.046183274290945400, 0.050934557873779400,
            0.060894050594582310, 0.079756974761451550, 0.111726436012404120,
            0.160827489528609120, 0.238157528628643760, 0.345789776046534700,
            0.475071789657585000, 0.623137730242535800, 0.775980839482896400,
            0.902561036549910300, 0.975332551046523800, 1.0};

    public static final double[] EQUITIES_NAIVE = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.043272936401100234, //index 3 is score of 0
            0.043272941733898396, 0.043273084756514040, 0.043274717430650530,
            0.043285928924708980, 0.043340768171511760, 0.043558559308860996,
            0.044309422856956040, 0.046534822024770786, 0.052164169541830105,
            0.064561539238436520, 0.089436759165168900, 0.133636443689504170,
            0.202169876222985960, 0.296551129395260700, 0.415205540008828000,
            0.556686106872727800, 0.687166745130514800, 0.799757945443496800,
            0.889305932514944400, 0.954813869254533800, 1.0};
    public static final double[] EQUITIES_GREEDY = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.14809936688132622, //index 3 is score of 0
            0.148099370486992620, 0.148099465571903570, 0.148100532734851850,
            0.148107790260042620, 0.148143195148847470, 0.148285123592654250,
            0.148783512738060180, 0.150289939754534700, 0.154161238545490440,
            0.162798424244780330, 0.180639018508011840, 0.213408623028379120,
            0.265740428643149700, 0.339330772989428100, 0.434689432002550400,
            0.558765692530095900, 0.676143480453409400, 0.780875329310785500,
            0.866444020786910500, 0.936180578838422700, 1.0};
    public static final double[] EQUITIES_NAIVE_ENHANCED = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.043272936401100234, //index 3 is score of 0
            0.043272936411635880, 0.043272937027903580, 0.0432729544783842860,
            0.043273216642900510, 0.043275595881903990, 0.0432908283016926300,
            0.043367341702273410, 0.043684535741841040, 0.0448107717138983840,
            0.048221782343967755, 0.057116280999636160, 0.0773521889988338000,
            0.117432909734998120, 0.186628468324122580, 0.2911136506084998000,
            0.428653071989725130, 0.585904336953864700, 0.7379386941750790000,
            0.865461328221997700, 0.954813869254533800, 1.0
    };
    public static final double[] EQUITIES_GREEDY_ENHANCED = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.14809936688132622, //index 3 is score of 0
            0.14809936688590880, 0.14809936716405045, 0.14809937573029028,
            0.14809951288441536, 0.14810082098394795, 0.14810963644394556,
            0.14815594538694798, 0.14835580713209404, 0.14908864673972260,
            0.15136947905848983, 0.15745210492992520, 0.17168423388255807,
            0.20077844933762257, 0.25249560074055450, 0.33334574772248170,
            0.44475113056975270, 0.58022101231390620, 0.71795019137779270,
            0.83905458485081340, 0.93618057883842270, 1.0
    };
    public static final double[] EQUITIES_NAIVE_ENHANCED_TIES = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.021636468200550117, //index 3 is score of 0
            0.043272936406368060, 0.043272936736911696, 0.043272946894428240,
            0.043273113091793006, 0.043274733265602220, 0.043285716594378236,
            0.043343066548667610, 0.043587020904946860, 0.044474391860279884,
            0.047228534804601804, 0.054573071612741234, 0.071605703971085230,
            0.106077142148786500, 0.167037039888427270, 0.261300808044805930,
            0.388101562391353200, 0.538612524138272200, 0.690462210679307900,
            0.823667910222892300, 0.922059900884739100, 0.977406934627266900
    };
    public static final double[] EQUITIES_GREEDY_ENHANCED_TIES = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.07404968344066311, //index 3 is score of 0
            0.14809936688361752, 0.14809936703375010, 0.14809937206330515,
            0.14809945976822697, 0.14810035555887155, 0.14810672245622247,
            0.14814139793869350, 0.14829466578343203, 0.14887023108705813,
            0.15070626709111180, 0.15571237376245490, 0.16762787359445808,
            0.19248535132231210, 0.23778789443026707, 0.31024394146952370,
            0.41160287777590310, 0.53927300436050100, 0.67473662383594730,
            0.79969391860476310, 0.90131229981266660, 0.96809028941921140
    };
    public static final double[] EQUITIES_NAIVE_ENHANCED_TIES_SETTLES = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.021636468200550117, //index 3 is score of 0, and this is by definition same as ties non-settles
            0.043432620317285690, 0.043993865912229660, 0.045294076853175380,
            0.047444680925088720, 0.050909048691572350, 0.056407166308563140,
            0.064376898951492870, 0.074607153281900150, 0.086751739024482550,
            0.101725798534190430, 0.121324991667731610, 0.147089072946142460,
            0.182440709342038300, 0.234755778810049220, 0.313623580049141540,
            0.420902289348796140, 0.545917607873338700, 0.682565178722113200,
            0.810735783318413700, 0.913118174274884200, 0.977406934627266900
    };
    public static final double[] EQUITIES_GREEDY_ENHANCED_TIES_SETTLES = {0.0, 0.0, 0.0, //indices 0, 1, 2 same at 0.0
            0.07404968344066311, //index 3 is score of 0, and this is by definition same as ties non-settles
            0.14819152367763500, 0.14853766695613302, 0.14937497074844830,
            0.15081368731460523, 0.15313653389941903, 0.15692489949641542,
            0.16252826890334252, 0.16994149450743773, 0.17876013483810232,
            0.18965701582312460, 0.20476175774155764, 0.22484450078478102,
            0.25231254049859003, 0.29212134336831386, 0.35448505094480500,
            0.44364296307345350, 0.54900036143948440, 0.66936537053129560,
            0.78764820114382230, 0.89104126133663010, 0.96809028941921140
    };

    //OLD 0 means highest average score, with failure to qualify counting as 0 (but everything considered "pass" here)
    //btw it seems that OLD condition 0 is equivalent to condition 4, so has been changed to
    //0 means highest average equity for P1 vs a most conservative P2 (condition 4+) but P1 loses ties
    //1 means highest average equity for P1 vs a most conservative P2 (condition 4+) and ties are ties
    //2 means highest average equity for P1 vs a most conservative P2 (condition 4+) and P1 wins ties
    //3 means highest average equity for P1 against an optimal P2 and P1 wins ties
    //4~24 is an N+ condition
    //25 means highest average equity for P2 against optimal P3, assuming P1 has scored 0 (so P2 0 never ever wins)
    //The above also works for P1 against an optimal P2, but where P2 wins 0-0 ties (P1 still wins other nonzero ties)
    //26~44 means highest average equity for P2 against optimal P3, assuming P1 has scored 22+N
    //(46 means P1 has scored 24 which cannot be beaten, and 45 means P1 has scored 23 which can only be beaten by 24,
    // so use the same strategy as the 24 condition)
    //45 means highest average equity for P1 against optimal P2 and P3

    //Other conditions to consider: (NAIVE is like an unskilled 4 condition, GREEDY is like a less extreme 24 condition)
    //46: highest average equity for P1 vs a naive (keep 1 and 4, then only 6s else single highest) P2 but P1 loses ties
    //47: highest average equity for P1 vs a naive (keep 1 and 4, then only 6s else single highest) P2 and ties are ties
    //48: highest average equity for P1 vs a naive (keep 1 and 4, then only 6s else single highest) P2 and P1 wins ties
    //49: highest average equity for P1 vs a greedy (but not condition 24, so keep 1 and 4 and 6s) P2 but P1 loses ties
    //50: highest average equity for P1 vs a greedy (but not condition 24, so keep 1 and 4 and 6s) P2 and ties are ties
    //51: highest average equity for P1 vs a greedy (but not condition 24, so keep 1 and 4 and 6s) P2 and P1 wins ties
    //although the last 3 don't quite work since the Midnight APP's AI will keep all if it has already won by doing so
    //thus
    //52: highest average equity for P1 vs an enhanced naive (naive but bank all if 100+% to win) P2 but P1 loses ties
    //53: highest average equity for P1 vs an enhanced naive (naive but bank all if 100% to win) P2 and ties are ties
    //54: highest average equity for P1 vs an enhanced naive (naive but bank all if 100% to win) P2 and P1 wins ties
    //55: highest average equity for P1 vs an enhanced greedy (greedy but bank all if 100+% to win) P2 but P1 loses ties
    //56: highest average equity for P1 vs an enhanced greedy (greedy but bank all if 100% to win) P2 and ties are ties
    //57: highest average equity for P1 vs an enhanced greedy (greedy but bank all if 100% to win) P2 and P1 wins ties

    //58~77: (N-54)+ condition but matching the condition is only a tie, not a win (no 78, would just be a 24 condition)
    //78: highest average equity for P1 against an optimal P2 but ties are ties
    //79: highest average equity for P1 against an optimal P2 but P1 loses ties

    //ADDENDUM: conditions 58, 59, and 60 ~~might be~~ ARE provably equivalent to conditions 5, 6, and 7 in policy.
    //But they cannot easily be replaced since the Dist.txt files contain different info from conditions 5, 6, and 7.

    private final int condition;
    private final int numLiveDice; //1~6
    private final int[] liveDice; //array of length 1~6
    private final boolean hasLowQualifier;
    private final boolean hasHighQualifier;
    private final int pointsBanked;

    private int optimalPolicyQual; //how many qualifier dice to keep (4 > 1)
    private int optimalPolicyHigh; //how many high dice to keep (6 > 5 > 4 > 3 > 2 > 1)
    private long successDenom; //this should always be 6^[[number of live dice - 1]th triangular number] (or x2 if ties)
    private long successNum; //number of times the condition is fulfilled (or if with ties, 2 for a win and 1 for a tie)
    private double equityGivenSuccess; //average score (which can differ from Midnight score) given condition fulfilled
    private double equityGivenFailure; //average score given condition not fulfilled
    private double equityOverall; //always equityGivenSuccess * successNum / successDenom +
                                  //equityGivenFailure * (successDenom - successNum) / successDenom

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
        this.equityOverall = -1.0;
    }

    public static void main(String[] args) {

    }

    public void calculateEquityIfKeptAllDice() {
        optimalPolicyQual = 0;
        if (canKeepLowQualifier()) {
            optimalPolicyQual++;
        }
        if (canKeepHighQualifier()) {
            optimalPolicyQual++;
        }
        optimalPolicyHigh = numLiveDice - optimalPolicyQual;

        switch (condition) {
            case 0:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_SAFEST[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_SAFEST[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 1:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_SAFEST[calculateScoreIfKeptAllDice() - 1]/2 +
                                         EQUITIES_SAFEST[calculateScoreIfKeptAllDice()]/2;
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_SAFEST[3]/2; // + EQUITIES_SAFEST[0]/2 which is 0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 2:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_SAFEST[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_SAFEST[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 3:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
            case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19:
            case 20: case 21: case 22: case 23: case 24:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() >= condition) {
                    successNum = successDenom;
                    equityGivenSuccess = calculateScoreIfKeptAllDice();
                } else {
                    successNum = 0;
                    equityGivenFailure = calculateScoreIfKeptAllDice();
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 25:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES[calculateScoreIfKeptAllDice()]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33:
            case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41:
            case 42: case 43: case 44:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > condition - 22) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice()];
                } else {
                    successNum = 0;
                    equityGivenFailure = EQUITIES[calculateScoreIfKeptAllDice()];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 45:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_3P[calculateScoreIfKeptAllDice()];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_3P[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 46:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 47:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE[calculateScoreIfKeptAllDice() - 1]/2 +
                            EQUITIES_NAIVE[calculateScoreIfKeptAllDice()]/2;
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE[3]/2; // + EQUITIES_NAIVE[0]/2 which is 0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 48:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 49:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 50:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY[calculateScoreIfKeptAllDice() - 1]/2 +
                            EQUITIES_GREEDY[calculateScoreIfKeptAllDice()]/2;
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY[3]/2; // + EQUITIES_GREEDY[0]/2 which is 0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 51:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 52:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE_ENHANCED[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE_ENHANCED[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 53:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE_ENHANCED_TIES[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE_ENHANCED_TIES[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 54:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_NAIVE_ENHANCED[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_NAIVE_ENHANCED[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 55:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY_ENHANCED[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY_ENHANCED[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 56:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY_ENHANCED_TIES[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY_ENHANCED_TIES[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 57:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES_GREEDY_ENHANCED[calculateScoreIfKeptAllDice()];
                } else {  // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES_GREEDY_ENHANCED[3];
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65:
            case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73:
            case 74: case 75: case 76: case 77:
                successDenom = (long)(2.0 * Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > condition - 54) {
                    successNum = successDenom;
                    equityGivenSuccess = calculateScoreIfKeptAllDice();
                } else if (calculateScoreIfKeptAllDice() == condition - 54) {
                    //in the tie branch, both equityGivenSuccess and equityGivenFailure must be set to override -1.0
                    successNum = successDenom / 2L;
                    equityGivenSuccess = calculateScoreIfKeptAllDice();
                    equityGivenFailure = calculateScoreIfKeptAllDice();
                } else {
                    successNum = 0;
                    equityGivenFailure = calculateScoreIfKeptAllDice();
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

                break;
            case 79:
                successDenom = (long)(Math.pow(6, (numLiveDice - 1)*(numLiveDice)/2.0));
                if (calculateScoreIfKeptAllDice() > 0) {
                    successNum = successDenom;
                    equityGivenSuccess = EQUITIES[calculateScoreIfKeptAllDice() - 1];
                } else { // == 0
                    successNum = 0;
                    equityGivenFailure = EQUITIES[0]; //0.0
                }
                equityOverall = equityGivenSuccess * successNum / successDenom +
                        equityGivenFailure * (successDenom - successNum) / successDenom;

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

    public double getEquityOverall() {
        return equityOverall;
    }

    public void setEquityOverall(double equityOverall) {
        this.equityOverall = equityOverall;
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
        string += "Key: " + getKey() + "\n";
        string += "optimalPolicyQual: " + optimalPolicyQual + "\n";
        string += "optimalPolicyHigh: " + optimalPolicyHigh + "\n";
        string += "successDenom: " + successDenom + "\n";
        string += "successNum: " + successNum + "\n";
        string += "successPercentage: " + 100.0 * successNum / successDenom + "%\n";
        string += "equityGivenSuccess: " + equityGivenSuccess + "\n";
        string += "equityGivenFailure: " + equityGivenFailure + "\n";
        string += "equityOverall: " + equityOverall + "\n";
        return string;
    }

    public String toStringAbridged() {
        String string = condition < 10 ? "Cond: 0" : "Cond: ";
        string += condition + ", ";
        string += "Key: " + getKey() + "\n";
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
