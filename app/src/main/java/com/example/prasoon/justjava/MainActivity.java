package com.example.prasoon.justjava;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.checkbox;
import static android.R.id.message;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity = 0;
    int coffeePrice = 5;
    int whippedCreamPrice = 3;
    int chocolateToppingPrice = 2;
    Editable name;

    /**
     * This method is called when the order button is clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void submitOrder(View view) {

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whippedCream_checkBox);
        if (whippedCreamCheckBox.isChecked() && quantity == 0) {
            Toast.makeText(this, "Cant' add WhippedCream topping on the air!! Select atleast one coffee..", Toast.LENGTH_LONG).show();
            whippedCreamCheckBox.setChecked(false);
        }
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.Chocolate_checkBox);
        if (chocolateCheckBox.isChecked() && quantity == 0) {
            Toast.makeText(this, "Cant' add Chocolate topping on the air!! Select atleast one coffee..", Toast.LENGTH_LONG).show();
            chocolateCheckBox.setChecked(false);
        }
        boolean hasChocolateTopping = chocolateCheckBox.isChecked();
        boolean hasWhippedCreamTopping = whippedCreamCheckBox.isChecked();
        Log.v("MainActivity", "Has Whipped Cream: " + hasWhippedCreamTopping);
        Log.v("MainActivity", "Has Chocolate Topping: " + hasChocolateTopping);
        EditText customerName = (EditText) findViewById(R.id.name_editText);
        name = customerName.getText();
        displayOrderSummary(createOrderSummary(quantity, hasWhippedCreamTopping, hasChocolateTopping));


    }


    public void sendBillEmail(View view){
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whippedCream_checkBox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.Chocolate_checkBox);
        String subject = "Coffee order";
        String to = "pbirla29@gmail.com";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mail:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, createOrderSummary(quantity, whippedCreamCheckBox.isChecked(), chocolateCheckBox.isChecked()));

        email.setType("message/rfc822");
        if (email.resolveActivity(getPackageManager()) != null)
            startActivity(email);
    }

    public void incrementNumberOfCoffees(View view) {
        ++quantity;
        display(quantity);
    }

    public void decrementNumberOfCoffees(View view) {
        if (quantity == 0)
            Toast.makeText(this, "Atleast one coffee needede for the order..", Toast.LENGTH_LONG).show();
        if (quantity > 0)
            --quantity;
        display(quantity);

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_textView);
        quantityTextView.setText(String.valueOf(number));
    }


    /**
     * This method displays the given text on the screen.
     */
    private void displayOrderSummary(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.orderSummary_textView);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method displays the given orderSummary on the screen.
     */
    private int calculatePrice(int quantity, boolean hasWhippedCreamTopping, boolean hasChocolateTopping) {
        int price = quantity * coffeePrice;
        if (hasChocolateTopping)
            price += quantity * chocolateToppingPrice;
        if (hasWhippedCreamTopping)
            price += quantity * whippedCreamPrice;
        return price;
    }

    private String createOrderSummary(int quantity, boolean hasWhippedCream, boolean hasChocolate) {

        String OrderSummary = "Name: " + name;
        OrderSummary += "\nQunatity: " + quantity;
        OrderSummary += "\n Coffee Price :" + java.text.NumberFormat.getCurrencyInstance().format(coffeePrice);


        if (hasWhippedCream) {
            OrderSummary += "\nChocolate Topping : Yes";
            OrderSummary += "\nWhippedCream Topping price :" + java.text.NumberFormat.getCurrencyInstance().format(whippedCreamPrice);
        } else
            OrderSummary += "\nWhippedCream : No";

        if (hasChocolate) {
            OrderSummary += "\nChocolate Topping : Yes";
            OrderSummary += "\nChocolate Topping price : " + java.text.NumberFormat.getCurrencyInstance().format(chocolateToppingPrice);
        } else
            OrderSummary += "\nChocolate Topping : No";
        OrderSummary += "\nTotal: " + java.text.NumberFormat.getCurrencyInstance().format(calculatePrice(quantity, hasWhippedCream, hasChocolate));
        OrderSummary += "\nThank You!";

        return OrderSummary;
    }
}
