package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import pap.ta.lj_krsan.Model.Game;
import pap.ta.lj_krsan.Model.PlayerList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    Button btnBuatGame, btnGabungGame, btnLogout;

    Game game;
    PlayerList playerList;
    String idGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        btnBuatGame = findViewById(R.id.btnBuatGame);
        btnGabungGame = findViewById(R.id.btnJoinGame);
        btnLogout = findViewById(R.id.btnLogout);

        btnBuatGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idGame = String.valueOf(UUID.randomUUID());
                game = new Game(idGame, "Silit pitik", 1, 0, user.getUid(), 1);
                databaseReference.child("games_info").child(idGame).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(getApplicationContext(), RuangTungguActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("idGame", idGame);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });
        btnGabungGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AvailableGamesActivity.class);
                startActivity(i);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }
}
