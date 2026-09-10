package com.example.androidappdevelopment_task1_unitconverter; // Replace with your actual package name

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSource, spinnerTarget;
    private EditText editTextInput;
    private TextView textViewResult;
    private Button buttonConvert;

    // Arrays for Spinners
    private final String[] categories = {"Length", "Weight", "Temperature"};
    private final String[] lengthUnits = {"Meters", "Centimeters", "Inches"};
    private final String[] weightUnits = {"Kilograms", "Grams", "Pounds"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSource = findViewById(R.id.spinnerSourceUnit);
        spinnerTarget = findViewById(R.id.spinnerTargetUnit);
        editTextInput = findViewById(R.id.editTextInput);
        textViewResult = findViewById(R.id.textViewResult);
        buttonConvert = findViewById(R.id.buttonConvert);

        // Setup Category Spinner
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        // Change Unit Spinners based on Category selection
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle Convert Button Click
        buttonConvert.setOnClickListener(v -> performConversion());
    }

    private void updateUnitSpinners(int categoryIndex) {
        String[] selectedUnits;
        switch (categoryIndex) {
            case 1: selectedUnits = weightUnits; break;
            case 2: selectedUnits = tempUnits; break;
            case 0:
            default: selectedUnits = lengthUnits; break;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedUnits);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(unitAdapter);
        spinnerTarget.setAdapter(unitAdapter);
    }

    private void performConversion() {
        String inputStr = editTextInput.getText().toString().trim();

        // Input Validation
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        String sourceUnit = spinnerSource.getSelectedItem().toString();
        String targetUnit = spinnerTarget.getSelectedItem().toString();

        if (sourceUnit.equals(targetUnit)) {
            textViewResult.setText(String.format("%s %s", inputValue, targetUnit));
            return;
        }

        double result = 0.0;

        // Routing to the correct conversion logic
        if (category.equals("Length")) {
            result = convertLength(inputValue, sourceUnit, targetUnit);
        } else if (category.equals("Weight")) {
            result = convertWeight(inputValue, sourceUnit, targetUnit);
        } else if (category.equals("Temperature")) {
            result = convertTemperature(inputValue, sourceUnit, targetUnit);
        }

        // Display Result formatted to 4 decimal places
        textViewResult.setText(String.format("%.4f %s", result, targetUnit));
    }

    private double convertLength(double val, String from, String to) {
        // Convert to base unit (Meters) first
        double meters = val;
        if (from.equals("Centimeters")) meters = val / 100.0;
        else if (from.equals("Inches")) meters = val * 0.0254;

        // Convert from base unit (Meters) to target
        if (to.equals("Centimeters")) return meters * 100.0;
        if (to.equals("Inches")) return meters / 0.0254;
        return meters;
    }

    private double convertWeight(double val, String from, String to) {
        // Convert to base unit (Kilograms) first
        double kgs = val;
        if (from.equals("Grams")) kgs = val / 1000.0;
        else if (from.equals("Pounds")) kgs = val * 0.453592;

        // Convert from base unit to target
        if (to.equals("Grams")) return kgs * 1000.0;
        if (to.equals("Pounds")) return kgs / 0.453592;
        return kgs;
    }

    private double convertTemperature(double val, String from, String to) {
        // Convert to base unit (Celsius) first
        double celsius = val;
        if (from.equals("Fahrenheit")) celsius = (val - 32) * 5 / 9;
        else if (from.equals("Kelvin")) celsius = val - 273.15;

        // Convert from base unit to target
        if (to.equals("Fahrenheit")) return (celsius * 9 / 5) + 32;
        if (to.equals("Kelvin")) return celsius + 273.15;
        return celsius;
    }
}