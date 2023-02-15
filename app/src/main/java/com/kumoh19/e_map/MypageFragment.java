package com.kumoh19.e_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kumoh19.e_map.databinding.FragmentMypageBinding;

public class MypageFragment extends Fragment {

    private MypageViewModel notificationsViewModel;
    private FragmentMypageBinding binding;
    String strNickname, strProfile, strId;

    Fragment fg;

    private void setChildFragment(Fragment child) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            childFt.replace(R.id.mypage_child_fragment, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        TextView tvNickname = view.findViewById(R.id.profile_name);
        ImageView ivProfile = view.findViewById(R.id.profile_image);


        // Inflate the layout for this fragment
        strId = ((MainActivity)getActivity()).strId;
        strNickname = ((MainActivity)getActivity()).strNickname;
        strProfile = ((MainActivity)getActivity()).strProfile;
        //Log.d("HomeActivity","myseo: nickname" + strNickname);
        tvNickname.setText(strNickname);
        Glide.with(this).load(strProfile).into(ivProfile);

        // MY등록 버튼
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                fg = MyRegisterFragment.newInstance(strId, null);
                setChildFragment(fg);
            }
        });

        // 로그아웃 버튼
        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}