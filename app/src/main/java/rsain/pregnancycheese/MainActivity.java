package rsain.pregnancycheese;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * References to UI components
     */
    private ImageView ivResult;
    private TextView tvResult;
    private ToggleButton toggleButtonPasteurized;
    private Spinner spinnerMilkFat, spinnerMoisture;

    /**
     * Variables
     */
    private double computedRisk;

    /**
     * Method when the apps is launched. We instanciate references to UI components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.textViewResult);
        toggleButtonPasteurized = (ToggleButton) findViewById(R.id.toggleButtonPasteurized);
        spinnerMilkFat = (Spinner) findViewById(R.id.spinnerMilkFat);
        spinnerMoisture = (Spinner) findViewById(R.id.spinnerMoisture);
        ivResult = (ImageView) findViewById(R.id.imageViewResult);
    }

    /**
     * Method before showing the UI to the user. We fill and set up components of the UI.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Elements for the spinners (from 1 to 100, and the symbil "--")
        List<String> spinElements = new LinkedList<String>();
        spinElements.add(getString(R.string.noValue));
        for (int i=1; i <= 100; i++)
        {
            spinElements.add(String.valueOf(i) + "%");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinElements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMilkFat.setAdapter(adapter);
        spinnerMilkFat.setOnItemSelectedListener(this);
        spinnerMoisture.setAdapter(adapter);
        spinnerMoisture.setOnItemSelectedListener(this);

        // Event for the toogle button
        toggleButtonPasteurized.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int milkFat, moisture;

                if (isChecked) {
                    // The toggle is enabled
                    if (!spinnerMilkFat.getSelectedItem().toString().equals(getString(R.string.noValue))) {
                        if (!spinnerMoisture.getSelectedItem().toString().equals(getString(R.string.noValue))) {
                            milkFat = Integer.valueOf(spinnerMilkFat.getSelectedItem().toString().replace("%",""));
                            moisture = Integer.valueOf(spinnerMoisture.getSelectedItem().toString().replace("%",""));
                            computedRisk = computeRisk(milkFat, moisture);

                            if (computedRisk >= getResources().getInteger(R.integer.riskThreshold)) {
                                UIForLowRisk();
                            } else {
                                UIForHighRisk();
                            }
                        } else {
                            UIInitial();
                        }
                    }
                    else {
                        UIInitial();
                    }
                } else {
                    // The toggle is disabled
                    tvResult.setText(R.string.milkMustBePasteurized);
                    ivResult.setImageResource(R.drawable.cross);
                }
            }
        });
    }

    /**
     * Method when the UI is shown to the user
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        UIInitial();
    }

    /**
     * Event for spinners. We compute the risk of the cheese and we inform the user.
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        int milkFat, moisture;

        if (toggleButtonPasteurized.isChecked())
        {
            if (!spinnerMilkFat.getSelectedItem().toString().equals(getString(R.string.noValue))) {
                if (!spinnerMoisture.getSelectedItem().toString().equals(getString(R.string.noValue))) {
                    milkFat = Integer.valueOf(spinnerMilkFat.getSelectedItem().toString().replace("%",""));
                    moisture = Integer.valueOf(spinnerMoisture.getSelectedItem().toString().replace("%",""));
                    computedRisk = computeRisk(milkFat, moisture);

                    if (computedRisk >= getResources().getInteger(R.integer.riskThreshold)) {
                        UIForLowRisk();
                    } else {
                        UIForHighRisk();
                    }
                } else {
                    UIInitial();
                }
            }
            else {
                UIInitial();
            }
        }
        else
        {
            tvResult.setText(R.string.milkMustBePasteurized);
            ivResult.setImageResource(R.drawable.cross);
        }
    }

    /**
     * Function to compute the risk of a cheese
     * @param milkFat Milk fat (in %)
     * @param moisture Moisture (in %)
     * @return A percentage representing the risk of the cheese
     */
    private double computeRisk(double milkFat, double moisture) {
        System.out.println("FDDFDF " + (moisture/(100-milkFat))*100);
        return (moisture/(100-milkFat))*100;
    }

    /**
     * Update the UI to inform the user about the high risk of the chosen cheese
     */
    private void UIForHighRisk() {
        ivResult.setImageResource(R.drawable.cross);
        tvResult.setText(R.string.highRisk);
    }

    /**
     * Update the UI to inform the user about the low risk of the chosen cheese
     */
    private void UIForLowRisk() {
        ivResult.setImageResource(R.drawable.tick);
        tvResult.setText(R.string.lowRisk);
    }

    /**
     * Initial set up for components of the UI
     */
    private void UIInitial() {
        ivResult.setImageResource(R.drawable.info);
        tvResult.setText(R.string.fillFields);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
