<TableLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:stretchColumns="*"
    android:padding="16dp"
    android:showDividers="middle"
    android:divider="?android:attr/dividerHorizontal"
    android:dividerPadding="0dp">

    <!-- Header Row (Excel-style column titles in bold) -->
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Column 1"
            android:textStyle="bold"
            android:textSize="16sp"
            android:gravity="center"
            android:padding="8dp"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Column 2"
            android:textStyle="bold"
            android:textSize="16sp"
            android:gravity="center"
            android:padding="8dp"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Column 3"
            android:textStyle="bold"
            android:textSize="16sp"
            android:gravity="center"
            android:padding="8dp"
            android:background="@drawable/table_cell_border" />
    </TableRow>

    <!-- Row 1 -->
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 1, Col 1"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 1, Col 2"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 1, Col 3"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />
    </TableRow>

    <!-- Row 2 -->
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 2, Col 1"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 2, Col 2"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 2, Col 3"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />
    </TableRow>

    <!-- Row 3 -->
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 3, Col 1"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 3, Col 2"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Row 3, Col 3"
            android:padding="8dp"
            android:textSize="14sp"
            android:gravity="center"
            android:background="@drawable/table_cell_border" />
    </TableRow>

</TableLayout>


<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@android:color/white" />
    <stroke android:width="1dp" android:color="#CCCCCC" /> <!-- Border color and thickness -->
    <corners android:radius="4dp" /> <!-- Rounded corners for each cell -->
</shape>



lll
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardAdapter cardAdapter;
    private List<String> cardTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        Button addButton = findViewById(R.id.addButton);

        // Initialize card list and adapter
        cardTitles = new ArrayList<>();
        cardAdapter = new CardAdapter(cardTitles);
        viewPager.setAdapter(cardAdapter);

        // Add button click listener
        addButton.setOnClickListener(v -> addNewCard(viewPager));
    }

    private void addNewCard(ViewPager2 viewPager) {
        // Add new card title
        cardTitles.add("Card " + (cardTitles.size() + 1));
        cardAdapter.notifyItemInserted(cardTitles.size() - 1);

        // Adjust height dynamically
        viewPager.post(() -> {
            int itemHeight = viewPager.getChildAt(0).getHeight();
            ViewGroup.LayoutParams params = viewPager.getLayoutParams();
            params.height = cardTitles.size() * itemHeight;
            viewPager.setLayoutParams(params);
        });
    }
}


ppppp

<LinearLayout
    android:id="@+id/progressBarContainer"
    android:layout_width="match_parent"
    android:layout_height="30dp"
    android:orientation="horizontal"
    android:background="@drawable/progress_bar_background"
    android:padding="2dp">

    <!-- Placeholder for dynamically added views -->
</LinearLayout>

<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#E0E0E0" />
    <corners android:radius="15dp" />
</shape>


import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout progressBarContainer = findViewById(R.id.progressBarContainer);

        // Example weights and colors
        float[] weights = {0.3f, 0.4f, 0.2f, 0.1f}; // Example dynamic weights
        String[] colors = {"#4CAF50", "#2196F3", "#9C27B0", "#BDBDBD"}; // Colors for segments

        // Clear any previous views
        progressBarContainer.removeAllViews();

        // Calculate total weight
        float totalWeight = 0;
        for (float weight : weights) {
            totalWeight += weight;
        }

        // Dynamically add segments
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > 0) { // Skip segments with zero/null weights
                View segment = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weights[i]);
                segment.setLayoutParams(params);
                segment.setBackgroundColor(Color.parseColor(colors[i]));

                // Apply corner radius for first and last segments
                if (i == 0) {
                    segment.setBackground(getResources().getDrawable(R.drawable.first_segment_background));
                } else if (i == weights.length - 1) {
                    segment.setBackground(getResources().getDrawable(R.drawable.last_segment_background));
                } else {
                    segment.setBackground(getResources().getDrawable(R.drawable.middle_segment_background));
                }

                progressBarContainer.addView(segment);
            }
        }
    }
}



<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#4CAF50" />
    <corners android:topLeftRadius="15dp" android:bottomLeftRadius="15dp" />
</shape>


<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#2196F3" />
    <corners android:radius="0dp" />
</shape>


<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#9C27B0" />
    <corners android:topRightRadius="15dp" android:bottomRightRadius="15dp" />
</shape>


p0p0p0
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout progressBarContainer = findViewById(R.id.progressBarContainer);

        // Example percentages for camera, microphone, and location
        int cameraPercentage = 30; // Example: 30%
        int microphonePercentage = 50; // Example: 50%
        int locationPercentage = 40; // Example: 40%

        // Call the function to update the progress bar
        updateProgressBar(progressBarContainer, cameraPercentage, microphonePercentage, locationPercentage);
    }

    private void updateProgressBar(LinearLayout progressBarContainer, int camera, int mic, int loc) {
        // Normalize percentages if the total is not 100
        int total = camera + mic + loc;
        if (total != 100) {
            float normalizationFactor = 100.0f / total;
            camera = Math.round(camera * normalizationFactor);
            mic = Math.round(mic * normalizationFactor);
            loc = Math.round(loc * normalizationFactor);

            // Adjust for rounding errors to ensure the total equals 100
            int normalizedTotal = camera + mic + loc;
            if (normalizedTotal > 100) {
                // Reduce the largest segment by 1
                if (camera >= mic && camera >= loc) camera -= 1;
                else if (mic >= loc) mic -= 1;
                else loc -= 1;
            } else if (normalizedTotal < 100) {
                // Increase the smallest segment by 1
                if (camera <= mic && camera <= loc) camera += 1;
                else if (mic <= loc) mic += 1;
                else loc += 1;
            }
        }

        // Clear any previous views
        progressBarContainer.removeAllViews();

        // Percentages converted to weights (0-1 range)
        float cameraWeight = camera / 100.0f;
        float micWeight = mic / 100.0f;
        float locWeight = loc / 100.0f;

        // Colors for each segment
        String[] colors = {"#4CAF50", "#2196F3", "#9C27B0"}; // Camera, Microphone, Location

        // Add Camera Segment
        addSegment(progressBarContainer, cameraWeight, colors[0], true, false);

        // Add Microphone Segment
        addSegment(progressBarContainer, micWeight, colors[1], false, false);

        // Add Location Segment
        addSegment(progressBarContainer, locWeight, colors[2], false, true);
    }

    private void addSegment(LinearLayout container, float weight, String color, boolean isFirst, boolean isLast) {
        if (weight > 0) {
            View segment = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight);
            segment.setLayoutParams(params);
            segment.setBackgroundColor(Color.parseColor(color));

            // Apply corner radius for the first and last segments
            if (isFirst) {
                segment.setBackground(getResources().getDrawable(R.drawable.first_segment_background));
            } else if (isLast) {
                segment.setBackground(getResources().getDrawable(R.drawable.last_segment_background));
            } else {
                segment.setBackground(getResources().getDrawable(R.drawable.middle_segment_background));
            }

            container.addView(segment);
        }
    }
}