package com.kumoh19.e_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashcanInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashcanInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrashcanInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrashcanInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrashcanInfoFragment newInstance(String param1, String param2) {
        TrashcanInfoFragment fragment = new TrashcanInfoFragment();
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

    TextView trashcanAddress;
    TextView trashcanDetail;

    String POIName;

    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trashcan_info, container, false);
        // 정보 가져오기
        trashcanAddress = (TextView) root.findViewById(R.id.trashcan_address);
        trashcanDetail = (TextView) root.findViewById(R.id.trashcan_time);


        Bundle bundle = getArguments();
        // Log.i("Add", "번들: " + bundle);
        if(bundle != null) {
            POIName = bundle.getString("param1");
            trashcanAddress.setText(POIName);
        }

        // firebase에서 데이터 가져오기
        mDatabase = FirebaseDatabase.getInstance("https://e-map-17167-default-rtdb.firebaseio.com/").getReference("Trashcan");
        readTrashcanInfo();

        return root;
    }

    String TrashcanAddress;
    String TrashcanDetail;

    public void readTrashcanInfo() { // firebase 에서 데이터 가져와서 출력
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    for(DataSnapshot data1 : data.getChildren()) {
                        if(data.getValue(Trashcan.class) != null){
                            Trashcan post = data1.getValue(Trashcan.class);
                            TrashcanAddress = post.getTrashcanAddress();
                            if(POIName.equals(TrashcanAddress)) {
                                TrashcanDetail = post.getTrashcanDetail();

                                trashcanAddress.setText(TrashcanAddress);
                                trashcanDetail.setText(TrashcanDetail);
                            }
                        } else {
                            Toast.makeText(getContext(), "데이터 없음...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                // Get Post object and use the values to update the UI

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}