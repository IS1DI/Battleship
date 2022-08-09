package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Battleship firstPlayer = new Battleship(10,10, "Player 1");
        Scanner scan = new Scanner(System.in);
        System.out.println(firstPlayer.getName() + ", place your ships on the game field");
        System.out.println();
        firstPlayer.print(firstPlayer.getPole());
        for (int i = 5; i > 0; i--){
            firstPlayer.changeType(i);
            firstPlayer.printPlaceTheShip();
            while(!firstPlayer.examination(scan.next(),scan.next())){

            }
        }
        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        scan.nextLine();
        scan.nextLine();
        System.out.println("...");
        System.out.println(firstPlayer.getName() + ", place your ships on the game field");
        System.out.println();
        Battleship secondPlayer = new Battleship(10,10, "Player 2");
        secondPlayer.print(secondPlayer.getPole());
        for (int i = 5; i > 0; i--){
            secondPlayer.changeType(i);
            secondPlayer.printPlaceTheShip();
            while(!secondPlayer.examination(scan.next(),scan.next())){

            }
        }
        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        scan.nextLine();
        scan.nextLine();
        System.out.println("...");
        System.out.println();
        int returnValue = 10;
        while (returnValue != 2) {
            if(Battleship.isFirstPlayerTurn()){
                firstPlayer.printTwoPoles();
                returnValue = firstPlayer.examTakeShot(scan.next(),secondPlayer);

            }else {
                secondPlayer.printTwoPoles();
                returnValue = secondPlayer.examTakeShot(scan.next(),firstPlayer);

            }
            scan.nextLine();
            scan.nextLine();
        }
    }
}

class Battleship {
    private char[][] pole;
    private int sizeOfCurrentShip;
    private int typeOfShip;
    private String ship;
    public final int maxCountShipsCells = 17;
    private char[][] poleInFog;

    private String name;
    Battleship(int sizeX, int sizeY, String name) {
        this.pole = new char[sizeX][sizeY];
        this.poleInFog = new char[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeY; j++) {
                this.pole[i][j] = '~';
                this.poleInFog[i][j] = '~';
            }
        }
        this.typeOfShip = 5;
        this.ship = new String();
        this.name = name;
        FirstPlayerTurn = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changeType(int type) {
        this.typeOfShip = type;
    }
    public void printPlaceTheShip() {
        System.out.println();
        System.out.print("Enter the coordinates of the ");
        switch (typeOfShip) {
            case 5:
                this.ship = "Aircraft Carrier";
                this.sizeOfCurrentShip = 5;
                break;
            case 4:
                this.ship = "Battleship";
                this.sizeOfCurrentShip = 4;
                break;
            case 3:
                this.ship = "Submarine";
                this.sizeOfCurrentShip = 3;
                break;
            case 2:
                this.ship = "Cruiser";
                this.sizeOfCurrentShip = 3;
                break;
            case 1:
                this.ship = "Destroyer";
                this.sizeOfCurrentShip = 2;
                break;
        }
        System.out.printf("%s (%d cells):",ship,sizeOfCurrentShip);
        System.out.println();
        System.out.println();
        System.out.print("> ");



    }

    private static boolean FirstPlayerTurn;

    public static boolean isFirstPlayerTurn() {
        return FirstPlayerTurn;
    }

    public static void setFirstPlayerTurn(boolean isFirstPlayerTurn) {
        Battleship.FirstPlayerTurn = isFirstPlayerTurn;
    }

    public void printTwoPoles(){
        print(this.getPoleInFog());
        System.out.println();
        System.out.println("---------------------");
        print(this.getPole());
        System.out.println();
        System.out.println(this.getName() + ", it's your turn:");
        System.out.println();
        System.out.println("> ");
    }
    public int examTakeShot(String coordinate, Battleship anotherPlayer) {
        int x, y; // -1 - Error ; 0 - Miss ; 1 - Hit ; 2 - all ships destroyed
        try {
            x = coordinate.charAt(0);
            y = Integer.parseInt(coordinate.substring(1));
        } catch (Exception e) {
            System.out.println();
            System.out.println("Error! You entered the wrong coordinates");
            System.out.println();
            System.out.print("> ");
            return -1;
        }
        if (x > 64 && x < 75 && y > 0 && y < 11) {
            if (anotherPlayer.pole[x - 65][y - 1] != 'O' && anotherPlayer.pole[x - 65][y - 1] != 'X') {
                poleInFog[x-65][y-1] = 'M';
                anotherPlayer.placeMark(x,y,'M');
                System.out.println();
                System.out.println("You missed!");
                System.out.println("Press Enter and pass the move to another player");
                System.out.println();
                setFirstPlayerTurn( isFirstPlayerTurn() ? false : true );
                return 0;
            } else {
                this.poleInFog[x-65][y-1] = 'X';
                System.out.println();
                anotherPlayer.placeMark(x,y,'X');
                if(isAllShipsDestroyed(poleInFog)){
                    System.out.print(getName() + " won. Congratulations!");
                    return 2;
                }else if(isShipDestroyed(x, y, anotherPlayer)) {
                    System.out.println("You sank a ship!");
                    System.out.println("Press Enter and pass the move to another player");
                    System.out.println();
                    System.out.print("> ");
                    setFirstPlayerTurn( isFirstPlayerTurn() ? false : true );
                    return 3;
                } else {
                    System.out.println("You hit a ship!");
                    System.out.println("Press Enter and pass the move to another player");
                    System.out.println();
                    System.out.print("> ");
                    setFirstPlayerTurn( isFirstPlayerTurn() ? false : true );
                    return 1;
                }
            }
        } else {
            System.out.println();
            System.out.println("Error! You entered the wrong coordinates. Try again:");
            System.out.println();
            System.out.print("> ");
            return -1;
        }
    }
    public boolean isShipDestroyed(int x1, int y1, Battleship anotherPlayer) {
        boolean DestroyedX = true;
        boolean DestroyedY = true;
        int x = x1-65;
        int y = y1 - 1;
        for(int i = 1; i < 6; i++){
            if(x+i < 10 && pole[x+i][y] == '~' || x+i < 10 &&pole[x+i][y] == 'M'){
                break;
            }else if(x+i < 10 && pole[x+i][y] == 'O'){
                return false;
            }
        }
        for(int j = -1; j > -6; j--){
            if(x+j >= 0 && pole[x+j][y] == '~' || x+j>=0 &&pole[x+j][y] == 'M') {
                break;
            }else if(x+j>=0 && pole[x+j][y]=='O'){
                return false;
            }
        }
        for(int i = 1; i < 6; i++){
            if(y+i < 10 && pole[x][y+i] == '~' || y+i < 10 &&pole[x][y+i] == 'M'){
                break;
            }else if(y+i < 10 && pole[x][y+i] == 'O'){
                return false;
            }
        }
        for(int j = -1; j > -6; j--){
            if(y+j >= 0 && pole[x][y+j] == '~' || y+j >= 0 && pole[x][y+j] == 'M') {
                break;
            }else if(y+j>=0 && pole[x][y+j]=='O'){
                return false;
            }
        }
        return DestroyedX||DestroyedY;
    }
    public boolean isAllShipsDestroyed(char[][] pole) {
        int count = 0;
        for (int i = 0; i < pole.length; i++){
            for(int j = 0; j < pole[i].length; j++){
                if(pole[i][j] == 'X'){
                    ++count;
                }
            }
        }
        if (count == maxCountShipsCells){
            return true;
        }else {
            return false;
        }
    }
    public void printTakeShot() {
        this.print(getPoleInFog());
        System.out.println();
        System.out.println("Take a shot!");
        System.out.println();
        System.out.print("> ");
    }
    public boolean examination(String firstCoordinate, String secondCoordinate) {
        int x1,x2,y1,y2;
        try {
            x1 = (firstCoordinate.charAt(0));
            x2 = (secondCoordinate.charAt(0));
            y1 = Integer.parseInt(firstCoordinate.substring(1));
            y2 = Integer.parseInt(secondCoordinate.substring(1));
        } catch (Exception e) {
            System.out.println();
            System.out.println("Error! Wrong ship location! Try again:");
            System.out.println();
            System.out.print("> ");
            return false;
        }
        if (x1 == x2 ^ y1 == y2 && x1 > 64 && x1 < 75 && y1 > 0 && y1 < 11) {
            int max_x = Integer.max(x1,x2);
            int max_y = Integer.max(y1,y2);
            int min_x = Integer.min(x1,x2);
            int min_y = Integer.min(y1,y2);
            if (max_x - min_x == this.sizeOfCurrentShip - 1 || max_y - min_y == this.sizeOfCurrentShip - 1) {
                if (!isCloseToShip(max_x,min_x,max_y,min_y)) {
                    for (int y = min_y; y <= max_y; y++) {
                        for (int x = min_x; x <= max_x; x++) {
                            this.placeMark(x,y,'O');
                        }
                    }
                    System.out.println();
                    this.print(getPole());
                    return true;
                }else {
                    System.out.println();
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    System.out.println();
                    return false;
                }
            } else {
                System.out.println();
                System.out.printf("Error! Wrong length of the %s! Try again:",this.ship);
                System.out.println();
                System.out.println();
                System.out.print("> ");
                return false;
            }
        } else {
            System.out.println();
            System.out.println("Error! Wrong ship location! Try again:");
            System.out.println();
            System.out.print("> ");
            return false;
        }
    }

    public void placeMark(int x, int y, char mark) {
        if (x > 64 && x < 75 && y > 0 && y < 11) {
            this.pole[(int) (x - 65)][y-1] = mark;
        }
    }

    public boolean isCloseToShip(int max_x, int min_x, int max_y, int min_y) {
        boolean isTrue = false;
        for(int x = min_x - 65; x <= max_x - 65; x++) {
            for(int y = min_y-1; y <= max_y-1; y++) {
                if (x != 0 && x < 9) {
                    if(y!=0 && y < 9) {
                        if(!(pole[x][y] == '~' && pole[x][y+1]=='~'&&pole[x][y-1] == '~' && pole[x-1][y] == '~' && pole[x+1][y] == '~' && pole[x+1][y+1] == '~' && pole[x+1][y-1] == '~' && pole[x-1][y+1] == '~' && pole[x-1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else if (y != 0) {
                        if(!(pole[x][y] == '~' && pole[x][y-1] == '~' && pole[x-1][y] == '~' && pole[x+1][y] == '~' && pole[x+1][y-1] == '~'  && pole[x-1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else {
                        if(!(pole[x][y+1]=='~'&& pole[x][y] == '~' && pole[x-1][y] == '~' && pole[x+1][y] == '~' && pole[x+1][y+1] == '~' && pole[x-1][y+1] == '~' )) {
                            isTrue = true;
                        }
                    }
                }else if (x != 0) {
                    if(y!=0 && y < 9) {
                        if(!(pole[x][y] == '~' && pole[x][y+1]=='~'&&pole[x][y-1] == '~' && pole[x-1][y] == '~'  && pole[x-1][y+1] == '~' && pole[x-1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else if (y != 0) {
                        if(!(pole[x][y] == '~' && pole[x][y-1] == '~' && pole[x-1][y] == '~' && pole[x-1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else {
                        if(!(pole[x][y+1]=='~'&& pole[x][y] == '~' && pole[x-1][y] == '~' && pole[x-1][y+1] == '~' )) {
                            isTrue = true;
                        }
                    }
                } else {
                    if(y!=0 && y < 9) {
                        if(!(pole[x][y] == '~' && pole[x][y+1]=='~'&&pole[x][y-1] == '~' && pole[x+1][y] == '~' && pole[x+1][y+1] == '~' && pole[x+1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else if (y != 0) {
                        if(!(pole[x][y] == '~' && pole[x][y-1] == '~' && pole[x+1][y] == '~' && pole[x+1][y-1] == '~')) {
                            isTrue = true;
                        }
                    }else {
                        if(!(pole[x][y+1]=='~'&& pole[x][y] == '~' && pole[x+1][y] == '~' && pole[x+1][y+1] == '~')) {
                            isTrue = true;
                        }
                    }
                }
            }
        }
        return isTrue;
    }

    public char[][] getPole(){
        return this.pole;
    }

    public char[][] getPoleInFog() {
        return poleInFog;
    }

    public void print(char[][] pole){
        for(int i = 0; i <= pole.length; i++) {
            if (i == 0) {
                System.out.print("  ");
            }else {
                System.out.printf("%d ", i);
            }
        }
        System.out.println();
        for(int i = 0; i < pole.length; i++) {
            System.out.printf("%c ", 65 + i);
            for(int j = 0; j < pole[i].length; j++) {
                System.out.printf("%c ", pole[i][j]);
            }
            System.out.println();
        }
    }
}