package vinnsla;

import javafx.fxml.FXML;

public class Floskur {

    private int dosir;
    private int floskur;
    public CurrencyConverter currencyConverter;
    private String selectedCurrency = "ISK"; // Default currency
    private int iskGreidaValue = 0; // ISK-Wert für Greida
    // Neue Felder für die ursprünglichen ISK-Werte
    private int iskDosirValue = 0;
    private int iskFloskurValue = 0;

    private static final int VALUE_DOSIR = 20;
    private static final int VALUE_FLOSKUR = 25;

    public Floskur() {
        // Initialize the converter
        this.currencyConverter = new CurrencyConverter();
    }

    /**
     * Sets the selected currency.
     *
     * @param currency the currency code (ISK, EUR, USD, etc.)
     */
    public void setSelectedCurrency(String currency) {
        this.selectedCurrency = currency;
    }

    /**
     * Returns the currently selected currency.
     *
     * @return the selected currency code.
     */
    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    /**
     * Speichert den ISK-Wert für Greida.
     *
     * @param value der ISK-Wert
     */
    public void setISKGreidaValue(int value) {
        this.iskGreidaValue = value;
    }

    /**
     * Gibt den gespeicherten ISK-Wert für Greida zurück.
     *
     * @return der gespeicherte ISK-Wert
     */
    public int getISKGreidaValue() {
        return iskGreidaValue;
    }

    /**
     * Gibt den gespeicherten ISK-Wert für Dósir zurück.
     *
     * @return der gespeicherte ISK-Wert für Dósir
     */
    public int getISKDosirValue() {
        return iskDosirValue;
    }

    /**
     * Speichert den ISK-Wert für Dósir.
     *
     * @param value der ISK-Wert für Dósir
     */
    public void setISKDosirValue(int value) {
        this.iskDosirValue = value;
    }

    /**
     * Gibt den gespeicherten ISK-Wert für Flöskur zurück.
     *
     * @return der gespeicherte ISK-Wert für Flöskur
     */
    public int getISKFloskurValue() {
        return iskFloskurValue;
    }

    /**
     * Speichert den ISK-Wert für Flöskur.
     *
     * @param value der ISK-Wert für Flöskur
     */
    public void setISKFloskurValue(int value) {
        this.iskFloskurValue = value;
    }

    /**
     * Reads in the amount of cans and sets it.
     *
     * @param dosir amount of cans.
     */
    public void setAmountDosir(int dosir) {
        this.dosir = dosir;
        // Aktualisiere auch gleich den ISK-Wert
        this.iskDosirValue = dosir * VALUE_DOSIR;
    }

    /**
     * Calculates the value in ISK of the returned cans.
     *
     * @return value of cans in ISK.
     */
    public int getISKDosir() {
        return dosir * VALUE_DOSIR;
    }

    /**
     * Reads in and sets the amount of bottles.
     *
     * @param floskur amount of bottles.
     */
    public void setAmountFloskur(int floskur) {
        this.floskur = floskur;
        // Aktualisiere auch gleich den ISK-Wert
        this.iskFloskurValue = floskur * VALUE_FLOSKUR;
    }

    /**
     * Calculates the value in ISK of the bottles.
     *
     * @return value of bottles in ISK.
     */
    public int getISKFloskur() {
        return floskur * VALUE_FLOSKUR;
    }

    /**
     * Calculates the total count of cans and bottles.
     *
     * @return combined count of cans and bottles.
     */
    public int getAmountSamtals() {
        return dosir + floskur;
    }

    /**
     * Calculates the total ISK value of the cans and bottles.
     *
     * @return total ISK value.
     */
    public int getISKSamtals() {
        return getISKDosir() + getISKFloskur();
    }

    /**
     * Calculates the total value in the selected currency. If the selected currency is ISK,
     * it returns the total ISK value directly. Otherwise, it converts the ISK value.
     *
     * @return total amount in the selected currency or -1 on error.
     */
    public double getValueInSelectedCurrency() {
        // If the selected currency is ISK, return the total ISK value.
        if ("ISK".equalsIgnoreCase(selectedCurrency)) {
            return getISKSamtals();
        }

        try {
            // Uses the CurrencyConverter class to convert ISK to the desired currency.
            return currencyConverter.convert(getISKSamtals(), selectedCurrency);
        } catch (Exception e) {
            System.err.println("Fehler bei der Währungsumrechnung: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Returns the currency symbol (or code) for display.
     *
     * @return currency code (ISK, EUR, USD, etc.)
     */
    public String getCurrencySymbol() {
        return selectedCurrency;
    }

    /**
     * Calculates a new count by adding the current number of cans and bottles
     * to an old amount.
     *
     * @param oldAmount the previous total before adding.
     * @return new total count.
     */
    public int getAmountGreida(int oldAmount) {
        return oldAmount + getAmountSamtals();
    }

    /**
     * Calculates the new total ISK value by adding the current ISK value
     * to an old ISK amount.
     *
     * @param oldISK the previous ISK value.
     * @return new total ISK value.
     */
    public int getISKGreida(int oldISK) {
        return oldISK + getISKSamtals();
    }

    /**
     * Resets the amount of cans and bottles.
     */
    @FXML
    public void hreinsa() {
        this.setAmountDosir(0);
        this.setAmountFloskur(0);
        // Auch die ISK-Werte zurücksetzen
        this.iskDosirValue = 0;
        this.iskFloskurValue = 0;
    }
}