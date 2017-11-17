package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pap.ta.lj_krsan.Model.Game;
import pap.ta.lj_krsan.Model.User;

public class GameScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;

    Intent i;
    TextView txtPemain1, txtPemain2, txtObjective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        i = getIntent();
        System.out.println("User id 1 : " + i.getStringExtra("id_user_1"));
        System.out.println("User id 2 : " + i.getStringExtra("id_user_2"));

        txtPemain1 = findViewById(R.id.txtPemain1);
        txtPemain2 = findViewById(R.id.txtPemain2);
        txtObjective = findViewById(R.id.txtObjektif);
        GetPlayersName();
        GetObjektif();
    }

    private void GetPlayersName(){
        databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("player_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List<String> playerList = dataSnapshot.getValue(t);
                String[] namaPemain = new String[2];
                int i=0;
                for (String item : playerList){
                    namaPemain[i] = item;
                    i++;
                }
                databaseReference.child("users_info").child(namaPemain[0]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        txtPemain1.setText(dataSnapshot.getValue(User.class).getUsername());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseReference.child("users_info").child(namaPemain[1]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        txtPemain2.setText(dataSnapshot.getValue(User.class).getUsername());
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
//        try{
//            databaseReference.child("users_info").child(i.getStringExtra("id_user_1")).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    txtPemain1.setText(dataSnapshot.getValue(User.class).getUsername());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception ex){
//            System.out.println("Gagal get user id 1 : " + ex.toString());
//        }
//        try{
//            databaseReference.child("users_info").child(i.getStringExtra("id_user_2")).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    txtPemain2.setText(dataSnapshot.getValue(User.class).getUsername());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception ex){
//            System.out.println("Gagal get user id 2: " + ex.toString());
//        }
    }
    private void GetObjektif(){
        databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("objective").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtObjective.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
