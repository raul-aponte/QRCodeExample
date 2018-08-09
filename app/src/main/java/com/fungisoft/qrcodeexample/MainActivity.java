package com.fungisoft.qrcodeexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import net.glxn.qrgen.android.QRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_generate_qrgen)
    public void generateQr() {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        Bitmap bitmap = QRCode.from(text)
                .withSize(400,400)
                .bitmap();
        imageView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.button_generate_embedded)
    public void generateEmbedded() {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            showMessage("Error generating QR");
        }
        imageView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.button_read)
    public void readQr() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                showMessage("Cancelled");
            } else {
                showMessage("Scanned: " + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
