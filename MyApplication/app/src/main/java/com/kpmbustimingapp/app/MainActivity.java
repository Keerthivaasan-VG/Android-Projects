package com.kpmbustimingapp.app;




import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kpmbustimingapp.app.BusAdapter;
import com.kpmbustimingapp.app.BusInfo;
import com.google.android.material.tabs.TabLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private LinearLayout contentContainer;
    private LinearLayout headerLayout;
    private TextView nextBusTitle;
    private TextView routeHeader;
    private CardView nextBusCard1, nextBusCard2;
    private TextView nextBusTime1, nextBusType1, nextBusMinutes1;
    private TextView nextBusTime2, nextBusType2, nextBusMinutes2;
    private TextView fullListHeader;
    private RecyclerView busListRecyclerView;
    private ImageView busAnimationImage;
    private LinearLayout nextBusSection;

    private BusAdapter busAdapter;
    private List<BusInfo> currentBusList;
    private Handler handler;
    private Runnable updateRunnable;

    private int currentTab = 0;

    // Track the last bus time we showed to prevent re-animation
    private String lastDepartedBusTime = "";

    // Theme colors
    private final int BLUE_COLOR = Color.parseColor("#1565C0");
    private final int LIGHT_GREEN_COLOR = Color.parseColor("#4CAF50");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupTabs();
        loadBusData(0);
        startAutoUpdate();
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tabLayout);
        contentContainer = findViewById(R.id.contentContainer);
        headerLayout = findViewById(R.id.headerLayout);
        nextBusTitle = findViewById(R.id.nextBusTitle);
        routeHeader = findViewById(R.id.routeHeader);
        nextBusCard1 = findViewById(R.id.nextBusCard1);
        nextBusCard2 = findViewById(R.id.nextBusCard2);
        nextBusTime1 = findViewById(R.id.nextBusTime1);
        nextBusType1 = findViewById(R.id.nextBusType1);
        nextBusMinutes1 = findViewById(R.id.nextBusMinutes1);
        nextBusTime2 = findViewById(R.id.nextBusTime2);
        nextBusType2 = findViewById(R.id.nextBusType2);
        nextBusMinutes2 = findViewById(R.id.nextBusMinutes2);
        fullListHeader = findViewById(R.id.fullListHeader);
        busListRecyclerView = findViewById(R.id.busListRecyclerView);
        busAnimationImage = findViewById(R.id.busAnimationImage);
        nextBusSection = findViewById(R.id.nextBusSection);

        busListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("KPM to தேனி"));
        tabLayout.addTab(tabLayout.newTab().setText("தேனி to KPM"));

        // Set initial theme - Blue for Tab 1
        updateTheme(BLUE_COLOR);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                currentTab = position;

                // Reset departure tracking when switching tabs
                lastDepartedBusTime = "";

                if (position == 0) {
                    // Tab 1: Blue theme, left-to-right animation
                    animateTabSwitch(BLUE_COLOR, true);
                } else {
                    // Tab 2: Light Green theme, right-to-left animation
                    animateTabSwitch(LIGHT_GREEN_COLOR, false);
                }

                loadBusData(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void animateTabSwitch(int newColor, boolean leftToRight) {
        // Slide out animation
        float endX = leftToRight ? -contentContainer.getWidth() : contentContainer.getWidth();

        ObjectAnimator slideOut = ObjectAnimator.ofFloat(contentContainer, "translationX", 0, endX);
        slideOut.setDuration(300);
        slideOut.setInterpolator(new AccelerateDecelerateInterpolator());

        slideOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Update theme color
                updateTheme(newColor);

                // Slide in from opposite direction
                float slideInStart = leftToRight ? contentContainer.getWidth() : -contentContainer.getWidth();
                ObjectAnimator slideIn = ObjectAnimator.ofFloat(contentContainer, "translationX", slideInStart, 0);
                slideIn.setDuration(300);
                slideIn.setInterpolator(new AccelerateDecelerateInterpolator());
                slideIn.start();

                // Animate bus driving
                animateBusDriving(leftToRight);
            }
        });

        slideOut.start();
    }

    private void animateBusDriving(boolean leftToRight) {
        busAnimationImage.setVisibility(View.VISIBLE);

        // Make the bus icon bigger - scale it up to 3.5x size for better visibility
        busAnimationImage.setScaleY(3.5f);  // Increase vertical scale
        if (leftToRight) {
            busAnimationImage.setScaleX(3.5f);  // Increase horizontal scale for left-to-right
        } else {
            busAnimationImage.setScaleX(-3.5f);  // Increase horizontal scale (negative for flip)
        }

        // Get the width of the screen
        int containerWidth = getResources().getDisplayMetrics().widthPixels;

        // Start and end positions - with extra padding to ensure bus travels completely off-screen
        // For larger scaled bus (3.5x), we need more offset
        float startX = leftToRight ? -405 : containerWidth + 405;
        float endX = leftToRight ? containerWidth + 405 : -405;

        busAnimationImage.setTranslationX(startX);
        busAnimationImage.setAlpha(0f);

        // Fade in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(busAnimationImage, "alpha", 0f, 1f);
        fadeIn.setDuration(200);
        fadeIn.start();

        // Main driving animation
        ObjectAnimator busAnim = ObjectAnimator.ofFloat(busAnimationImage, "translationX", startX, endX);
        busAnim.setDuration(2000);
        busAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        // Add slight bounce effect (like bus on road)
        ObjectAnimator bounceAnim = ObjectAnimator.ofFloat(busAnimationImage, "translationY", 0f, -3f, 0f, -2f, 0f);
        bounceAnim.setDuration(2000);
        bounceAnim.setRepeatCount(0);

        busAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Fade out at the end
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(busAnimationImage, "alpha", 1f, 0f);
                fadeOut.setDuration(200);
                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        busAnimationImage.setVisibility(View.GONE);
                        busAnimationImage.setAlpha(1f);
                        busAnimationImage.setTranslationY(0f);
                        // Reset scale for next animation
                        busAnimationImage.setScaleX(1f);
                        busAnimationImage.setScaleY(1f);
                    }
                });
                fadeOut.start();
            }
        });

        // Start both animations together
        busAnim.start();
        bounceAnim.start();
    }

    private void updateTheme(int color) {
        // Apply color to header layout
        if (headerLayout != null) {
            headerLayout.setBackgroundColor(color);
        }

        // Apply color to tab layout background
        tabLayout.setBackgroundColor(color);

        // Apply color to tab indicator
        tabLayout.setSelectedTabIndicatorColor(color);

        // Apply color to full list header
        fullListHeader.setBackgroundColor(color);

        // Apply color to the status bar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    private void loadBusData(int tabPosition) {
        currentBusList = new ArrayList<>();

        if (tabPosition == 0) {
            // KPM to Theni
            routeHeader.setText("KPM to தேனி");
            fullListHeader.setText("KPM to தேனி (முழு பட்டியல்)");
            currentBusList = generateKPMToTheniData();
        } else {
            // Theni to KPM
            routeHeader.setText("தேனி to KPM");
            fullListHeader.setText("தேனி to KPM (முழு பட்டியல்)");
            currentBusList = generateTheniToKPMData();
        }

        updateNextBuses();

        busAdapter = new BusAdapter(currentBusList);
        busListRecyclerView.setAdapter(busAdapter);
    }

    private List<BusInfo> generateKPMToTheniData() {
        List<BusInfo> buses = new ArrayList<>();
        buses.add(new BusInfo("06:15 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("06:45 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("06:45 AM", "Govt bus (via Dharmapuri, Kottur)"));

        buses.add(new BusInfo("07:40 AM", "Sait (via Thadichery)"));
        buses.add(new BusInfo("08:20 AM", "Senthil Murugan (via Veerapandi)"));
        buses.add(new BusInfo("08:40 AM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("09:20 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("09:45 AM", "Govt bus (via Dharmapuri, Kottur)"));

        buses.add(new BusInfo("10:00 AM", "Rathinam (via Veerapandi)"));
        buses.add(new BusInfo("10:40 AM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("11:05 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("11:40 AM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("12:30 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("12:40 PM", "Govt bus (via Dharmapuri, Kottur)"));

        buses.add(new BusInfo("01:40 PM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("02:50 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("03:20 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("03:40 PM", "Govt bus (via Dharmapuri, Kottur)"));
        buses.add(new BusInfo("03:55 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("04:15 PM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("05:20 PM", "Govt bus (via Kupinayakanpatti - Thadichery)"));
        buses.add(new BusInfo("05:50 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("06:30 PM", "Govt bus (via Dharmapuri, Kottur)"));
        buses.add(new BusInfo("06:30 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("06:40 PM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("07:45 PM", "Govt bus (via Thadichery - Time to Confirm)"));

        buses.add(new BusInfo("08:20 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("09:10 PM", "Govt bus (via Thadichery or Srirangapuram)"));
        buses.add(new BusInfo("09:45 PM", "Govt bus (via Dharmapuri, Kottur)"));

        buses.add(new BusInfo("10:10 PM", "Govt bus (via Thadichery)"));
        return buses;
    }

    private List<BusInfo> generateTheniToKPMData() {
        List<BusInfo> buses = new ArrayList<>();
        buses.add(new BusInfo("05:10 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("05:35 AM", "Govt bus (via Dharmapuri)"));

        buses.add(new BusInfo("07:00 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("07:50 AM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("08:00 AM", "Govt bus (via Kupinayakanpatti)"));
        buses.add(new BusInfo("08:30 AM", "Govt bus (via Dharmapuri)"));
        buses.add(new BusInfo("08:45 AM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("09:35 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("09:45 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("10:00 AM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("11:00 AM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("11:20 AM", "Govt bus (via Dharmapuri)"));
        buses.add(new BusInfo("11:55 AM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("01:05 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("02:15 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("02:20 PM", "Govt bus (via Dharmapuri)"));
        buses.add(new BusInfo("02:45 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("03:30 PM", "Senthil Murugan (via Veerapandi)"));
        buses.add(new BusInfo("03:40 PM", "Govt bus (via Kupinayakanpatti)"));

        buses.add(new BusInfo("04:15 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("05:00 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("05:15 PM", "Govt bus (via Dharmapuri)"));
        buses.add(new BusInfo("05:20 PM", "Sait (via Thadichery)"));

        buses.add(new BusInfo("06:10 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("06:50 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("07:30 PM", "Govt bus (via Thadichery)"));

        buses.add(new BusInfo("08:40 PM", "Govt bus (via Thadichery)"));
        buses.add(new BusInfo("08:45 PM", "Govt bus (via Dharmapuri)"));

        buses.add(new BusInfo("10:30 PM", "Govt bus (via Thadichery)"));
        return buses;
    }

    private void updateNextBuses() {
        if (currentBusList == null || currentBusList.isEmpty()) {
            nextBusSection.setVisibility(View.GONE);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        List<BusInfo> upcomingBuses = new ArrayList<>();

        // Get current time
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentTotalMinutes = currentHour * 60 + currentMinute;

        // Find upcoming buses
        for (BusInfo bus : currentBusList) {
            try {
                Date busTime = sdf.parse(bus.time);
                if (busTime != null) {
                    Calendar busCal = Calendar.getInstance();
                    busCal.setTime(busTime);
                    int busHour = busCal.get(Calendar.HOUR_OF_DAY);
                    int busMinute = busCal.get(Calendar.MINUTE);
                    int busTotalMinutes = busHour * 60 + busMinute;

                    // If bus time is in the future
                    if (busTotalMinutes > currentTotalMinutes) {
                        upcomingBuses.add(bus);
                        if (upcomingBuses.size() == 2) break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // If no upcoming buses found (end of day), show first 2 buses of next day
        if (upcomingBuses.isEmpty() && currentBusList.size() >= 2) {
            upcomingBuses.add(currentBusList.get(0));
            upcomingBuses.add(currentBusList.get(1));
        } else if (upcomingBuses.size() == 1 && currentBusList.size() >= 2) {
            // If only 1 upcoming bus, add first bus of next day as second
            upcomingBuses.add(currentBusList.get(0));
        }

        // Display next bus section
        nextBusSection.setVisibility(View.VISIBLE);

        // Update first bus card
        if (upcomingBuses.size() >= 1) {
            BusInfo nextBus1 = upcomingBuses.get(0);
            nextBusCard1.setVisibility(View.VISIBLE);
            nextBusTime1.setText(nextBus1.time);
            nextBusType1.setText(nextBus1.type);

            long minutesLeft = calculateMinutesLeft(nextBus1.time);
            if (minutesLeft < 0) {
                // If negative, it's tomorrow's first bus
                nextBusMinutes1.setText("நாளை முதல் பஸ்");
            } else {
                nextBusMinutes1.setText(minutesLeft + " நிமிடங்கள் உள்ளன");
            }
        } else {
            nextBusCard1.setVisibility(View.GONE);
        }

        // Update second bus card
        if (upcomingBuses.size() >= 2) {
            BusInfo nextBus2 = upcomingBuses.get(1);
            nextBusCard2.setVisibility(View.VISIBLE);
            nextBusTime2.setText(nextBus2.time);
            nextBusType2.setText(nextBus2.type);

            long minutesLeft = calculateMinutesLeft(nextBus2.time);
            if (minutesLeft < 0) {
                // If negative, it's tomorrow's bus
                nextBusMinutes2.setText("நாளை முதல் பஸ்");
            } else {
                nextBusMinutes2.setText(minutesLeft + " நிமிடங்கள் உள்ளன");
            }
        } else {
            nextBusCard2.setVisibility(View.GONE);
        }
    }

    private long calculateMinutesLeft(String busTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date busDate = sdf.parse(busTime);

            if (busDate != null) {
                Calendar busCal = Calendar.getInstance();
                busCal.setTime(busDate);

                Calendar nowCal = Calendar.getInstance();

                // Set same date for comparison
                busCal.set(Calendar.YEAR, nowCal.get(Calendar.YEAR));
                busCal.set(Calendar.MONTH, nowCal.get(Calendar.MONTH));
                busCal.set(Calendar.DAY_OF_MONTH, nowCal.get(Calendar.DAY_OF_MONTH));

                long diff = busCal.getTimeInMillis() - nowCal.getTimeInMillis();
                return diff / (60 * 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void startAutoUpdate() {
        handler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateNextBuses();
                if (busAdapter != null) {
                    busAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 30000); // Update every 30 seconds
            }
        };
        handler.post(updateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }
}