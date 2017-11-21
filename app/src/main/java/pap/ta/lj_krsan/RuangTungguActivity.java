package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pap.ta.lj_krsan.Model.Game;

public class RuangTungguActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;

    Button btnHapusGame;
    String idGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruang_tunggu);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        Intent i = getIntent();
        idGame = i.getStringExtra("idGame");

        btnHapusGame = findViewById(R.id.btnHapusGame);
        btnHapusGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HapusGame();
            }
        });

        GetPlayerAmount();
    }
    private void HapusGame(){
        databaseReference.child("games_info").child(idGame).removeValue().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RuangTungguActivity.this, "Game berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else{
                    Toast.makeText(RuangTungguActivity.this, "Game gagal dihapus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void GetPlayerAmount(){
        databaseReference.child("games_info").child(idGame).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Game game = dataSnapshot.getValue(Game.class);
                    if(game.getPlayer_amount()>1){
                        Toast.makeText(RuangTungguActivity.this, "Player Amount : " + game.getPlayer_amount(), Toast.LENGTH_SHORT).show();
                        databaseReference.child("games_info").child(idGame).child("status").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent i = new Intent(getApplicationContext(), GameScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("idGame", idGame);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                } catch (Exception ex){
                    System.out.println("Error Get Player Amount : " + ex.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
