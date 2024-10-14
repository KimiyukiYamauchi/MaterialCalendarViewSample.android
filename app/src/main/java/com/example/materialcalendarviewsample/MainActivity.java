package com.example.materialcalendarviewsample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CATEGORY_SAMPLE =
            "com.example.materialcalendarviewsample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ResolveInfoAdapter(this, getAllSampleActivities()));
    }

    private List<ResolveInfo> getAllSampleActivities() {
        Intent filter = new Intent();
        filter.setAction(Intent.ACTION_RUN);
        filter.addCategory(CATEGORY_SAMPLE);
        return getPackageManager().queryIntentActivities(filter, 0);
    }

    private void onRouteClicked(ResolveInfo route) {
        ActivityInfo activity = route.activityInfo;
        ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
        startActivity(new Intent(Intent.ACTION_VIEW).setComponent(name));
    }

    class ResolveInfoAdapter extends RecyclerView.Adapter<ResolveInfoAdapter.ResolveInfoViewHolder> {

        private final PackageManager pm;
        private final LayoutInflater inflater;
        private final List<ResolveInfo> samples;

        private ResolveInfoAdapter(Context context, List<ResolveInfo> resolveInfos) {
            this.samples = resolveInfos;
            this.inflater = LayoutInflater.from(context);
            this.pm = context.getPackageManager();
        }

        @Override
        public ResolveInfoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.item_route, viewGroup, false);
            return new ResolveInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ResolveInfoViewHolder viewHolder, int i) {
            ResolveInfo item = samples.get(i);
            viewHolder.textView.setText(item.loadLabel(pm));
        }

        @Override
        public int getItemCount() {
            return samples.size();
        }

        class ResolveInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView textView;

            public ResolveInfoViewHolder(View view) {
                super(view);
                this.textView = view.findViewById(android.R.id.text1);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(@NonNull View v) {
                onRouteClicked(samples.get(getAdapterPosition()));
            }
        }
    }
}