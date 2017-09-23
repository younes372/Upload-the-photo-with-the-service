package ir.aliabadi.younes.myapplication;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by younes on 12/09/2017.
 */

public class Service extends android.app.Service {


	@Nullable
	@Override
	public IBinder onBind( Intent intent ) {

		return null;
	}


	@Override
	public void onCreate( ) {
		super.onCreate( );
	}

	@Override
	public int onStartCommand( Intent intent, int flags, int startId ) {

		String i1 = intent.getExtras( ).getString( "img1" );


		String image1 = getStringImage( i1 );


		JSONObject Object = new JSONObject( );

		try {
			Object.put( "image1", image1 );
		} catch ( JSONException e ) {
		}


		JsonObjectRequest request = new JsonObjectRequest
				( Request.Method.POST,"",Object,
						new Response.Listener< JSONObject >( ) {
							@Override
							public void onResponse( JSONObject response ) {


								Toast.makeText( Service.this, "ارسال شد", Toast.LENGTH_LONG ).show( );
								//	displayNotification( );
								Vibrator v = ( Vibrator ) getSystemService( Context.VIBRATOR_SERVICE );
								v.vibrate( 500 );
								stopSelf();
							}
						},
						new Response.ErrorListener( ) {
							@Override
							public void onErrorResponse( VolleyError error ) {


								Toast.makeText( Service.this, "ارسال نشد", Toast.LENGTH_LONG ).show( );
								Vibrator v = (Vibrator ) getSystemService( Context.VIBRATOR_SERVICE);
								v.vibrate(500);
								stopSelf();


							}
						} );

		request.setRetryPolicy( new DefaultRetryPolicy( 18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
		Volley.newRequestQueue( getApplication( ) ).add( request );




		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy( ) {
		super.onDestroy( );
	}


	public String getStringImage( String b ) {
		Bitmap bm1 = BitmapFactory.decodeFile( b );
		ByteArrayOutputStream bao = new ByteArrayOutputStream( );
		Bitmap bm = Bitmap.createScaledBitmap( bm1, 600, 600, true );
		bm.compress( Bitmap.CompressFormat.JPEG, 100, bao );
		byte[] ba = bao.toByteArray( );
		String ba1 = Base64.encodeToString( ba, Base64.DEFAULT );
		return ba1;
	}


	protected void displayNotification( ) {

		Intent intent = new Intent( this, MainActivity.class );
		PendingIntent contentIntent = PendingIntent.getActivity( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

		NotificationCompat.Builder b = new NotificationCompat.Builder( this );

		b.setAutoCancel( true )
				.setDefaults( Notification.DEFAULT_ALL )
				.setWhen( System.currentTimeMillis( ) )
				.setSmallIcon( R.mipmap.ic_launcher )
				.setTicker( "Hearty365" )
				.setContentTitle( "نام نرم افزار" )
				.setContentText( "عکس شما آپلود شد به زودی به اشتراک گذاشته میشود." )
				.setDefaults( Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND )
				//	.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentIntent( contentIntent )
				.setContentInfo( "Info" );


		NotificationManager notificationManager = ( NotificationManager ) this.getSystemService( Context.NOTIFICATION_SERVICE );
		notificationManager.notify( 1, b.build( ) );

	}


}

	/*try {
			int responseStatus = response.getInt( "response" );

			if ( responseStatus == 1 ) {

			stopSelf( );

			Toast.makeText( Service.this, "ارسال شد", Toast.LENGTH_LONG ).show( );
			displayNotification( );
			Vibrator v = ( Vibrator ) getSystemService( Context.VIBRATOR_SERVICE );
			v.vibrate( 500 );
			} else {
			Toast.makeText( Service.this, "ارسال نشد", Toast.LENGTH_LONG ).show( );
			Vibrator v = ( Vibrator ) getSystemService( Context.VIBRATOR_SERVICE );
			v.vibrate( 500 );
			stopSelf( );
			}

			} catch ( JSONException e ) {
			e.printStackTrace( );
			stopSelf( );
			}

*/