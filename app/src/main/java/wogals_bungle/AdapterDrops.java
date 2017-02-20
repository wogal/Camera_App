package wogals_bungle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egs.wogal.camera_app.R;

import java.util.ArrayList;

import io.realm.Realm;


/**
 * Created by wogal on 1/22/2017.
 */

public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> mItems = new ArrayList<>();
    private Realm mRealm;



    // constructor
    public AdapterDrops(Context context) {
        mInflater = LayoutInflater.from(context);
        mItems = generateValues();
    }

    private ArrayList<String> generateValues() {
        int a;
        String str = "";
        ArrayList<String> dummystuff = new ArrayList<>();
        for (a = 1; a != 100; a++) {
            str = String.format("Item %s", a);
            dummystuff.add(str);
        }
        return dummystuff;
    }


    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drop_layout, parent, false);
        DropHolder holder = new DropHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        holder.mtextview.setText(mItems.get(position));

    }

    @Override
    public int getItemCount() {
        int cnt;
        cnt = mItems.size();
        return cnt;
    }

    // CLASS DropHolder --
    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView mtextview;

        public DropHolder(View itemView) {
            super(itemView);
            mtextview = (TextView) itemView.findViewById(R.id.txt_id);
        }
    }


}
