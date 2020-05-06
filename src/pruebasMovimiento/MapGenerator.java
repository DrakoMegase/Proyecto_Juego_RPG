package pruebasMovimiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class MapGenerator {

    static Room[][] roomArray;

    public static int[][][] generateMap(int maxRoom) {

        int[][][] map = new int[maxRoom][maxRoom][2];
        roomArray = new Room[maxRoom][maxRoom];


        int exits = 4;
        int start = maxRoom / 2;
        LinkedList<int[]> rooms = new LinkedList<>();
        int[] startRoom = {start, start};
        rooms.add(startRoom);
        map[start][start][0] = 15;
        map[start][start][1] = 0;

        boolean shop = false, boss = false;

        int[] skip = {1, 2, 3, 4}; // U,D,R,L
        for (int i = 0; i < rooms.size(); i++) {
            int roomX = rooms.get(i)[0];
            int roomY = rooms.get(i)[1];

            boolean[] booleans = numToBool(map[roomX][roomY][0]);
            if (map[roomX][roomY][1] != 0) {
                booleans[map[roomX][roomY][1] - 1] = false;
                if (!shop && rooms.size() > start && (int) (Math.random() * 2) == 1) {
                    map[roomX][roomY][1] = 2;
                    shop = true;
                    System.out.println("Tiendecita");
                } else {
                    map[roomX][roomY][1] = 1;
                }
            }


            for (int j = 0; j < booleans.length; j++) {
                if (booleans[j]) {
                    int sumX = 0;
                    int sumY = 0;
                    int val = 0;
                    switch (j) {
                        case 0:
                            sumY -= 1;
                            val = 2;
                            break;
                        case 1:
                            sumY += 1;
                            val = 1;
                            break;
                        case 2:
                            sumX += 1;
                            val = 4;
                            break;
                        case 3:
                            sumX -= 1;
                            val = 3;
                            break;
                    }
                    int[] room = {roomX + sumX, roomY + sumY};

                    if (room[0] < 0) {
                        map[roomX][roomY][0] -= 1;
                        exits--;
                    } else if (room[0] >= maxRoom) {
                        map[roomX][roomY][0] -= 2;
                        exits--;
                    } else if (room[1] < 0) {
                        map[roomX][roomY][0] -= 4;
                        exits--;
                    } else if (room[1] >= maxRoom) {
                        map[roomX][roomY][0] -= 8;
                        exits--;
                    } else {
                        if (map[room[0]][room[1]][0] == 0) {
                            boolean limit = rooms.size() + exits > maxRoom;

                            int roomVal = generateNum(val, limit);



                            map[room[0]][room[1]][0] = roomVal;
                            map[room[0]][room[1]][1] = val;
                            roomArray[room[0]][room[1]] =  nuevasala(roomVal);
                            exits += -2 + numToExits(roomVal);

                            if (!limit) {
                                rooms.add(room);
                            } else if (!boss) {
                                map[room[0]][room[1]][1] = 3;
                                boss = true;
                                System.out.println("Boss");
                            } else {
                                map[room[0]][room[1]][1] = 1;
                            }

                        } else {
                            boolean[] nextRoom = numToBool(map[room[0]][room[1]][0]);
                            if (!nextRoom[val - 1]) {
                                map[room[0]][room[1]][0] += Math.pow(2, 3 - (val - 1));
                                exits -= 2;
                            } else {
                                exits--;
                            }
                        }
                    }
                    /*for (int y = 0; y < 12; y++) {
                        for (int x = 0; x < 12; x++) {
                            System.out.print(map[x][y][0]+"\t");
                        }
                        System.out.print("\n");
                    }

                    System.out.println();

                    for (int y = 0; y < 12; y++) {
                        for (int x = 0; x < 12; x++) {
                            System.out.print(map[x][y][1]+"\t");
                        }
                        System.out.print("\n");
                    }

                    System.out.println();
                    Scanner scanner=new Scanner(System.in);
                    scanner.nextLine();*/
                }
            }


        }

        return map;
    }

    private static Room nuevasala(int salaType) {
        Room r = new Room("res/jsonsMapasPruebas/0001.json","res/img/terrain_atlas.png");
        r.setSalaType(salaType);

        return r;
    }

    private static boolean[] numToBool(int num) {
        int res = num;
        boolean[] booleans = new boolean[4];

        for (int i = 3; i > -1; i--) {
            booleans[i] = res % 2 == 1;
            res /= 2;
        }

        return booleans;
    }

    private static int numToExits(int num) {
        boolean[] booleans = numToBool(num);
        int cont = 0;
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                cont++;
            }
        }
        return cont;
    }

    private static int boolToNum(boolean[] booleans) {
        int cont = 0;
        for (int i = 0; i > -booleans.length; i--) {
            if (booleans[0]) {
                cont += Math.pow(2, i * -1);
            }
        }
        return cont;
    }

    private static int generateNum(int val, boolean oneDoor) {
        int[] nums = new int[4];
        for (int i = 0; i < nums.length; i++) {
            if (val - 1 == i) {
                nums[i] = 1;
            } else {
                if (!oneDoor) {
                    nums[i] = (int) (Math.random() * 2);
                } else {
                    nums[i] = 0;
                }
            }
        }
        return 8 * nums[0] + 4 * nums[1] + 2 * nums[2] + nums[3];

    }


    public static ArrayList<Room> cargarSalasNivel(ArrayList<Room> salas, int maxRoom, String spriteSheet) {


        int[][][] map = generateMap(maxRoom);

        Room[][] mapaRooms = new Room[maxRoom][maxRoom];







        return salas;
    }


    public static void main(String[] args) {

        int[][][] map = generateMap(12);

        System.out.println("GENERACION MAPA RANDOM");
        //Puertas sala
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                System.out.print(map[x][y][0] + "\t");
            }
            System.out.print("\n");
        }

        System.out.println();

        //Tipos sala
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                System.out.print(map[x][y][1] + "\t");
            }
            System.out.print("\n");
        }


        ArrayList<Room>salas = new ArrayList<>();
        //System.out.println(cargarSalasNivel(salas, 12, "res/img/terrain_atlas.png"));


        System.out.println("  ");
        for (int i = 0; i < roomArray.length ; i++) {
            for (int j = 0; j <roomArray[i].length ; j++) {


                if (roomArray[i][j] == null){
                    System.out.print("0\t");
                }else System.out.print(roomArray[i][j].getSalaType() + "\t");


            }
            System.out.println("");
        }


//        System.out.println(numToExits(13));

//        System.out.println(Arrays.toString(numToBool(7)));

//        System.out.println(generateNum(3,false));

    }

}
