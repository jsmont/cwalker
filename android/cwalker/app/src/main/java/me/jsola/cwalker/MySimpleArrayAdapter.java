package me.jsola.cwalker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MySimpleArrayAdapter extends ArrayAdapter<Place> {
    private final Context context;
    private final List<Place> values;

    public MySimpleArrayAdapter(Context context, List<Place> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_base, parent, false);
        TextView Name = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView Data = (TextView) rowView.findViewById(R.id.secondLine);
        Name.setText(values.get(position).getName());
        Data.setText(values.get(position).getVicinity());
        return rowView;
    }

    @Override
    public Place getItem(int position){
        return values.get(position);
    }
}
