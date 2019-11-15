package com.example.debatetracker.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.debatetracker.R;
import com.example.debatetracker.models.Debate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private View view;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayList<String> mKeys;
    private ArrayAdapter<String> arrayAdapter;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        view = root;
        setupListView();
        return root;
    }

    private void setupListView() {
        databaseReference = FirebaseDatabase.getInstance().getReference("debates");
        arrayList = new ArrayList<>();
        mKeys = new ArrayList<>();
        listView = view.findViewById(R.id.listviewtxt);
        arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        databaseReference.addChildEventListener(new DebateChildListener());

    }

    private class DebateChildListener implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String value = dataSnapshot.getValue(Debate.class).toString();
            arrayList.add(value);
            mKeys.add(dataSnapshot.getKey());
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String value = dataSnapshot.getValue(Debate.class).toString();

            String key = dataSnapshot.getKey();

            int index = mKeys.indexOf(key);

            arrayList.set(index, value);

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
}