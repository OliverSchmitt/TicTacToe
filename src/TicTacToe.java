import java.util.InputMismatchException;
import java.util.Scanner;

enum GameOver {
    NOT_GAME_OVER,
    DRAW,
    P1_WON,
    P2_WON,
}

class TicTacToe {
    private static final int WIDTH = 3;

    private static final char TOP_BORDER_CHAR = '-';
    private static final char SIDE_BORDER_CHAR = '|';

    private static final char PLAYER_1_CHAR = 'X';
    private static final char PLAYER_2_CHAR = 'O';

    // Using numbers 1-9 for the fields indices.
//    private static final int[] INDICES = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    // Using num pad style indices.
    private static final int[] INDICES = {6, 7, 8, 3, 4, 5, 0, 1, 2};

    private boolean isRunning;
    private boolean p1Turns;

    private char[] fields = new char[ WIDTH * WIDTH ];

    private String name1;
    private String name2;

    private Scanner scanner;

    TicTacToe() {
        isRunning = true;
        p1Turns = true;
        clearFields();
        scanner = new Scanner( System.in );
        getNames();
    }

    void run() {
        while( isRunning ) {
            render();
            update();
            if( testGameOver() != GameOver.NOT_GAME_OVER ) {
                displayGameOver( testGameOver() );
                isRunning = false;
            }
        }
    }

    private int getIndex( int x, int y ) {
        return y * WIDTH + x;
    }

    private void clearFields() {
        for( int i = 0; i < WIDTH * WIDTH; i++ )
            fields[i] = ' ';
    }

    private void getNames() {
        clearScreen();
        final String namePrompt = "Player %d, what is your name? ";
        System.out.printf( namePrompt, 1 );
        name1 = scanner.nextLine();
        System.out.printf( namePrompt, 2 );
        name2 = scanner.nextLine();
    }

    private void render() {
        clearScreen();
        displayNames();
        displayFields();
    }

    private void update() {
        final String prompt = "%s, where do you want to place your character? ";
        int input = -1;
        while( !verifyInput( input ) ) {
            System.out.printf( prompt, (p1Turns) ? name1 : name2 );
            try {
                input = scanner.nextInt() - 1;
            } catch( InputMismatchException ime ) {
                scanner.next();
                input = -1;
            }
        }
        fields[INDICES[input]] = (p1Turns) ? PLAYER_1_CHAR : PLAYER_2_CHAR;
        p1Turns = !p1Turns;
    }

    private GameOver testGameOver() {
        if( testHorizontal() || testVertical() || testDiagonal() )
            return (!p1Turns) ? GameOver.P1_WON : GameOver.P2_WON;
        else if( testDraw() )
            return GameOver.DRAW;
        else
            return GameOver.NOT_GAME_OVER;
    }

    private boolean testHorizontal() {
        for( int i = 0; i < WIDTH; i++ )
            if( fields[getIndex( 0, i )] != ' ' &&
                fields[getIndex( 0, i )] == fields[getIndex( 1, i )] &&
                fields[getIndex( 1, i )] == fields[getIndex( 2, i )]
            )
                return true;
        return false;
    }

    private boolean testVertical() {
        for( int i = 0; i < WIDTH; i++ )
            if( fields[getIndex( i, 0 )] != ' ' &&
                fields[getIndex( i, 0 )] == fields[getIndex( i, 1 )] &&
                fields[getIndex( i, 1 )] == fields[getIndex( i, 2 )]
            )
                return true;
        return false;
    }

    private boolean testDiagonal() {
        return (fields[getIndex( 0, 0 )] != ' ' &&
                fields[getIndex( 0, 0 )] == fields[getIndex( 1, 1 )] &&
                fields[getIndex( 1, 1 )] == fields[getIndex( 2, 2 )])
                ||
                (fields[getIndex( 0, 2 )] != ' ' &&
                fields[getIndex( 0, 2 )] == fields[getIndex( 1, 1 )] &&
                fields[getIndex( 1, 1 )] == fields[getIndex( 2, 0 )]);
    }

    private boolean testDraw() {
        for( int i = 0; i < WIDTH * WIDTH; i++ )
            if( fields[i] == ' ' )
                return false;
        return true;
    }

    private boolean verifyInput( int input ) {
        return input >= 0 && input < 9 && fields[INDICES[input]] == ' ';
    }

    private void clearScreen() {
        for( int i = 0; i < 100; i++ )
            System.out.println();
    }

    private void displayNames() {
        final String nameDisplay = "Player %d: %s - %c\n";
        System.out.printf( nameDisplay, 1, name1, PLAYER_1_CHAR );
        System.out.printf( nameDisplay, 2, name2, PLAYER_2_CHAR );
    }

    private void displayFields() {
        displayLine();
        for( int y = 0; y < WIDTH; y++ ) {
            System.out.print( SIDE_BORDER_CHAR );
            for( int x = 0; x < WIDTH; x++ ) {
                System.out.print( fields[ getIndex( x, y ) ] );
                System.out.print( SIDE_BORDER_CHAR );
            }
            System.out.println();
            displayLine();
        }
    }

    private void displayLine() {
        for( int i = 0; i < 2 * WIDTH + 1; i++ )
            System.out.print( TOP_BORDER_CHAR );
        System.out.println();
    }

    private void displayGameOver( GameOver gameOver ) {
        render();
        String win = "%s Won!";
        switch( gameOver ) {
            case P1_WON:
                System.out.printf( win, name1 );
                break;
            case P2_WON:
                System.out.printf( win, name2 );
                break;
            case DRAW:
                System.out.println( "It's a Draw!" );
                break;
        }
    }
}
