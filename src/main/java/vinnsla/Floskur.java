package vinnsla;

import javafx.fxml.FXML;

public class Floskur {

    private int dosir;
    private int floskur;
    public CurrencyConverter currencyConverter;
    private String selectedCurrency = "ISK";
    private int iskGreidaValue = 0;
    private int iskDosirValue = 0;
    private int iskFloskurValue = 0;

    private static final int VALUE_DOSIR = 20;
    private static final int VALUE_FLOSKUR = 25;

    public Floskur() {
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
     * Saves ISK-Value for Greida.
     *
     * @param value  ISK-Value
     */
    public void setISKGreidaValue(int value) {
        this.iskGreidaValue = value;
    }

    /**
     *
     *
     * @return saved ISK-Value
     */
    public int getISKGreidaValue() {
        return iskGreidaValue;
    }

    /**
     *
     *
     * @return saved ISK-Value for Dósir
     */
    public int getISKDosirValue() {
        return iskDosirValue;
    }

    /**
     *
     *
     * @param value  ISK-Value for Dósir
     */
    public void setISKDosirValue(int value) {
        this.iskDosirValue = value;
    }

    /**
     *
     *
     * @return saved ISK-Values for Flöskur
     */
    public int getISKFloskurValue() {
        return iskFloskurValue;
    }

    /**
     *
     *
     * @param value ISK-Value for Flöskur
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
        if ("ISK".equalsIgnoreCase(selectedCurrency)) {
            return getISKSamtals();
        }

        try {
            return currencyConverter.convert(getISKSamtals(), selectedCurrency);
        } catch (Exception e) {
            System.err.println("Error with conversion: " + e.getMessage());
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
        this.iskDosirValue = 0;
        this.iskFloskurValue = 0;
    }
}