package com.example.spendingactivitymgmt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnPlus,btnMinus,btnClr;
    EditText dateLine,amtLine,infoLine;
    TextView blnLine,noteContent;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateLine = findViewById(R.id.dateID);
        amtLine = findViewById(R.id.amtID);
        infoLine = findViewById(R.id.infoID);
        noteContent = findViewById(R.id.txtContent);
        blnLine = findViewById(R.id.blnID);

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnClr = findViewById(R.id.clrID);

        sh = getPreferences(Context.MODE_PRIVATE);
        String body = sh.getString("note",null);
        String top = sh.getString("balance",null);
        if (top != null) blnLine.setText(top);
        noteContent.setText(body);


    }
    @Override
    protected void onStart() {
        super.onStart();

        // Add money spent on minus click
        btnMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String date = dateLine.getText().toString();
                String amt = amtLine.getText().toString();
                String info = infoLine.getText().toString();
                String balanceTxt = blnLine.getText().toString();
                double bal = Double.parseDouble(balanceTxt.substring(balanceTxt.indexOf("$") + 1));

                Log.i("info", "The value of bal is " + bal);
                String line = "Spent $" + amt + " on " + date + " for " + info;
                if((date.length() == 0) || (amt.length() == 0) || (info.length() == 0)) {
                    Log.e("error", "Missing content in either 'date', 'amt', or 'info'");
                    return;
                }
                if (bal == 0) {
                    Toast.makeText(v.getContext(), "Balance too low", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = noteContent.getText().toString();
                Log.i("info","The content of body is: " + content);

                if (content.length() == 0) {
                    content = line;
                }
                else {
                    content = content + "\n" + line;
                }

                bal = bal - Double.parseDouble(amt);
                String balance = "Current Balance: $" + String.format("%.2f",bal);

                SharedPreferences.Editor editor = sh.edit();
                editor.putString("note",content);
                editor.putString("balance",balance);
                editor.commit();

                dateLine.getText().clear();
                amtLine.getText().clear();
                infoLine.getText().clear();

                noteContent.setText(content);
                blnLine.setText(balance);
            }
        });

        // Add balance on Plus click
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateLine.getText().toString();
                String amt = amtLine.getText().toString();
                String info = infoLine.getText().toString();
                String balanceTxt = blnLine.getText().toString();
                double bal = Double.parseDouble(balanceTxt.substring(balanceTxt.indexOf("$") + 1));

                String line = "Added $" + amt + " on " + date + " from " + info;
                if((date.length() == 0) || (amt.length() == 0) || (info.length() == 0)) {
                    Log.e("error", "Missing content in either 'date', 'amt', or 'info'");
                    return;
                }

                String content = noteContent.getText().toString();
                Log.i("info","The content of body is: " + content);

                if (content.length() == 0) {
                    content = line;
                }
                else {
                    content = content + "\n" + line;
                }

                bal = bal + Double.parseDouble(amt);
                String balance = "Current Balance: $" + String.format("%.2f",bal);

                SharedPreferences.Editor editor = sh.edit();
                editor.putString("note",content);
                editor.putString("balance",balance);
                editor.commit();

                dateLine.getText().clear();
                amtLine.getText().clear();
                infoLine.getText().clear();

                noteContent.setText(content);
                blnLine.setText(balance);
            }
        });

        btnClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteContent.setText("");
                blnLine.setText("Current Balance: $0.00");
                SharedPreferences.Editor editor = sh.edit();
                editor.remove("note");
                editor.remove("balance");
                editor.commit();
                Toast toast = Toast.makeText(v.getContext(), "Data Cleared", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

    }
}
