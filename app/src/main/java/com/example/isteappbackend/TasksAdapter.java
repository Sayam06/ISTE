package com.example.isteappbackend;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

//import java.io.BufferedOutputStream;
//import java.io.BufferedWriter;
//import java.io.DataOutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    Context context;
    Map<String,Task> taskMap =new HashMap<>();
    List<String> keys;

    public TasksAdapter(Context ct, Map<String,Task> tasks){
        this.context = ct;
        this.taskMap =tasks;
    }

    public void notification() {
        
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        //Inflate the row xml file
        View view = inflater.inflate(R.layout.task_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String key=keys.get(position);

        Task t= taskMap.get(key);
        holder.title.setText(t.getTitle());
        holder.desc.setText(t.getDescription());
        holder.assignedTo.setText(t.getAssignedTo());
        holder.date.setText(t.getDate());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete task")
                        .setMessage("This action is irreversible. Are you sure you want to delete the task")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("mine","clicked");
                                FirebaseDatabase db= FirebaseDatabase.getInstance();
                                db.getReference().child("Tasks").child(keys.get(position)).removeValue();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Task");
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.task_input_dialog, TasksFragment.vg, false);
                final EditText title_textView = (EditText) viewInflated.findViewById(R.id.TaskTitleIP),
                        description_textView = (EditText) viewInflated.findViewById(R.id.TaskDescriptionIP),
                        date_textView = (EditText) viewInflated.findViewById(R.id.TaskDueDateIP),
                        assigned_textView=(EditText) viewInflated.findViewById(R.id.TaskAssignedIP);
                title_textView.setText(t.getTitle());
                description_textView.setText(t.getDescription());
                date_textView.setText(t.getDate());
                assigned_textView.setText(t.getAssignedTo());
                builder.setView(viewInflated);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String,Object> newMap=new HashMap<>();
                        newMap.put("title",title_textView.getText().toString().trim());
                        newMap.put("description",description_textView.getText().toString().trim());
                        newMap.put("dueDate",date_textView.getText().toString().trim());
                        newMap.put("assignedTo",assigned_textView.getText().toString().trim());
                        FirebaseDatabase db= FirebaseDatabase.getInstance();
                        db.getReference().child("Tasks").child(keys.get(position)).updateChildren(newMap);
                        notification();
                    }
                });
                builder.setNegativeButton("Cancel", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Collection<String> kc= taskMap.keySet();
        keys= new ArrayList<>(kc);
        return taskMap.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc,date,assignedTo;
        FloatingActionButton deleteButton,editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //find view itemview.find
            title=itemView.findViewById(R.id.task_title);
            desc=itemView.findViewById(R.id.task_desc);
            date=itemView.findViewById(R.id.task_date);
            assignedTo=itemView.findViewById(R.id.task_assigned);
            deleteButton=itemView.findViewById(R.id.delete);
            editButton=itemView.findViewById(R.id.edit);
        }
    }
}
