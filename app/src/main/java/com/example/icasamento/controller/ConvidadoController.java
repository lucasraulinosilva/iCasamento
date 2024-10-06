package com.example.icasamento.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.icasamento.R;
import com.example.icasamento.view.LoginConvidado;
import com.example.icasamento.model.Convidado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ConvidadoController extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://testando-firebase-android-default-rtdb.firebaseio.com/").getReference();
    DrawerLayout drawerLayout;
    Button clickBtn;
    String nomeLogin;
    String cpfLogin;
    ImageView qrCode;

    public ConvidadoController() {

    }

    public ConvidadoController(DrawerLayout drawerLayout, Button clickBtn, String cpfLogin, String nomeLogin, ImageView qrCode) {
        this.drawerLayout = drawerLayout;
        this.clickBtn = clickBtn;
        this.cpfLogin = cpfLogin;
        this.nomeLogin = nomeLogin;
        this.qrCode = qrCode;
    }

    public ImageView getQrCode() {
        return qrCode;
    }

    public void setQrCode(ImageView qrCode) {
        this.qrCode = qrCode;
    }

    public String getCpfLogin() {
        return cpfLogin;
    }

    public void setCpfLogin(String cpfLogin) {
        this.cpfLogin = cpfLogin;
    }

    public String getNomeLogin() {
        return nomeLogin;
    }

    public void setNomeLogin(String nomeLogin) {
        this.nomeLogin = nomeLogin;
    }

    public Button getClickBtn() {
        return clickBtn;
    }

    public void setClickBtn(Button clickBtn) {
        this.clickBtn = clickBtn;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public void autenticarLogin(String nome, String cpf, Context context) {
        mDatabase.child("Convidados").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Convidado[] convidados = gson.fromJson(json, Convidado[].class);
                    boolean logar = false;
                    String nomeConvidado = "";
                    String cpfConvidado = "";

                    for(int i = 0; i < convidados.length; i++) {
                        if(convidados[i] != null) {
                            if(convidados[i].getNome().equals(nome) && convidados[i].getCpf().equals(cpf)) {
                                logar = true;
                                nomeConvidado = convidados[i].getNome();
                                cpfConvidado = convidados[i].getCpf();
                                break;
                            }  else {
                                logar = false;
                            }
                        }
                    }

                    if(logar) {
                        Intent intent = new Intent(context, LoginConvidado.class);
                        intent.putExtra("nome", nomeConvidado);
                        intent.putExtra("cpf", cpfConvidado);
                        context.startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(context, "Nome ou CPF errado", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    public void menuConvidado() {
        if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
            clickBtn.setGravity(Gravity.CENTER);
        }
        else {
            drawerLayout.closeDrawer(Gravity.LEFT);
            clickBtn.setGravity(Gravity.START);
        }
    }

    public void gerarQrCode() {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(nomeLogin + "," + cpfLogin, BarcodeFormat.QR_CODE, 400, 400);
            qrCode.setImageBitmap(bitmap);
            drawerLayout.close();
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
