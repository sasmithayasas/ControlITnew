package com.example.controlit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


public class HomeFragment extends Fragment {

    Button btn_moreinfo;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_moreinfo = view.findViewById(R.id.btn_moreinfo);
        btn_moreinfo.setOnClickListener(v -> {
            showMoreInfoPopup();
        });


    }



    private void showMoreInfoPopup() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.moreinfo_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Show in the center
        View rootView = requireActivity().findViewById(android.R.id.content);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        // Dismiss when outside CardView is clicked
        FrameLayout container = popupView.findViewById(R.id.popup_container);
        CardView cardView = popupView.findViewById(R.id.cardView);

        container.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        // Prevent dismiss when clicking inside the CardView
        cardView.setOnClickListener(v -> {
            // Do nothing – this prevents the container click from triggering
        });

        Button closeBtn = popupView.findViewById(R.id.close_button);
        closeBtn.setOnClickListener(v -> {
            //popupWindow.dismiss();

            // Optional delay to ensure clean UI transition
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                showAreYouSurePopup(popupWindow);
//            }, 200); // 200ms delay
        });
    }

    private void showAreYouSurePopup(PopupWindow popupWindow) {
        View secondPopupView = LayoutInflater.from(getContext()).inflate(R.layout.are_you_sure_popup, null);

        final PopupWindow secondPopupWindow = new PopupWindow(secondPopupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        secondPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        secondPopupWindow.setOutsideTouchable(true);
        secondPopupWindow.setFocusable(true);

        View rootView = requireActivity().findViewById(android.R.id.content);
        secondPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        // Handle outside tap to dismiss (reuse same structure as before)
        FrameLayout container = secondPopupView.findViewById(R.id.popup_container);
        CardView cardView = secondPopupView.findViewById(R.id.cardView);

        Button yesButton = secondPopupView.findViewById(R.id.btn_yes);
        yesButton.setOnClickListener(v -> {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

            secondPopupView.startAnimation(fadeOut);

            // After animation ends, dismiss both popups
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    secondPopupWindow.dismiss();
                    popupWindow.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });

        Button noButton = secondPopupView.findViewById(R.id.btn_no); // Optional
        noButton.setOnClickListener(v -> secondPopupWindow.dismiss());
    }
}