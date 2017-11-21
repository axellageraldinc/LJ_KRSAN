package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pap.ta.lj_krsan.Model.Game;
import pap.ta.lj_krsan.Model.Makul;
import pap.ta.lj_krsan.Model.User;

public class GameScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;

    Intent i;
    TextView txtPemain1, txtPemain2, txtObjective, txtMakul;
    TextView txtMakul1, txtMakul2, txtMakul3, txtMakul4, txtMakul5, txtMakul6, txtMakul7, txtMakul8;

    List<Makul> makulList = new ArrayList<>();
    ListView listMakul;
    ListMakulAdapter adapter;
    int urutanKlik=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        listMakul = findViewById(R.id.listMakul);
        adapter = new ListMakulAdapter(getApplicationContext(), makulList);

        i = getIntent();
        System.out.println("User id 1 : " + i.getStringExtra("id_user_1"));
        System.out.println("User id 2 : " + i.getStringExtra("id_user_2"));

        txtPemain1 = findViewById(R.id.txtPemain1);
        txtPemain2 = findViewById(R.id.txtPemain2);
        txtObjective = findViewById(R.id.txtObjektif);
        txtMakul = findViewById(R.id.txtMakul);
        txtMakul1 = findViewById(R.id.txtMakul1); txtMakul2 = findViewById(R.id.txtMakul2); txtMakul3 = findViewById(R.id.txtMakul3); txtMakul4 = findViewById(R.id.txtMakul4);
        txtMakul5 = findViewById(R.id.txtMakul5); txtMakul6 = findViewById(R.id.txtMakul6); txtMakul7 = findViewById(R.id.txtMakul7); txtMakul8= findViewById(R.id.txtMakul8);
        GetPlayersName();
        GetMakulFromDb();
        GetObjektif();
        RandomMakul();
    }

    private void GetMakulFromDb(){
        databaseReference.child("makul").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                makulList.clear();
                try{
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Makul makul = data.getValue(Makul.class);
                        makulList.add(makul);
                    }
                } catch (Exception ex){
                    System.out.println("Gagal get makul from db : " + ex.toString());
                }
                Collections.shuffle(makulList);
                listMakul.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    private void RandomMakul(){
        Random random = new Random();
        final int i=6; int target;
        final List<String> makulList = new ArrayList<>();
        for (int j=0; j<i; j++){
            target = random.nextInt((8 - 1) + 1) + 1;
            System.out.println("Random makul : " + target);
            final int finalJ = j;
            databaseReference.child("makul").child(String.valueOf(target)).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    makulList.add(dataSnapshot.getValue(String.class));
                    if(finalJ == i-1){
                        String makul = new String();
                        for (String item : makulList){
                            makul = makul + item + " ";
                        }
                        txtMakul.setText(makul);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
