import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class SatyamCurrencyConverter extends JFrame {

    private JTextField amountInput;
    private JComboBox<String> fromDropdown, toDropdown;
    private JLabel outputLabel;

    private Map<String, Double> rateMap = new HashMap<>();
    private Map<String, String> currencyNames = new LinkedHashMap<>();
    private boolean usingOffline = false;

    private final String API_KEY = "fca_live_cU0ziNJvaNawUqJZJ7J5OlQWMRR2ldn5APYHizUE";
    private final String API_URL = "https://api.freecurrencyapi.com/v1/latest?apikey=" + API_KEY;

    public SatyamCurrencyConverter() {
        setTitle("Satyam Currency Converter");
        setSize(480, 310);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // Title
        JLabel title = new JLabel("Satyam Currency Converter");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(110, 10, 300, 30);
        add(title);

        // Labels and Fields
        createLabel("Amount:", 40, 60);
        amountInput = new JTextField();
        amountInput.setBounds(160, 60, 250, 25);
        add(amountInput);

        createLabel("From Currency:", 40, 100);
        fromDropdown = new JComboBox<>();
        fromDropdown.setBounds(160, 100, 250, 25);
        add(fromDropdown);

        createLabel("To Currency:", 40, 140);
        toDropdown = new JComboBox<>();
        toDropdown.setBounds(160, 140, 250, 25);
        add(toDropdown);

        // Buttons
        JButton convertBtn = new JButton("Convert");
        convertBtn.setBounds(70, 190, 120, 30);
        add(convertBtn);

        JButton switchBtn = new JButton("Swap");
        switchBtn.setBounds(230, 190, 120, 30);
        add(switchBtn);

        // Output
        outputLabel = new JLabel("Enter amount and choose currencies.");
        outputLabel.setBounds(40, 230, 380, 30);
        add(outputLabel);

        // Actions
        convertBtn.addActionListener(e -> convertAmount());
        switchBtn.addActionListener(e -> swapSelection());
        getRootPane().setDefaultButton(convertBtn); // ENTER to convert

        loadCurrencyNames();
        loadRates();

        setVisible(true);
    }

    private void createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        add(label);
    }

    private void loadCurrencyNames() {
        currencyNames.put("INR", "Indian Rupee");
        currencyNames.put("USD", "US Dollar");
        currencyNames.put("EUR", "Euro");
        currencyNames.put("JPY", "Japanese Yen");
        currencyNames.put("GBP", "British Pound");
        currencyNames.put("CAD", "Canadian Dollar");
        currencyNames.put("AUD", "Australian Dollar");
        currencyNames.put("CNY", "Chinese Yuan");
        currencyNames.put("CHF", "Swiss Franc");
    }

    private void loadRates() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();
            String[] items = json.split("\"data\":\\{")[1].split("}")[0].split(",");
            for (String item : items) {
                String[] parts = item.replace("\"", "").split(":");
                String code = parts[0].trim();
                double rate = Double.parseDouble(parts[1].trim());

                if (currencyNames.containsKey(code)) {
                    rateMap.put(code, rate);
                    String label = code + " - " + currencyNames.get(code);
                    fromDropdown.addItem(label);
                    toDropdown.addItem(label);
                }
            }

        } catch (Exception e) {
            usingOffline = true;
            loadOfflineRates();
            outputLabel.setText("Offline Mode: Live rates not available.");
        }
    }

    private void loadOfflineRates() {
        rateMap.put("INR", 83.0);
        rateMap.put("USD", 1.0);
        rateMap.put("EUR", 0.91);
        rateMap.put("JPY", 157.4);
        rateMap.put("GBP", 0.78);
        rateMap.put("CAD", 1.35);
        rateMap.put("AUD", 1.47);
        rateMap.put("CNY", 7.25);
        rateMap.put("CHF", 0.89);

        for (String code : currencyNames.keySet()) {
            if (rateMap.containsKey(code)) {
                String label = code + " - " + currencyNames.get(code);
                fromDropdown.addItem(label);
                toDropdown.addItem(label);
            }
        }
    }

    private void convertAmount() {
        String fromFull = (String) fromDropdown.getSelectedItem();
        String toFull = (String) toDropdown.getSelectedItem();
        String textAmount = amountInput.getText().trim();

        if (fromFull == null || toFull == null || textAmount.isEmpty()) {
            outputLabel.setText("All fields must be filled.");
            return;
        }

        String from = fromFull.split(" - ")[0];
        String to = toFull.split(" - ")[0];

        try {
            double amount = Double.parseDouble(textAmount);
            if (amount <= 0) {
                outputLabel.setText("Amount must be more than 0.");
                return;
            }

            double fromRate = rateMap.getOrDefault(from, 0.0);
            double toRate = rateMap.getOrDefault(to, 0.0);

            if (fromRate == 0 || toRate == 0) {
                outputLabel.setText("Missing exchange rate.");
                return;
            }

            double result = (toRate / fromRate) * amount;
            outputLabel.setText(String.format("%.2f %s = %.2f %s", amount, from, result, to));
        } catch (NumberFormatException e) {
            outputLabel.setText("Enter a valid number.");
        }
    }

    private void swapSelection() {
        int fromIndex = fromDropdown.getSelectedIndex();
        int toIndex = toDropdown.getSelectedIndex();
        fromDropdown.setSelectedIndex(toIndex);
        toDropdown.setSelectedIndex(fromIndex);
    }

    public static void main(String[] args) {
        new SatyamCurrencyConverter();
    }
}
