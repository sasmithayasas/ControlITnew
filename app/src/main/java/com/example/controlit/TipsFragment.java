package com.example.controlit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class TipsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static final String JSON_URL = "https://gist.githubusercontent.com/sasmithayasas/a70fe352e9779a5d663384e469c9a121/raw/d9f202287b5690bdfdf4d91389e75b8da05b76c7/tips.json";

    public TipsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = new ProgressBar(getContext());
        fetchTipsFromUrl();

        return view;
    }

    private void fetchTipsFromUrl() {
        new Thread(() -> {
            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream())
                );

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                urlConnection.disconnect();

                loadTipsFromJson(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Loading local data", Toast.LENGTH_SHORT).show()
                );
                loadLocalJsonFallback();
            }
        }).start();
    }

    private void loadTipsFromJson(String jsonData) {
        Gson gson = new Gson();
        Tip[] tipsArray = gson.fromJson(jsonData, Tip[].class);
        List<Tip> tipsList = Arrays.asList(tipsArray);

        requireActivity().runOnUiThread(() -> {
            TipAdapter adapter = new TipAdapter(tipsList);
            recyclerView.setAdapter(adapter);
        });
    }

    private void loadLocalJsonFallback() {
        try {
            InputStream is = requireContext().getAssets().open("tips_fallback.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            loadTipsFromJson(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), "Failed to load local data", Toast.LENGTH_LONG).show()
            );
        }
    }
}