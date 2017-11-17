package pap.ta.lj_krsan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import pap.ta.lj_krsan.Model.Game;

public class AvailableGamesActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;

    AvailableGamesListAdapter adapter;

    ListView listView;
    List<Game> gameList = new ArrayList<>();
    List<String> playerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        listView = findViewById(R.id.listAvailableGames);
        adapter = new AvailableGamesListAdapter(getApplicationContext(), gameList);
        GetListGames();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Game game = gameList.get(i);
                final String firstPlayerId = game.getPlayer_list();
                playerList.add(firstPlayerId);
                playerList.add(user.getUid());
                databaseReference.child("games_info").child(game.getId()).child("player_amount").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child("games_info").child(game.getId()).child("player_list").setValue(playerList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(getApplicationContext(), GameScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        i.putExtra("id_user_1", firstPlayerId);
//                                        i.putExtra("id_user_2", user.getUid());
                                        i.putExtra("idGame", game.getId());
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
//                databaseReference.child("games_info").child(game.getId()).child("player_list").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            databaseReference.child("games_info").child(game.getId()).child("player_list").setValue(playerList).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
            }
        });
    }
    private void GetListGames(){
        databaseReference.child("games_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameList.clear();
                try{
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Game game = data.getValue(Game.class);
                        if(game.getStatus()==1)
                            gameList.add(game);
                    }
                } catch (Exception ex){
                    System.out.println("Error get list games : " + ex.toString());
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
