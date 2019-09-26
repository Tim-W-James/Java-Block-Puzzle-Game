package comp1110.ass2.OurTests;

import comp1110.ass2.Challenge;
import comp1110.ass2.GameBoardArray;
import comp1110.ass2.State;
import comp1110.ass2.TestUtility;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;
import static comp1110.ass2.State.*;
import static comp1110.ass2.State.RED;

public class ChallengeTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(500);

    // Isn't a valid challenge, just a regular starting state
    private State[][] s_ONE = new State[][]{ // note that positions are inverted when printed in this form
            //y=0 y=1  y=2  y=3  y=4
            {EMP, EMP, EMP, EMP, NLL}, // x = 0
            {EMP, EMP, EMP, EMP, EMP}, // x = 1
            {EMP, EMP, EMP, EMP, EMP}, // x = 2
            {EMP, EMP, EMP, EMP, EMP}, // x = 3
            {EMP, EMP, EMP, EMP, EMP}, // x = 4
            {EMP, EMP, EMP, EMP, EMP}, // x = 5
            {EMP, EMP, EMP, EMP, EMP}, // x = 6
            {EMP, EMP, EMP, EMP, EMP}, // x = 7
            {EMP, EMP, EMP, EMP, NLL}  // x = 8
    };

    // Equal to challenge.png or /assets/a00.png
    // Is a valid challenge == Solution 1
    private State[][] s_FOUR = new State[][]{
            {GRN, GRN, GRN, BLE, NLL},
            {WTE, RED, GRN, WTE, WTE},
            {RED, BLE, WTE, RED, RED},
            {BLE, RED, BLE, BLE, BLE},
            {WTE, RED, WTE, RED, RED},
            {WTE, RED, BLE, BLE, GRN},
            {WTE, BLE, BLE, WTE, GRN},
            {GRN, WTE, WTE, WTE, RED},
            {GRN, GRN, WTE, RED, NLL}
    };

    private String CHALLENGE_1 = "RRRBWBBRB";
    private String CHALLENGE_2 = "RWWRRRWWW";
    private String CHALLENGE_3 = "BGGWGGRWB";

    private String BADCHALLENGE_1 = "a000b013c113d302e323f400g420h522i613j701";
    private String BADCHALLENGE_2 = "a110";
    private String BADCHALLENGE_3 = "BGGWGGRWZ";
    private String BADCHALLENGE_4 = "";
    private String BADCHALLENGE_5 = "RRRRRRRRR";
    private String BADCHALLENGE_6 = "";
    private String BADCHALLENGE_7 = "";




    private GameBoardArray FinishedGameBoard = new GameBoardArray("a000b013c113d302e323f400g420h522i613j701");
    private GameBoardArray NotFinishedGameBoard = new GameBoardArray();





    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed1() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_1);
        Challenge challenge = new Challenge(BADCHALLENGE_1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed2() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_2);
        Challenge challenge = new Challenge(BADCHALLENGE_2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed3() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_3);
        Challenge challenge = new Challenge(BADCHALLENGE_3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed4() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_4);
        Challenge challenge = new Challenge(BADCHALLENGE_4);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed5() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_5);
        Challenge challenge = new Challenge(BADCHALLENGE_5);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed6() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_6);
        Challenge challenge = new Challenge(BADCHALLENGE_6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadFormed7() {
        System.out.println("Testing the challenge constructor with the bad string: " + BADCHALLENGE_7);
        Challenge challenge = new Challenge(BADCHALLENGE_7);
    }

    @Test
    public void testWellFormed() {
        var C1 = new Challenge(CHALLENGE_1);
        var C2 = new Challenge(CHALLENGE_2);
        var C3 = new Challenge(CHALLENGE_3);

        assertEquals("Challenge 1 is created correctly", CHALLENGE_1,
                C1.getChallengeStr());
        assertEquals("Challenge 2 is created correctly", CHALLENGE_2,
                C2.getChallengeStr());
        assertEquals("Challenge 3 is created correctly", CHALLENGE_3,
                C3.getChallengeStr());
    }

    @Test
    public void testBoardChallenge() {
        var challenge = new Challenge(CHALLENGE_1);
        assertEquals("Test if challenge 1 states that a given board is correct",true,challenge.isChallengeVld(FinishedGameBoard));
    }



}
