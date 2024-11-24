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