package com.kumoh19.e_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kumoh19.e_map.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    Fragment fg;

    private void setChildFragment(Fragment child) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            childFt.replace(R.id.register_child_fragment, child);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        // 쓰레기통 버튼
        ImageButton btn_trashcan = (ImageButton) root.findViewById(R.id.btn_trashcan);
        btn_trashcan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fg = TrashcanRegFragment.newInstance();
                setChildFragment(fg);
            }
        });

        // 화장실 버튼
        ImageButton btn_toilet = (ImageButton) root.findViewById(R.id.btn_toilet);
        btn_toilet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fg = ToiletRegFragment.newInstance();
                setChildFragment(fg);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}