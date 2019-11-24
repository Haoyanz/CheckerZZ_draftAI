import java.util.Random;
import java.util.Vector;

// The following part should be completed by students.
// Students can modify anything except the class name and exisiting functions and varibles.

public class StudentAI extends AI {

    private int movesMade;

    public StudentAI(int col, int row, int k) throws InvalidParameterError {
        super(col, row, k);

        this.board = new Board(col, row, k);
        this.board.initializeGame();
        this.player = 2;

        this.movesMade = 0;
    }

    public Move GetMove(Move move) throws InvalidMoveError {
        if (!move.seq.isEmpty())
            board.makeMove(move, (player == 1) ? 2 : 1);
        else
            player = 1;
        Vector<Vector<Move>> moves = board.getAllPossibleMoves(player);
        Random randGen = new Random();
        int index = randGen.nextInt(moves.size());
        int innerIndex = randGen.nextInt(moves.get(index).size());

        Vector<Vector<Move>> allPossibleMoves = board.getAllPossibleMoves(player);

        ScoreMove[] sm = new ScoreMove[]{new ScoreMove(getHeuristic(board), allPossibleMoves.get(0).get(0))};
        if(board.col * board.row <= 56) { //small board
            if (movesMade < 3) { //early game
                alphaBeta(6, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
            } else {             //late game
                alphaBeta(8, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
            }
        } else {                          //larger board
            if (movesMade < 10) { //early game
                alphaBeta(5, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
            } else if (movesMade > 20) { //late game
                alphaBeta(5, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
            } else {
                alphaBeta(7, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
            }
        }
        board.makeMove(sm[0].move, player);
        movesMade++;
        return sm[0].move;
    }

    public int getHeuristic(Board board) {
        int blackKing = 0;
        int whiteKing = 0;
        for(Vector<Checker> checkers : board.board){
            for(Checker checker : checkers){
                if(checker.color.equals("B") && checker.isKing){
                    blackKing++;
                } else if(checker.color.equals("W") && checker.isKing){
                    whiteKing++;
                }
            }
        }
        return board.blackCount - board.whiteCount + blackKing * 3 - whiteKing * 3;
    }

    public int alphaBeta(int depth, int player, int alpha, int beta, Board board, ScoreMove[] sm) throws InvalidMoveError{
        Vector<Vector<Move>> allPossibleMoves = board.getAllPossibleMoves(player);

        int score = sm[0].score;
        Move bestMove = sm[0].move;

        //base case
        if(board.isWin(player) != 0 || depth == 0){
            score = getHeuristic(board);
            sm[0].score = score;
            sm[0].move = bestMove;
            return sm[0].score;
        }

        //recursive rule
        for(Vector<Move> checkerToMove : allPossibleMoves){
            for(Move tryMove : checkerToMove){

                board.makeMove(tryMove, player);

                if(player == 1){  //max player
                   score = alphaBeta(depth - 1, 2, alpha, beta, board, sm);
                    if (score > alpha) {
                        alpha = score;
                        bestMove = tryMove;
                    }
                } else {  //min player
                    score = alphaBeta(depth - 1, 1, alpha, beta, board, sm);
                    if (score < beta) {
                        beta = score;
                        bestMove = tryMove;
                    }
                }

                board.Undo();

                if (alpha >= beta) break;
            }
        }
        sm[0].score = player == 1 ? alpha : beta;
        sm[0].move = bestMove;
        return sm[0].score;
    }

    static class ScoreMove{
        int score;
        Move move;
        public ScoreMove(int score, Move move){
            this.score = score;
            this.move = move;
        }
    }
}

/**
 * further improvement
 *  change determinant of early game (# of movesMade)
 *  change heuristic function
 *  flatten 2-D Vectors and rearrange the Moves
 */

//python3 AI_Runner.py 9 8 2 l ../src/checkers-java/Main.jar Sample_AIs/Poor_AI/main.py