package com.kumoh19.e_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MyRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    String userId;
    Fragment fg;


    RecyclerView recyclerView;
    Adapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRegisterFragment newInstance(String param1, String param2) {
        MyRegisterFragment fragment = new MyRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyRegisterFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_my_register, container, false);

        // 아이디 가져오기
        Bundle bundle = getArguments();
        if(bundle != null) {
            userId = bundle.getString("param1");
            Log.i("MyRegisterFragment", "사용자아이디 " + userId);
        }

        // 리사이클러뷰
        recyclerView = (RecyclerView) view.findViewById(R.id.recyceler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mDatabase = FirebaseDatabase.getInstance("https://e-map-17167-default-rtdb.firebaseio.com/").getReference("MY등록");
        readShopMyRegister(userId);



        //recyclerView.setAdapter(adapter);

        return view;
    }

    String trashcanAddress;
    String toiletName;

    public void readShopMyRegister(String userId) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter = new Adapter();
                ArrayList<Item> mItems = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String id = data.getKey();
                    if (id.equals(userId)) {
                        for(DataSnapshot data1 : data.getChildren()) {
                            String category = data1.getKey();
                            if(category.equals("Trashcan")) {
                                for(DataSnapshot data2 : data1.getChildren()) {
                                    if(data2.getValue(Trashcan.class) != null) {
                                        Trashcan post = data2.getValue(Trashcan.class);
                                        trashcanAddress = post.getTrashcanAddress();
                                        mItems.add(new Item(trashcanAddress));
                                    }
                                }
                                recyclerView.setAdapter(adapter);
                                adapter.setList(mItems);

                            }
                            else if(category.equals("Toilet")) {
                                for(DataSnapshot data2 : data1.getChildren()) {
                                    if(data2.getValue(Toilet.class) != null) {
                                        Toilet post = data2.getValue(Toilet.class);
                                        toiletName = post.getToiletName();
                                        mItems.add(new Item(toiletName));
                                    }
                                }
                                recyclerView.setAdapter(adapter);
                                adapter.setList(mItems);
                            }
                        }
                    } else {
                        //Toast.makeText(getContext(), "데이터 없음...", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            // Get Post object and use the values to update the UI

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}