package com.kumoh19.e_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MapView mapView;
    private GpsTracker gpsTracker;
    private DatabaseReference mDatabase;


    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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

    private void setChildFragment(Fragment child) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            childFt.replace(R.id.category_child_fragment, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) root.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        if (((MainActivity)getActivity()).checkLocationServicesStatus()) {
            ((MainActivity)getActivity()).checkRunTimePermission();
        } else {
            ((MainActivity)getActivity()).showDialogForLocationServiceSetting();
        }

        gpsTracker = new GpsTracker(getActivity());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(2, true);

        mapView.setPOIItemEventListener(this);

        // 쓰레기통 or 화장실 가져오기
        Bundle bundle = getArguments();
        if(bundle != null) {
            category = bundle.getString("param1");
            //Log.i("MapFragment", "카테고리 " + category);
        }

        mDatabase = FirebaseDatabase.getInstance("https://e-map-17167-default-rtdb.firebaseio.com/").getReference(category);

        if("Trashcan".equals(category)) {
            readTrashcan();
        }
        else if("Toilet".equals(category)) {
            readToilet();
        }

        return root;
    }

    String TrashcanAddress;
    String ToiletName;
    double Lat;
    double Lng;

    public void readTrashcan() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    for(DataSnapshot data1 : data.getChildren()) {
                        if(data.getValue(Trashcan.class) != null) {
                            Trashcan post = data1.getValue(Trashcan.class);
                            TrashcanAddress = post.getTrashcanAddress();
                            Lat = post.getTrashcanLatitude();
                            Lng = post.getTrashcanLongitude();

                            // 마커찍기
                            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(Lat, Lng);
                            MapPOIItem marker = new MapPOIItem();
                            marker.setItemName(TrashcanAddress);
                            marker.setTag(0);
                            marker.setMapPoint(MARKER_POINT);
                            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                            mapView.addPOIItem(marker);
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
    public void readToilet() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    for(DataSnapshot data1 : data.getChildren()) {
                        if(data.getValue(Toilet.class) != null) {
                            Toilet post = data1.getValue(Toilet.class);
                            ToiletName = post.getToiletName();
                            Lat = post.getToiletLatitude();
                            Lng = post.getToiletLongitude();

                            // 마커찍기
                            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(Lat, Lng);
                            MapPOIItem marker = new MapPOIItem();
                            marker.setItemName(ToiletName);
                            marker.setTag(0);
                            marker.setMapPoint(MARKER_POINT);
                            marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                            mapView.addPOIItem(marker);
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

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    Fragment fg;

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        if("Trashcan".equals(category)) {
            fg = TrashcanInfoFragment.newInstance(mapPOIItem.getItemName(), null);
            setChildFragment(fg);
        }
        else if("Toilet".equals(category)) {
            fg = ToiletInfoFragment.newInstance(mapPOIItem.getItemName(), null);
            setChildFragment(fg);
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}