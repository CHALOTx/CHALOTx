import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;

public class chalotx {
    static JTextArea outputArea;
    public static void main(String[] args) {
        //Windows display
        JFrame frame = new JFrame();
        frame.setTitle("CHALOTx");
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Layout Display
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Display Output
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        //New Row Combobox
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] choice = {"Search IP", "Image Info", "Scan Web", "Nmap", "Phone Tool", "Email Tool"};
        JComboBox<String> combo = new JComboBox<>(choice);

        JTextField inputField = new JTextField(20);
        JButton runButton = new JButton("Search");

        runButton.addActionListener(e -> {
            String select = (String) combo.getSelectedItem();
            String input = inputField.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Input cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                String tor = "sudo systemctl start tor";
                run(tor);
            }

            //Clear 
            outputArea.setText("");
            String cmd = "";

            switch (select) {
                case "Search IP":
                cmd = "torsocks curl http://ip-api.com/json/" + input;
                break;
                case "Image Info":
                cmd = "exiftool " + input;
                break;
                case "Scan Web":
                cmd = "torsocks dnsenum --enum --threads 20 " + input;
                break;
                case "Nmap":
                cmd = "torsocks nmap -A " + input;
                break;
                case "Phone Tool":
                cmd = "torsocks curl --request GET 'https://api.apilayer.com/number_verification/validate?number=" + input +  "' --header 'apikey: bxwD1Bbj65h3e40ve0dNGuU0HNaSPqBr'";
                break;
                case "Email Tool":
                cmd = "torsocks curl --request GET 'https://api.apilayer.com/email_verification/" + input + "' --header 'apikey: bxwD1Bbj65h3e40ve0dNGuU0HNaSPqBr'";
                break;
            }
            outputArea.append(">>>" + cmd + "\n");
            run(cmd);
        });

        panel.add(createFormRow("CHALOTx"));
        row.add(combo);
        row.add(inputField);
        row.add(runButton);

        //Display Rows
        /*panel.add(createFormRow("TARGET IP : ", "Search", e -> {
            String ip = ((JTextField) e.getSource()).getText();
            String cmd1 = "curl http://ip-api.com/json/" + ip;
            run(cmd1);
        }));
        panel.add(createFormRow("PATH TO IMAGE : ", "Search", e -> {
            String path = ((JTextField) e.getSource()).getText();
            String cmd2 = "exiftool " + path;
            run(cmd2);
        }));
        panel.add(createFormRow("Scan WebSite : ", "Search", e -> {
            String web = ((JTextField) e.getSource()).getText();
            String cmd3 = "dnsenum -enum " + web;
            run(cmd3);
        }));*/

        //Display Set Text
        panel.add(row);
        
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    //Methods Rows input
    public static JPanel createFormRow(String labelText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        //JTextField text = new JTextField(20);
        //JButton button = new JButton(buttonText);

       // button.addActionListener(e -> action.actionPerformed(new ActionEvent(text, ActionEvent.ACTION_PERFORMED, text.getText())));

        row.add(label);
        //row.add(text);
        //row.add(button);

        return row;
    }

    //Method Run Command
    public static void run(String cmd) {
        List<String> cmdO = new ArrayList<>(List.of("bash", "-c", cmd.trim()));
        try {
            Process process = new ProcessBuilder(cmdO).directory(new File("/home")).redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
            process.waitFor();
        } catch (Exception e) {
            e.getMessage();
            System.out.println();
        }
    }
}
