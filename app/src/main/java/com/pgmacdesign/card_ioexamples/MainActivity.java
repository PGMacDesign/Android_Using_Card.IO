package com.pgmacdesign.card_ioexamples;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView tv;
    private Context context;
    private Bitmap bitmap;
    ImageView imageView;

    public static int MY_SCAN_REQUEST_CODE = 54553;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.results);

        context = this;

        //Initialize
        bitmap = null;

        imageView = (ImageView) findViewById(R.id.credit_card);
    }

    //For button click or section open to be called
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    //OnActiviityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

                //Full credit card (15-16 digits) number
                if (scanResult.getFormattedCardNumber() != null){
                    Log.d("Formatted Card #", scanResult.getFormattedCardNumber());
                }
                //Type (Visa, Mastercard)
                if (scanResult.getCardType() != null){
                    Log.d("Card Type", scanResult.getCardType().toString());
                }
                //Last four of card
                if (scanResult.getLastFourDigitsOfCardNumber() != null){
                    Log.d("Last 4", scanResult.getLastFourDigitsOfCardNumber());
                }
                //Full credit card WITH dots to block out all but last 4 digits
                if (scanResult.getRedactedCardNumber() != null){
                    Log.d("Redacted Card #", scanResult.getRedactedCardNumber());
                }

                //This is the credit card (IE VISA) logo image (Bitmap)
                if(scanResult.getCardType().imageBitmap(this) != null){
                    bitmap = scanResult.getCardType().imageBitmap(this);
                }

                //This is the credit card (IE VISA) logo text (The word Visa)
                if(scanResult.getCardType().imageBitmap(this) != null){
                    String str = scanResult.getCardType().getDisplayName("en_US");
                    Log.d("TESTING", str);
                }



            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            tv.setText(resultDisplayStr);
            imageView.setImageBitmap(bitmap);
        } else {
            // else handle other activity results
            tv.setText("Error!");
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button1:
                //
                onScanPress(v);
                break;


        }
    }
}
