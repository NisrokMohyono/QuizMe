package com.diamondprize.quizme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.diamondprize.quizme.databinding.FragmentMainmainBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MainmainFragment extends Fragment {

    public MainmainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMainmainBinding binding;
    FirebaseFirestore database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainmainBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        final ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapterPermainan adapter = new CategoryAdapterPermainan(getContext(), categories);

        database.collection("urlpermainan")
                .addSnapshotListener((value, error) -> {
                    categories.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        CategoryModel model = snapshot.toObject(CategoryModel.class);
                        model.setCategoryId(snapshot.getId());
                        categories.add(model);
                    }
                    adapter.notifyDataSetChanged();

                });


        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.categoryList.setAdapter(adapter);



        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}