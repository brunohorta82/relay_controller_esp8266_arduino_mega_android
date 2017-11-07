package bhsystems.eu.relaycontroller.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import bhsystems.eu.relaycontroller.R;

import bhsystems.eu.relaycontroller.application.RelayControllerApplication;
import bhsystems.eu.relaycontroller.controllers.adapters.ButtonsAdapter;
import bhsystems.eu.relaycontroller.itemtouchlistener.RecyclerItemTouchHelper;
import bhsystems.eu.relaycontroller.model.RelayControllerButton;

public class ButtonsListController extends AppCompatActivity implements ButtonsAdapter.ButtonSelectedListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final int BUTTON_REQUEST_CODE = 1;
    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final String CONTROLLER_NAME = "relaycontroller";
    private static final int REQUEST_GPIO_STATES = -1;
    private static final String TAG = ButtonsListController.class.getSimpleName();

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private NsdServiceInfo mServiceInfo;


    private RecyclerView rvButtons;
    private ButtonsAdapter buttonsAdapter;

    private ArrayList<RelayControllerButton> buttons = new ArrayList<>();

    private ImageView ivLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        rvButtons = findViewById(R.id.rv_buttons);

        new ButtonsRepository(this).execute();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ButtonsListController.this, ButtonAddController.class), BUTTON_REQUEST_CODE);
            }
        });


    }


    private void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!
                String name = service.getServiceName();
                String type = service.getServiceType();
                Log.d("NSD", "Service Name=" + name);
                Log.d("NSD", "Service Type=" + type);
                invalidateConnection(false);
                if (type.equals(SERVICE_TYPE) && name.contains(CONTROLLER_NAME)) {
                    Log.d("NSD", "Service Found @ '" + name + "'");
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                invalidateConnection(true);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                invalidateConnection(true);
            }
        };
    }

    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e("NSD", "Resolve failed" + errorCode);
                invalidateConnection(true);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mServiceInfo = serviceInfo;
                InetAddress host = mServiceInfo.getHost();
                String address = host.getHostAddress();
                Log.d("NSD", "Resolved address = " + address);
                RelayControllerApplication.sharedInstance().setControllerIp(address);
                sendRequestToController(REQUEST_GPIO_STATES);
                updateLedState();
            }
        };
    }


    private void prepareRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvButtons.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rvButtons.setLayoutManager(linearLayoutManager);
        buttonsAdapter = new ButtonsAdapter(this);
        buttonsAdapter.addAll(buttons);
        rvButtons.setAdapter(buttonsAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvButtons);
    }

    @Override
    public void onButtonClicked(RelayControllerButton relayControllerButton) {
        sendRequestToController(relayControllerButton.getPin());
    }
private boolean waitingForController = false;
    private void sendRequestToController(int gpIO) {
        if (RelayControllerApplication.sharedInstance().getControllerIp() == null) {
            invalidateConnection(true);
            return;
        }
        if(waitingForController)
            return;
        waitingForController = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + RelayControllerApplication.sharedInstance().getControllerIp() + "/" + gpIO;
        queue.cancelAll(TAG);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        waitingForController = false;
                        Log.i("RESP", response);
                        if (response.length() == 30) {
                            for (int i = 0; i < response.length(); i++) {
                                char s = response.charAt(i);
                                if (s != '1' && s != '0') {
                                    Toast.makeText(getApplicationContext(), "Erro - Estado inválido", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                int itemIndex = 0;
                                for (RelayControllerButton button : buttons) {
                                    if (button.getPin() == i + 23) {
                                        boolean realState = s == '1';
                                        if (button.getRelayControllerButtonType() == RelayControllerButton.RelayControllerButtonType.TOGGLE && button.isActive() != realState) {
                                            button.setActive(realState);
                                            buttonsAdapter.notifyItemChanged(itemIndex);
                                        }
                                    }
                                    itemIndex++;
                                }

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Erro - Tamanho da resposta inválido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingForController = false;
                Log.d("NSD", error.getMessage() == null ? "" : error.getMessage());
                invalidateConnection(true);
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    private void invalidateConnection(boolean showToast) {
        RelayControllerApplication.sharedInstance().lostConnection();
        if(showToast)
            Toast.makeText(getApplicationContext(), "Controlador não encontrado", Toast.LENGTH_SHORT).show();
        updateLedState();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        new DeleteButtonTask(this).execute(buttons.get(position));
    }

    private static class ButtonsRepository extends AsyncTask<Void, Void, List<RelayControllerButton>> {

        private WeakReference<ButtonsListController> activityReference;

        // only retain a weak reference to the activity
        ButtonsRepository(ButtonsListController context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<RelayControllerButton> doInBackground(Void... params) {
            return RelayControllerApplication.sharedInstance().getDb().relayControllerButtonDao().getAll();
        }

        @Override
        protected void onPostExecute(List<RelayControllerButton> result) {
            ButtonsListController activity = activityReference.get();
            if (activity == null) return;
            activity.buttons.clear();
            activity.buttons.addAll(result);
            activity.prepareRecycleView();

        }
    }

    private static class DeleteButtonTask extends AsyncTask<RelayControllerButton, Void, RelayControllerButton[]> {

        private WeakReference<ButtonsListController> activityReference;

        // only retain a weak reference to the activity
        DeleteButtonTask(ButtonsListController context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected RelayControllerButton[] doInBackground(RelayControllerButton... relayControllerButtons) {
            for (RelayControllerButton relayControllerButton : relayControllerButtons) {
                RelayControllerApplication.sharedInstance().getDb().relayControllerButtonDao().delete(relayControllerButton);
            }
            return relayControllerButtons;
        }

        @Override
        protected void onPostExecute(RelayControllerButton[] relayControllerButtons) {
            for (RelayControllerButton relayControllerButton : relayControllerButtons) {
                ButtonsListController activity = activityReference.get();
                activity.buttonsAdapter.removeItem(relayControllerButton);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BUTTON_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    new ButtonsRepository(this).execute();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buttons_list_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.mi_led);
        if (menuItem != null) {
            View actionView = MenuItemCompat.getActionView(menuItem);
            ivLed = actionView.findViewById(R.id.iv_led);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RelayControllerApplication.sharedInstance().hasConnection()) {
                        return;
                    }
                    searchController();

                }
            });
            searchController();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void searchController() {
        mNsdManager = (NsdManager) (getApplicationContext().getSystemService(Context.NSD_SERVICE));
        initializeResolveListener();
        initializeDiscoveryListener();
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    private void updateLedState() {
        if (ivLed == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivLed.setImageResource(RelayControllerApplication.sharedInstance().hasConnection() ? R.drawable.ic_led_on : R.drawable.ic_led_off);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }
}
