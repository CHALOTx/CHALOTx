import java.lang.*;
import java.util.*;
import java.io.*;

public class chalotT {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int choice;
        String cmd;
        String input;
        String[] menu = {"\n\t\t====MENU====" , "\t [01] Search IP", "\t [02] Image Info", "\t [03] Scan Web", "\t [04] Nmap", "\t [05] Phone Tool", "\t [06] Email Tool\n"};

        for (int i = 0; i < menu.length; i++) {
            System.out.println(menu[i]);
        }

        while (true) {
            System.out.print("select>> ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 01 || choice == 1) {
                System.out.println(">>> Search IP...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "curl http://ip-api.com/json/" + input;
                run(cmd);
            } else if (choice == 02 || choice == 2) {
                System.out.println(">>> Image Info...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "exiftool " + input;
                run(cmd);
            } else if (choice == 03 || choice == 3) {
                System.out.println(">>> Scan Web...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "whois " + input;
                run(cmd);
            } else if (choice == 04 || choice == 4) {
                System.out.println(">>> Nmap Scanning...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "nmap -A " + input;
                run(cmd);
            } else if (choice == 05 || choice == 5) {
                System.out.println(">>> Phone Tool...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "curl --request GET 'https://api.apilayer.com/number_verification/validate?number=" + input +  "' --header 'apikey: bxwD1Bbj65h3e40ve0dNGuU0HNaSPqBr'";
                run(cmd);
            } else if (choice == 06 || choice == 6) {
                System.out.println(">>> Email Tool...");
                System.out.print("target>>> ");
                input = sc.nextLine();
                cmd = "curl --request GET 'https://api.apilayer.com/email_verification/" + input + "' --header 'apikey: bxwD1Bbj65h3e40ve0dNGuU0HNaSPqBr'";
                run(cmd);
            }
        }
    }
    static void run(String cmd) {
        List<String> cmdRun = new ArrayList<>(List.of("bash", "-c", cmd.trim()));
        try {
            new ProcessBuilder(cmdRun).directory(new File("$HOME")).inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("[!] Error cannot run." + e.getMessage());
        }
    }
}
