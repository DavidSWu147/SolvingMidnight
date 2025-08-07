/*
 *If you roll 4 sixes and no qualifiers, should you keep 3 or 4 sixes?
 * Keeping 4 is obviously 30/216 == 5/36 chance
 * If you keep 3:
 * 6/216 chance of winning outright (146/164/416/461/614/641)
 * 72/216 chance of a 6/36
 * 111/216 chance of a 5/36
 * 27/216 chance of losing outright (no 1s, 4s, or 6s)
 * This calculates out to 401/2592 ~= 15.4707% which is actually better than the 13.8889% you get from keeping four!
 */
// 99998668
//999847659

public class Midnight24 {
    public static final int OTHER_LOW_QUALIFIER = 0; //when nonzero, is a qualifier but only alongside the low qualifier
    public static final int LOW_QUALIFIER = 1;
    public static final int HIGH_QUALIFIER = 4;
    public static final int SIX = 6;

    public Midnight24() {

    }

    public static void main(String[] args) {
        Midnight24 midnight24 = new Midnight24();
        midnight24.run();
    }

    public void run() {
        long numAttempts;
        long numSuccesses = 0L;
        for (numAttempts = 0L; numAttempts < 10000000000L; numAttempts++) {
            if (numAttempts % 1000000000L == 0L) {
                System.out.println(numAttempts / 1000000000L);
            }
            if (runOneAttempt()) {
                numSuccesses++;
            }
        }
        System.out.println(numSuccesses);
        System.out.println(numAttempts);
        System.out.println((double)(numSuccesses) / (double)(numAttempts));
    }

    public boolean runOneAttempt() {
        boolean hasLowQualifier = false;
        boolean hasHighQualifier = false;
        int numSixesKept = 0;

        int diceLeftToRoll = 6;
        while (true) {
            int[] diceRolled = rollNDice(diceLeftToRoll);
            if (numSixesKept == 4) {
                if (hasHighQualifier) {
                    return contains(diceRolled, LOW_QUALIFIER);
                } else if (hasLowQualifier) {
                    return contains(diceRolled, HIGH_QUALIFIER);
                } else {
                    if (contains(diceRolled, HIGH_QUALIFIER)) {
                        if (contains(diceRolled, LOW_QUALIFIER)) {
                            return true;
                        } else {
                            hasHighQualifier = true;
                            diceLeftToRoll--;
                            continue;
                        }
                    } else {
                        if (contains(diceRolled, LOW_QUALIFIER)) {
                            hasLowQualifier = true;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            return false;
                        }
                    }
                }
            }
            else if (numSixesKept == 3) {
                if (hasHighQualifier) {
                    if (hasLowQualifier) {
                        return contains(diceRolled, SIX);
                    } else {
                        if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept++;
                                diceLeftToRoll--;
                                continue;
                            }
                        } else {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (hasLowQualifier) {
                        if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept++;
                                diceLeftToRoll--;
                                continue;
                            }
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    return true;
                                } else {
                                    numSixesKept++;
                                    hasHighQualifier = true;
                                    diceLeftToRoll -= 2;
                                    continue;
                                }
                            } else {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    numSixesKept++;
                                    hasLowQualifier = true;
                                    diceLeftToRoll -= 2;
                                    continue;
                                } else {
                                    numSixesKept++;
                                    diceLeftToRoll --;
                                    continue;
                                }
                            }
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    hasHighQualifier = true;
                                    hasLowQualifier = true;
                                    diceLeftToRoll -= 2;
                                    continue;
                                } else {
                                    hasHighQualifier = true;
                                    diceLeftToRoll--;
                                    continue;
                                }
                            } else {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    hasLowQualifier = true;
                                    diceLeftToRoll--;
                                    continue;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            else if (numSixesKept == 2) {
                if (hasHighQualifier) {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 2) { //== 2
                            return true;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            return false;
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 2) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 2;
                                diceLeftToRoll -= 2;
                                continue;
                            }
                        } else if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept++;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 2;
                                continue;
                            } else {
                                numSixesKept++;
                                diceLeftToRoll--;
                                continue;
                            }
                        } else {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 2) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 2;
                                diceLeftToRoll -= 2;
                                continue;
                            }
                        } else if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                numSixesKept++;
                                hasHighQualifier = true;
                                diceLeftToRoll -= 2;
                                continue;
                            } else {
                                numSixesKept++;
                                diceLeftToRoll--;
                                continue;
                            }
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 2) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    return true;
                                } else {
                                    numSixesKept += 2;
                                    hasHighQualifier = true;
                                    diceLeftToRoll -= 3;
                                    continue;
                                }
                            } else {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    numSixesKept += 2;
                                    hasLowQualifier = true;
                                    diceLeftToRoll -= 3;
                                    continue;
                                } else {
                                    numSixesKept++; //this is the exception to max 6s!
                                    diceLeftToRoll--;
                                    continue;
                                }
                            }
                        } else if (contains(diceRolled, SIX)) {
                            if (contains(diceRolled, HIGH_QUALIFIER) && contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept++;
                                hasHighQualifier = true;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 3;
                                continue;
                            } else { //this is also an exception
                                numSixesKept++;
                                diceLeftToRoll--;
                                continue;
                            }
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            else if (numSixesKept == 1) {
                if (hasHighQualifier) {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 3) { //== 3
                            return true;
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            numSixesKept += 2;
                            diceLeftToRoll -= 2;
                            continue;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            return false;
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 3;
                                diceLeftToRoll -= 3;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept += 2;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 3;
                                continue;
                            } else {
                                numSixesKept += 2;
                                diceLeftToRoll -= 2;
                                continue;
                            }
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 3;
                                diceLeftToRoll -= 3;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                numSixesKept += 2;
                                hasHighQualifier = true;
                                diceLeftToRoll -= 3;
                                continue;
                            } else {
                                numSixesKept += 2;
                                diceLeftToRoll -= 2;
                                continue;
                            }
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    return true;
                                } else {
                                    numSixesKept += 3;
                                    hasHighQualifier = true;
                                    diceLeftToRoll -= 4;
                                    continue;
                                }
                            } else {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    numSixesKept += 3;
                                    hasLowQualifier = true;
                                    diceLeftToRoll -= 4;
                                    continue;
                                } else {
                                    numSixesKept += 2; //this is the exception to max 6s!
                                    diceLeftToRoll -= 2;
                                    continue;
                                }
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            if (contains(diceRolled, HIGH_QUALIFIER) && contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept += 2;
                                hasHighQualifier = true;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 4;
                                continue;
                            } else {
                                numSixesKept += 2; //this is also an exception
                                diceLeftToRoll -= 2;
                                continue;
                            }
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            else {    //numSixesKept == 0
                if (hasHighQualifier) {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 4) {    //== 4
                            return true;
                        } else if (containsHowMany(diceRolled, SIX) >= 3) {
                            numSixesKept += 3;
                            diceLeftToRoll -= 3;
                            continue;
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            numSixesKept += 2;
                            diceLeftToRoll -= 2;
                            continue;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            return false;
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 4) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 4;
                                diceLeftToRoll -= 4;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept += 3;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 4;
                                continue;
                            } else {
                                numSixesKept += 3;
                                diceLeftToRoll -= 3;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            numSixesKept += 2;
                            diceLeftToRoll -= 2;
                            continue;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (hasLowQualifier) {
                        if (containsHowMany(diceRolled, SIX) >= 4) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                return true;
                            } else {
                                numSixesKept += 4;
                                diceLeftToRoll -= 4;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                numSixesKept += 3;
                                hasHighQualifier = true;
                                diceLeftToRoll -= 4;
                                continue;
                            } else {
                                numSixesKept += 3;
                                diceLeftToRoll -= 3;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            numSixesKept += 2;
                            diceLeftToRoll -= 2;
                            continue;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (containsHowMany(diceRolled, SIX) >= 4) {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    return true;
                                } else {
                                    numSixesKept += 4;
                                    hasHighQualifier = true;
                                    diceLeftToRoll -= 5;
                                    continue;
                                }
                            } else {
                                if (contains(diceRolled, LOW_QUALIFIER)) {
                                    numSixesKept += 4;
                                    hasLowQualifier = true;
                                    diceLeftToRoll -= 5;
                                    continue;
                                } else {
                                    numSixesKept += 3;  //this is the exception to max 6s!
                                    diceLeftToRoll -= 3;
                                    continue;
                                }
                            }
                        } else if (containsHowMany(diceRolled, SIX) >= 3) {
                            if (contains(diceRolled, HIGH_QUALIFIER) && contains(diceRolled, LOW_QUALIFIER)) {
                                numSixesKept += 3;
                                hasHighQualifier = true;
                                hasLowQualifier = true;
                                diceLeftToRoll -= 5;
                                continue;
                            } else {
                                numSixesKept += 3;
                                diceLeftToRoll -= 3;
                                continue;
                            }
                        } else if (containsHowMany(diceRolled, SIX) == 2) {
                            numSixesKept += 2;
                            diceLeftToRoll -= 2;
                            continue;
                        } else if (contains(diceRolled, SIX)) {
                            numSixesKept++;
                            diceLeftToRoll--;
                            continue;
                        } else {
                            if (contains(diceRolled, HIGH_QUALIFIER)) {
                                hasHighQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else if (contains(diceRolled, LOW_QUALIFIER)) {
                                hasLowQualifier = true;
                                diceLeftToRoll--;
                                continue;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
    }

    public int[] rollNDice(int num) {
        int[] diceRolled = new int[num];
        for (int i = 0; i < num; i++) {
            diceRolled[i] = (int)(6 * Math.random() + 1);
        }
        return diceRolled;
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