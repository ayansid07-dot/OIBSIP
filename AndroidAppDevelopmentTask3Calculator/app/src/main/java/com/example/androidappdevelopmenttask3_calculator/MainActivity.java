package com.example.androidappdevelopmenttask3_calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvDisplay;
    StringBuilder expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        expression = new StringBuilder();


        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnAdd, R.id.btnSub, R.id.btnMul,
                R.id.btnDiv, R.id.btnDot, R.id.btnEqual, R.id.btnC, R.id.btnOpen, R.id.btnClose
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();

        if (buttonText.equals("C")) {
            expression.setLength(0);
            tvDisplay.setText("0");
        } else if (buttonText.equals("=")) {
            try {
                double result = evaluate(expression.toString());
                // Remove .0 if it's a whole number
                String resultStr = String.valueOf(result);
                if (resultStr.endsWith(".0")) {
                    resultStr = resultStr.substring(0, resultStr.length() - 2);
                }
                tvDisplay.setText(resultStr);
                expression.setLength(0);
                expression.append(resultStr);
            } catch (Exception e) {
                tvDisplay.setText("Error");
                expression.setLength(0);
            }
        } else {
            expression.append(buttonText);
            tvDisplay.setText(expression.toString());
        }
    }

    // Function to evaluate mathematical expressions
    private double evaluate(String str) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }
}