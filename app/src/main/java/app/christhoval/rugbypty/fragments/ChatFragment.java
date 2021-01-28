package app.christhoval.rugbypty.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.christhoval.rugbypty.Manifest;
import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.MessageAdapter;
import app.christhoval.rugbypty.fragments.base.BaseFragment;
import app.christhoval.rugbypty.models.Message;
import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.Request;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {

    public final int REQUEST_CODE_GALLERY = 0x11;
    public final int REQUEST_CODE_CAMERA = 0x12;
    public final int REQUEST_CODE_PERMITIONS = 0x13;
    private static final int TYPING_TIMER_LENGTH = 600;
    public static final String DEEP_LINK = "chat";
    public static final String TOPIC = "chat";

    private EditText mInputMessageView, mUsernameView;
    private RecyclerView mMessagesView;
    private List<Message> mMessages = new ArrayList<>();
    private MessageAdapter mAdapter;
    private ProgressBar loading;
    private View emptyData, chatView, loginView;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;

    private Socket mSocket;
    private Boolean isConnected = true;

    public static ChatFragment newInstance(Bundle extras) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Debug.i("ATTACH");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.i("CREATE");
        //setHasOptionsMenu(true);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.i("CREATE VIEW");
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.i("VIEW CREATED");

        RugbyPTY rugbyPTY = (RugbyPTY) getActivity().getApplication();
        mSocket = rugbyPTY.getSocket();
        connectSocket();
        mAdapter = new MessageAdapter(mMessages);
        loadMessagesFromServer();

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);

        mMessagesView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        mMessagesView.setLayoutManager(mLayoutManager);

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mUsername) return;
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        loading = (ProgressBar) view.findViewById(R.id.loading);
        emptyData = view.findViewById(R.id.no_data);
        chatView = view.findViewById(R.id.chatView);
        loading.setVisibility(View.VISIBLE);

        mUsername = getPreferences().get(Constant.CHAT_USERNAME, null);
        login(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Debug.i("START: " + mUsername);
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.i("RESUME");
    }

    @Override
    public void onPause() {
        super.onPause();
        Debug.i("PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Debug.i("STOP");
        //ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Debug.i("DESTROY VIEW");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectSocket();
        Debug.i("DESTROY");
    }


    /**
     * MENU
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_attach:
                Debug.i("action_attach");
                FromCard();
                return true;
            case R.id.action_capture:
                Debug.i("action_capture");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.socket_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * SOCKET
     **/

    private void connectSocket() {
        mSocket.on("message", handleIncomingMessages);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
    }

    private void disconnectSocket() {
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
    }

    /**
     * LOGIN
     **/

    private void login(View view) {

        loginView = findViewById(R.id.loginView);
        mUsernameView = (EditText) findViewById(R.id.username_input);
        if (mUsername == null) {
            mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
            Button setUserName = (Button) view.findViewById(R.id.sign_in_button);
            setUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                }
            });

            loginView.setVisibility(View.VISIBLE);
        } else {
            loginView.setVisibility(View.GONE);
            mAdapter.setCurrentUser(mUsername);
            mSocket.emit("add user", mUsername);
        }
        mSocket.on("login", onLogin);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }

        mUsername = username;

        // perform the user login attempt.
        mSocket.emit("add user", username);
    }

    private void logged(String username, int numUsers) {
        Debug.i("logged: " + username);
        getPreferences().set(Constant.CHAT_USERNAME, username);
        mAdapter.setCurrentUser(mUsername);
        addLog(getString(R.string.message_welcome));
        addParticipantsLog(numUsers);
        loginView.animate()
                .translationY(loginView.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        loginView.setVisibility(View.GONE);
                        enableChat();
                        loading.setVisibility(View.GONE);
                    }
                });
        mSocket.off("login", onLogin);
    }

    private void noData() {
        emptyData.setVisibility(View.VISIBLE);
    }

    private void enableChat() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableChat();
                    }
                }, 100);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQUEST_CODE_PERMISSIONS_CHAT);
            }
            return;
        }
        chatView.setVisibility(View.VISIBLE);
    }

    private void attemptSend() {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        addMessage(mUsername, message);

        // perform the sending message attempt.
        mSocket.emit("new message", Message.TYPE_MESSAGE_TEXT, message);
    }

    private void loadMessagesFromServer() {
        Request request = new Request(new AQuery(getActivity()));
        Map<String, String> params = new HashMap<>();
        //params.put("filters", "{\"state\":\"published\",\"format\":\"standard\"}");
        //params.put("limit", "30");
        //params.put("sort", "-publishedDate");
        try {
            request.createRequest("GET", "/message/list", params, new Request.RequestListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        ArrayList<Message> messages = Message.fromJson(response.getJSONArray(Message.MESSAGES));
                        if (messages.size() > 0) {
                            for (Message message : messages) {
                                mAdapter.addItem(message);
                            }
                        } else {
                            noData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onError(String error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        if (!message.isEmpty()) {
            emit("text", message);
        }

    }

    public void sendImage(String path) {
        path = encodeImage(path);
        emit("image", path);
    }

    private void emit(String key, String data) {
        JSONObject json = new JSONObject();
        try {
            json.put(key, data);
            json.put("username", getPreferences().get(Constant.CHAT_USERNAME, "guest"));
            mSocket.emit("message", json);
            add(key, json.getString("username"), json.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void add(String key, String username, String data) {
        switch (key) {
            case "text":
                addMessage(username, data);
                break;
            case "image":
                Bitmap bmp = decodeImage(data);
                addImage(username, bmp);
                break;
        }
    }

    private void addMessage(String username, String message) {

        mAdapter.addItem(new Message.Builder(Message.TYPE_MESSAGE).type(Message.TYPE_MESSAGE_TEXT).username(username).message(message).build());
        scrollToBottom();
    }

    private void addImage(String username, Bitmap bmp) {
        mAdapter.addItem(new Message.Builder(Message.TYPE_MESSAGE).type(Message.TYPE_MESSAGE_IMAGE).username(username).image(bmp).build());
        scrollToBottom();
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }


    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private String encodeImage(String path) {
        Debug.i(path);
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            Debug.i("cool");
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap decodeImage(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String username = data.has("username") ? data.getString("username") : "";
                        if (data.has("text")) {
                            addMessage(username, data.getString("text"));
                        } else if (data.has("image")) {
                            addImage(username, decodeImage(data.getString("image")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        if (null != mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(getContext(),
                                R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                        Debug.i(getString(R.string.connect));
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isConnected) {
                        isConnected = false;
                        Toast.makeText(getContext(), R.string.disconnect, Toast.LENGTH_LONG).show();
                        Debug.i(getString(R.string.disconnect));
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
            Debug.i("onLogin");
            logged(mUsername, numUsers);
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };

    private void FromCard() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_GALLERY);
    }

    public void FromCamera() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic.jpg");
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK && null != data) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    sendImage(imgDecodableString);
                    break;
                case REQUEST_CODE_CAMERA:
                    break;
            }
        }
    }
}
