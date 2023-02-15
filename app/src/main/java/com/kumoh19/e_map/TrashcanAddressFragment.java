package com.kumoh19.e_map;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashcanAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashcanAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrashcanAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrashcanAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrashcanAddressFragment newInstance(String param1, String param2) {
        TrashcanAddressFragment fragment = new TrashcanAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static TrashcanAddressFragment newInstance() {
        return new TrashcanAddressFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setParentFragment(Fragment parent) {
        FragmentTransaction parentFt = getParentFragmentManager().beginTransaction();

        if (!parent.isAdded()) {
            parentFt.replace(R.id.register_child_fragment, parent);
            parentFt.addToBackStack(null);
            parentFt.commit();
        }
    }

    private GpsTracker gpsTracker;
    String ADR;
    Fragment fg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trashcan_address, container, false);
        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) root.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        gpsTracker = new GpsTracker(getActivity());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(2, true);

        //마커 찍기
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재위치");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);


        Geocoder mGeoCoder = new Geocoder(getActivity());
        try {
            List<Address> mResultList = mGeoCoder.getFromLocation(
                    latitude,
                    longitude,
                    1
            );
            ADR = mResultList.get(0).getAddressLine(0);
            TextView address = (TextView) root.findViewById(R.id.address_name);
            address.setText(ADR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button register_btn = (Button) root.findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                fg = TrashcanRegFragment.newInstance(ADR, latitude, longitude);
                setParentFragment(fg);
            }
        });

        return root;
    }
}