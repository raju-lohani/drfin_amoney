package com.drfin.drfin_amoney.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.KycResponse;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.ImageConversionClass;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.drfin.drfin_amoney.utils.SelectFragmentCallbacks;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class KycFragment extends Fragment implements View.OnClickListener {
    private PrefrenceHandler pref;
    private ImageView Image, Pancard, AddharCard, Backadhar, PanCardError, AddharError, backAddharError, cheque;
    private int PAN_IMAGE_CAPTURE = 200;
    private int PAN_IMAGE_GALLARY = 201;
    private int ADHAR_FRONT_IMAGE_CAPTURE = 300;
    private int ADHAR_FRONT_IMAGE_GALLARY = 301;
    private int ADHAR_BACK_IMAGE_CAPTURE = 302;
    private int ADHAR_BACK_IMAGE_GALLARY = 303;
    private int CHEQUE_IMAGE_CAPTURE = 304;
    private int CHEQUE_IMAGE_GALLARY = 305;
    private String Addhar = "";
    private String pancared = "";
    private String image = "";
    private String backadhar = "";
    private String chequeImage = "";
    private SelectFragmentCallbacks selectFragmentCallbacks;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private FaceDetector detector;
    private RelativeLayout backLayout;
    private TextView message;
    private Dialog Logindialog;
    private int CAPTURE_CODE = 0;
    private int GALLARY_CODE = 0;
    private ArrayList<String> documentData;
    private String verifyCode = "0";
    private String verifyPanCode = "0";
    private String verifyAadharCode = "0";

    private String panExtractData = "";
    private String aadharExtractData = "";
    private String aadharExtractBackData = "";
    private String chequeExtractData = "";

    private int deviceWidth = getScreenWidth();
    private int deviceHeight = getScreenHeight();


    private LinearLayout progress_bar_layout;

    public KycFragment(SelectFragmentCallbacks selectFragmentCallbacks) {
        this.selectFragmentCallbacks = selectFragmentCallbacks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kyc, container, false);
        init(view);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        return view;
    }

    private void init(View view) {
        Image = view.findViewById(R.id.user_image);
        cheque = view.findViewById(R.id.cheque);
        Pancard = view.findViewById(R.id.PanCard);
        AddharCard = view.findViewById(R.id.Addhar);
        Button sendKycData = view.findViewById(R.id.sendKyc);
        Backadhar = view.findViewById(R.id.backAddhar);
        PanCardError = view.findViewById(R.id.PanCardError);
        AddharError = view.findViewById(R.id.AddharError);
        backAddharError = view.findViewById(R.id.backAddharError);
        progress_bar_layout = view.findViewById(R.id.progress_bar_layout);
        sendKycData.setOnClickListener(this);
        Pancard.setOnClickListener(this);
        AddharCard.setOnClickListener(this);
        Image.setOnClickListener(this);
        Backadhar.setOnClickListener(this);
        cheque.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        switch (v.getId()) {
            case R.id.user_image:
                imageCapturing();
                break;

            case R.id.PanCard:
                alert.setView(R.layout.image_formate_layout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage("PanCard");
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.Addhar:
                alert.setView(R.layout.image_formate_layout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage("AadharFront");
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.cheque:
                alert.setView(R.layout.image_formate_layout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage("cheque");
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.sendKyc:
                AlertDialog.Builder errorAlert = new AlertDialog.Builder(getContext());
                errorAlert.setTitle("Error Message");
                if (PanCardError.getVisibility() == View.VISIBLE) {
                    errorAlert.setMessage("Pancard Image is not valid.Please follow instruction");
                    errorAlert.show();
                } else if (AddharError.getVisibility() == View.VISIBLE) {
                    errorAlert.setMessage("Aadhaar Front Image is not valid.Please follow instruction");
                    errorAlert.show();
                } else if (backAddharError.getVisibility() == View.VISIBLE) {
                    errorAlert.setMessage("Aadhaar Back Image is not valid.Please follow instruction");
                    errorAlert.show();
                }
//                else if (chequeImage.equals("")) {
//                    errorAlert.setMessage("Upload cancel cheque.");
//                    errorAlert.show();
//                }

                else {
                    progress_bar_layout.setVisibility(View.VISIBLE);
                    sendKycData();
                }
                errorAlert.setPositiveButton("Re-Capture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.backAddhar:
                alert.setView(R.layout.image_formate_layout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage("AadharBack");
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
        }


    }

    private void selectImage(final String REQUEST_CODE) {
        CAPTURE_CODE = 0;
        GALLARY_CODE = 0;
        switch (REQUEST_CODE) {

            case "PanCard":
                CAPTURE_CODE = PAN_IMAGE_CAPTURE;
                GALLARY_CODE = PAN_IMAGE_GALLARY;
                break;

            case "AadharFront":
                CAPTURE_CODE = ADHAR_FRONT_IMAGE_CAPTURE;
                GALLARY_CODE = ADHAR_FRONT_IMAGE_GALLARY;
                break;

            case "AadharBack":
                CAPTURE_CODE = ADHAR_BACK_IMAGE_CAPTURE;
                GALLARY_CODE = ADHAR_BACK_IMAGE_GALLARY;
                break;

            case "cheque":
                CAPTURE_CODE = CHEQUE_IMAGE_CAPTURE;
                GALLARY_CODE = CHEQUE_IMAGE_GALLARY;
                break;
        }

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        final int finalGALLARY_CODE = GALLARY_CODE;
        final int finalCAPTURE_CODE = CAPTURE_CODE;
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    DocumentCapturing();
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, finalCAPTURE_CODE);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), finalGALLARY_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        int USER_IMAGE_CAPTURE = 100;
        if (requestCode == USER_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
                Bitmap photo = BitmapFactory.decodeByteArray(data.getByteArrayExtra("imageData"), 0, Objects.requireNonNull(data.getByteArrayExtra("imageData")).length, options);
                String encodedImage = ImageConversionClass.BitmapToString(photo);
                image = "";
                image = encodedImage;
                Image.setImageBitmap(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PAN_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert photo != null;
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                pancared = "";
                pancared = encodedImage;
                Pancard.setImageBitmap(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PAN_IMAGE_GALLARY && resultCode == Activity.RESULT_OK) {
            try {
                documentData = new ArrayList<>();
                ArrayList<String> documentWordData = new ArrayList<>();

                final String photo = ImageConversionClass.ImageToString(Objects.requireNonNull(getContext()), data.getData());
                byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (textRecognizer.isOperational() && photo != null) {
                    Frame frame = new Frame.Builder().setBitmap(decodedByte).build();
                    SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                    String blocks = "";
                    String lines = "";
                    StringBuilder words = new StringBuilder();
                    for (int index = 0; index < textBlocks.size(); index++) {
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            lines = lines + line.getValue() + "\n";
                            documentData.add(line.getValue().toLowerCase());
                            Log.d("TAG", "onPictureTakenlines: " + lines);
                            for (Text element : line.getComponents()) {
                                words.append(element.getValue()).append(", ");
                                documentWordData.add(element.getValue().toLowerCase());
                            }
                        }
                    }
                    panExtractData = words.toString().toLowerCase();
                }
                pancared = "";
                pancared = ImageConversionClass.ImageToString(Objects.requireNonNull(getContext()), data.getData());

                Pancard.setImageBitmap(decodedByte);
                if (documentData.contains("Permanent Account Number".toLowerCase()) || documentData.contains("Permanent Account Number Card".toLowerCase())|| documentData.contains("INCOME TAX DEPARTMENT".toLowerCase())|| documentData.contains("Permanent Accout Number Card".toLowerCase())) {
                    PanCardError.setVisibility(View.GONE);
                    verifyPanCode = "1";
                } else {
                    PanCardError.setVisibility(View.VISIBLE);
                    verifyPanCode = "0";
                }

                if (documentData.contains(pref.getUSER_PAN().toLowerCase())) {
                    verifyPanCode = "1";
                } else {
                    verifyPanCode = "0";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == ADHAR_FRONT_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert photo != null;
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            Addhar = "";
            Addhar = encodedImage;
            AddharCard.setImageBitmap(photo);
        } else if (requestCode == ADHAR_FRONT_IMAGE_GALLARY && resultCode == Activity.RESULT_OK) {
            documentData = new ArrayList<>();
            ArrayList<String> documentWordData = new ArrayList<>();
            Addhar = "";
            Addhar = ImageConversionClass.ImageToString(Objects.requireNonNull(getContext()), data.getData());
            byte[] decodedString = Base64.decode(Addhar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (textRecognizer.isOperational() && Addhar != null) {
                Frame frame = new Frame.Builder().setBitmap(decodedByte).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                String blocks = "";
                String lines = "";
                StringBuilder words = new StringBuilder();
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        lines = lines + line.getValue() + "\n";
                        documentData.add(line.getValue().toLowerCase());
                        Log.d("TAG", "onPictureTakenlines: " + lines);
                        for (Text element : line.getComponents()) {
                            words.append(element.getValue()).append(", ");
                            documentWordData.add(element.getValue().toLowerCase().trim());
                        }
                    }
                }
                aadharExtractData = words.toString().toLowerCase();
            }
            Log.d("TAG", "onPictureTakenWord: " + documentWordData.toString());
            if (documentWordData.contains("vid:")
                    || documentData.contains("vid")
                    || documentData.contains("government of india")
                    || documentData.contains("govermment of india")) {
                AddharError.setVisibility(View.GONE);
            } else {
                AddharError.setVisibility(View.VISIBLE);
            }
            AddharCard.setImageBitmap(decodedByte);
        } else if (requestCode == ADHAR_BACK_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert photo != null;
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            backadhar = "";
            backadhar = encodedImage;
            Backadhar.setImageBitmap(photo);
        } else if (requestCode == ADHAR_BACK_IMAGE_GALLARY && resultCode == Activity.RESULT_OK) {
            documentData = new ArrayList<>();
            ArrayList<String> documentWordData = new ArrayList<>();
            backadhar = "";
            backadhar = ImageConversionClass.ImageToString(Objects.requireNonNull(getContext()), data.getData());
            byte[] decodedString = Base64.decode(backadhar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (textRecognizer.isOperational() && backadhar != null) {
                Frame frame = new Frame.Builder().setBitmap(decodedByte).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                StringBuilder blocks = new StringBuilder();
                StringBuilder lines = new StringBuilder();
                StringBuilder words = new StringBuilder();
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks.append(tBlock.getValue()).append("\n").append("\n");
                    for (Text line : tBlock.getComponents()) {
                        lines.append(line.getValue()).append("\n");
                        documentData.add(line.getValue().toLowerCase());
                        Log.d("TAG", "onPictureTakenlines: " + lines);
                        for (Text element : line.getComponents()) {
                            words.append(element.getValue()).append(", ");
                            documentWordData.add(element.getValue().toLowerCase().trim());
                        }
                    }
                }
                aadharExtractBackData = words.toString();
            }
            Backadhar.setImageBitmap(decodedByte);
            if (documentData.contains("Address:".toLowerCase())
                    || documentData.contains("Address".toLowerCase())
                    || documentData.contains("Unique ldentification Authority of India".toLowerCase())) {
                backAddharError.setVisibility(View.GONE);
            } else {
                backAddharError.setVisibility(View.VISIBLE);
            }

        } else if (requestCode == CHEQUE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert photo != null;
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            chequeImage = "";
            chequeImage = encodedImage;
            cheque.setImageBitmap(photo);
        } else if (requestCode == CHEQUE_IMAGE_GALLARY && resultCode == Activity.RESULT_OK) {
            documentData = new ArrayList<>();
            ArrayList<String> documentWordData = new ArrayList<>();
            chequeImage = "";
            chequeImage = ImageConversionClass.ImageToString(Objects.requireNonNull(getContext()), data.getData());
            byte[] decodedString = Base64.decode(chequeImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (textRecognizer.isOperational() && chequeImage != null) {
                Frame frame = new Frame.Builder().setBitmap(decodedByte).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                StringBuilder blocks = new StringBuilder();
                StringBuilder lines = new StringBuilder();
                StringBuilder words = new StringBuilder();
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks.append(tBlock.getValue()).append("\n").append("\n");
                    for (Text line : tBlock.getComponents()) {
                        lines.append(line.getValue()).append("\n");
                        documentData.add(line.getValue().toLowerCase());
                        Log.d("TAG", "onPictureTakenlines: " + lines);
                        for (Text element : line.getComponents()) {
                            words.append(element.getValue()).append(", ");
                            documentWordData.add(element.getValue().toLowerCase());
                        }
                    }
                }
                chequeExtractData = words.toString();
            }
            cheque.setImageBitmap(decodedByte);
            if (documentData.contains(pref.getUSER_Bank().toLowerCase()) && !pref.getUSER_Bank().equals("")) {
                verifyCode = "1";
            } else {
                verifyCode = "0";
            }
        }
    }

    private void sendKycData() {
        ApiClientService apiClientService = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<KycResponse> callkyc = apiClientService.KycDetail(
                pref.getAuth_Token(),
                pancared,
                Addhar,
                pref.getLastApplicationNo(),
                image,
                backadhar,
                chequeImage,
                verifyCode,
                verifyPanCode,
                verifyAadharCode,
                panExtractData,
                aadharExtractData,
                aadharExtractBackData,
                chequeExtractData);
        callkyc.enqueue(new Callback<KycResponse>() {
            @Override
            public void onResponse(@NonNull Call<KycResponse> call, @NonNull Response<KycResponse> response) {
                if (response.isSuccessful()) {
                    progress_bar_layout.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        Log.d("TAG", "Succesfull hit" + response.body().getInfo());
                        Toast.makeText(getContext(), "Loan Applied Successful", Toast.LENGTH_LONG).show();
                        pref.setLastApplicationStatus("pending");
                        selectFragmentCallbacks.onActionChange(getContext(), "kyc_done");
                    } else {
                        Log.d("TAG", "Succesfull hit" + response.body().getInfo());
                    }
                } else {
                    progress_bar_layout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Some Error try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<KycResponse> call, @NonNull Throwable t) {
                progress_bar_layout.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void imageCapturing() {
        Logindialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Logindialog.setContentView(R.layout.fragment_camera);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(Logindialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        Logindialog.getWindow().setAttributes(lp);
        cameraView = Logindialog.findViewById(R.id.mView);
        final SurfaceView cameraView1 = Logindialog.findViewById(R.id.mView1);
        backLayout = Logindialog.findViewById(R.id.backLayout);
        message = Logindialog.findViewById(R.id.message);
        message.setVisibility(View.VISIBLE);
        detector = new FaceDetector.Builder(getContext())
                .setProminentFaceOnly(true) // optimize for single, relatively large face
                .setTrackingEnabled(true) // enable face tracking
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS) /* eyes open and smile */
                .setMode(FaceDetector.SELFIE_MODE) // for one face this is OK
                .build();

        if (!detector.isOperational()) {
            Log.w("MainActivity", "Detector Dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(Objects.requireNonNull(getContext()), detector)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        cameraSource.start(cameraView.getHolder());
                        detector.setProcessor(new LargestFaceFocusingProcessor(detector, new GraphicFaceTracker()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            cameraView1.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    cameraView1.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                    cameraView1.setZOrderMediaOverlay(true);
                    Draw(deviceWidth, deviceHeight, 600, 700, cameraView1);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }
        Logindialog.show();
    }

    class GraphicFaceTracker extends Tracker<Face> {

        private int state = 0;

        void blink(float value, Face face) {
            float OPEN_THRESHOLD = 0.85f;
            float CLOSE_THRESHOLD = 0.4f;
            switch (state) {
                case 0:
                    if (value > OPEN_THRESHOLD) {
                        backLayout.setBackgroundColor(getResources().getColor(R.color.RED));
                        state = 1;
                    }
                    break;

                case 1:
                    if (value < CLOSE_THRESHOLD) {
                        // Both eyes become closed
                        backLayout.setBackgroundColor(getResources().getColor(R.color.RED));
                        state = 2;
                    }
                    break;

                case 2:
                    if (value > OPEN_THRESHOLD) {
                        final float left = face.getIsLeftEyeOpenProbability();
                        final float right = face.getIsRightEyeOpenProbability();
                        if ((left == Face.UNCOMPUTED_PROBABILITY) || (right == Face.UNCOMPUTED_PROBABILITY)) {
                            backLayout.setBackgroundColor(getResources().getColor(R.color.RED));
                            return;
                        } else {
                            state = 0;
                            Log.i("BlinkTracker", "blink occurred!");
                            cameraSource.takePicture(new CameraSource.ShutterCallback() {
                                @Override
                                public void onShutter() {
                                }
                            }, new CameraSource.PictureCallback() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onPictureTaken(final byte[] bytes) {
                                    message.setText("Capturing...");
                                    backLayout.setBackgroundColor(getResources().getColor(R.color.GREEN));
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inPurgeable = true;
                                    String encodedImage = null;
                                    Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                                    encodedImage = ImageConversionClass.BitmapToString(photo);
                                    image = "";
                                    image = encodedImage;
                                    Image.setImageBitmap(photo);
                                    Logindialog.dismiss();

                                }
//                                }
                            });
                        }
                    }
                    break;
            }
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            float left = face.getIsLeftEyeOpenProbability();
            float right = face.getIsRightEyeOpenProbability();
            if ((left == Face.UNCOMPUTED_PROBABILITY) || (right == Face.UNCOMPUTED_PROBABILITY)) {
                backLayout.setBackgroundColor(getResources().getColor(R.color.RED));
            } else {
                float value = Math.min(left, right);
                blink(value, face);
            }
        }
    }

    private void DocumentCapturing() {
        Logindialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Logindialog.setContentView(R.layout.fragment_camera);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(Logindialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        Logindialog.getWindow().setAttributes(lp);
        cameraView = Logindialog.findViewById(R.id.mView);
        backLayout = Logindialog.findViewById(R.id.backLayout);
        final SurfaceView cameraView1 = Logindialog.findViewById(R.id.mView1);
        backLayout.setPadding(0, 0, 0, 0);
        ImageButton captureBtn = Logindialog.findViewById(R.id.captureBtn);
        captureBtn.setVisibility(View.VISIBLE);
        Logindialog.findViewById(R.id.message).setVisibility(View.GONE);
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector Dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(Objects.requireNonNull(getContext()), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedFps(15.0f)
                    .setRequestedPreviewSize(deviceWidth - 40, 500)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            cameraView1.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    cameraView1.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                    cameraView1.setZOrderMediaOverlay(true);
                    Draw(deviceWidth, deviceHeight, deviceWidth - 40, 500, cameraView1);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.takePicture(new CameraSource.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        documentData = new ArrayList<>();
                        ArrayList<String> documentWordData = new ArrayList<>();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPurgeable = true;
                        options.inSampleSize = 2;
                        Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                        if (textRecognizer.isOperational() && photo != null) {
                            Frame frame = new Frame.Builder().setBitmap(photo).build();
                            SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                            StringBuilder blocks = new StringBuilder();
                            StringBuilder lines = new StringBuilder();
                            StringBuilder words = new StringBuilder();
                            for (int index = 0; index < textBlocks.size(); index++) {
                                TextBlock tBlock = textBlocks.valueAt(index);
                                blocks.append(tBlock.getValue()).append("\n").append("\n");
                                for (Text line : tBlock.getComponents()) {
                                    lines.append(line.getValue()).append("\n");
                                    documentData.add(line.getValue().toLowerCase());
                                    for (Text element : line.getComponents()) {
                                        words.append(element.getValue()).append(", ");
                                        documentWordData.add(element.getValue().toLowerCase().trim());
                                    }
                                }
                            }
                            if (CAPTURE_CODE == PAN_IMAGE_CAPTURE) {
                                panExtractData = words.toString();
                            } else if (CAPTURE_CODE == ADHAR_FRONT_IMAGE_CAPTURE) {
                                aadharExtractData = words.toString();
                            } else if (CAPTURE_CODE == ADHAR_BACK_IMAGE_CAPTURE) {
                                aadharExtractBackData = words.toString();
                            } else if (CAPTURE_CODE == CHEQUE_IMAGE_CAPTURE) {
                                chequeExtractData = words.toString();
                            }
                        }

                        String encodedImage = ImageConversionClass.BitmapTolowString(photo);
                        if (CAPTURE_CODE == PAN_IMAGE_CAPTURE) {
                            pancared = "";
                            pancared = encodedImage;
                            Pancard.setImageBitmap(photo);
                            if (documentData.contains("Permanent Account Number".toLowerCase()) || documentData.contains("Permanent Account Number Card".toLowerCase())) {
                                PanCardError.setVisibility(View.GONE);
                                verifyPanCode = "1";
                            } else {
                                PanCardError.setVisibility(View.VISIBLE);
                                verifyPanCode = "0";
                            }

                            if (documentData.contains(pref.getUSER_PAN().toLowerCase())) {
                                verifyPanCode = "1";
                            } else {
                                verifyPanCode = "0";
                            }
                            Logindialog.dismiss();
                        } else if (CAPTURE_CODE == ADHAR_FRONT_IMAGE_CAPTURE) {
                            Addhar = "";
                            Addhar = encodedImage;
                            AddharCard.setImageBitmap(photo);
                            if (documentWordData.contains("vid:")
                                    || documentData.contains("vid")
                                    || documentData.contains("government of india")
                                    || documentData.contains("govermment of india")) {
                                AddharError.setVisibility(View.GONE);
                            } else {
                                AddharError.setVisibility(View.VISIBLE);
                            }

                            Logindialog.dismiss();
                        } else if (CAPTURE_CODE == ADHAR_BACK_IMAGE_CAPTURE) {
                            backadhar = "";
                            backadhar = encodedImage;
                            Backadhar.setImageBitmap(photo);
                            if (documentData.contains("Address:".toLowerCase())
                                    || documentData.contains("Address".toLowerCase())
                                    || documentData.contains("Unique ldentification Authority of India".toLowerCase())) {
                                backAddharError.setVisibility(View.GONE);
                            } else {
                                backAddharError.setVisibility(View.VISIBLE);
                            }

                            Logindialog.dismiss();
                        } else if (CAPTURE_CODE == CHEQUE_IMAGE_CAPTURE) {
                            chequeImage = "";
                            chequeImage = encodedImage;
                            cheque.setImageBitmap(photo);
                            if (documentData.contains(pref.getUSER_Bank().toLowerCase()) && !pref.getUSER_Bank().equals("")) {
                                verifyCode = "1";
                            } else {
                                verifyCode = "0";
                            }
                            Logindialog.dismiss();
                        }
                    }
                });
            }
        });
        Logindialog.show();
    }

    private void Draw(int deviceWidth, int deviceHeight, int rectWidth, int rectHeight, SurfaceView cameraView1) {
        Canvas canvas = cameraView1.getHolder().lockCanvas(null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        Point centerOfCanvas = new Point(deviceWidth / 2, deviceHeight / 2);
        int rectW = rectWidth;
        int rectH = rectHeight;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);
        cameraView1.getHolder().unlockCanvasAndPost(canvas);
    }
}
