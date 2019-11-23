import java.util.Random;
import java.util.Vector;

// The following part should be completed by students.
// Students can modify anything except the class name and exisiting functions and varibles.

public class StudentAI extends AI {
    public StudentAI(int col, int row, int k) throws InvalidParameterError {
        super(col, row, k);

        this.board = new Board(col, row, k);
        this.board.initializeGame();
        this.player = 2;
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
        alphaBeta(8, player, Integer.MIN_VALUE, Integer.MAX_VALUE, board, sm);
        board.makeMove(sm[0].move, player);
        return sm[0].move;
    }

    public int getHeuristic(Board board) {
        return board.blackCount - board.whiteCount;
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

//        if (player == 1) {
//
//                int maxHeuristic = Integer.MIN_VALUE;
//                Move bestMoveMax = moves.get(0).get(0);
//                for (Vector<Move> checkerToMove : moves) {
//        for (Move tryMove : checkerToMove) {
//
//        board.makeMove(tryMove, player);
//        Vector<Vector<Move>> movesMin = board.getAllPossibleMoves(2);
//
//        //int minHeuristic = Integer.MAX_VALUE;
//        //Move bestMoveMin = null;
//
//        for (Vector<Move> checkerToMoveMin : movesMin) {
//        for (Move tryMoveMin : checkerToMoveMin) {
//
//        board.makeMove(tryMoveMin, 2);
//        if (getHeuristic(board) > maxHeuristic) {
//        maxHeuristic = getHeuristic(board);
//        bestMoveMax = tryMove;
//        }
//        board.Undo();
//
//        }
//        }
//
//        board.Undo();
//
//        }
//        }
//        board.makeMove(bestMoveMax, player);
//        return bestMoveMax;
//        } else {
//
//        int minHeuristic = Integer.MAX_VALUE;
//        Move bestMoveMin = moves.get(0).get(0);
//        for (Vector<Move> checkerToMove : moves) {
//        for (Move tryMove : checkerToMove) {
//
//        board.makeMove(tryMove, player);
//        Vector<Vector<Move>> movesMax = board.getAllPossibleMoves(1);
//
//        for (Vector<Move> checkerToMoveMax : movesMax) {
//        for (Move tryMoveMax : checkerToMoveMax) {
//
//        board.makeMove(tryMoveMax, 1);
//        if (getHeuristic(board) < minHeuristic) {
//        minHeuristic = getHeuristic(board);
//        bestMoveMin = tryMove;
//        }
//        board.Undo();
//
//        }
//        }
//
//        board.Undo();
//
//        }
//        }
//        board.makeMove(bestMoveMin, player);
//        return bestMoveMin;
//        }