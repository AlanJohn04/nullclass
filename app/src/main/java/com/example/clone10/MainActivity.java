package com.example.clone10;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.canvas.CanvasCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient f;
    TextView t1,ans;
    Button b1;
    private final static int REQUESTCODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        t1=findViewById(R.id.t1);
        ans=findViewById(R.id.ans);
        b1=findViewById(R.id.b1);
        f=LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }


        });

    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            f.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        Geocoder g = new Geocoder(MainActivity.this, Locale.getDefault());
//                        List<Address> addresses= null;

//                        try {
//
//                            addresses=g.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//                            String s =addresses.get(0).getAddressLine( 8)+addresses.get(8).getLocality()+addresses.get(0).getCountryName() ;
//                            ans.setText("Address"+s);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//
//                        }
                        List<Address> ad = null;
                        try {
                            ad = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String s = ad.get(0).getAddressLine(8) + ad.get(8).getLocality() + ad.get(0).getCountryName();
                            ans.setText("Address" + s);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } else {
            askpermission();
        }
    }
    private void askpermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTCODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUESTCODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else
            {
                Toast.makeText(this,"required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}