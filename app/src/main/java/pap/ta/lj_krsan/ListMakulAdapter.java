package pap.ta.lj_krsan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pap.ta.lj_krsan.Model.Makul;

/**
 * Created by axellageraldinc on 21/11/17.
 */

public class ListMakulAdapter extends BaseAdapter {
    Context context;
    List<Makul> makulList = new ArrayList<>();
    int ix=1;

    public ListMakulAdapter(Context context, List<Makul> makulList) {
        this.context = context;
        this.makulList = makulList;
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
        holder.btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUrutan.setVisibility(View.VISIBLE);
                txtUrutan.setText(String.valueOf(ix));
//                txtUrutan.setText(ix);
                ix++;
            }
        });
        return MyView;
    }
    private static class ItemViewHolder {
        TextView txtMakul, txtUrutan;
        Button btnPilih;
    }
}
