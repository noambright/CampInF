package com.campin.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.bumptech.glide.util.Util;
import com.campin.Activities.LoginActivity;
import com.campin.R;
import com.campin.Utils.User;
import com.campin.Utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> _names;
    ArrayList<String> _id;
    public ArrayList<String> _friends;
    Activity context;
    String value;

    public CustomAdapter(Activity context, ArrayList<String> names, ArrayList<String> id) {
        super();
        this.context = context;
        this._names = new ArrayList<String>();
        this._names = names;
        this._id = new ArrayList<String>();
        this._id = id;
    }

    private class ViewHolder {
        CheckedTextView txtView;
    }

    public int getCount() {
        return _names.size();
    }

    @Override
    public String getItem(int position) {
        return _names.get(position);
    }


    public String getIdItem(int position) {
        return _id.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.check_list, null);
            holder = new ViewHolder();

            holder.txtView = (CheckedTextView) convertView.findViewById(R.id.simpleCheckedTextView);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        String name = getItem(position);
        holder.txtView.setText(name);

        if (User.getInstance().isShowFriends())
        {
            Bitmap bitmap = LoginActivity.getFacebookProfilePicture(getIdItem(position));
            Drawable d = new BitmapDrawable(context.getResources(), Utils.getCroppedBitmap(bitmap));
            holder.txtView.setCompoundDrawablesWithIntrinsicBounds(null,null,d,null);
            holder.txtView.setTag(getIdItem(position));
            holder.txtView.setText(getItem(position));
            holder.txtView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            _friends = new ArrayList<String>();
        }
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtView.isChecked()) {
                    // set cheek mark drawable and set checked property to false
                    value = "un-Checked";
                    holder.txtView.setCheckMarkDrawable(null);
                    holder.txtView.setChecked(false);
                    _friends.remove(holder.txtView.getTag().toString());
                } else {
                    // set cheek mark drawable and set checked property to true
                    value = "Checked";
                    _friends.add(holder.txtView.getTag().toString());
                    holder.txtView.setCheckMarkDrawable(R.drawable.checked);
                    holder.txtView.setChecked(true);
                }
            }
        });

        return convertView;
    }
}