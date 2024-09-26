package com.example.rinkdepth2.ui.jrRink;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth2.R;
import com.example.rinkdepth2.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView touchLocation;
        ImageView jrRinkImage;

        touchLocation = root.findViewById(R.id.touchLocation);
        jrRinkImage = root.findViewById(R.id.jrRinkImg);
        jrRinkImage.setImageResource(R.drawable.jrrink);

        jrRinkImage.setOnTouchListener(((view, motionEvent) -> {
            String location = motionEvent.getX() + " " + motionEvent.getY();
            touchLocation.setText(location);
            return true;
        }));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}