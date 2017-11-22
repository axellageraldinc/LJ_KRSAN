package pap.ta.lj_krsan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import pap.ta.lj_krsan.Model.Makul;
import pap.ta.lj_krsan.Model.Score;

/**
 * Created by axellageraldinc on 21/11/17.
 */

public class ListMakulAdapter extends BaseAdapter {
    Context context;
    List<Makul> makulList = new ArrayList<>();
    List<String> clickedMakul = new ArrayList<>();
    int counter=0;
    int ix=1;
    int score=100;
    String idGame;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String[] makulClicked = new String[99];
    String[] makulObyektif = new String[99];

    public ListMakulAdapter(Context context, List<Makul> makulList, String idGame) {
        this.context = context;
        this.makulList = makulList;
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.idGame = idGame;
    }

    @Override
    public int getCount() {
        return makulList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListMakulAdapter.ItemViewHolder holder = null;
        View MyView = view;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyView = inflater.inflate(R.layout.list_makul_adapter, viewGroup, false);
            holder = new ListMakulAdapter.ItemViewHolder();

            holder.txtMakul = MyView.findViewById(R.id.txtMakul);
            holder.txtUrutan = MyView.findViewById(R.id.txtUrutan);
            holder.btnPilih = MyView.findViewById(R.id.btnPilih);
            MyView.setTag(holder);
        } else{
            holder = (ListMakulAdapter.ItemViewHolder)MyView.getTag();
        }
        holder.txtMakul.setText(makulList.get(i).getName());
        final TextView txtUrutan = holder.txtUrutan;
        final TextView txtMakul = holder.txtMakul;
        final Button btnPilih = holder.btnPilih;
        holder.btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPilih.setEnabled(false);
                if(ix<=makulList.size()){
                    txtUrutan.setVisibility(View.VISIBLE);
                    txtUrutan.setText(String.valueOf(ix));
                    clickedMakul.add(txtMakul.getText().toString());
                    if(ix==makulList.size()){
                        databaseReference.child("games_info").child(idGame).child("makul_obyektif").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                                List<String> makulObyektifList = dataSnapshot.getValue(t);
                                for (String item : makulObyektifList){
                                    makulObyektif[counter] = item;
                                    System.out.println("Makul obyektif : " + item);
                                    counter++;
                                }
                                counter=0;
                                for (String item : clickedMakul){
                                    makulClicked[counter] = item;
                                    System.out.println("Makul clicked : " + item);
                                    counter++;
                                }
                                counter=0;
                                for (int i = 0; i<makulObyektifList.size(); i++){
                                    if(!makulClicked[i].equals(makulObyektif[i])){
                                        score-=10;
                                    }
                                }
                                Score scoree = new Score(score, user.getUid());
                                databaseReference.child("games_info").child(idGame).child("scores").child(user.getUid()).setValue(scoree);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    ix++;
                }
            }
        });
        return MyView;
    }
    private static class ItemViewHolder {
        TextView txtMakul, txtUrutan;
        Button btnPilih;
    }
}
