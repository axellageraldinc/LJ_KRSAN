package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pap.ta.lj_krsan.Model.Score;
import pap.ta.lj_krsan.Model.User;

public class RankActivity extends AppCompatActivity {
    String id_winner, id_game;
    Intent i;
    TextView txtNama1, txtNama2;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    Score score;
    int score_juara=0, score_satunya=0;
    String id_user_satunya;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        i = getIntent();
        id_winner = i.getStringExtra("id_winner");
        id_game = i.getStringExtra("id_game");

        txtNama1 = findViewById(R.id.txtNama1);
        txtNama2 = findViewById(R.id.txtNama2);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference.child("games_info").child(id_game).child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    score = data.getValue(Score.class);
                    if(score.getId_user()!=user.getUid()){
                        score_satunya=score.getScore();
                        id_user_satunya = score.getId_user();
                    } else{
                        score_juara = score.getScore();
                    }
                }
                databaseReference.child("users_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user1 = dataSnapshot.getValue(User.class);
                        if(user1.getId().equals(id_winner)){
                            txtNama1.setText(user1.getUsername() + " - " + score_juara + " (1) ");
                            databaseReference.child("users_info").child(id_user_satunya).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user2 = dataSnapshot.getValue(User.class);
                                    txtNama2.setText(user2.getUsername() + " - " + score_satunya + " (2) ");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else{
                            databaseReference.child("users_info").child(id_user_satunya).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user2 = dataSnapshot.getValue(User.class);
                                    txtNama1.setText(user2.getUsername() + " - " + score_satunya + " (1) ");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            databaseReference.child("games_info").child(id_game).child("scores").child(user1.getId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int score = dataSnapshot.getValue(Score.class).getScore();
                                    txtNama2.setText(user1.getUsername() + " - " + score + " (2) ");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
