import java.util.Scanner;

public class game {

    static int winner = 0;
    static int Acats = 0, Bcats = 0;
    static int Awins = 0, Bwins = 0;
    static char[][] state = new char[6][6];
    static boolean onGoing = true;

    public static void main(String[] args) {
        startGame();
    }

    public static void initGame() {
        for(int i = 0 ; i<6 ; i++){
            for(int j = 0; j<6 ; j++){
                state[i][j] = '0';
            } 
        }
        winner = 0;
        Acats = Bcats = 0;
        Awins = Bwins = 0;
        onGoing = true;
    }

    public static void startGame(){
        initGame();
        int turnNum = 1;
        boolean player = false;
        while (onGoing) {
            System.out.println("=================turn "+turnNum+":");
            System.out.println("{0}{1}{2}{3}{4}{5}{6}");
            int r = 1;
            for(char[] row : state){
                System.out.print("{"+r+"}");
                for(char box : row){
                    System.out.print("["+box+"]");
                }
                r++;
                System.out.println();
            }

            int[] input = getInput(player);
            int i= input[0],j=input[1],s=input[2];
            
            updateGame(player , i , j , s);
            //======================================
            turnNum++;
            player = !player;
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if(winner == 0){
            System.out.println("IT IS A DRAW");
        }else{
            System.out.println("PLAYER "+ winner + " WON!!!");
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    private static int[] getInput(boolean player) {
        Scanner scan = new Scanner(System.in);
        int[] input = new int[3];
        while(true){
            System.out.println("Player "+(player ? "2" : "1"));
            System.out.println("You have "+ (player ? Bcats : Acats) + " BIG cats!");
            System.out.println("Enter 1 <= i, j <= 6 and size (0/1)");
            for(int i = 0; i < 3 ;i++)input[i] = scan.nextInt();
            if(input[0] > 6 || input[0] < 1 || input[1] > 6 || input[1] < 1){
                System.out.println("use correct bounds");
                continue;
            }
            input[0]--;input[1]--;
            if(state[input[0]][input[1]] != '0'){
                System.out.println("already taken slot");
                continue;
            }
            if(input[2] != 0 && ((player && !(Bcats > 0)) || (!player && !(Acats > 0)))){
                input[2] = 0;
            }
            if(input[2] != 0){
                input[2] = 1;
            }
            break;
        }
        return input;
    }

    private static void updateGame(boolean player, int i, int j, int s) {
        
        if(player && s == 0)
            state[i][j] = 'b';
        else if(player && s == 1){
            state[i][j] = 'B';
            Bcats--;
        }
        else if(!player && s == 0)
            state[i][j] = 'a';
        else if(!player && s == 1){
            state[i][j] = 'A';
            Acats--;
        }
        
        for(int k = -1 ; k <= 1 ; k++){
            for(int w = -1; w <= 1 ; w++){
                if(k ==0 && w == 0)
                    continue;
                push(i,j,k,w,s);
            }
        }
        check();
    }

    private static void push(int i, int j, int k, int w, int s) {
        int iPushedFrom = i+k, jPushedFrom = j+w;
        int iPushedTo = iPushedFrom+k, jPushedTo = jPushedFrom+w;

        if(iPushedFrom > 5 || iPushedFrom < 0 || jPushedFrom > 5 || jPushedFrom < 0)
            return;
        char toBePushed = state[iPushedFrom][jPushedFrom];
        boolean isOut = iPushedTo > 5 || iPushedTo < 0 || jPushedTo > 5 || jPushedTo < 0;
        if(!isOut && state[iPushedTo][jPushedTo] != '0'){
            return;
        }
        if((toBePushed == 'A' || toBePushed == 'B')){
            if(s == 0)
                return;
            state[iPushedFrom][jPushedFrom] = '0';
            if(isOut){
                if (toBePushed == 'A') {
                    Acats++;
                } else {
                    Bcats++;
                }
            }else{
                state[iPushedTo][jPushedTo] = toBePushed;
            }
        }else{
            state[iPushedFrom][jPushedFrom] = '0';
            if(!isOut){
                state[iPushedTo][jPushedTo] = toBePushed;
            }
        }
    }

    private static void check() {
        boolean zeros = false;
        for(int i = 0 ; i < 6 ;i++){
            for(int j = 0; j < 6 ; j++){
                if(state[i][j] == 'A' || state[i][j] == 'B'){
                    checkForWin(i,j);
                }else if(state[i][j] == '0'){
                    zeros = true;
                }
            } 
        }
        if(Awins > 0 || Bwins > 0){
            onGoing = false;
            winner = Awins == Bwins ? 0 : Awins > Bwins ? 1 : 2;
            return;
        }
        for(int i = 0; i< 6 ; i++){
             for(int j = 0; j < 6 ; j++){
                if(state[i][j] != '0'){
                    checkForBigCat(i,j);
                }
            }
        }
        onGoing &= zeros;
    }

    private static void checkForBigCat(int i, int j) {
        char c1 = state[i][j],c2= '0',c3='0';
        if(i < 4){
            c2 = state[i+1][j];
            c3 = state[i+2][j];
        }
        if(checkForBigCat(c1, c2, c3)){
            state[i+1][j] = '0';
            state[i+2][j] = '0';
            state[i][j] = '0';
            return;
        }
        c2 = '0'; c3 = '0';
        if(j < 4){
            c2 = state[i][j+1];
            c3 = state[i][j+2];
        }
        if(checkForBigCat(c1, c2, c3)){
            state[i][j] = '0';
            state[i][j+1] = '0';
            state[i][j+2] = '0';
            return;
        }

        c2 = '0'; c3 = '0';
        if(j < 4 && i < 4){
            c2 = state[i+1][j+1];
            c3 = state[i+2][j+2];
        }
        if(checkForBigCat(c1, c2, c3)){
            state[i][j] = '0';
            state[i+1][j+1] = '0';
            state[i+2][j+2] = '0';
            return;
        }

        c2 = '0'; c3 = '0';
        if(j < 4 && i > 1){
            c2 = state[i-1][j+1];
            c3 = state[i-2][j+2];
        }
        if(checkForBigCat(c1, c2, c3)){
            state[i][j] = '0';
            state[i-1][j+1] = '0';
            state[i-2][j+2] = '0';
            return;
        }

    }

    public static boolean checkForBigCat (char c1, char c2 ,char c3){
        c1 = (""+c1).toUpperCase().charAt(0);
        c2 = (""+c2).toUpperCase().charAt(0);
        c3 = (""+c3).toUpperCase().charAt(0);

        if(c1 == 'A' && c2 == 'A' && c3 == 'A'){
            Acats += 3;
            return true;
        }
        if(c1 == 'B' && c2 == 'B' && c3 == 'B'){
            Bcats += 3;
            return true;
        }

        return false;
    }

    public static void checkForWin(int i, int j) {
        char c1 = state[i][j],c2= '0',c3='0';
        if(i < 4){
            c2 = state[i+1][j];
            c3 = state[i+2][j];
        }
        checkRow(c1, c2, c3);

        c2 = '0'; c3 = '0';
        if(j < 4){
            c2 = state[i][j+1];
            c3 = state[i][j+2];
        }
        checkRow(c1, c2, c3);

        c2 = '0'; c3 = '0';
        if(j < 4 && i < 4){
            c2 = state[i+1][j+1];
            c3 = state[i+2][j+2];
        }
        checkRow(c1, c2, c3);

        c2 = '0'; c3 = '0';
        if(j < 4 && i > 1){
            c2 = state[i-1][j+1];
            c3 = state[i-2][j+2];
        }
        checkRow(c1, c2, c3);

    }

    private static void checkRow(char c1, char c2, char c3) {
        if(c1 == 'A' && c2 == 'A' && c3 == 'A'){
            Awins++;
        }
        if(c1 == 'B' && c2 == 'B' && c3 == 'B'){
            Bwins++;
        }
    }
}