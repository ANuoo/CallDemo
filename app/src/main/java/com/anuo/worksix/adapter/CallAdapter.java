package com.anuo.worksix.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anuo.worksix.R;
import com.anuo.worksix.data.CallLogInfo;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {
    private AlertDialog.Builder builder;
    private String number;
    private List<CallLogInfo> list;

    public CallAdapter(List<CallLogInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fragment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.ViewHolder viewHolder, int i) {
        if (builder == null) {
            builder = new AlertDialog.Builder(viewHolder.itemView.getContext()).setTitle("please choose:")
                    .setPositiveButton("call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + number));
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                viewHolder.itemView.getContext().getApplicationContext().startActivity(intent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("sent message", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("smsto:" + number));
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                viewHolder.itemView.getContext().getApplicationContext().startActivity(intent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = list.get(i).getNumber();
                builder.show();
            }
        });
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.number.setText(list.get(i).getNumber());
        viewHolder.time.setText(list.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, time;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            number = itemView.findViewById(R.id.txt_number);
            time = itemView.findViewById(R.id.txt_time);
        }
    }
}
