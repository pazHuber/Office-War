package com.example.paz.officewar;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.os.Vibrator;


import com.example.paz.officewar.util.UserDetails;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseError;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GameActivity extends AppCompatActivity {
    private TcpClient mTcpClient;
    private ArrayList<int[]> messageList;
    int i =-1;
    int readMessageCounter=0;
    boolean flag = false;
    private  String uid;
    private  String uName;
    Firebase mRef;
    Mat imgMat;
    UserDetails usr;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imgMat=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        uid = getIntent().getExtras().getString("USER_ID");
        uName = getIntent().getExtras().getString("USER_NAME");
        mRef = new Firebase(getResources().getString(R.string.firebase_url));
        ImageButton leftBtn = (ImageButton) findViewById(R.id.act_game_left_arrow);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.act_game_right_arrow);
        ImageButton upBtn = (ImageButton) findViewById(R.id.act_game_up_arrow);
        ImageButton downBtn = (ImageButton) findViewById(R.id.act_game_down_arrow);
        ImageButton fireBtn = (ImageButton) findViewById(R.id.act_game_fire_btn);
        Button endGame = (Button) findViewById(R.id.act_game_engGame_btn);
        messageList = new ArrayList<int[]>();
        usr = new UserDetails(uName,0);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.child("users").child(uid).exists()) {
                    mRef.child("users").child(uid).setValue(usr);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        mRef.child("users").child(uid).setValue(usr);

        // connect to the server
        new connectTask().execute("");
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "l";
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }

        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "r";

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
                //String data = mTcpClient.receiveMesage();
                //System.out.println(data);


            }
        });
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "f";

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }

        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "b";

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }
        });
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "z";

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
                mTcpClient.stopClient();
                Firebase userScore = new Firebase("https://blazing-heat-7486.firebaseio.com"+"/users/"+uid+"/score");
                userScore.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String sc = snapshot.getValue().toString();
                        Integer score = new Integer(sc);
                        if(score < usr.getScore()) {
                            mRef.child("users").child(uid).setValue(usr);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });


            }
        });
        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "s";

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }
        });
        new Thread(new Runnable() {
            public void run() {
                try {
                    while(true) {
                    while (flag) {
                        while (messageList.size() > readMessageCounter) {
                            //hit msg
                            if (messageList.get(readMessageCounter)[0] == 0x02) {
                                int[] data = messageList.get(readMessageCounter);

                                //success
                                if(data[1]==1) {
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    // Vibrate for 500 milliseconds
                                    v.vibrate(900);
                                    updateScore();
                                    }
                                readMessageCounter++;
                            } else if(messageList.get(readMessageCounter)[0]==0x01) {
                                //imgMat = new Mat(640,480, CvType.CV_8UC1);
                                //imgMat.put(0,0,messageList.get(readMessageCounter));
                                //Imshow im = new Imshow("office-war");
                                //im.showImage(imgMat);
                                //final Bitmap bm = Bitmap.createBitmap(imgMat.cols(), imgMat.rows(),Bitmap.Config.ARGB_8888);
                                //Utils.matToBitmap(imgMat, bm);
                                //Bitmap bmp = Bitmap.createBitmap(messageList.get(i), 640, 480, Bitmap.Config.ARGB_8888);
                                //final Bitmap convertedBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                                readMessageCounter++;

                                //runOnUiThread(new Runnable() {
                                 //   @Override
                                  //  public void run() {
                                 //       ImageView robotView = (ImageView) findViewById(R.id.act_game_camera);
                                //        robotView.setImageBitmap(bm);
                               //     }
                              //  });
                            }
                        }
                    }}
                } catch (Exception e) {
                    Log.e("EXECPTION:", e.toString());
                }
                ;

            }

        }).start();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*private void ExportMatToBitmap(Mat img_mat, String name) {
        Bitmap bmp = Bitmap.createBitmap(640, 480,
                Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_mat, bmp);
        try {
            FileOutputStream out = new FileOutputStream(name);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g. = new Firebase(getResources().getString(R.string.firebase_url));co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.paz.officewar/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.paz.officewar/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class connectTask extends AsyncTask<String, int[], TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(int[] message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(int[]... values) {
            super.onProgressUpdate(values);


            //in the arrayList we add the messaged received from server
            messageList.add(values[0]);
            i++;
            flag = true;
        }
    }
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, GameActivity.this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    private void updateScore() {
        Integer tempScore = usr.getScore();
        tempScore += 100;
        usr.setScore(tempScore);
    }
}
