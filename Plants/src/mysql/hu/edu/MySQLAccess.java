package mysql.hu.edu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MySQLAccess {
    public static void main(String[] args) throws Exception {
        PlantDAO dao = new PlantDAO();
//        dao.readDataBase();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String name;
        String description;
        Double height;
        String cont = "Y";

        while (cont.toUpperCase().equals("YES") || cont.toUpperCase().equals("Y")) {
            System.out.println("Enter a plant name:");
            name = in.readLine();

            System.out.println("Enter a description for the plant:");
            description = in.readLine();

            System.out.println("Enter the average height for the plant:");
            height = Double.parseDouble(in.readLine());

            dao.writeRow(name, description, height);

            System.out.println("Do you want to enter another plant?");
            cont = in.readLine();
        }

        dao.readDataBase();
    }

} 