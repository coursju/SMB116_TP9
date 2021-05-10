package com.smb116.tp9;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorsFragment extends Fragment implements SensorEventListener {

    private static final String TAG = "SensorsFragment";
    private SensorManager mSensorManager;

    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    private Sensor mMagneticField;
    private Sensor mPressure;

    private TextView mAccelerometreTitle;
    private TextView mAccelerometreX;
    private TextView mAccelerometreY;
    private TextView mAccelerometreZ;
    private TextView mAccelerometrePrecision;
    private TextView mGyroscopeTitle;
    private TextView mGyroscopeX;
    private TextView mGyroscopeY;
    private TextView mGyroscopeZ;
    private TextView mGyroscopePrecision;
    private TextView mLumiereTitle;
    private TextView mLumiere;
    private TextView mLumierePrecision;
    private TextView mChampMagnetiqueTitle;
    private TextView mChampMagnetiqueX;
    private TextView mChampMagnetiqueY;
    private TextView mChampMagnetiqueZ;
    private TextView mChampMagnetiquePrecision;
    private TextView mPressionTitle;
    private TextView mPression;
    private TextView mPressionPrecision;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sensors, container, false);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        configureSensors();
        bindView();

        return view;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometreX.setText("X: "+event.values[0]+" m/s2");
            mAccelerometreY.setText("Y: "+event.values[1]+" m/s2");
            mAccelerometreZ.setText("Z: "+event.values[2]+" m/s2");
            mAccelerometrePrecision.setText("Precision: "+getStringAccuracy(event.accuracy));
        }
        else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mGyroscopeX.setText("X: "+event.values[0]+" radians/s");
            mGyroscopeY.setText("Y: "+event.values[1]+" m/radians/s");
            mGyroscopeZ.setText("Z: "+event.values[2]+" m/radians/s");
            mGyroscopePrecision.setText("Precision: "+getStringAccuracy(event.accuracy));
        }
        else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            mLumiere.setText("Lumiere: "+event.values[0]+" lux");
            mLumierePrecision.setText("Precision: "+getStringAccuracy(event.accuracy));
        }
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mChampMagnetiqueX.setText("X: "+event.values[0]+" µT");
            mChampMagnetiqueY.setText("Y: "+event.values[1]+" µT");
            mChampMagnetiqueZ.setText("Z: "+event.values[2]+" µT");
            mChampMagnetiquePrecision.setText("Precision: "+getStringAccuracy(event.accuracy));
        }
        else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            mPression.setText("Lumiere: "+event.values[0]+" mbar");
            mPressionPrecision.setText("Precision: "+getStringAccuracy(event.accuracy));
        }
    }

    private void configureSensors(){
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }

    private void bindView(){
        mAccelerometreTitle = (TextView) view.findViewById(R.id.accelerometre_title);
        mAccelerometreX = (TextView) view.findViewById(R.id.accelerometre_x);
        mAccelerometreY = (TextView) view.findViewById(R.id.accelerometre_y);
        mAccelerometreZ = (TextView) view.findViewById(R.id.accelerometre_z);
        mAccelerometrePrecision = (TextView) view.findViewById(R.id.accelerometre_precision);
        mGyroscopeTitle = (TextView) view.findViewById(R.id.gyroscope_title);
        mGyroscopeX = (TextView) view.findViewById(R.id.gyroscope_x);
        mGyroscopeY = (TextView) view.findViewById(R.id.gyroscope_y);
        mGyroscopeZ = (TextView) view.findViewById(R.id.gyroscope_z);
        mGyroscopePrecision = (TextView) view.findViewById(R.id.gyroscope_precision);
        mLumiereTitle = (TextView) view.findViewById(R.id.lumiere_title);
        mLumiere = (TextView) view.findViewById(R.id.lumiere_lumiere);
        mLumierePrecision = (TextView) view.findViewById(R.id.lumiere_precision);
        mChampMagnetiqueTitle = (TextView) view.findViewById(R.id.champ_magnetique_title);
        mChampMagnetiqueX = (TextView) view.findViewById(R.id.champ_magnetique_x);
        mChampMagnetiqueY = (TextView) view.findViewById(R.id.champ_magnetique_y);
        mChampMagnetiqueZ = (TextView) view.findViewById(R.id.champ_magnetique_z);
        mChampMagnetiquePrecision = (TextView) view.findViewById(R.id.champ_magnetique_precision);
        mPressionTitle = (TextView) view.findViewById(R.id.pression_title);
        mPression = (TextView) view.findViewById(R.id.pression);
        mPressionPrecision = (TextView) view.findViewById(R.id.pression_precision);
    }

    public String getStringAccuracy(int precision){
        switch (precision){
            case 0: return "Pas fiable";
            case 1: return "Basse";
            case 2: return "Moyenne";
            case 3: return "Haute";
            default: return "Mausaise mesure";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}