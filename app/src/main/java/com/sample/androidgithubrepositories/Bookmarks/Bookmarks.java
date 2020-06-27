package com.sample.androidgithubrepositories.Bookmarks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sample.androidgithubrepositories.CustomListView.ListCollection;
import com.sample.androidgithubrepositories.R;

import java.util.ArrayList;

public class Bookmarks extends ArrayAdapter<ListCollection> {

    private final Context context;
    private final ArrayList values;
    BookmarksCollection bcr;

    public Bookmarks(Context context, ArrayList values) {
        super(context, R.layout.activity_bookmarks, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.bookmarks_list, parent, false);
        final TextView tv = rowView.findViewById(R.id.textView2);
        TextView tv1 = rowView.findViewById(R.id.textView3);
        tv1.setTextColor(Color.BLACK);
        tv.setTextColor(Color.BLACK);
        rowView.setTag(holder);
        holder.name = rowView.findViewById(R.id.checkBox2);
        final BookmarksCollection lc = (BookmarksCollection) values.get(position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    lc.setChecked(true);
                } else {
                    lc.setChecked(false);
                }

            }
        });

        tv.setText(lc.getRepository_Name());
        tv1.setText(lc.getDescription());
        /*chkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), lc.getURL(),Toast.LENGTH_LONG).show();

                *//*if(chkItem.isChecked()==false)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext().getApplicationContext());
                    dialog.create();
                    dialog.setTitle("Remove Bookmark?");
                    dialog.setMessage("Are you Sure you want to remove repository from bookmark ?");
                    dialog.setPositiveButton("delete", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Removed",Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "Ok baba ! not Removing",Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.show();
                }
                else
                    Toast.makeText(getContext(), "Clicked on Checkbox: " + tv.getText() + " is " + chkItem.isChecked(),
                            Toast.LENGTH_LONG).show();*//*
            }
        });*/
        return rowView;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }
}
