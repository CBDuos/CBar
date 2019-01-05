package com.u3xj.collegebar;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Students> studentsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, status, collegename;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            status = (ImageView) view.findViewById(R.id.status);
            collegename = (ImageView) view.findViewById(R.id.college);
        }
    }


    public StudentsAdapter(Context mContext, List<Students> studentsList) {
        this.mContext = mContext;
        this.studentsList = studentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Students students = studentsList.get(position);
        holder.title.setText(students.getName());
        holder.count.setText(students.getGender());
        holder.thumbnail.setImageBitmap(students.getThumbnail());

        if (students.getStatus().equals("1")) {
            holder.status.setImageResource(R.drawable.online);
        } else{
            holder.status.setImageResource(R.drawable.offline);
        }

        holder.collegename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getApplicationContext(),students.getCollegename(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
