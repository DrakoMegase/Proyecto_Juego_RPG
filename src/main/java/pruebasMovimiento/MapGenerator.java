package pruebasMovimiento;

import java.util.*;

class MapGenerator {


    static Room[][] generateMap(int maxRoom) {

        int mapLimit=20;
        int[][][] map = new int[mapLimit][mapLimit][2];
        Room[][] roomArray = new Room[mapLimit][mapLimit];
        Room.setContador(0);


        int exits = 4;
        int start = mapLimit / 2;
        LinkedList<int[]> rooms = new LinkedList<>();
        int[] startRoom = {start, start};
        rooms.add(startRoom);
        map[start][start][0] = 15;
        map[start][start][1] = 0;
        Room firstRoom=new Room(15);
        firstRoom.salaClass=0;
        firstRoom.x=start;
        firstRoom.y=start;
        firstRoom.setDistancia(0);
        firstRoom.clear=true;
        firstRoom.setVisited(true);
        roomArray[start][start]=firstRoom;



        for (int i = 0; i < rooms.size(); i++) {
            int roomX = rooms.get(i)[0];
            int roomY = rooms.get(i)[1];
            Room salaActual=roomArray[roomX][roomY];

            boolean[] booleans = numToBool(map[roomX][roomY][0]);
            if (map[roomX][roomY][1] != 0) {
                booleans[map[roomX][roomY][1] - 1] = false;
                map[roomX][roomY][1] = 1;
                roomArray[roomX][roomY].salaClass=1;
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
                    } else if (room[0] >= mapLimit) {
                        map[roomX][roomY][0] -= 2;
                        exits--;
                    } else if (room[1] < 0) {
                        map[roomX][roomY][0] -= 4;
                        exits--;
                    } else if (room[1] >= mapLimit) {
                        map[roomX][roomY][0] -= 8;
                        exits--;
                    } else {
                        if (map[room[0]][room[1]][0] == 0) {
                            boolean limit = rooms.size() + exits > maxRoom;

                            int roomVal = generateNum(val, limit);

                            Room nuevaSala=new Room(roomVal);
                            nuevaSala.x=room[0];
                            nuevaSala.y=room[1];
                            nuevaSala.setDistancia(salaActual.getDistancia()+1);
                            map[room[0]][room[1]][0] = roomVal;
                            map[room[0]][room[1]][1] = val;
                            roomArray[room[0]][room[1]] =  nuevaSala;
                            exits += -2 + numToExits(roomVal);

                            Salida salidaActual= new Salida(salaActual,j+1);
                            Salida nuevaSalida= new Salida(nuevaSala,val);

                            salidaActual.setConexion(nuevaSalida);
                            nuevaSalida.setConexion(salidaActual);


                            if (!limit) {
                                rooms.add(room);
                            } else {
                                map[room[0]][room[1]][1] = 1;
                                roomArray[room[0]][room[1]].salaClass=1;
                            }

                        } else {
                            boolean[] nextRoom = numToBool(map[room[0]][room[1]][0]);
                            if (!nextRoom[val - 1]) {
                                map[room[0]][room[1]][0] += Math.pow(2, 3 - (val - 1));

                                Room nuevaSala=roomArray[room[0]][room[1]];

                                nuevaSala.salaType+= Math.pow(2, 3 - (val - 1));

                                Salida salidaActual= new Salida(salaActual,j+1);
                                Salida nuevaSalida= new Salida(nuevaSala,val);

                                salidaActual.setConexion(nuevaSalida);
                                nuevaSalida.setConexion(salidaActual);

                                exits -= 2;
                            } else {
                                Room nuevaSala=roomArray[room[0]][room[1]];
                                Salida salidaActual= new Salida(salaActual,j+1);
                                Salida nuevaSalida= new Salida(nuevaSala,val);
                                salidaActual.setConexion(nuevaSalida);
                                nuevaSalida.setConexion(salidaActual);
                                exits--;
                            }
                        }
                    }
                }
            }


        }

        boolean boss=false;

        for (int i = 0; !boss && i < roomArray.length ; i++) {
            for (int j = 0; !boss && j <roomArray[i].length ; j++) {
                Room room=roomArray[j][i];
                if(room!=null&&room.getDistancia()>1&&(room.salaType==1||room.salaType==2||room.salaType==4||room.salaType==8)){
                    boss=true;
                    room.salaClass=3;
                }
            }
        }

        boolean shop = false;
        for (int i = 0; !shop && i < roomArray.length ; i++) {
            for (int j = 0; !shop && j <roomArray[i].length ; j++) {
                Room room=roomArray[j][i];
                if(room!=null&&room.salaClass==1&&room.getDistancia()>1&&(room.salaType==1||room.salaType==2||room.salaType==4||room.salaType==8)){
                    shop=true;
                    room.salaClass=2;
                }
            }
        }



        return roomArray;
    }

    private static boolean[] numToBool(int num) {
        int res = num;
        boolean[] booleans = new boolean[4];

        for (int i = 3; i > -1; i--) {
            booleans[i] = res % 2 != 0;
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

    private static int generateNum(int val, boolean oneDoor) {
        int[] nums = new int[4];
        Random random=new Random();
        for (int i = 0; i < nums.length; i++) {
            if (val - 1 == i) {
                nums[i] = 1;
            } else {
                if (!oneDoor) {
                    nums[i] = random.nextInt(2);
                } else {
                    nums[i] = 0;
                }
            }
        }
        return 8 * nums[0] + 4 * nums[1] + 2 * nums[2] + nums[3];

    }

}
