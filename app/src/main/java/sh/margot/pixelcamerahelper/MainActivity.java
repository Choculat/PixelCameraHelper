package sh.margot.pixelcamerahelper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sh.margot.pixelcamerahelper.databinding.ActivityMainBinding;
import sh.margot.pixelcamerahelper.databinding.ItemAppBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final List<ItemAppBinding> cards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        populate();
    }

    private void populate() {
        PackageManager pm = getPackageManager();
        Intent view = new Intent(Intent.ACTION_VIEW);
        view.setDataAndType(Uri.parse("content://media/external/images/media/1"), "image/*");

        // Dedupe by package, skip ourselves
        Map<String, ResolveInfo> apps = new LinkedHashMap<>();
        for (ResolveInfo ri : pm.queryIntentActivities(view, PackageManager.MATCH_ALL)) {
            String pkg = ri.activityInfo.packageName;
            if (!getPackageName().equals(pkg)) apps.putIfAbsent(pkg, ri);
        }

        String selected = Prefs.targetPackage(this);
        LinearLayout container = binding.appContainer;
        container.removeAllViews();
        cards.clear();
        LayoutInflater inflater = getLayoutInflater();

        for (ResolveInfo ri : apps.values()) {
            String pkg = ri.activityInfo.packageName;
            ItemAppBinding item = ItemAppBinding.inflate(inflater, container, false);
            item.appIcon.setImageDrawable(ri.loadIcon(pm));
            item.appName.setText(ri.loadLabel(pm));
            item.appPackage.setText(pkg);
            item.checkIcon.setVisibility(pkg.equals(selected) ? View.VISIBLE : View.GONE);
            item.getRoot().setTag(pkg);
            item.getRoot().setOnClickListener(v -> select(pkg));
            container.addView(item.getRoot());
            cards.add(item);
        }
    }

    private void select(String pkg) {
        Prefs.setTargetPackage(this, pkg);
        for (ItemAppBinding item : cards) {
            item.checkIcon.setVisibility(pkg.equals(item.getRoot().getTag()) ? View.VISIBLE : View.GONE);
        }
    }
}
