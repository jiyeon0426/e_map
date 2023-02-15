package com.kumoh19.e_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToiletRegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToiletRegFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private double mParam3;
    private double mParam4;

    private DatabaseReference mDatabase;


    public ToiletRegFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToiletRegFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToiletRegFragment newInstance(String param1, String param2) {
        ToiletRegFragment fragment = new ToiletRegFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ToiletRegFragment newInstance(String param1, double param3, double param4) {
        ToiletRegFragment fragment = new ToiletRegFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM3, param3);
        args.putDouble(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    public static ToiletRegFragment newInstance() {
        return new ToiletRegFragment();
    }


    Fragment fg;
    String address;
    double lat;
    double lng;

    private void setChildFragment(Fragment child) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            childFt.replace(R.id.register_child_fragment, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getDouble(ARG_PARAM1);
            mParam4 = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_toilet_reg, container, false);

        // 지도모양 버튼
        ImageButton address_map = (ImageButton) root.findViewById(R.id.address_map);
        address_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fg = ToiletAddressFragment.newInstance();
                setChildFragment(fg);
            }
        });

        // 주소 가져오기
        TextView address_edit = (TextView) root.findViewById(R.id.toilet_address);

        Bundle bundle = getArguments();
        // Log.i("Add", "번들: " + bundle);
        if(bundle != null) {
            address = bundle.getString("param1");
            //Log.i("Add", "주소: " + address);
            address_edit.setText(address);
            lat = bundle.getDouble("param3");
            lng = bundle.getDouble("param4");

            Log.i("Add", "위도: " + lat);
            Log.i("Add", "경도: " + lng);
        }

        // 등록 버튼
        Button register = (Button) root.findViewById(R.id.add_toilet);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //firebase 정의
                mDatabase = FirebaseDatabase.getInstance("https://e-map-17167-default-rtdb.firebaseio.com/").getReference("MY등록");
                readToilet();

                EditText name = root.findViewById(R.id.toilet_name);
                EditText address = root.findViewById(R.id.toilet_address);
                EditText time = root.findViewById(R.id.toilet_time);

                String strName = name.getText().toString();
                String strAddress = address.getText().toString();
                String strTime = time.getText().toString();

                double douLat = lat;
                double douLng = lng;

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("name", strName);
                result.put("address", strAddress);
                result.put("detail", strTime);
                result.put("위도", douLat);
                result.put("경도", douLng);

                writeMyShop(strName, strAddress, strTime, douLat, douLng);

                mDatabase = FirebaseDatabase.getInstance("https://e-map-17167-default-rtdb.firebaseio.com/").getReference("Toilet");
                writeNewShop(strName, strAddress, strTime, douLat, douLng);
            }
        });


        return root;
    }

    private void readToilet() {
        mDatabase.child(((MainActivity)getActivity()).strId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(Toilet.class) != null){
                    Toilet post = dataSnapshot.getValue(Toilet.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    //Toast.makeText(getContext(), "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeMyShop(String toiletName, String toiletAddress, String toiletTime, double toiletLatitude, double toiletLongitude) {
        Toilet toilet = new Toilet(toiletName, toiletAddress, toiletTime, toiletLatitude, toiletLongitude);

        mDatabase.child(((MainActivity)getActivity()).strId).child("Toilet").child(toiletName).setValue(toilet)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void writeNewShop(String toiletName, String toiletAddress, String toiletTime, double toiletLatitude, double toiletLongitude) {
        Toilet toilet = new Toilet(toiletName, toiletAddress, toiletTime, toiletLatitude, toiletLongitude);

        mDatabase.child(((MainActivity)getActivity()).strId).child(toiletName).setValue(toilet)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}