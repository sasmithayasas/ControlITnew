package com.example.controlit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{

    Button btn_moreinfo;
    ImageView showList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<DeviceItem> devices = getDeviceList(); // your list
        DeviceAdapter adapter = new DeviceAdapter(getContext(),devices);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       btn_moreinfo = view.findViewById(R.id.btn_moreinfo);
        showList = view.findViewById(R.id.showList);
        btn_moreinfo.setOnClickListener(v -> {
            showMoreInfoPopup();
        });
        showList.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_Container, new TipsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });


    }

//    @Override
//    public void onMoreInfoClicked(DeviceItem device) {
//        showMoreInfoPopup(device);
//    }

    private List<DeviceItem> getDeviceList() {
        List<DeviceItem> list = new ArrayList<>();
        list.add(new DeviceItem("Tablet", R.drawable.tablet_image));
        list.add(new DeviceItem("Phone", R.drawable.laptop_image));
        list.add(new DeviceItem("Laptop", R.drawable.laptop_image));
        // Add more items as needed
        return list;
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
            // Trigger vibration
            Context context = popupView.getContext(); // or use activity context if available

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // API 31+ (Android 12)
                VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                Vibrator vibrator = vibratorManager.getDefaultVibrator();
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Below API 31
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100); // Deprecated but used for older versions
                }
            }

            // Continue with your popup
            showAreYouSurePopup(popupWindow);
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


//    private void showMoreInfoPopup(DeviceItem device) {
//        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.moreinfo_popup, null);
//
//        final PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                true
//        );
//
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//
//        View rootView = requireActivity().findViewById(android.R.id.content);
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//
//        FrameLayout container = popupView.findViewById(R.id.popup_container);
//        CardView cardView = popupView.findViewById(R.id.cardView);
//
//        container.setOnClickListener(v -> popupWindow.dismiss());
//        cardView.setOnClickListener(v -> {}); // Prevent dismiss on card click
//
//        // ✨ Set dynamic content
//        TextView nameText = popupView.findViewById(R.id.popup_device_name);
//        ImageView imageView = popupView.findViewById(R.id.popup_device_image);
//
//        nameText.setText(device.getName());
//        imageView.setImageResource(device.getImageResId());
//
//        // Close Button
//        Button closeBtn = popupView.findViewById(R.id.close_button);
//        closeBtn.setOnClickListener(v -> {
//            triggerVibration(popupView.getContext());
//            showAreYouSurePopup(popupWindow); // still same
//        });
//    }
//
//    private void triggerVibration(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
//            Vibrator vibrator = vibratorManager.getDefaultVibrator();
//            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else {
//            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
//            } else {
//                vibrator.vibrate(100);
//            }
//        }
//    }
//
//
//
//    private void showAreYouSurePopup(PopupWindow popupWindow) {
//        View secondPopupView = LayoutInflater.from(getContext()).inflate(R.layout.are_you_sure_popup, null);
//
//        final PopupWindow secondPopupWindow = new PopupWindow(
//                secondPopupView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                true
//        );
//
//        secondPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        secondPopupWindow.setOutsideTouchable(true);
//        secondPopupWindow.setFocusable(true);
//
//        View rootView = requireActivity().findViewById(android.R.id.content);
//        secondPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//
//        FrameLayout container = secondPopupView.findViewById(R.id.popup_container);
//        CardView cardView = secondPopupView.findViewById(R.id.cardView);
//
//        container.setOnClickListener(v -> secondPopupWindow.dismiss());
//        cardView.setOnClickListener(v -> {}); // Prevent dismiss
//
//        Button yesButton = secondPopupView.findViewById(R.id.btn_yes);
//        yesButton.setOnClickListener(v -> {
//            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
//            secondPopupView.startAnimation(fadeOut);
//
//            fadeOut.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {}
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    secondPopupWindow.dismiss();
//                    popupWindow.dismiss();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {}
//            });
//        });
//
//        Button noButton = secondPopupView.findViewById(R.id.btn_no);
//        noButton.setOnClickListener(v -> secondPopupWindow.dismiss());
//    }

}