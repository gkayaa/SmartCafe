package com.example.smartcafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotificationsListAdapter extends BaseAdapter
{
    private ArrayList<Notification> userNotifications;
    private Context context;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;

    public NotificationsListAdapter(Context context, ArrayList<Notification> userNotifications){
        this.context = context;
        this.userNotifications = userNotifications;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return userNotifications.size();
    }

    @Override
    public Object getItem(int i) {
        return userNotifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent)
    {
        View notView = layoutInflater.inflate(R.layout.list_notification,null);
        TextView txtNotText = notView.findViewById(R.id.txtNotText);
        TextView txtNotDate = notView.findViewById(R.id.txtNotDate);
        ImageView imgNotf = notView.findViewById(R.id.imgNotf);
        mAuth = FirebaseAuth.getInstance();
        txtNotText.setText(userNotifications.get(i).getContent());
        txtNotDate.setText(userNotifications.get(i).getDate());

        if(txtNotText.getText().toString().startsWith("Your order was created"))
        {
            imgNotf.setImageResource(R.drawable.ic_baseline_pending_actions_24);
        }










        return notView;
    }
}
