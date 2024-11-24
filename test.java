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