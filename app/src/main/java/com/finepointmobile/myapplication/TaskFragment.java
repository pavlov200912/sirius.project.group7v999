package com.finepointmobile.myapplication;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "LOGS";

    AppDatabase db;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeController swipeController = null;
    TaskAdapter adapter;
    //TODO Оставить
    Task daniel;


    public TaskFragment() {

    }

    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        fab = view.findViewById(R.id.fabT);
        recyclerView = view.findViewById(R.id.recycler_task);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        adapter = new TaskAdapter(db.taskDao().getAllSorted());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(),TaskActivity.class);
                        String card_id = String.valueOf(adapter.tasks.get(position).getTaskId());
                        Log.d(TAG, "onEditClicked: " + String.valueOf(card_id));
                        intent.putExtra("edit",false);
                        intent.putExtra("id",card_id);
                        startActivity(intent);
                    }
                })
        );
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                db.taskDao().deleteTaskById(adapter.tasks.get(position).getTaskId());
                db.checkDao().deleteById(adapter.tasks.get(position).getTaskId());
                adapter.tasks.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }

            @Override
            public void onLeftClicked(int position) {
                Toast.makeText(getActivity(),"Well done! +10 TP",Toast.LENGTH_SHORT).show();
                SavePreferences("TP",sharedPreferences.getInt("TP",0) + 20);
                db.taskDao().deleteTaskById(adapter.tasks.get(position).getTaskId());
                db.checkDao().deleteById(adapter.tasks.get(position).getTaskId());
                adapter.tasks.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),TaskActivity.class);
                String card_id = String.valueOf(sharedPreferences.getInt("id",1));
                SavePreferences("id",sharedPreferences.getInt("id",1) + 1);
                Log.d(TAG, "onNewClicked: " + String.valueOf(card_id));
                intent.putExtra("id",card_id);
                intent.putExtra("edit",true);
                startActivity(intent);
            }
        });
        return view;
    }

    public void SavePreferences(String key, int value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
