package jp.ac.hal.androiduberremake.Services;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

import jp.ac.hal.androiduberremake.Common;
import jp.ac.hal.androiduberremake.Model.EventBus.DriverRequestRecieved;
import jp.ac.hal.androiduberremake.Utils.UserUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            UserUtils.updateToken(this,s);
    }


    //ここで受け取るかも
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> dataRecv = remoteMessage.getData();
        if(dataRecv != null)
        {
            if(dataRecv.get(Common.NOTI_TITLE).equals(Common.REQUEST_DRIVER_TITLE))
            {
                DriverRequestRecieved driverRequestRecieved = new DriverRequestRecieved();
                driverRequestRecieved.setKey(dataRecv.get(Common.RIDER_KEY));
                driverRequestRecieved.setPickupLocation(dataRecv.get(Common.RIDER_PICKUP_LOCATION));
                driverRequestRecieved.setPickupLocationString(dataRecv.get(Common.RIDER_PICKUP_LOCATION_STRING));
                driverRequestRecieved.setDestinationLocation(dataRecv.get(Common.RIDER_DESTINATION));
                driverRequestRecieved.setDestinationLocationString(dataRecv.get(Common.RIDER_DESTINATION_STRING));
                EventBus.getDefault().postSticky(driverRequestRecieved);
            }
            else {
                Common.showNotification(this, new Random().nextInt(),
                        dataRecv.get(Common.NOTI_TITLE),
                        dataRecv.get(Common.NOTI_CONTENT),
                        null);
            }
        }
    }
}
