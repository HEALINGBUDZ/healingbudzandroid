package com.codingpixel.healingbudz.Utilities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetUserLatLng {
    public void getUserLocation(final Context context, final UserLocationListner locationListner) {
        if (isLocationServicesAvailable(context)) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            final LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    locationListner.onLocationSuccess(location);
                }

                public void onProviderDisabled(String arg0) {

                    locationListner.onLocationFailed(arg0);
                }

                public void onProviderEnabled(String arg0) {

                    locationListner.onLocationFailed(arg0);

                }

                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

                    locationListner.onLocationFailed(arg0 + "/" + arg1 + "/" + arg2);

                }
            };
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationListner.onLocationFailed("permisson error..");
                return;
            }
            Location get_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (Build.BRAND.equalsIgnoreCase("Samsung")) {
                if (Build.DEVICE.equalsIgnoreCase("Note 5")) {
                    get_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            if (get_location != null) {
                locationListner.onLocationSuccess(get_location);
            } else {
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                if (Build.BRAND.equalsIgnoreCase("Samsung")) {
                    if (Build.DEVICE.equalsIgnoreCase("Note 5")) {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                    } else {
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                    }
                } else {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                }
            }
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(context.getString(R.string.title_location_permission))
                    .setContentText(context.getString(R.string.text_location_permission_open))
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
//            new AlertDialog.Builder(context)
//                    .setTitle(R.string.title_location_permission)
//                    .setMessage(R.string.text_location_permission_open)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //Prompt the user once explanation has been shown
//                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    })
//                    .create()
//                    .show();
        }
    }

    public void getUserLocation(final Context context, final UserLocationListner locationListner, boolean isFromMAinBudz) {
        if (isLocationServicesAvailable(context)) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            final LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    locationListner.onLocationSuccess(location);
                }

                public void onProviderDisabled(String arg0) {

                    locationListner.onLocationFailed(arg0);
                }

                public void onProviderEnabled(String arg0) {

                    locationListner.onLocationFailed(arg0);

                }

                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

                    locationListner.onLocationFailed(arg0 + "/" + arg1 + "/" + arg2);

                }
            };
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationListner.onLocationFailed("permisson error..");
                return;
            }
            Location get_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (Build.BRAND.equalsIgnoreCase("Samsung")) {
                if (Build.DEVICE.equalsIgnoreCase("Note 5")) {
                    get_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            if (get_location != null) {
                locationListner.onLocationSuccess(get_location);
            } else {
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                if (Build.BRAND.equalsIgnoreCase("Samsung")) {
                    if (Build.DEVICE.equalsIgnoreCase("Note 5")) {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                    } else {
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                    }
                } else {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
                }
            }
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(context.getString(R.string.title_location_permission))
                    .setContentText(context.getString(R.string.text_location_permission_open))
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setCancelText("Cancel")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            locationListner.onLocationFailed("");
                        }
                    })
                    .show();
//            new AlertDialog.Builder(context)
//                    .setTitle(R.string.title_location_permission)
//                    .setMessage(R.string.text_location_permission_open)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //Prompt the user once explanation has been shown
//                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    })
//                    .create()
//                    .show();
        }
    }

    public void GetAddressFromLocation(Context context, Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList;
        try {
            myList = myLocation.getFromLocation(latitude, longitude, 1);
            if (myList.size() == 1) {
                myList.get(0).toString();
            }
        } catch (IOException e1) {

            e1.printStackTrace();
        }
    }

    public static String GetAddressFromLatlng(Context context, double latitude, double longitude) {
        if (context != null) {
            Geocoder myLocation = new Geocoder(context, Locale.getDefault());
            List<Address> myList;
            try {
                myList = myLocation.getFromLocation(latitude, longitude, 1);
                if (myList.size() == 1) {
                    return myList.get(0).getAddressLine(0);
                } else {
                    return "";
                }
            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.getMessage();
            }
        } else {
            return "";
        }

    }

    public static String GetZipcode_AddressFromLatlng(Context context, double latitude, double longitude) {
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList;
        try {
            myList = myLocation.getFromLocation(latitude, longitude, 1);
            if (myList.size() == 1) {
                for (int i = 0; i < myList.size(); i++) {
                    if (myList.get(i).getPostalCode() != null) {
                        return myList.get(0).getAddressLine(0) + "&&&---&&&" + myList.get(i).getPostalCode();
                    }
                }
                return myList.get(0).getAddressLine(0) + "&&&---&&&" + myList.get(0).getPostalCode();
            } else {
                return "";
            }
        } catch (IOException e1) {

            e1.printStackTrace();
            Log.d("GetZipcode: ", e1.getLocalizedMessage());
            return e1.getMessage();
        }
    }

    public static boolean isLocationServicesAvailable(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }

        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable && (coarsePermissionCheck || finePermissionCheck);
    }
}
