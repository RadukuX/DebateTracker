package com.example.debatetracker.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.debatetracker.R;
import com.example.debatetracker.models.Debate;
import com.example.debatetracker.models.Location;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GalleryFragment extends Fragment {
    private View view;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<Debate> arrayList;
    private ArrayList<String> mKeys;
    private DebateViewAdapter arrayAdapter;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        galleryViewModel =
//                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s.toString());
//            }
//        });
        view = root;
        setupListView();
        return root;
    }

    private void setupListView() {
        databaseReference = FirebaseDatabase.getInstance().getReference("debates");
        arrayList = new ArrayList<>();
        mKeys = new ArrayList<>();
        listView = view.findViewById(R.id.listviewtxt);
        arrayAdapter = new DebateViewAdapter(arrayList, getContext());
        listView.setAdapter(arrayAdapter);
        databaseReference.addChildEventListener(new DebateChildListener(this));

    }

    private class DebateChildListener implements ChildEventListener {

        private Fragment parentFragment;
        public DebateChildListener(GalleryFragment parentFragment) {
            this.parentFragment = parentFragment;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Debate debate = dataSnapshot.getValue(Debate.class);
            if (debate == null) {
                return;
            }
            List nonNullLocations = debate.getMarkerLocations().stream().filter(new Predicate<Location>() {
                @Override
                public boolean test(Location location) {
                    return location != null;
                }
            }).collect(Collectors.toList());
            debate.setMarkerLocations((ArrayList)nonNullLocations);
            String value = debate.toString();
            arrayList.add(debate);
            mKeys.add(dataSnapshot.getKey());
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Debate debate = dataSnapshot.getValue(Debate.class);

            String key = dataSnapshot.getKey();

            int index = mKeys.indexOf(key);

            arrayList.set(index, debate);

            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private class DebateViewAdapter extends BaseAdapter {
        private ArrayList<Debate> debates;
        private Context context;

        public DebateViewAdapter(ArrayList<Debate> debates, Context context) {
            this.debates = debates;
            this.context = context;
        }

        @Override
        public int getCount() {
            return debates.size();
        }

        @Override
        public Object getItem(int i) {
            return debates.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = view;
            final Debate current = (Debate)getItem(i);
            if (myView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = inflater.inflate(R.layout.debate_item_template, null);
            }

            final Context c = this.context;
            TextView debateDetails = (TextView)myView.findViewById(R.id.debate_details);
            debateDetails.setText(current.toString());

            Button locationsButton = (Button)myView.findViewById(R.id.debate_locations);
            Button subscribeButton = (Button)myView.findViewById(R.id.debate_subscribe);

            locationsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent startMapsWithMarkersIntent = new Intent(c, DebateLocation.class);
                    startMapsWithMarkersIntent.putExtra("markerLocations", current.getMarkerLocations());
                    startActivity(startMapsWithMarkersIntent);
                }
            });

            return myView;
        }
    }
}