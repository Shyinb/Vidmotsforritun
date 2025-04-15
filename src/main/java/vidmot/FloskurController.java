package vidmot;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import vinnsla.Floskur;

import java.io.IOException;
import java.text.DecimalFormat;

public class FloskurController {

    private Floskur floskur = new Floskur();
    private DecimalFormat df = new DecimalFormat("#.##");

    // Text Fields
    @FXML
    private TextField amountFloskur;

    @FXML
    private TextField amountDosir;

    // Labels
    @FXML
    private Label ISKDosir;

    @FXML
    private Label ISKFloskur;

    @FXML
    private Label amountSamtals;

    @FXML
    private Label ISKSamtals;

    @FXML
    private Label amountGreida;

    @FXML
    private Label ISKGreida;

    @FXML
    private ComboBox<String> currencyComboBox;

    @FXML
    private Label exchangeRateLabel;

    @FXML
    public void initialize() {

        currencyComboBox.setItems(FXCollections.observableArrayList("ISK", "EUR", "USD"));
        currencyComboBox.setValue("ISK");

        updateCurrencyLabels();
        updateSamtals();
        updateExchangeRateLabel();
        updateGreidaLabel();
    }

    // Event Handlers

    @FXML
    private void onAmountFloskur(ActionEvent event) {
        inputAmount(amountFloskur, false);
    }

    @FXML
    private void onAmountDosir(ActionEvent event) {
        inputAmount(amountDosir, true);
    }

    @FXML
    private void onGreida(ActionEvent event) {
        int oldAmountGreida = Integer.parseInt(amountGreida.getText().isEmpty() ? "0" : amountGreida.getText());

        int oldISKGreida = floskur.getISKGreidaValue();

        int newAmountGreida = floskur.getAmountGreida(oldAmountGreida);
        int newISKGreida = floskur.getISKGreida(oldISKGreida);

        floskur.setISKGreidaValue(newISKGreida);

        amountGreida.setText(String.valueOf(newAmountGreida));

        String currencySymbol = floskur.getSelectedCurrency();
        if ("ISK".equalsIgnoreCase(currencySymbol)) {
            ISKGreida.setText(newISKGreida + " " + currencySymbol);
        } else {
            try {
                double convertedAmount = floskur.currencyConverter.convert(newISKGreida, currencySymbol);
                ISKGreida.setText(df.format(convertedAmount) + " " + currencySymbol);
            } catch (Exception e) {
                ISKGreida.setText(newISKGreida + " ISK");
            }
        }

        amountDosir.setText("");
        amountFloskur.setText("");
        ISKDosir.setText("0 " + currencySymbol);
        ISKFloskur.setText("0 " + currencySymbol);
        amountSamtals.setText("0");
        ISKSamtals.setText("0 " + currencySymbol);

        floskur.hreinsa();
    }

    @FXML
    private void onHreinsa(ActionEvent event) {
        String currencySymbol = floskur.getSelectedCurrency();

        amountDosir.setText("");
        amountFloskur.setText("");
        ISKDosir.setText("0 " + currencySymbol);
        ISKFloskur.setText("0 " + currencySymbol);
        amountSamtals.setText("0");
        ISKSamtals.setText("0 " + currencySymbol);

        floskur.hreinsa();
    }

    @FXML
    private void onTyping(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String input = textField.getText().trim();

        if (input.isEmpty()) {
            textField.getStyleClass().removeAll("input-valid", "input-invalid");
        } else {
            try {
                int amount = Integer.parseInt(input);
                if (amount >= 0) {
                    textField.getStyleClass().removeAll("input-invalid");
                    textField.getStyleClass().add("input-valid");
                } else {
                    throw new NumberFormatException("Negative number");
                }
            } catch (NumberFormatException e) {
                textField.getStyleClass().removeAll("input-valid");
                textField.getStyleClass().add("input-invalid");
            }
        }
    }

    @FXML
    private void onCurrencyChanged(ActionEvent event) {
        String selectedCurrency = currencyComboBox.getValue();
        floskur.setSelectedCurrency(selectedCurrency);

        updateCurrencyLabels();
        updateSamtals();
        updateExchangeRateLabel();
        updateGreidaLabel();
    }

    /**
     * Updates the labels ISKDosir, ISKFloskur, ISKSamtals.
     * Uses the saved values for conversions
     */
    private void updateCurrencyLabels() {
        String currencySymbol = floskur.getSelectedCurrency();


        int iskDosirValue = floskur.getISKDosirValue();
        if (iskDosirValue == 0) {
            ISKDosir.setText("0 " + currencySymbol);
        } else {
            try {
                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKDosir.setText(iskDosirValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskDosirValue, currencySymbol);
                    ISKDosir.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            } catch (IOException e) {
                ISKDosir.setText(iskDosirValue + " ISK");
            }
        }


        int iskFloskurValue = floskur.getISKFloskurValue();
        if (iskFloskurValue == 0) {
            ISKFloskur.setText("0 " + currencySymbol);
        } else {
            try {
                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKFloskur.setText(iskFloskurValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskFloskurValue, currencySymbol);
                    ISKFloskur.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            } catch (IOException e) {
                ISKFloskur.setText(iskFloskurValue + " ISK");
            }
        }


        int iskSamtalsValue = floskur.getISKSamtals();
        if (iskSamtalsValue == 0) {
            ISKSamtals.setText("0 " + currencySymbol);
        } else {
            try {
                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKSamtals.setText(iskSamtalsValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskSamtalsValue, currencySymbol);
                    ISKSamtals.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            } catch (IOException e) {
                ISKSamtals.setText(iskSamtalsValue + " ISK");
            }
        }
    }

    /**
     * updates ISKGreida
     */
    private void updateGreidaLabel() {
        String currencySymbol = floskur.getSelectedCurrency();
        if (ISKGreida.getText() == null || ISKGreida.getText().trim().isEmpty() ||
                ISKGreida.getText().replaceAll("[^0-9]", "").trim().isEmpty() ||
                "0".equals(ISKGreida.getText().replaceAll("[^0-9]", "").trim())) {
            ISKGreida.setText("0 " + currencySymbol);
        } else {

            try {

                int iskValue = floskur.getISKGreidaValue();

                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKGreida.setText(iskValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskValue, currencySymbol);
                    ISKGreida.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            } catch (IOException e) {
                ISKGreida.setText("0 " + currencySymbol);
            }
        }
    }

    /**
     *
     *
     * @param label          label that is getting updated
     * @param currencySymbol new currency
     */
    private void updateLabelWithCurrency(Label label, String currencySymbol) {
        String currentText = label.getText();
        String numericPart = currentText.replaceAll("[^0-9]", "").trim();

        try {
            int value = Integer.parseInt(numericPart);
            if ("ISK".equalsIgnoreCase(currencySymbol)) {
                label.setText(value + " " + currencySymbol);
            } else {
                double convertedValue = floskur.currencyConverter.convert(value, currencySymbol);
                label.setText(df.format(convertedValue) + " " + currencySymbol);
            }
        } catch (NumberFormatException e) {
            label.setText("0 " + currencySymbol);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates ISKSamtals
     */
    private void updateSamtals() {
        int totalAmount = floskur.getAmountSamtals();
        amountSamtals.setText(String.valueOf(totalAmount));

        String currencySymbol = floskur.getSelectedCurrency();
        if ("ISK".equalsIgnoreCase(currencySymbol)) {
            int totalISK = floskur.getISKSamtals();
            ISKSamtals.setText(totalISK + " " + currencySymbol);
        } else {
            try {
                double convertedAmount = floskur.getValueInSelectedCurrency();
                ISKSamtals.setText(df.format(convertedAmount) + " " + currencySymbol);
            } catch (Exception e) {
                ISKSamtals.setText("Error");
                System.err.println("Error with conversion: " + e.getMessage());
            }
        }
    }


    private void updateExchangeRateLabel() {
        String selectedCurrency = floskur.getSelectedCurrency();

        if ("ISK".equalsIgnoreCase(selectedCurrency)) {
            exchangeRateLabel.setText("");
            return;
        }

        try {
            double rate = floskur.currencyConverter.getExchangeRate(selectedCurrency);
            exchangeRateLabel.setText("1 " + selectedCurrency + " = " + df.format(rate) + " ISK");
        } catch (Exception e) {
            exchangeRateLabel.setText("Error with conversion");
        }
    }
    /**
     * Reads and tests if the user input is valid
     *
     * @param textField user input
     * @param isDosir   true if input is for dosir, false for Floskur
     */
    private void inputAmount(TextField textField, boolean isDosir) {
        String input = textField.getText().trim();

        try {
            int amount = Integer.parseInt(input);

            if (amount < 0) {
                throw new NumberFormatException("Negative number");
            }

            String currencySymbol = floskur.getSelectedCurrency();

            if (isDosir) {
                floskur.setAmountDosir(amount);
                int iskValue = floskur.getISKDosir();

                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKDosir.setText(iskValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskValue, currencySymbol);
                    ISKDosir.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            } else {
                floskur.setAmountFloskur(amount);
                int iskValue = floskur.getISKFloskur();

                if ("ISK".equalsIgnoreCase(currencySymbol)) {
                    ISKFloskur.setText(iskValue + " " + currencySymbol);
                } else {
                    double convertedAmount = floskur.currencyConverter.convert(iskValue, currencySymbol);
                    ISKFloskur.setText(df.format(convertedAmount) + " " + currencySymbol);
                }
            }
            updateSamtals();
        } catch (NumberFormatException | IOException e) {
            System.err.println("Invalid input: " + textField.getText());
        }
    }
}
