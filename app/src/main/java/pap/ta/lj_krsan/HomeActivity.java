package pap.ta.lj_krsan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

import pap.ta.lj_krsan.Model.Game;
import pap.ta.lj_krsan.Model.PlayerList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    Button btnBuatGame, btnGabungGame, btnLogout;
    TextView txtBuatGame, txtGabungGame, txtEnter;
    EditText txtRoomName;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Game game;
    PlayerList playerList;
    String idGame, room_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        txtBuatGame = findViewById(R.id.txtBuatGame);
        txtGabungGame = findViewById(R.id.txtGabungGame);
        btnLogout = findViewById(R.id.btnLogout);

        txtBuatGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRoomName();
//                databaseReference.child("games_info").child(idGame).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Intent i = new Intent(getApplicationContext(), RuangTungguActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            i.putExtra("idGame", idGame);
//                            startActivity(i);
//                            finish();
//                        }
//                    }
//                });
            }
        });
        txtGabungGame.setOnClickListener(new View.OnClickListener() {
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
    private void DialogRoomName(){
        dialog = new AlertDialog.Builder(HomeActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.activity_dialog_room_name, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        txtRoomName = dialogView.findViewById(R.id.txtRoomName);
        txtEnter = dialogView.findViewById(R.id.txtEnter);

        txtEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idGame = String.valueOf(UUID.randomUUID());
//                game = new Game(idGame, RandomObjective(), 1, 0, user.getUid(), 1);
//                System.out.println(RandomObjective());
                Random random = new Random();
                int objId = random.nextInt(); //(max-min) + 1) + min
                if(objId%2==0)
                    objId=1;
                else
                    objId=2;

                room_name = txtRoomName.getText().toString();

                databaseReference.child("objective_list").child(String.valueOf(objId)).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String objective = dataSnapshot.getValue(String.class);
                        game = new Game(idGame, objective, 1, 0, user.getUid(), 1, room_name);
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

//        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                nama    = txt_nama.getText().toString();
//                usia    = txt_usia.getText().toString();
//                alamat  = txt_alamat.getText().toString();
//                website = txt_website.getText().toString();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
//    private String RandomObjective(){
//        final String objective;
//        Random random = new Random();
//        int objId = random.nextInt(); //(max-min) + 1) + min
//        if(objId%2==0)
//            objId=1;
//        else
//            objId=2;
//        databaseReference.child("objective_list").child(String.valueOf(objId)).child("name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                objective = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return objective;
//    }
}
