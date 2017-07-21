package com.zfy.homevr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MapActivity extends Activity {

    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    Marker activeMarker = null;
    private SlidingUpPanelLayout mLayout;

    @JavascriptInterface
    public String getMarkerTitle(){
        return activeMarker.getTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        //获取上拖滑动控件的引用
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        WebView webView = (WebView)findViewById(R.id.webView01);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/housetype_list/housetype_map.html");
        webView.addJavascriptInterface(new JSUtils(MapActivity.this),"jsutil");
        webView.addJavascriptInterface(MapActivity.this,"mapActivity");

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState== SlidingUpPanelLayout.PanelState.EXPANDED){
                    mLayout.setPanelHeight(120);
                }
            }
        });

        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        this.setCenter(30.6638,104.072265);

        this.generateMarker("蓝润广场",30.609137,104.00928);
        this.generateMarker("蓝润光华春天",30.696444,103.895618);
        this.generateMarker("蓝润ISC",30.532355,104.073363);
        this.generateMarker("蓝润棠湖春天",30.571203,103.985614);
        this.generateMarker("蓝润锦江春天",30.588458,104.159046);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLayout.setPanelHeight(0);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String name = marker.getTitle();
//                Intent intent = new Intent(MapActivity.this,HouseTypeListActivity.class);
//                intent.putExtra("params", name);
//                startActivity(intent);

                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                TextView markerText = (TextView)findViewById(R.id.marker_map_title);
                markerText.setText(marker.getTitle());

                if(activeMarker!=null){
                    if(activeMarker!=null){
                        BitmapDescriptor markerIcon = getIcon(activeMarker.getTitle(),R.layout.view_marker);
                        activeMarker.setIcon(markerIcon);
                    }
                }
                BitmapDescriptor markerIcon = getIcon(name,R.layout.view_marker_active);
                marker.setIcon(markerIcon);
                activeMarker = marker;
                return true;

            }
        });

    }

    private void setCenter(double lat,double lng){

        LatLng cenpt = new LatLng(lat,lng);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
               // .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }



    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
    private void generateMarker(String name,double lat, double lng) {

            LatLng ll = new LatLng(lat, lng);
            BitmapDescriptor markerIcon = getIcon(name,R.layout.view_marker);

            Bundle bundle = new Bundle();
            bundle.putSerializable("name", name);

            OverlayOptions oo = new MarkerOptions().title(name).position(ll).icon(markerIcon).zIndex(9).draggable(true).extraInfo(bundle);

            mBaiduMap.addOverlay(oo);
    }

    private BitmapDescriptor getIcon(String name, int resource){

        View view = LayoutInflater.from(getApplicationContext()).inflate(resource, null);

        TextView tv_hotel_price = (TextView) view.findViewById(R.id.tv_hotel_price);
        tv_hotel_price.setText(name);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
        return markerIcon;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}