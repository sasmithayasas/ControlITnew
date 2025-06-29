package com.example.controlit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ImportantFragment extends Fragment {

    TextView textView;

    public ImportantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_important, container, false);

        textView = view.findViewById(R.id.clickableNotif);
        textView.setOnClickListener(v -> {
            showAreYouSurePopup();
        });

        return view;
    }


    private void showAreYouSurePopup() {
        View secondPopupView = LayoutInflater.from(getContext()).inflate(R.layout.delete_notif_popup, null);

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
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });

        Button noButton = secondPopupView.findViewById(R.id.btn_no); // Optional
        noButton.setOnClickListener(v -> secondPopupWindow.dismiss());
    }
}