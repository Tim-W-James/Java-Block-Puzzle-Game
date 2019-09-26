package comp1110.ass2.OurTests;

import comp1110.ass2.Challenge;
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




    @Test (expected = IllegalArgumentException.class)
    public void testWellFormed() {
        Challenge challenge = new Challenge(BADCHALLENGE_1);
    }
}
