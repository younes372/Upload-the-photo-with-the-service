package ir.aliabadi.younes.myapplication;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {

	public RequestManager mGlideRequestManager;
	Uri selectedUri1;
	private ImageView ivImage1;
	private Button btn_send;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mGlideRequestManager = Glide.with( this );



		ivImage1 = ( ImageView ) findViewById( R.id.imageView );
		btn_send = (Button) findViewById( R.id.button );


		ivImage1.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {

				setSingleShowButton();

			}
		} );


		btn_send.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {

				Intent data = new Intent( MainActivity.this, Service.class );
				data.putExtra( "img1", selectedUri1.getPath() );
				startService( data );

				Toast.makeText( MainActivity.this, " در حال ارسال... لطفا صبر کنید ", Toast.LENGTH_LONG ).show( );



			}
		} );






	}


	private void setSingleShowButton( ) {

		PermissionListener permissionlistener = new PermissionListener( ) {
			@Override
			public void onPermissionGranted( ) {


				TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder( MainActivity.this )
						.setOnImageSelectedListener( new TedBottomPicker.OnImageSelectedListener( ) {
							@Override
							public void onImageSelected( final Uri uri ) {
								selectedUri1 = uri;

								ivImage1.setVisibility( View.VISIBLE );
								//mSelectedImagesContainer.setVisibility(View.GONE);
								ivImage1.post( new Runnable( ) {
									@Override
									public void run( ) {

										mGlideRequestManager
												.load( uri )
												.into( ivImage1 );

									}
								} );

							}
						} )
						//.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
						.setSelectedUri( selectedUri1 )
						.setPreviewMaxCount(20)
						//این زیاد نبر بالا باعث میشه اگر گوشی طرف کوچیک باشه ارور بده //
						.setPeekHeight( 800 )//این تابع یه جورای نوشتن روی کارت حافضه حساب میشه باید اجاره اون رو هم بگیری
						.create( );
				bottomSheetDialogFragment.show( getSupportFragmentManager( ) );




			}

			@Override
			public void onPermissionDenied( ArrayList< String > deniedPermissions ) {
				Toast.makeText( MainActivity.this, "اجازه  داده نشد\n" + deniedPermissions.toString( ), Toast.LENGTH_SHORT ).show( );
			}


		};
		TedPermission.with( MainActivity.this )
				.setPermissionListener( permissionlistener )
				.setDeniedMessage( "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]" )
				.setPermissions( Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.check( );

	}


}
