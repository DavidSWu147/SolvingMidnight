# SolvingMidnight

This repository contains files that describe how to play Midnight (AKA 1-4-24) **perfectly**, as well as the code that generated the files.

How to use: open the Strat.txt file for the condition you are playing (explained below). Within the file, search for the key that corresponds to the current game state you are in.
The key is composed of three parts: 
1. the score you have banked so far, as a two digit number with a leading zero if less than 10,
2. the qualifiers you have banked so far, either HL for both, H\_ for the high qualifier (4) only, \_L for the low qualifier (1) only, or \_\_ for neither, and
3. | followed by the dice you have rolled this round, in ascending order.


For example, the key for the best possible way to start the game is 00\_\_|146666. I'm assuming you know how to play that roll.
You will see two numbers, optimalPolicyQual and optimalPolicyHigh. The first number tells you how many (still needed) qualifiers you should keep.
If you rolled both qualifiers (4 and 1), but optimalPolicyQual is 1, you should **always** keep the 4, never the 1.
The second number tells you how many high dice (i.e. non-qualifiers) you should keep, starting with the highest number, like a 6 if it exists, and going downwards.
Therefore, for a key of 00\_\_|146666, optimalPolicyQual is 2 and optimalPolicyHigh is 4.

Now to explain the more important conditions you could be playing:

Condition 3: you are the first player in a 2 player game, and your opponent is a perfect player, but you win if the score is tied

Conditions 4\~24: you are the last player, and you need to score at least N to win, no ties. N is the condition number

Condition 25: you are the second player in a 3 player game, the first player did not qualify (scored 0), and your remaining opponent is a perfect player

Conditions 26\~44: you are the second player in a 3 player game, the first player scored N-22, and your remaining opponent is a perfect player. N is the condition number

Condition 45: you are the first player in a 3 player game, and your opponents are perfect players

Note that for the 3 player game, assume that ties in score are broken in favor of the player earlier in turn order. So P1 wins ties over P2 and P3, and P2 wins ties over P3.

Condition 56: you are the first player in a 2 player game, and your opponent is using the enhanced semi-greedy strategy, and ties are not broken

Condition 57: you are the first player in a 2 player game, and your opponent is using the enhanced semi-greedy strategy, but you win if the score is tied

Conditions 58\~77: you are the last player, and you need to score exactly N-54 to tie, or more than N-54 to win. N is the condition number

Condition 78: you are the first player in a 2 player game, and your opponent is a perfect player, and ties are not broken

There are a total of 80 distinct conditions, labeled from 0 to 79.
The semi-greedy strategy is defined as follows: 
First, keep any missing qualifiers you still need. Then, if you have both qualifiers, keep all the 6s you rolled. Otherwise, keep as many 6s as you can, 
without going over 3 non-qualifier dice if you have kept one qualifier so far, or 2 non-qualifier dice if you have not kept any qualifiers yet. 
Finally, failing all else, default to keeping the single highest dice number. The enhanced semi-greedy strategy is defined as the same as the semi-greedy strategy,
but with the additional caveat that if you can beat the other player's score by keeping all dice you rolled, simply keep all dice and immediately end the game.
