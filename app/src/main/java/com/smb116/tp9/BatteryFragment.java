package com.smb116.tp9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryFragment extends Fragment {

    private static final String TAG = "BatteryFragment";
    private View view;
    private BroadcastReceiver batteryInfoReceiver;

    private TextView mTechnology;
    private TextView mHealth;
    private TextView mLevel;
    private TextView mPlugged;
    private TextView mPresent;
    private TextView mStatus;
    private TextView mTemperature;
    private TextView mVoltage;
    private TextView mCapacity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_battery, container, false);
        bindView();
        configureRegister();
        return view;
    }

    public void bindView(){
        mTechnology = view.findViewById(R.id.battery_statut);
        mHealth = view.findViewById(R.id.battery_health);
        mLevel = view.findViewById(R.id.battery_level);
        mPlugged = view.findViewById(R.id.battery_plugged);
        mPresent = view.findViewById(R.id.battery_present);
        mStatus = view.findViewById(R.id.battery_statut);
        mTemperature = view.findViewById(R.id.battery_temperature);
        mVoltage = view.findViewById(R.id.battery_voltage);
        mCapacity = view.findViewById(R.id.battery_capacity);
    }

    private void configureRegister(){
        batteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateBatteryData(intent);
            }
        };
    }

    private void loadBatterySection() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        getActivity().registerReceiver(batteryInfoReceiver, intentFilter);
    }

    private void updateBatteryData(Intent intent) {

        Boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        mPresent.setText("Present: "+String.valueOf(present));

        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        int healthLbl = -1;

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                healthLbl = R.string.battery_health_cold;
                break;

            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthLbl = R.string.battery_health_dead;
                break;

            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthLbl = R.string.battery_health_good;
                break;

            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthLbl = R.string.battery_health_over_voltage;
                break;

            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthLbl = R.string.battery_health_overheat;
                break;

            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthLbl = R.string.battery_health_unspecified_failure;
                break;

            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            default:
                break;
        }

        if (healthLbl != -1) {
            mHealth.setText("Health : " + getString(healthLbl));
        }

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                mLevel.setText("Level : " + batteryPct + " %");
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            int pluggedLbl;

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    pluggedLbl = R.string.battery_plugged_wireless;
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    pluggedLbl = R.string.battery_plugged_usb;
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    pluggedLbl = R.string.battery_plugged_ac;
                    break;

                default:
                    pluggedLbl = R.string.battery_plugged_none;
                    break;
            }

            mPlugged.setText("Plugged : " + getString(pluggedLbl));

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int statusLbl;

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusLbl = R.string.battery_status_charging;
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusLbl = R.string.battery_status_discharging;
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    statusLbl = R.string.battery_status_full;
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusLbl = -1;
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    statusLbl = R.string.battery_status_discharging;
                    break;
            }

            if (statusLbl != -1) {
                mStatus.setText("Battery : " + getString(statusLbl));
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                mTechnology.setText("Technology : " + technology);
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                mTemperature.setText("Temperature : " + temp + "C");
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                mVoltage.setText("Voltage : " + voltage + " mV");
            }

            long capacity = getBatteryCapacity(getContext());

            if (capacity > 0) {
                mCapacity.setText("Capacity : " + capacity + " mAh");
            }
    }

    public long getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }

        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBatterySection();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(batteryInfoReceiver);
    }
}