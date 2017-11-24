package pap.ta.lj_krsan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import pap.ta.lj_krsan.Model.Score;
import pap.ta.lj_krsan.Model.User;

public class GameScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;

    Intent i;
    int winner=0;
    String id_winner;
    TextView txtPemain1, txtPemain2, txtObjective, txtMakul;

    List<Makul> makulList = new ArrayList<>();
    List<String> makuls = new ArrayList<>();
    List<Score> scoreList = new ArrayList<>();
    ListView listMakul;
    GridView gridview;
    ListMakulAdapter adapter;
    GridAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        i = getIntent();

        listMakul = findViewById(R.id.listMakul);
        adapter = new ListMakulAdapter(getApplicationContext(), makulList, i.getStringExtra("idGame"));
        gridview = findViewById(R.id.gridMakul);
//        gridview.setAdapter(new GridAdapter(getApplicationContext(), makulList, i.getStringExtra("idGame")));
        txtPemain1 = findViewById(R.id.txtPemain1);
        txtPemain2 = findViewById(R.id.txtPemain2);
        txtObjective = findViewById(R.id.txtObjektif);
        txtMakul = findViewById(R.id.txtMakul);
        GetPlayersName();
        GetMakulFromDb();
        GetObjektif();
        RandomMakul();
        CekScore();
    }
    private void CekScore(){
        databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Score score = data.getValue(Score.class);
                    scoreList.add(score);
                    if(scoreList.size()>1){
                        //Game selesai, penentuan pemenang disini
                        for(Score item : scoreList){
                            if(item.getScore()>winner) {
                                id_winner = item.getId_user();
                                winner = item.getScore();
                            }
                        }
//                        System.out.println("Pemenangnya adalah " + id_winner + " dengan score " + winner);
//                        databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("scores").child(user.getUid()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                int score1 = dataSnapshot.getValue(Score.class).getScore();
//                                System.out.println("Score anda : " + score1);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                adapter2 = new GridAdapter(getApplicationContext(), makulList, i.getStringExtra("idGame"));
                gridview.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
//                listMakul.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
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
        databaseReference.child("makul").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                makuls.clear();
                try{
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String makul = data.getValue(Makul.class).getName();
                        makuls.add(makul);
                    }
                } catch (Exception ex){
                    System.out.println("Gagal get makul from db : " + ex.toString());
                }
                Collections.shuffle(makuls);
                databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("makul_obyektif").setValue(makuls);
                    databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("makul_obyektif").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String makul = new String();
                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            List<String> makulList = dataSnapshot.getValue(t);
                            for(String item : makulList){
                                makul = makul + item + " ";
                            }
                            txtMakul.setText(makul);
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
//        Random random = new Random();
//        final int counter=6; int target;
//        final List<String> makulList = new ArrayList<>();
//        for (int j=0; j<counter; j++){
//            target = random.nextInt((8 - 1) + 1) + 1;
//            System.out.println("Random makul : " + target);
//            final int finalJ = j;
//            databaseReference.child("makul").child(String.valueOf(target)).child("name").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    makulList.add(dataSnapshot.getValue(String.class));
////                    if(finalJ == counter-1){
////                        String makul = new String();
////                        for (String item : makulList){
////                            makul = makul + item + " ";
////                        }
////                        txtMakul.setText(makul);
////                    }
//                    databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("makul_obyektif").setValue(makulList);
//                    databaseReference.child("games_info").child(i.getStringExtra("idGame")).child("makul_obyektif").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String makul = new String();
//                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
//                            List<String> makulList = dataSnapshot.getValue(t);
//                            for(String item : makulList){
//                                makul = makul + item + " ";
//                            }
//                            txtMakul.setText(makul);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Tidak boleh meninggalkan perang KRS!", Toast.LENGTH_SHORT).show();
    }
}

