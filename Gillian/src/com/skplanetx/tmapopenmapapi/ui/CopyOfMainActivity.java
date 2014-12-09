package com.skplanetx.tmapopenmapapi.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skplanetx.tmapopenmapapi.bluetooth.*;
import com.skp.Tmap.BizCategory;
import com.skp.Tmap.TMapCircle;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.BizCategoryListenerCallback;
import com.skp.Tmap.TMapData.ConvertGPSToAddressListenerCallback;
import com.skp.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skp.Tmap.TMapData.FindAroundNamePOIListenerCallback;
import com.skp.Tmap.TMapData.FindPathDataAllListenerCallback;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData.TMapPathType;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapLabelInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapPolygon;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapView.MapCaptureImageListenerCallback;
import com.skp.Tmap.TMapView.TMapLogoPositon;
import com.skplanetx.tmapopenmapapi.LogManager;
import com.skplanetx.tmapopenmapapi.R;
import com.skplanetx.tmapopenmapapi.bluetooth.BluetoothChatService;

public class CopyOfMainActivity extends BaseActivity implements onLocationChangedCallback
{
	@Override
	public void onLocationChange(Location location) {
		LogManager.printLog("onLocationChange " + location.getLatitude() +  " " + location.getLongitude());
		if(m_bTrackingMode) {
			mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
		}
	}

	private TMapView		mMapView = null;
	

	private TMapPoint startpoint = null;
	private TMapPoint endpoint= null;
	private TMapPolyLine polyLine=null; //그림그리??객체
	private TMapPoint pointA ;
	
	private TMapData tmapdata ; 
	private ArrayList<TMapPoint> RoutePoint = new ArrayList<TMapPoint>();
	private Navigation navi =null;
	private SendMassgeHandler mMainHandler = null;
	private int i = 0 ;
	double oldangle;
	
	 private Context 		mContext;
	private ArrayList<Bitmap> mOverlayList;
	private ImageOverlay mOverlay;

	public static String mApiKey; // 발급받�? appKey
	public static String mBizAppID; // 발급받�? BizAppID (TMapTapi�?TMap???�동??????BizAppID �??�요)
	private TextView state;
	private TextView dis;
	private Button seticon ;
	
	private boolean isDevice = false;
//	private boolean selectFinish=false;
	 AlertDialog alert;
	
	private ImageView goStraight ;
	private ImageView turnLeft;
	private ImageView turnRight;
	
	//////////////// bluetooth 
	private Activity myActivity ;
	   private static final String TAG = "MainActivity";
	    private static final boolean D = true;

	    // Message types sent from the BluetoothChatService Handler
	    public static final int MESSAGE_STATE_CHANGE = 1;
	    public static final int MESSAGE_READ = 2;
	    public static final int MESSAGE_WRITE = 3;
	    public static final int MESSAGE_DEVICE_NAME = 4;
	    public static final int MESSAGE_TOAST = 5;

	    // Key names received from the BluetoothChatService Handler
	    public static final String DEVICE_NAME = "device_name";
	    public static final String TOAST = "toast";

	    // Intent request codes
	    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	    private static final int REQUEST_ENABLE_BT = 3;


	    // Name of the connected device
	    private String mConnectedDeviceName = null;
	    // Array adapter for the conversation thread
//	    private ArrayAdapter<String> mConversationArrayAdapter;
	    // String buffer for outgoing messages
	    private StringBuffer mOutStringBuffer;
	    // Local Bluetooth adapter
	    private BluetoothAdapter mBluetoothAdapter = null;
	    // Member object for the chat services
	    private BluetoothChatService mChatService = null;

	    private ProgressDialog mProgressDialog;
	    
    private String leftDevice ="0";
    private String rightDevice ="1" ;

    Intent serverIntent = null;
    
    ///////////////
	private static final int[] mArrayMapButton = {

		R.id.btnAnimateTo,
		R.id.btnSetIcon,
		R.id.btnSetCompassMode,
		R.id.POISearch,
		R.id.btncancel
	
	};

	private 	int 		m_nCurrentZoomLevel = 0;
	private 	double 		m_Latitude  = 0;
	private     double  	m_Longitude = 0;
	private 	boolean 	m_bShowMapIcon = false;

	private 	boolean 	m_bTrafficeMode = false;
	private 	boolean 	m_bSightVisible = false;
	private 	boolean 	m_bTrackingMode = false;
	
	private 	boolean 	m_bOverlayMode = false;
	
	ArrayList<String>		mArrayID;
	
	ArrayList<String>		mArrayCircleID;
	private static 	int 	mCircleID;
	
	ArrayList<String>		mArrayLineID;
	private static 	int 	mLineID;
	
	ArrayList<String>		mArrayPolygonID;
	private static  int 	mPolygonID;

	ArrayList<String>       mArrayMarkerID;
	private static int 		mMarkerID;
	
	TMapGpsManager gps = null;


	protected Handler mHandler1;






	
	/**
	 * onCreate() 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_activity);
		
		startActivity(new Intent(this,LoadingActivity.class));
		setTitle("");
		mContext = this;
		
		mMapView = new TMapView(this);
		addView(mMapView);
		
		configureMapView();
		
		initView();
		
		mArrayID = new ArrayList<String>();
		
		mArrayCircleID = new ArrayList<String>();
		mCircleID = 0;
		
		mArrayLineID = new ArrayList<String>();
		mLineID = 0;
		
		mArrayPolygonID = new ArrayList<String>();
		mPolygonID = 0;
		
		mArrayMarkerID	= new ArrayList<String>();
		mMarkerID = 0;
		
		
		gps = new TMapGpsManager(CopyOfMainActivity.this);
		gps.setMinTime(1000);
		gps.setMinDistance(5);
		
		gps.setProvider(gps.NETWORK_PROVIDER);
		gps.OpenGps();
		
		mMapView.setTMapLogoPosition(TMapLogoPositon.POSITION_BOTTOMRIGHT);
LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		Location location = locationManager
	                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		double latitude = location.getLatitude();
	    double longitude = location.getLongitude();  
	    
		TMapPoint point = new TMapPoint(latitude, longitude);
		/////////////////////////
        point.setLatitude(37.374127);
        point.setLongitude(126.634211);
        ///////////////////////////////
		mMapView.setCenterPoint(point.getLongitude(),point.getLatitude());
		mMapView.setIconVisibility(true);
		mMapView.setLocationPoint(point.getLongitude(),point.getLatitude());
		
		startpoint = point;
		
		seticon = (Button)findViewById(mArrayMapButton[1]);
		seticon.setEnabled(false);
//        /////////////////////////////////////
//	    state = (TextView)findViewById(R.id.state);
//	    dis = (TextView)findViewById(R.id.dis);
//	    
	    goStraight = (ImageView)findViewById(R.id.go_straight);
		turnLeft= (ImageView)findViewById(R.id.turn_left);
		turnRight= (ImageView)findViewById(R.id.turn_right);
		
    	goStraight.setVisibility(View.GONE);
    	turnLeft.setVisibility(View.GONE);
    	turnRight.setVisibility(View.GONE);
    	//////////// bluetooth Ac
    	myActivity= this;
    	
	    mMainHandler = new SendMassgeHandler();
	      
	}

	/**
	 * setSKPMapApiKey()??ApiKey�??�력 ?�다.
	 * setSKPMapBizappId()??mBizAppID�??�력?�다.
	 * -> setSKPMapBizappId??TMapTapi(TMap???�동)�??�용?�때 BizAppID ?�정 ?�야 ?�다. TMapTapi ?�용?��? ?�는?�면 setSKPMapBizappId�??��? ?�아???�다.
	 */
	private void configureMapView() {
		mMapView.setSKPMapApiKey("1ca0c796-467a-34a5-bceb-773d01c98ae0");
		mMapView.setSKPMapBizappId(mBizAppID);
	}

	/**
	 * initView - 버튼????�� 리스?��? ?�록?�다. 
	 */
	private void initView() {	
		for (int btnMapView : mArrayMapButton) {
			Button ViewButton = (Button)findViewById(btnMapView);
			ViewButton.setOnClickListener(this);
		}

		mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
			@Override
			public void SKPMapApikeySucceed() {
				LogManager.printLog("MainActivity SKPMapApikeySucceed");
			}
			
			@Override
			public void SKPMapApikeyFailed(String errorMsg) {
				LogManager.printLog("MainActivity SKPMapApikeyFailed " + errorMsg);
			}
		});
		
		mMapView.setOnBizAppIdListener(new TMapView.OnBizAppIdListenerCallback() {
			@Override
			public void SKPMapBizAppIdSucceed() {
				LogManager.printLog("MainActivity SKPMapBizAppIdSucceed");
			}
			
			@Override
			public void SKPMapBizAppIdFailed(String errorMsg) {
				LogManager.printLog("MainActivity SKPMapBizAppIdFailed " + errorMsg);
			}
		});

		
		mMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
			@Override
			public void onEnableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
				LogManager.printLog("MainActivity onEnableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
			}
		});

		mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
			@Override
			public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
				LogManager.printLog("MainActivity onDisableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
			}
		});
		
		mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
			@Override
			public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
				LogManager.printLog("MainActivity onPressUpEvent " + markerlist.size());
				return false;
			}
			
			@Override
			public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
				LogManager.printLog("MainActivity onPressEvent " + markerlist.size());

				for (int i = 0; i < markerlist.size(); i++) {
					TMapMarkerItem item = markerlist.get(i);
					LogManager.printLog("MainActivity onPressEvent " + item.getName() + " " + item.getTMapPoint().getLatitude() + " " + item.getTMapPoint().getLongitude());
				}
				return false;
			}
		});
		
		mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
			@Override
			public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) {
				LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
				 TMapData tmapdata = new TMapData(); 
//		            TMapPoint endpoint = new TMapPoint(point.getLatitude(), point.getLongitude());
				 
				 TMapPoint endpoint = new TMapPoint(37.378126  , 126.645284);
		            TMapPolyLine polyLine=null;
		            ///////////////////
//		            startpoint.setLatitude(37.374127);
//		            startpoint.setLongitude(126.634211);
		            //////////////////
		            try {
		                polyLine = tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startpoint, endpoint);
		             } catch (MalformedURLException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             } catch (IOException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             } catch (ParserConfigurationException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             } catch (FactoryConfigurationError e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             } catch (SAXException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             } 
//		            for(TMapPoint tmp : polyLine.getLinePoint()){
//		                RoutePoint.add(tmp);
//		                LogManager.printLog("abc"+tmp.toString());
//		             }
		            
		            RoutePoint.add(new TMapPoint(37.374123 , 126.634211));
		            RoutePoint.add(new TMapPoint(37.374182  , 126.63411));
		            RoutePoint.add(new TMapPoint(37.374182  , 126.63411));
		            RoutePoint.add(new TMapPoint(37.37386  , 126.633816));
		            RoutePoint.add(new TMapPoint(37.373104  , 126.633141));
		            RoutePoint.add(new TMapPoint(37.372658  , 126.63272));
		            RoutePoint.add(new TMapPoint(37.372658  , 126.63272));
		            RoutePoint.add(new TMapPoint(37.371952  , 126.63393));
		            RoutePoint.add(new TMapPoint(37.371952  , 126.63393));
		            RoutePoint.add(new TMapPoint(37.374019  , 126.635891));
		            RoutePoint.add(new TMapPoint(37.375203  , 126.636976));
		            RoutePoint.add(new TMapPoint(37.376122  , 126.63775));
		            RoutePoint.add(new TMapPoint(37.376295  , 126.637892));
		            
		            RoutePoint.add(new TMapPoint(37.377686  , 126.638929));
		            RoutePoint.add(new TMapPoint(37.378759    , 126.639745));
		            RoutePoint.add(new TMapPoint(37.379367   , 126.640141));
		            RoutePoint.add(new TMapPoint(37.38013  , 126.64068));
		            RoutePoint.add(new TMapPoint(37.380684  , 126.641077));
		            RoutePoint.add(new TMapPoint(37.381438  , 126.641616));
		            RoutePoint.add(new TMapPoint(37.381584  , 126.641715));
		            
		            RoutePoint.add(new TMapPoint(37.381584  , 126.641715));
		            RoutePoint.add(new TMapPoint(37.38165   , 126.641963));
		            RoutePoint.add(new TMapPoint(37.381678   , 126.642086));
		            RoutePoint.add(new TMapPoint(37.381688  , 126.642131));
		            RoutePoint.add(new TMapPoint(37.381689  , 126.642233));
		            RoutePoint.add(new TMapPoint(37.381672  , 126.642301));
		            RoutePoint.add(new TMapPoint(37.381645  , 126.64238));
		            RoutePoint.add(new TMapPoint(37.381539  , 126.642541));
		            RoutePoint.add(new TMapPoint(37.379511  , 126.64409));
		            RoutePoint.add(new TMapPoint(37.378081  , 126.645192));
		            RoutePoint.add(new TMapPoint(37.378081  , 126.645192));
		            RoutePoint.add(new TMapPoint(37.378126  , 126.645284));
		             
		             showMarkerPoint(startpoint, "출발지", "");
		             showMarkerPoint(endpoint, "목적지", "");
		             mMapView.addTMapPolyLine("Go Home",polyLine);
		             LogManager.printLog("long end ");
		             
		             seticon.setEnabled(true);
//				pointA = point;
//				
//	            //TMapPoint endpoint = new TMapPoint(point.getLatitude(), point.getLongitude());
//	            
//				//hana
//				// 출발�? ?�착�??�택 ?�림�?�?��.
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//				
//				builder.setTitle("출발�??�착�?? ?�택?�세??); // ?�목 ?�정
//				
//				builder.setCancelable(true); // ?�로 버튼 ?�릭??취소 �?�� ?�정
//				builder.setPositiveButton("출발", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						// 출발 버튼 ?�릭???�정
//						startpoint = new TMapPoint(pointA.getLatitude(), pointA.getLongitude());		
//						
//					}
//				});
//				
//				builder.setNegativeButton("?�착", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						// ?�착 버튼 ?�릭???�정
//						endpoint = new TMapPoint(pointA.getLatitude(), pointA.getLongitude());
//						TMapData tmapdata = new TMapData(); 
//						
//						if(startpoint == null)
//						{
//							///////////////////////////?�기�??�돼??
//							Toast.makeText(getApplicationContext(), "출발 ?�인", Toast.LENGTH_LONG).show();
//							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//							builder.setTitle("출발�?? ?�택??주세??) ;
//							
//							builder.setNegativeButton("?�인", new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									dialog.cancel();
//								}
//							});
//						}else{
//						}
//						
//							// 경로?�정
//							try {
//								polyLine = tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startpoint, endpoint);
//							} catch (MalformedURLException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (ParserConfigurationException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (FactoryConfigurationError e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (SAXException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//			            
//							mMapView.addTMapPolyLine("Go Home",polyLine);
//							//?�착버튼 ?�르�?출발�?�� ?�동////////////////////////////////////
////							LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////							
////							Location location = locationManager
////						                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////
////							double latitude = location.getLatitude();
////						    double longitude = location.getLongitude();  
////						    
////							TMapPoint point = new TMapPoint(latitude, longitude);
////							mMapView.setCenterPoint(point.getLongitude(),point.getLatitude());
//						
//					}
//				});
				
//				AlertDialog dialog = builder.create();    // ?�림�?객체 ?�성
//				dialog.show();    // ?�림�??�우�?
			}
		});
		
		mMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
			@Override
			public void onCalloutRightButton(TMapMarkerItem markerItem) {
				String strMessage = "";
				strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
				Common.showAlertDialog(CopyOfMainActivity.this, "Callout Right Button", strMessage);
			}
		});
		
		mMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
			@Override
			public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
				if(findReverseLabel != null) {
					LogManager.printLog("MainActivity setOnClickReverseLabelListener " + findReverseLabel.id + " / " + findReverseLabel.labelLat
							 + " / " + findReverseLabel.labelLon + " / " + findReverseLabel.labelName);

				}
			}
		});
		
		m_nCurrentZoomLevel = -1;
		m_bShowMapIcon = false;
		m_bTrafficeMode = false;
		m_bSightVisible = false;
		m_bTrackingMode = false;	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		gps.CloseGps();
		if(mOverlayList != null){
			mOverlayList.clear();
		}
		//Debug.stopMethodTracing();
		System.gc() ;
	}
	
	/**
	 * onClick Event 
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		 case R.id.btnAnimateTo        :    animateTo();          break;
	      case R.id.btnSetIcon        :      goNavi();            break; //?�중???�정?�기
	      case R.id.btnSetCompassMode   :      setCompassMode();      break;
	      case R.id.btncancel           :     cacelNavi();         break;
	      case R.id.POISearch			:		findAllPoi();	break;
		}
	} 
	
	public TMapPoint randomTMapPoint() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		Location location = locationManager
	                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		double latitude = location.getLatitude();//((double)Math.random() ) * (37.575113-37.483086) + 37.483086;
	    double longitude = location.getLongitude();//((double)Math.random() ) * (127.027359-126.878357) + 126.878357;    

	    
		TMapPoint point = new TMapPoint(latitude, longitude);
		
		return point;
	}
	
//	public void overlay() {
//		m_bOverlayMode = !m_bOverlayMode;
//		if(m_bOverlayMode) {
//			mMapView.setZoomLevel(6);
//			
//			if(mOverlay == null){
//				mOverlay = new ImageOverlay(this, mMapView);
//			}
//			
//			mOverlay.setLeftTopPoint(new TMapPoint(45.640171, 114.9652948));
//			mOverlay.setRightBottomPoint(new TMapPoint(29.2267177, 138.7206798));
//			mOverlay.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));
//			
//			if(mOverlayList == null){
//				mOverlayList = new ArrayList<Bitmap>();
//				mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));
//				mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani1));
//				mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani2));
//			}
//			
//			mOverlay.setAnimationIcons(mOverlayList);
//			mOverlay.setAniDuration(10000);
//			mOverlay.startAnimation();
//			mMapView.addTMapOverlayID(0, mOverlay);
//		} else {
//			mOverlay.stopAnimation();
//			mMapView.removeTMapOverlayID(0);
//		}
//	}
	
	public void animateTo() {
		TMapPoint point = randomTMapPoint();
		mMapView.setIconVisibility(true);
		
		mMapView.setCenterPoint(point.getLongitude(), point.getLatitude(), true);
		mMapView.setLocationPoint(point.getLongitude(), point.getLatitude());
	}
	  public void goNavi()
	   {/*
	      Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
	      
	      //intent.putStringArrayListExtra("route", tmapdata);
	      intent.putStringArrayListExtra("RoutePoint", RoutePoint);
	      startActivity(intent);
	      finish();*/
//	      //////////////////////////////////////
		  
		    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage("어떻게 안내 받으시겠습니까? ^0^").setCancelable(
		        false).setPositiveButton("지도 안내 ",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            // Action for 'Yes' Button
		        	isDevice=false;
//		        	alert.dismiss();
		        	//////////////////
		  	      goNavigation();
			      navi= new Navigation();
			      navi.set(polyLine, mMapView, getBaseContext());
			      navi.start();
			      
			      ////////////////////
		        }
		        }).setNegativeButton("LED, 진동 안내",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            // Action for 'NO' Button
		        	isDevice=true;
				    
		        	blueCreate();
		        	blueStart();
//		        	blueResume();
		        	Selected();
//		        	setupBluetoothChat();
		        	
		        	
		        	
		            
//		            

//		        	dialog.cancel();
		        }
		        });
		    alert = alt_bld.create();
		    // Title for AlertDialog
		    alert.setTitle("Gillian");
		    // Icon for AlertDialog
//		    alert.setIcon(R.drawable.icon);
		    alert.show();


//		    	

	    
	   }
	   
	   private void cacelNavi() {
	      // TODO Auto-generated method stub
		i = 0 ;
		isDevice=false;
	      cancelNavigation();
	      navi.stopNavigation();
	      mMapView.removeTMapPolyLine("Go Home");
	      mMapView.removeMarkerItem("now");
	      mMapView.removeMarkerItem("mok");
	      seticon.setEnabled(false);
	    	goStraight.setVisibility(View.GONE);
	    	turnLeft.setVisibility(View.GONE);
	    	turnRight.setVisibility(View.GONE);
	    	RoutePoint.clear();
	      animateTo();
			
	      
	   }
	   // Handler ?�래??
	    class SendMassgeHandler extends Handler {
	        @Override
	        public void handleMessage(Message msg) {
		           boolean ok = false;
		            super.handleMessage(msg);
		            TMapPoint po = (TMapPoint) msg.obj;
		            
//		            double old_distance;
		            double distance = 0 ; 
		            double angle = 0 ; 
		            
		            TMapPoint back = RoutePoint.get(i);
		            if((i>2)&&(i<9))
		            {
		            	LogManager.printLog("좌회전");
		            	if(!isDevice){
		            	goStraight.setVisibility(View.GONE);
		            	turnLeft.setVisibility(View.VISIBLE);
		            	turnRight.setVisibility(View.GONE);
		            	}else{
		            		LogManager.printLog("devece left");
		            		sendBluetoothMessage(leftDevice);
		            			
		            	}
		            }else if ( (i >14) && (i < 28))
		            {
		            	if(!isDevice){
		            		LogManager.printLog("우회전");
		            	goStraight.setVisibility(View.GONE);
		            	turnLeft.setVisibility(View.GONE);
		            	turnRight.setVisibility(View.VISIBLE);
		            	}else
		            	{
		            		LogManager.printLog("devece right");
		            		sendBluetoothMessage(rightDevice);
		            	}
		            }else if(i == RoutePoint.size()-1)
		            {
		            	LogManager.printLog("도착");
		            	goStraight.setVisibility(View.GONE);
		            	turnLeft.setVisibility(View.GONE);
		            	turnRight.setVisibility(View.GONE);
		            	cacelNavi();
		            	
		            }else
		            {

		            	
		            	if(!isDevice){
		            		LogManager.printLog("직진");
			            	goStraight.setVisibility(View.VISIBLE);
			            	turnLeft.setVisibility(View.GONE);
			            	turnRight.setVisibility(View.GONE);
		            	}else
		            	{
		            		LogManager.printLog("device go");
//		            		btService.write(rightDevice);
		            	}
		            }
		            mMapView.setCenterPoint(back.getLongitude()+0.001, back.getLatitude());
		            
		            showMarkerPoint3(back);
		            i++;
		           }
	       };
	       double findLine(TMapPoint front, TMapPoint back, TMapPoint nowLocation)
	       {
	          double x1 = back.getLongitude();
	          double x2 = front.getLongitude();
	          double y1 = back.getLatitude();
	          double y2 = front.getLatitude();
	          
	          double a = nowLocation.getLongitude();
	          double b = nowLocation.getLatitude();
	          
	          double ki = (y1-y2)/(x1-x2);
	          
	          double d = Math.abs(b-(ki)*a+x1*ki-y1)/(Math.sqrt((a*a) + (b*b)));
	          return d;
	       }
	       class Navigation extends Thread{
	    	      TMapPolyLine polyLine;
	    	      TMapView mMapView ; 
	    	      Context mContext;
	    	      boolean r = true;
	    	      TMapPoint point;
	    	      
	    	      public void set(TMapPolyLine polyLine, TMapView mMapView, Context mContext )
	    	      {
	    	         this.polyLine=polyLine;
	    	         this.mMapView = mMapView;
	    	         this.mContext = mContext;
	    	         
	    	      }
	    	      public void stopNavigation()
	    	      {
	    	         r=false;
	    	      }
	    	      
	    	      public void restartNavigation()
	    	      {
	    	         r=true;
	    	      }
	    	      public void run()
	    	      {
	    	         while(r)
	    	         {
	    	            Message msg = mMainHandler.obtainMessage();
	    	            
	    	            point = randomTMapPoint();
	    	            msg.obj = point;
	    	            mMainHandler.sendMessage(msg);
//	    	            mMapView.setCenterPoint(point.getLongitude(), point.getLatitude(), true);
//	    	            mMapView.setLocationPoint(point.getLongitude(), point.getLatitude());
	    	            
	    	            try {
	    	               Thread.sleep(800);
	    	            } catch (InterruptedException e) {
	    	               // TODO Auto-generated catch block
	    	               e.printStackTrace();
	    	            }
	    	         }
	    	      }
	    	   }
	    	      
	public Map<String, AroundusItems> mAroundusItemHash = new LinkedHashMap<String, AroundusItems>();
	
	public void setAroundus() {
		AroundusJsonItem aroundusItem = requestAroundersList();
		addAroundersMarker(aroundusItem);
	}
	
	public AroundusJsonItem requestAroundersList() {
		TMapPoint center = mMapView.getCenterPoint();

		TMapPoint rightBottom = mMapView.getRightBottomPoint();
		TMapPoint leftTop = mMapView.getLeftTopPoint();
		
		AroundusJsonItem aroundusItem = null;

		StringBuilder url = new StringBuilder();
		url.append("adRequest?version=1");
		url.append("&centerLat=").append(center.getLatitude());
		url.append("&centerLon=").append(center.getLongitude());
		url.append("&reqCoordType=").append("WGS84GEO");
		url.append("&resCoordType=").append("WGS84GEO");
		url.append("&rightLat=").append(rightBottom.getLatitude());
		url.append("&rightLon=").append(rightBottom.getLongitude());
		url.append("&leftLat=").append(leftTop.getLatitude());
		url.append("&leftLon=").append(leftTop.getLongitude());
		url.append("&countReqAd=").append(10);
		url.append("&formAd=0");
		
		try {
			aroundusItem = new AroundusJsonItem(url);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return aroundusItem;
	}
	
	public void addAroundersMarker(AroundusJsonItem aroundusItem) {
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		try {
			for(int i = 0; i < aroundusItem.getPoiItems().size(); i++) {
				AroundusItems poiItems = aroundusItem.getPoiItems().get(i);
	
				Bitmap bgBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.poi_img);
				Bitmap iconBitmap = BitmapFactory.decodeStream(new URL(poiItems.getIcon()).openStream());
				Bitmap marker = overlayMark(bgBitmap, iconBitmap, width, height);
				
				TMapPoint point = new TMapPoint(poiItems.getLatitude(), poiItems.getLongitude());
				AroundusOverlay item = new AroundusOverlay(mContext, mMapView);
	
				String strID = String.format("%02d", i);
				item.setID(strID);
				item.setTMapPoint(point);	
				item.setIcon(marker);
				item.setPosition(0.5f, 1.0f);
	
				iconBitmap = Bitmap.createScaledBitmap(iconBitmap, 100, 100, true);
				item.setLeftImage(iconBitmap);
				item.setTitle(poiItems.getCompany());
				item.setSubTitle(poiItems.getPromotion());
	
				mAroundusItemHash.put(strID, poiItems);
				mMapView.addMarkerItem2(strID, item);
	
				mMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
					@Override
					public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
						LogManager.printLog("ClickEvent " + " id " + id + " " + mAroundusItemHash.get(id).getUrl());
						LogManager.printLog("ClickEvent " + " id " + id + " " + mAroundusItemHash.get(id).getAdClickKey());
	
						Intent intent = new Intent(getBaseContext(), WebActivity.class);
						intent.putExtra("URL", mAroundusItemHash.get(id).getUrl());
						intent.putExtra("ADCLICKKEY", mAroundusItemHash.get(id).getAdClickKey());
						startActivity(intent);
					}
				});
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, int width, int height) {
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		
		int marginLeft = 7;
		int marginTop = 5;

		if(width >= 1500 || height > 1500) {
			bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 40, bmp1.getHeight() - 50, true);
			marginLeft = 20;
			marginTop = 10;
		} else if(width >= 1200 || height > 1200) {
			bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 22, bmp1.getHeight() - 35, true);
			marginLeft = 11;
			marginTop = 7;
		} else {
			bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 15, bmp1.getHeight() - 25, true);
		}
		
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bmp1, 0, 0, null);
		canvas.drawBitmap(bmp2, marginLeft, marginTop, null);
		return bmOverlay;
	}
	
	/**
	 * mapZoomIn
	 * �?���??�단�??��??�다. 
	 */
	public void mapZoomIn() {
		mMapView.MapZoomIn();   
	}
	
	/**
	 * mapZoomOut
	 * �?���??�단�?축소?�다. 
	 */
	public void mapZoomOut() {
		mMapView.MapZoomOut();
	}
	
	/**
	 * getZoomLevel
	 * ?�재 줌의 ?�벨??�??�??�다. 
	 */
	public void getZoomLevel() {
		int nCurrentZoomLevel = mMapView.getZoomLevel();
		Common.showAlertDialog(this, "", "?�재 Zoom Level : " + Integer.toString(nCurrentZoomLevel));
	}
	
	/**
	 * setZoomLevel
	 * Zoom Level???�정?�다. 
	 */
	public void setZoomLevel() {
    	final String[] arrString = getResources().getStringArray(R.array.a_zoomlevel);
		AlertDialog dlg = new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("Select Zoom Level")
			.setSingleChoiceItems(R.array.a_zoomlevel, m_nCurrentZoomLevel, new DialogInterface.OnClickListener() {						
				@Override
				public void onClick(DialogInterface dialog, int item) {							
					m_nCurrentZoomLevel = item;
					dialog.dismiss();
					mMapView.setZoomLevel(Integer.parseInt(arrString[item]));					
				}
			}).show();		
    }
    
    /**
     * seetMapType  
     * Map??Type???�정?�다.
     */
	public void setMapType() {
    	AlertDialog dlg = new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("Select MAP Type")
		.setSingleChoiceItems(R.array.a_maptype, -1, new DialogInterface.OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int item) {							
				LogManager.printLog("Set Map Type " + item);
				dialog.dismiss();
				mMapView.setMapType(item);
			}
		}).show();		
    }
    
    /**
     * getLocationPoint
     * ?�재?�치�??�시??좌표???�도, 경도�?반환?�다. 
     */
	public void getLocationPoint() {
		TMapPoint point = mMapView.getLocationPoint();
		
		double Latitude = point.getLatitude();
		double Longitude = point.getLongitude();
		
		m_Latitude  = Latitude;
		m_Longitude = Longitude;
		
		LogManager.printLog("Latitude " + Latitude + " Longitude " + Longitude);
		
		String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);
		
		Common.showAlertDialog(this, "", strResult);
	}
	
	/**
	 * setLocationPoint
	 * ?�재?�치�??�시??좌표???�도,경도�??�정?�다. 
	 */
	public void setLocationPoint() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	      
	      Location location = locationManager
	                   .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	      double latitude = location.getLatitude();//((double)Math.random() ) * (37.575113-37.483086) + 37.483086;
	       double longitude = location.getLongitude();
		
		LogManager.printLog("setLocationPoint " + latitude + " " + longitude);
		
		mMapView.setLocationPoint(latitude, longitude);
	}
	
	/**
	 * setMapIcon
	 * ?�재?�치�??�시???�이콘을 ?�정?�다. 
	 */
	public void setMapIcon() {
		m_bShowMapIcon = !m_bShowMapIcon;

		if (m_bShowMapIcon) {
			Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_launcher);
			mMapView.setIcon(bitmap);
		}
		mMapView.setIconVisibility(m_bShowMapIcon);
	}
	
	/**
	 * setCompassMode
	 * ?�말??방항???�라 ??��?�는 ?�침반모?�로 ?�정?�다. 
	 */
	public void setCompassMode() {
		mMapView.setCompassMode(!mMapView.getIsCompass());
	}
	
	/**
	 * getIsCompass
	 * ?�침반모?�의 ?�용?��?�?반환?�다. 
	 */
	public void getIsCompass() {
		Boolean bGetIsCompass = mMapView.getIsCompass();
		Common.showAlertDialog(this, "", "?�재 ?�침�?모드??: " + bGetIsCompass.toString() );
	}
	
	/**
	 * setTrafficeInfo
	 * ?�시�?교통?�보�??�출?��?�??�정?�다. 
	 */
	public void setTrafficeInfo() {
		m_bTrafficeMode = !m_bTrafficeMode;
		mMapView.setTrafficInfo(m_bTrafficeMode);
	}
	
	/**
	 * getIsTrafficeInfo
	 * ?�시�?교통?�보 ?�출?�태�?반환?�다. 
	 */
	public void getIsTrafficeInfo() {
		Boolean bIsTrafficeInfo = mMapView.IsTrafficInfo();
		Common.showAlertDialog(this, "", "?�재 ?�시�?교통?�보 ?�출?�태?? : " + bIsTrafficeInfo.toString() );
	}
	
	/**
	 * setSightVisible
	 * ?�야?�출?��?�??�정?�다. 
	 */
	public void setSightVisible() {
		m_bSightVisible = !m_bSightVisible;
		mMapView.setSightVisible(m_bSightVisible);
	}
	
	/**
	 * setTrackingMode
	 * ?�면중심???�말???�재?�치�??�동?�켜주는 ?�래?�모?�로 ?�정?�다. 
	 */
	public void setTrackingMode() {
		m_bTrackingMode = !m_bTrackingMode;
		mMapView.setTrackingMode(m_bTrackingMode);
	}
	
	/**
	 * getIsTracking
	 * ?�래?�모?�의 ?�용?��?�?반환?�다. 
	 */
	public void getIsTracking() {
		Boolean bIsTracking = mMapView.getIsTracking();
		Common.showAlertDialog(this, "", "?�재 ?�래?�모???�용 ?��?  : " + bIsTracking.toString() );
	}
	
	/**
	 * addTMapCircle()
	 * �?��???�클??추�??�다. 
	 */
	public void addTMapCircle() {
		TMapCircle circle = new TMapCircle();
		
		circle.setRadius(300);
		circle.setLineColor(Color.BLUE);
		circle.setAreaAlpha(50);
		circle.setCircleWidth((float)10);
		circle.setRadiusVisible(true);
		
		TMapPoint point = randomTMapPoint();
		circle.setCenterPoint(point);
		
		String strID = String.format("circle%d", mCircleID++);
		mMapView.addTMapCircle(strID, circle);
		
		mArrayCircleID.add(strID);
	}
	
	/**
	 * removeTMapCircle
	 * �?��?�의 ?�당 ?�클???�거?�다. 
	 */
	public void removeTMapCircle() {
		if(mArrayCircleID.size() <= 0 )
			return;
		
		String strCircleID = mArrayCircleID.get(mArrayCircleID.size() - 1 );
		mMapView.removeTMapCircle(strCircleID);
		
		mArrayCircleID.remove(mArrayCircleID.size() - 1);
		//mMapView.showCallOutViewWithMarkerItemID("02");
	}
	
	public void showMarkerPoint2() {
		ArrayList<Bitmap> list = null;
		for(int i = 0; i < 50; i++) {
			
			MarkerOverlay marker1 = new MarkerOverlay(this, mMapView);
			String strID = String.format("%02d", i);
			
			marker1.setID(strID);
			marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));		
			marker1.setTMapPoint(randomTMapPoint());
			
			if(list == null){
				 list = new ArrayList<Bitmap>();
			}
			
			list.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red));
			list.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));
			
			marker1.setAnimationIcons(list);
			//marker1.setAniDuration(1000);
			
			//marker1.startAnimation();
			mMapView.addMarkerItem2(strID, marker1);
			
		}
				
		mMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
			
			@Override
			public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
				LogManager.printLog("ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude);
				
				String strMessage = "ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude;
				
				Common.showAlertDialog(CopyOfMainActivity.this, "TMapMarker2", strMessage);
			}
		});
		
	}
	public void showMarkerPoint3(TMapPoint point) {
	      Bitmap bitmap = null;
	      //TMapPoint point = new TMapPoint(37.566474, 126.985022);
	      TMapMarkerItem item1 = new TMapMarkerItem();
	      
	      bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.poi_star);
	            
	      item1.setTMapPoint(point);
	      item1.setVisible(item1.VISIBLE);
	   
	      item1.setIcon(bitmap);
//	      LogManager.printLog("bitmap " + bitmap.getWidth() + " " + bitmap.getHeight());
	      
	      bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.i_location);      
//	      item1.setCalloutTitle("?�재?�치");
//	      item1.setCanShowCallout(true);
//	      item1.setAutoCalloutVisible(true);
	      
	      Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
	      mMapView.addMarkerItem("now", item1);
	      
	      item1.setCalloutRightButtonImage(bitmap_i);
	      }
	/**
	 * showMarkerPoint
	 * �?��??마커�??�출?�다. 
	 */
	public void showMarkerPoint(TMapPoint point, String title, String subtitle)

	{	

		Bitmap bitmap = null;
		//TMapPoint point = new TMapPoint(37.566474, 126.985022);
		TMapMarkerItem item1 = new TMapMarkerItem();
		bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.map_pin_red);

		item1.setTMapPoint(point);
//		item1.setName("SKT??��");
//		item1.setVisible(item1.VISIBLE);

		item1.setIcon(bitmap);

		LogManager.printLog("bitmap " + bitmap.getWidth() + " " + bitmap.getHeight());

		bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.i_location);		

		item1.setCalloutTitle(title);
//		item1.setCalloutSubTitle(subtitle);
		item1.setCanShowCallout(true);
		item1.setAutoCalloutVisible(true);

		Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);


		mMapView.addMarkerItem("mok", item1);

		//item1.setCalloutLeftImage(bitmap);
		item1.setCalloutRightButtonImage(bitmap_i);

	}
	
		
	
	public void removeMarker()
	{
		if(mArrayMarkerID.size() <= 0 )
			return;
		
		String strMarkerID = mArrayMarkerID.get(mArrayMarkerID.size() - 1);
		mMapView.removeMarkerItem(strMarkerID);
		
		mArrayMarkerID.remove(mArrayMarkerID.size() - 1);
		
	}
	
	
	/**
	 * moveFrontMarker
	 * 마커�?�??�으�??�시 ?�도�??�다. 
	 * showMarkerPoint() ?�수�?먼�? ?�릭?????? ?�릭???�야 ??
	 */
	public void moveFrontMarker()
	{
		TMapMarkerItem item = mMapView.getMarkerItemFromID("1");
		
		mMapView.bringMarkerToFront(item);
	}
	
	
	/**
	 * moveBackMarker
	 * 마커�?�??�에 ?�시?�도�??�다. 
	 * showMarkerPoint() ?�수�?먼�? ?�릭?????? ?�릭???�야 ??
	 */
	public void moveBackMarker()
	{
		TMapMarkerItem item = mMapView.getMarkerItemFromID("1");
		
		mMapView.sendMarkerToBack(item);
	}
	
	
	/**
	 * drawLine
	 * �?��???�인??추�??�다. 
	 */
	public void drawLine()
	{	
		TMapPolyLine polyLine = new TMapPolyLine();
		polyLine.setLineColor(Color.BLUE);
		polyLine.setLineWidth(5);
		
		for(int i = 0; i < 5; i++)
		{
			TMapPoint point = randomTMapPoint();
			polyLine.addLinePoint(point);
		}
		
		String strID = String.format("line%d", mLineID++);
		
		mMapView.addTMapPolyLine(strID, polyLine);
		
		mArrayLineID.add(strID);
		
	}
	
	
	/**
	 * erasePolyLine
	 * �?��???�인???�거?�다. 
	 */
	public void erasePolyLine()
	{
		if(mArrayLineID.size() <= 0)
			return;
		
		String strLineID = mArrayLineID.get(mArrayLineID.size() - 1 );
		mMapView.removeTMapPolyLine(strLineID);
		
		mArrayLineID.remove(mArrayLineID.size() - 1);
		
	}

	
	
	/**
	 * drawPolygon
	 * �?��???�리곤에 그린?? 
	 */
	public void drawPolygon()
	{			
		int Min = 3;
		int Max = 10;
		int rndNum = (int)(Math.random() * ( Max - Min ));
		
		LogManager.printLog("drawPolygon" + rndNum);
		
		TMapPolygon polygon = new TMapPolygon();
		polygon.setLineColor(Color.BLUE);
		polygon.setPolygonWidth((float)4);
		polygon.setAreaAlpha(2);
		    
		TMapPoint point = null;
		
		if(rndNum < 3 )
		{
			rndNum = rndNum + (3 - rndNum);
		}
		
		for(int i = 0; i < rndNum; i++)
		{
			point = randomTMapPoint(); 
			polygon.addPolygonPoint(point);
			
		}
				
		String strID = String.format("polygon%d", mPolygonID++);
		mMapView.addTMapPolygon(strID, polygon);
		
		mArrayPolygonID.add(strID);
		
	}
	
	/**
	 * erasePolygon
	 * �?��??그려�??�리곤을 ?�거?�다. 
	 */
	public void erasePolygon()
	{	
		if(mArrayPolygonID.size() <= 0)
			return;
		
		String strPolygonID = mArrayPolygonID.get(mArrayPolygonID.size() - 1 );
		
		LogManager.printLog("erasePolygon " + strPolygonID);
		
		mMapView.removeTMapPolygon(strPolygonID);
		
		mArrayPolygonID.remove(mArrayPolygonID.size() - 1);
		
	}
	
	
	
	/**
	 * drawMapPath
	 * �?��???�작-종료 ?�에 ??��??경로�??�시?�다. 
	 */
	public void drawMapPath()
	{			
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		
		TMapData tmapdata = new TMapData();
			
		tmapdata.findPathData(point1, point2, new FindPathDataListenerCallback() {
			
			@Override
			public void onFindPathData(TMapPolyLine polyLine) {

				mMapView.addTMapPath(polyLine);
			}
		});
		
	}
	
	
	private String getContentFromNode(Element item, String tagName){
		NodeList list = item.getElementsByTagName(tagName);
		if(list.getLength() > 0){
			if(list.item(0).getFirstChild() != null)
			{
				return list.item(0).getFirstChild().getNodeValue();
			}
		}
		return null;
	}
	
	
	
	/**
	 * displayMapInfo()
	 * POI?�이 모두 ?�시?????�는 줌레�?결정?�수??중심?�리?�하???�수
	 */
	public void displayMapInfo()
	{	
		/*
		TMapPoint point1 = mMapView.getCenterPoint();		
		TMapPoint point2 = randomTMapPoint();
		*/
		TMapPoint point1 = new TMapPoint(37.541642248630524, 126.99599611759186);
		
		TMapPoint point2 = new TMapPoint(37.541243493556976, 126.99659830331802);
		
		TMapPoint point3 = new TMapPoint(37.540909826755524, 126.99739581346512);
		
		TMapPoint point4 = new TMapPoint(37.541080713272095, 126.99874675273895);
					
		ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
		
		point.add(point1);
		point.add(point2);
		point.add(point3);
		point.add(point4);
		
		
		TMapInfo info = mMapView.getDisplayTMapInfo(point);
		
		String strInfo = "Center Latitude" + info.getTMapPoint().getLatitude() + "Center Longitude" + info.getTMapPoint().getLongitude() + 
						"Level " + info.getTMapZoomLevel();
		
		Common.showAlertDialog(this, "", strInfo );
		
		
	}
	
	
	/**
	 * removeMapPath
	 * 경로 ?�시�???��?�다. 
	 */
	public void removeMapPath()
	{	
		mMapView.removeTMapPath();
		
	}
	
	
	
	/**
	 * naviGuide
	 * 길안??
	 */
	public void naviGuide()
	{			
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		
		TMapData tmapdata = new TMapData();
		
		tmapdata.findPathDataAll(point1, point2, new FindPathDataAllListenerCallback() {
			
			@Override
			public void onFindPathDataAll(Document doc) {
				
			
			}
		});
		
		
	}
	
	
	public void drawCarPath()
	{	
		
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		
		TMapData tmapdata = new TMapData();
		
		tmapdata.findPathDataWithType(TMapPathType.CAR_PATH, point1, point2, new FindPathDataListenerCallback() {
			
			@Override
			public void onFindPathData(TMapPolyLine polyLine) {
				
				mMapView.addTMapPath(polyLine);
				
			}
		});
		
				
	}
	
	
	
	public void  drawPedestrianPath()
	{				
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		
		TMapData tmapdata = new TMapData();
		
		tmapdata.findPathDataWithType(TMapPathType.PEDESTRIAN_PATH, point1, point2, new FindPathDataListenerCallback() {
			
			@Override
			public void onFindPathData(TMapPolyLine polyLine) {
				
				polyLine.setLineColor(Color.BLUE);
				mMapView.addTMapPath(polyLine);
				
			}
		});
		
		
	}
	
	
	
	public void drawBicyclePath()
	{		
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		
		TMapData tmapdata = new TMapData();
		
		
		tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, point1, point2, new FindPathDataListenerCallback() {
			
			@Override
			public void onFindPathData(TMapPolyLine polyLine) {
				
				mMapView.addTMapPath(polyLine);
				
			}
		});
		
		
	}
	
	
	
	
	
	
	
	/**
	 * getCenterPoint
	 * �?��??중심?�을 �??�??�다. 
	 */
	public void getCenterPoint()
	{
		TMapPoint point = mMapView.getCenterPoint();
		
		Common.showAlertDialog(this, "", "중심 좌표" + point.getLatitude() + " " + point.getLongitude() );
		
	}
	
	
	/**
	 * findAllPoi
	 * ?�합�?�� POI�??�청?�다. 
	 */
	public void findAllPoi()
	{		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("POI ");
		final EditText input = new EditText(this);
		builder.setView(input);

		builder.setPositiveButton("", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    			    		    	
		    	final String strData = input.getText().toString();
		        
			    final TMapData tmapdata = new TMapData();
			      
			    tmapdata.findAllPOI(strData, new FindAllPOIListenerCallback() {
					
					@Override
					public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
						
						
						for(int i = 0; i < poiItem.size(); i++)
			        	{
			        		TMapPOIItem  item = poiItem.get(i);
			        		TMapPoint test = item.getPOIPoint();
			        	
//			        		LogManager.printLog("POI Name: " + item.getPOIName().toString() + ", " + 
//			        				            "Address: " + item.getPOIAddress().replace("null", "")  + ", " + 
//			        				            "Point: " + item.getPOIPoint().toString() + ", " +
//			        				            "test: " + test.getLatitude() + ", " + "test2: " + test.getLongitude());
			        		//showMarkerPoint(test,item.getPOIName().toString(),item.getPOIAddress().replace("null, ", ""));
			        		//showMarkerPoint2(test,item.getPOIName().toString(),item.getPOIAddress().replace("null, ", ""));
			        		ArrayList<Bitmap> list = null;
			        			
			        			MarkerOverlay marker1 = new MarkerOverlay(getApplicationContext(), mMapView);
			        			String strID = String.format("%02d", i);
			        			
			        			marker1.setID(strID);
			        			marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));		
			        			marker1.setTMapPoint(test);
			        		
			        			
			        			
			        			if(list == null){
			        				 list = new ArrayList<Bitmap>();
			        			}
			        			
			        			list.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red));
			        			list.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));
			        			
			        			marker1.setAnimationIcons(list);
			        			//marker1.setAniDuration(1000);
			        			
			        			//marker1.startAnimation();
			        			mMapView.addMarkerItem2(strID, marker1);
			        			
			        			if(i==0)
			        			{
			        				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			        			
			        				Location location = locationManager
			        		                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			        				double latitude = location.getLatitude();
			        		    	double longitude = location.getLongitude();  
			        		    
			        				TMapPoint point = new TMapPoint(marker1.latitude, marker1.longitude);
			        				mMapView.setCenterPoint(point.getLongitude(),point.getLatitude());
			        				mMapView.setIconVisibility(true);
			        			}
			        		}
						
			        				
			        		mMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
			        			
			        			@Override
			        			public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
			        				//LogManager.printLog("ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude);
			        				try {
										String Address = tmapdata.convertGpsToAddress(markerItem2.latitude, markerItem2.longitude);
										LogManager.printLog("ClickEvent " +Address);
				        				Common.showAlertDialog(CopyOfMainActivity.this, input.getText().toString(), Address);
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (FactoryConfigurationError e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SAXException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			        				
			        				String strMessage = "ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude;
			        				
			        				//Common.showAlertDialog(MainActivity.this, "TMapMarker2", strMessage);
			        			}
			        		});
			        	}
						
					}
				);
			    
			    
		    }
		    
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});
		builder.show();		

	}
	
	
	/**
	 * convertToAddress
	 * �?��?�서 ?�택??�?��??주소�?�?��?�청?�다. 
	 */
	public void convertToAddress()
	{			
		TMapPoint point = mMapView.getCenterPoint();
		
	    TMapData tmapdata = new TMapData();
	      
	    if(mMapView.isValidTMapPoint(point))
	    {
		    tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
				
				@Override
				public void onConvertToGPSToAddress(String strAddress) {
					
					LogManager.printLog("주소" + strAddress);
				}
			});
//		    tmapdata.reverseGeocoding(point.getLatitude(), point.getLongitude(), "A03", new reverseGeocodingListenerCallback() {
//				
//				@Override
//				public void onReverseGeocoding(TMapAddressInfo addressInfo) {
//					
//					LogManager.printLog("?�택???�치??주소??" + addressInfo.strFullAddress);
//				}
//			});
//		    tmapdata.geoCodingWithAddressType("F02", "?�울??, "구로�?, "?�말�?, "6", "", new GeoCodingWithAddressTypeListenerCallback() {
//		    	
//				@Override
//				public void onGeoCodingWithAddressType(TMapGeocodingInfo geocodingInfo) {
//					LogManager.printLog(">>> strMatchFlag : " + geocodingInfo.strMatchFlag);
//					LogManager.printLog(">>> strLatitude : " + geocodingInfo.strLatitude);
//					LogManager.printLog(">>> strLongitude : " + geocodingInfo.strLongitude);
//					LogManager.printLog(">>> strCity_do : " + geocodingInfo.strCity_do);
//					LogManager.printLog(">>> strGu_gun : " + geocodingInfo.strGu_gun);
//					LogManager.printLog(">>> strLegalDong : " + geocodingInfo.strLegalDong);
//					LogManager.printLog(">>> strAdminDong : " + geocodingInfo.strAdminDong);
//					LogManager.printLog(">>> strBunji : " + geocodingInfo.strBunji);
//					LogManager.printLog(">>> strNewMatchFlag : " + geocodingInfo.strNewMatchFlag);
//					LogManager.printLog(">>> strNewLatitude : " + geocodingInfo.strNewLatitude);
//					LogManager.printLog(">>> strNewLongitude : " + geocodingInfo.strNewLongitude);
//					LogManager.printLog(">>> strNewRoadName : " + geocodingInfo.strNewRoadName);
//					LogManager.printLog(">>> strNewBuildingIndex : " + geocodingInfo.strNewBuildingIndex);
//					LogManager.printLog(">>> strNewBuildingName : " + geocodingInfo.strNewBuildingName);
//				}
//			});
	    }
		  
	}    
	    
	
	/**
	 * getBizCategory
	 * ?�종�?category�??�청?�다. 
	 */
	public void getBizCategory()
	{		
		TMapData tmapdata = new TMapData();
		
        tmapdata.getBizCategory(new BizCategoryListenerCallback() {
			
			@Override
			public void onGetBizCategory(ArrayList<BizCategory> poiItem) {
				
				for(int i = 0; i < poiItem.size(); i++)
		        {
		        	BizCategory item = poiItem.get(i);
		        	
		        	LogManager.printLog("UpperBizCode " + item.upperBizCode + " " + "UpperBizName " + item.upperBizName );
		        	LogManager.printLog("MiddleBizcode " + item.middleBizCode + " " + "MiddleBizName " + item.middleBizName);
		        }
			}
		});
            
	}
	
	
	
	/**
	 * getAroundBizPoi
	 * ?�종�?주�?�?�� POI ?�이?��? ?�청?�다. 
	 */
	public void getAroundBizPoi()
	{				
		TMapData tmapdata = new TMapData();
		 
		TMapPoint point = mMapView.getCenterPoint();
		
		
		/*
		tmapdata.findAroundBizPOI(point, "01", "?�의??, new FindAroundBizPOIListenerCallback() {
			
			@Override
			public void onFindAroundBizPOI(ArrayList<TMapPOIItem> poiItem) {
				
				for(int i = 0; i < poiItem.size(); i++)
	            {
	            	TMapPOIItem item = poiItem.get(i);
	            	
	            	LogManager.printLog("POI Name: " + item.getPOIName() + "," + 
	            						"Address: " + item.getPOIAddress().replace("null", ""));
	            
	            }
				
			}
		});
		*/		
		
		
		
		tmapdata.findAroundNamePOI(point, " ",1, 99, new FindAroundNamePOIListenerCallback() {
			
			@Override
			public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
				
				for(int i = 0; i < poiItem.size(); i++)
	            {
					TMapPOIItem item = poiItem.get(i);
	            	
	            	LogManager.printLog("POI Name: " + item.getPOIName() + "," + 
	            						"Address: " + item.getPOIAddress().replace("null", ""));
	            }
				
			}
		});
		
		
		
	}
	
	
	public void setTileType() {
    	AlertDialog dlg = new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("Select MAP Tile Type")
		.setSingleChoiceItems(R.array.a_tiletype, -1, new DialogInterface.OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int item) {							
				LogManager.printLog("Set Map Tile Type " + item);
				dialog.dismiss();
				mMapView.setTileType(item);
			}
		}).show();	
	}
	
	public void setBicycle()
	{
		mMapView.setBicycleInfo(!mMapView.IsBicycleInfo());
	}	
	
	public void setBicycleFacility()
	{
		mMapView.setBicycleFacilityInfo(!mMapView.isBicycleFacilityInfo());
	}
	
	
	public void invokeRoute()
	{	

		final TMapPoint point = mMapView.getCenterPoint();
		TMapData tmapdata = new TMapData();
				
		if(mMapView.isValidTMapPoint(point)) {
			
			tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
				
				@Override
				public void onConvertToGPSToAddress(String strAddress) {
					
					TMapTapi tmaptapi = new TMapTapi(CopyOfMainActivity.this);
									
					float fY = (float)point.getLatitude();
					float fX = (float)point.getLongitude();
					
					tmaptapi.invokeRoute(strAddress, fX, fY);
					
				}
			});
		}
	
	}
	
	
	public void invokeSetLocation()
	{				
		final TMapPoint point = mMapView.getCenterPoint();
		TMapData tmapdata = new TMapData();
		
		
		tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
			
			@Override
			public void onConvertToGPSToAddress(String strAddress) {
				
				TMapTapi tmaptapi = new TMapTapi(CopyOfMainActivity.this);
				
				float fY = (float)point.getLatitude();
				float fX = (float)point.getLongitude();
				
				tmaptapi.invokeSetLocation(strAddress, fX, fY);
				
			}
		});
		
		
	}
	
	
	
	String strSearch = "";
	
	public void invokeSearchProtal()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("T MAP ");

		final EditText input = new EditText(this);
		builder.setView(input);

		builder.setPositiveButton("", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        
		    	strSearch = input.getText().toString();
		        		    	
		    	new Thread()
				{
					@Override
					public void run()
					{	
						TMapTapi tmaptapi = new TMapTapi(CopyOfMainActivity.this);
												
						if(strSearch.trim().length() > 0)
							tmaptapi.invokeSearchPortal(strSearch);						
					}
					
				}.start();
		        
		    }
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
		
	}
	
	
	public void tmapInstall()
	{
    	new Thread()
		{
			@Override
			public void run()
			{	
				TMapTapi tmaptapi = new TMapTapi(CopyOfMainActivity.this);
		        Uri uri = Uri.parse(tmaptapi.getTMapDownUrl().get(0));
		        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		        startActivity(intent);					
			}
			
		}.start();

	}

	
	public void captureImage()
	{	
		/*
		Bitmap bitmap = mMapView.getCaptureImage();
		
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		 
		File path = new File(sdcard + File.separator + "image_write");
	    if (!path.exists()) 
		   path.mkdir();
	    
	    File fileCacheItem = new File(path.toString() + File.separator + System.currentTimeMillis() + ".png");
        OutputStream out = null;
        
        
        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
 
            bitmap.compress(CompressFormat.JPEG, 90, out);
            
            out.flush();
            out.close();  
        }catch (Exception e) {
            e.printStackTrace();
        }
        */
		
		
		mMapView.getCaptureImage(20, new MapCaptureImageListenerCallback() {
			
			@Override
			public void onMapCaptureImage(Bitmap bitmap) {
				
				String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
				 
				File path = new File(sdcard + File.separator + "image_write");
			    if (!path.exists()) 
				   path.mkdir();
			    
			    File fileCacheItem = new File(path.toString() + File.separator + System.currentTimeMillis() + ".png");
		        OutputStream out = null;
		        
		        try
		        {
		            fileCacheItem.createNewFile();
		            out = new FileOutputStream(fileCacheItem);
		 
		            bitmap.compress(CompressFormat.JPEG, 90, out);
		            
		            out.flush();
		            out.close();  
		        }
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		        }
				
			}
		});
        
        
	}
	
	
	private boolean bZoomEnable = false;


	
	public void disableZoom()
	{
		bZoomEnable = !bZoomEnable;
		mMapView.setUserScrollZoomEnable(bZoomEnable);
	}
	
	
	
	public void timeMachine() 
	{
		TMapData tmapdata = new TMapData();
		
		HashMap<String, String> pathInfo = new HashMap<String, String>();
		
		pathInfo.put("rStName", "T Tower");

		pathInfo.put("rStlat", Double.toString(37.566474));
		pathInfo.put("rStlon", Double.toString(126.985022));
		
		pathInfo.put("rGoName", "?�착");
		pathInfo.put("rGolat", "37.50861147");
		pathInfo.put("rGolon", "126.8911457");
		
		pathInfo.put("type", "arrival");
		
		Date currentTime = new Date();
		
		tmapdata.findTimeMachineCarPath(pathInfo,  currentTime, null);	
		
	}
    public void blueCreate() {
//      super.onCreate(savedInstanceState);
      if(D) Log.e(TAG, "+++ Blue CREATE +++");

      // Set up the window layout
//      setContentView(R.layout.main);

      // Get local Bluetooth adapter
      mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

      // If the adapter is null, then Bluetooth is not supported
      if (mBluetoothAdapter == null) {
          Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
//          finish();
          return;
      }
    }
      public void blueStart() {
//        super.onStart();
        if(D) Log.e(TAG, "++ blue START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) {
            	setupBluetoothChat();
            }
        }
    }
      public  void blueResume() {
//        super.onResume();
        if(D) Log.e(TAG, "+ blue RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
      
      private void ensureDiscoverable() {
          if(D) Log.d(TAG, "ensure discoverable");
          if (mBluetoothAdapter.getScanMode() !=
              BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
              Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
              discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
              startActivity(discoverableIntent);
          }
      }
      
      /**
       * Sends a message.
       * @param message  A string of text to send.
       */
      private void sendBluetoothMessage(String message) {
          // Check that we're actually connected before trying anything
          if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
              Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
              return;
          }

          // Check that there's actually something to send
          if (message.length() > 0) {
              // Get the message bytes and tell the BluetoothChatService to write
              byte[] send = message.getBytes();
              mChatService.write(send);

              // Reset out string buffer to zero and clear the edit text field
              mOutStringBuffer.setLength(0);
//              mOutEditText.setText(mOutStringBuffer);
          }
      }
      
      private final Handler mHandler = new Handler() {
          @Override
          public void handleMessage(Message msg) {
              switch (msg.what) {
              case MESSAGE_STATE_CHANGE:
                  if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                  switch (msg.arg1) {
                  case BluetoothChatService.STATE_CONNECTED:
                      LogManager.printLog(R.string.title_connected_to+" / "+mConnectedDeviceName);
//                      mConversationArrayAdapter.clear();
                      break;
                  case BluetoothChatService.STATE_CONNECTING:
                	  LogManager.printLog(R.string.title_connecting+" / ");
                      break;
                  case BluetoothChatService.STATE_LISTEN:
                  case BluetoothChatService.STATE_NONE:
                	  LogManager.printLog(R.string.title_not_connected+" / ");
                      break;
                  }
                  break;
              case MESSAGE_WRITE:
                  byte[] writeBuf = (byte[]) msg.obj;
                  // construct a string from the buffer
                  String writeMessage = new String(writeBuf);
                  LogManager.printLog("Me:  " + writeMessage);
                  
                  break;
              case MESSAGE_READ:
                  byte[] readBuf = (byte[]) msg.obj;
                  // construct a string from the valid bytes in the buffer
                  String readMessage = new String(readBuf, 0, msg.arg1);
                  LogManager.printLog(mConnectedDeviceName+":  " + readMessage);
                  break;
              case MESSAGE_DEVICE_NAME:
                  // save the connected device's name
                  mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                  Toast.makeText(getApplicationContext(), "Connected to "
                                 + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                  break;
              case MESSAGE_TOAST:
                  Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                                 Toast.LENGTH_SHORT).show();
                  break;
              }
          }
      };

      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          if(D) Log.d(TAG, "onActivityResult " + resultCode);
          switch (requestCode) {
          case REQUEST_CONNECT_DEVICE_SECURE:
              // When DeviceListActivity returns with a device to connect
              if (resultCode == Activity.RESULT_OK) {
                  connectDevice(data, true);
              }
              break;
          case REQUEST_CONNECT_DEVICE_INSECURE:
              // When DeviceListActivity returns with a device to connect
              if (resultCode == Activity.RESULT_OK) {
                  connectDevice(data, false);

              }
              break;
          case REQUEST_ENABLE_BT:
              // When the request to enable Bluetooth returns
              if (resultCode == Activity.RESULT_OK) {
                  // Bluetooth is now enabled, so set up a chat session
                  setupBluetoothChat();
              } else {
                  // User did not enable Bluetooth or an error occurred
                  Log.d(TAG, "BT not enabled");
                  Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                  finish();
              }
          }
      }

      private void connectDevice(Intent data, boolean secure) {
          // Get the device MAC address
          String address = data.getExtras()
              .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
          // Get the BluetoothDevice object
          BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
          // Attempt to connect to the device
          mChatService.connect(device, secure);
          goNavigation();
		      navi= new Navigation();
		      navi.set(polyLine, mMapView, getBaseContext());
		      navi.start();
      }


      public boolean Selected() {
          Intent serverIntent = null;
//          switch (item.getItemId()) {
//          case R.id.secure_connect_scan:
//              // Launch the DeviceListActivity to see devices and do scan
              serverIntent = new Intent(this, DeviceListActivity.class);
              startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
              return true;
//          case R.id.insecure_connect_scan:
              // Launch the DeviceListActivity to see devices and do scan
//              serverIntent = new Intent(this, DeviceListActivity.class);
//              startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
//              return true;
//          case R.id.discoverable:
//              // Ensure this device is discoverable by others
//              ensureDiscoverable();
//              return true;
//          }
//          return false;
      }
      

      
      
      private void setupBluetoothChat() {
          Log.d(TAG, "setupBluetoothChat()");

          // Initialize the array adapter for the conversation thread
//          mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//          mConversationView = (ListView) findViewById(R.id.in);
//          mConversationView.setAdapter(mConversationArrayAdapter);

          // Initialize the compose field with a listener for the return key
//          mOutEditText = (EditText) findViewById(R.id.edit_text_out);
//          mOutEditText.setOnEditorActionListener(mWriteListener);

          // Initialize the send button with a listener that for click events
//          mSendButton = (Button) findViewById(R.id.button_send);
//          mSendButton.setOnClickListener(new OnClickListener() {
//              public void onClick(View v) {
//                  // Send a message using content of the edit text widget
//                  TextView view = (TextView) findViewById(R.id.edit_text_out);
//                  String message = view.getText().toString();
//                  sendMessage(message);
//              }
//          });

          // Initialize the BluetoothChatService to perform bluetooth connections
          mChatService = new BluetoothChatService(this, mHandler);

          // Initialize the buffer for outgoing messages
          mOutStringBuffer = new StringBuffer("");
      }
}

