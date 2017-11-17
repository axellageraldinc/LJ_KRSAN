package pap.ta.lj_krsan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pap.ta.lj_krsan.Model.Game;

/**
 * Created by axellageraldinc on 17/11/17.
 */

public class AvailableGamesListAdapter extends BaseAdapter {
    private Context context;
    private List<Game> gameList = new ArrayList<>();

    public AvailableGamesListAdapter(@NonNull Context context, @NonNull List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        View MyView = convertView;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyView = inflater.inflate(R.layout.available_games_list_adapter, parent, false);
            holder = new ItemViewHolder();

            holder.txtGameId = MyView.findViewById(R.id.txtIdGame);
            MyView.setTag(holder);
        } else{
            holder = (ItemViewHolder)MyView.getTag();
        }
        holder.txtGameId.setText(gameList.get(position).getId());
        return MyView;
    }
    private static class ItemViewHolder {
        TextView txtGameId;
    }
}
