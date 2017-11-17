package pap.ta.lj_krsan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pap.ta.lj_krsan.Model.User;

public class RegisterActivity extends AppCompatActivity {
    Button btnDaftar;
    EditText txtEmail, txtUsername, txtPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String email, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnDaftar = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaftarUser();
            }
        });
    }
    private void DaftarUser(){
        email = txtEmail.getText().toString();
        username = txtUsername.getText().toString();
        try{
            firebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        try{
                            User user = new User(task.getResult().getUser().getUid(), email, username);
                            databaseReference.child("users_info").child(task.getResult().getUser().getUid()).setValue(user);
                            firebaseAuth.signOut();
                            txtEmail.setText(""); txtUsername.setText(""); txtPassword.setText("");
                            Toast.makeText(RegisterActivity.this, "Register berhasil!", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex){
                            System.out.println("Gagal register user : " + ex.toString());
                            Toast.makeText(RegisterActivity.this, "Gagal register user", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (Exception ex){
            System.out.println("Gagal daftar : " + ex.toString());
        }
    }
}
