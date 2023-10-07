import java.util.Scanner;
public class Menu {
    public static int menu (){
        Scanner input = new Scanner(System.in);
        int option;
        System.out.println("SELECCIONE LA BANDERA QUE QUIERE IMPRIMIR:" +
                "\n 1. BANDERA DE COLOMBIA" +
                "\n 2. BANDERA DE VENEZUELA" +
                "\n 3. BANDERA DE POLONIA" +
                "\n 4. BANDERA DE PANAMA" +
                "\n 5. BANDERA DE CHILE" +
                "\n 6. BANDERA DE USA" +
                "\n 7. BANDERA DE REPUBLICA CHECA" +
                "\n 8. BANDERA DE DINAMARCA" +
                "\n 9. BANDERA DE FINLANDIA" +
                "\n 10.BANDERA DE NORUEGA" +
                "\n 11. BANDERA DE SUDAN DEL SUR" +
                "\n 12. BANDERA DE SUDAFRICA" +
                "\n 13. BANDERA DE SUIZA");
        option = input.nextInt();
        return option;
    }
}
