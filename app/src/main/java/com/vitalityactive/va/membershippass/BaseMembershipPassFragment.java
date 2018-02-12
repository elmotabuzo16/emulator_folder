package com.vitalityactive.va.membershippass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.membershippass.presenter.MembershipPassPresenter;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.profile.PersonalDetailsPresenter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryInvoker;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by peter.ian.t.betos on 15/12/2017.
 */

public abstract class BaseMembershipPassFragment  extends BasePresentedFragment<MembershipPassPresenter.UI, MembershipPassPresenter> implements MembershipPassPresenter.UI,View.OnClickListener, RecyclerView.OnChildAttachStateChangeListener, MenuContainerViewHolder.OnMenuItemClickedListener {

    private static final String TAG = "MembershipPassFragment";
    public static final String KEY_TITLE = "title";

    @Inject
    MembershipPassPresenter presenter;
    @Inject
    PersonalDetailsPresenter personalDetailsPresenter;
    @Inject
    CameraGalleryInvoker photoInvoker;

    private TextView profileInitialsView, vitalityNumberView, membershipNumberView, customerNumberView, vitalityStatusView, membershipStartDateView, membershipStatusView, membershipPassEditBtn;
    private ImageView  infoButtonView3, profileImageView, qrCodeView, helpItemIcon;
    protected View profileImageContainer;
    private String content, contextFileDirectory;
    private Bitmap bmp;
    private File f;
    private String globalTintColor;
    private RecyclerView helpRecyclerView;
    private ContainersRecyclerViewAdapter container;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 666;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Shared variables
    protected String vitalityConcat;
    protected CardView digitalPassCardview;
    protected LinearLayout membershipNumbeContainer,customerNumberContainer;
    protected TextView membershipNumberLabel,membershipPassHeader1;
    protected ImageView memberpassHeader,infoButtonView2,infoButtonView1;
    protected View vitalityNumberContainer;

 
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_membership_pass, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);

       MenuItem item= menu.findItem(R.id.menu_item_share);

       item.setVisible(isDigitalPassShow());

    }

    protected  boolean isDigitalPassShow(){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {



        switch (item.getItemId()) {
            case R.id.menu_item_share:
                verifyStoragePermissions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);
        View parentView = getView();
        if (parentView == null) {
            return;
        }
        setUpDetailsView(parentView);
        setUIColor(globalTintColor);
    }

    private void setUpDetailsView(@NonNull View parentView) {
        profileImageView = parentView.findViewById(R.id.profile_image);
        profileImageContainer = parentView.findViewById(R.id.profile_image_container);
        profileInitialsView = parentView.findViewById(R.id.profile_image_initials);
        qrCodeView = parentView.findViewById(R.id.qr_code_view);
        membershipPassEditBtn = parentView.findViewById(R.id.membership_pass_editBtn);

        helpItemIcon = parentView.findViewById(R.id.help_item_icon);

        membershipNumberView = parentView.findViewById(R.id.membership_number);
        customerNumberView = parentView.findViewById(R.id.customer_number);
        vitalityStatusView = parentView.findViewById(R.id.vitality_status);
        membershipStartDateView = parentView.findViewById(R.id.membership_start_date);
        membershipStatusView = parentView.findViewById(R.id.membership_status);

        vitalityNumberContainer = parentView.findViewById(R.id.vitality_number_container);
        vitalityNumberView = parentView.findViewById(R.id.vitality_number);


        infoButtonView1 = parentView.findViewById(R.id.info_btnView);
        infoButtonView2 = parentView.findViewById(R.id.info_btnView2);
        infoButtonView3 = parentView.findViewById(R.id.info_btnView3);

        membershipPassEditBtn.setOnClickListener(this);
        profileImageContainer.setOnClickListener(this);
        infoButtonView1.setOnClickListener(this);
        infoButtonView2.setOnClickListener(this);
        infoButtonView3.setOnClickListener(this);

        photoInvoker.setFragmentCallback(this);
        photoInvoker.setCapturedImageDetails("personal_details", "photo");
        contextFileDirectory = getActivity().getFilesDir().toString();

        membershipNumbeContainer= parentView.findViewById(R.id.membership_number_container);
        customerNumberContainer= parentView.findViewById(R.id.customer_number_container);
        digitalPassCardview =parentView.findViewById(R.id.digital_pass_cardview);
        memberpassHeader =parentView.findViewById(R.id.memberpass_header);

        membershipNumberLabel=parentView.findViewById(R.id.membership_number_label);
        membershipPassHeader1=parentView.findViewById(R.id.membership_pass_header_1);

        setupRecyclerView(parentView);
    }


    @Override
    protected void setUIColor(String globalTintColor) {
        if (!TextUtils.isEmpty(globalTintColor) && !TextUtils.isEmpty(globalTintColor.trim())){
            infoButtonView1.setColorFilter(Color.parseColor(globalTintColor));
            infoButtonView2.setColorFilter(Color.parseColor(globalTintColor));
            infoButtonView3.setColorFilter(Color.parseColor(globalTintColor));
            membershipPassEditBtn.setTextColor(Color.parseColor(globalTintColor));
            memberpassHeader.setColorFilter(Color.parseColor(globalTintColor), PorterDuff.Mode.OVERLAY);
        }
    }


    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MembershipPassPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected MembershipPassPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showMembershipInfo(long partyId, String vitalityNumber, String membershipNumber, String customerNumber, String vitalityStatus, String membershipStartDate, String membershipStatus) {

        vitalityNumberView.setText(vitalityNumber);
        membershipNumberView.setText(membershipNumber);
        customerNumberView.setText(customerNumber);
        vitalityStatusView.setText(vitalityStatus);
        membershipStartDateView.setText(membershipStartDate);
        membershipStatusView.setText(membershipStatus);

        vitalityConcat = membershipNumber;
        marketUIUpdate(membershipNumber, partyId > 0 ? Long.toString(partyId): "");
        showHideVitalityNumber();

        vitalityNumberView.setText(vitalityConcat);
        content =
                " Vitality Number:" + vitalityConcat +
                        " Membership Number: " + membershipNumber +
                        " Customer Number: " + customerNumber +
                        " Vitality Status: " + vitalityStatus +
                        " Membership Date: " + membershipStartDate +
                        " Membership Status: " + membershipStatus;

        generateBarcode(content);
    }

    protected void showHideVitalityNumber(){
        if (TextUtils.isEmpty(vitalityConcat) || TextUtils.isEmpty(vitalityConcat.trim())){
            vitalityNumberContainer.setVisibility(View.GONE);
        } else {
            vitalityNumberContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProfileImage(String profileImagePath) {
        ImageLoader.loadImageFromUriRoundedAndCenterCrop(Uri.parse("file:" + profileImagePath), profileImageView);
        profileImageView.setVisibility(View.VISIBLE);
        profileInitialsView.setVisibility(View.GONE);
    }

    @Override
    public void activityDestroyed() {
        photoInvoker.finish();
    }


    @Override
    public void showProfileInitials(String userInitials) {
        profileImageView.setVisibility(View.GONE);
        profileInitialsView.setVisibility(View.VISIBLE);
        profileInitialsView.setText(userInitials);
    }

    public void generateBarcode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeView.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void editProfileImage() {
        AlertDialog.Builder choosePictureDialogBuilder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.select_dialog, null, false);

        TextView cameraOption = dialogView.findViewById(R.id.text1);
        TextView galleryOption = dialogView.findViewById(R.id.text2);

        Drawable photoDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.take_photo_40);
        Drawable galleryDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.choose_gallery_40);

        if (!TextUtils.isEmpty(globalTintColor) && !TextUtils.isEmpty(globalTintColor.trim())){
            photoDrawable = ViewUtilities.tintDrawable(photoDrawable, Color.parseColor(globalTintColor));
            galleryDrawable = ViewUtilities.tintDrawable(galleryDrawable, Color.parseColor(globalTintColor));
        }

        cameraOption.setCompoundDrawablesWithIntrinsicBounds(photoDrawable, null, null, null);
        galleryOption.setCompoundDrawablesWithIntrinsicBounds(galleryDrawable, null, null, null);


        choosePictureDialogBuilder.setView(dialogView);
        AlertDialog dialog = choosePictureDialogBuilder.create();

        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text1, CameraGalleryInvoker.ImageSource.CAMERA, dialog);
        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text2, CameraGalleryInvoker.ImageSource.GALLERY, dialog);

        dialog.show();
    }

    private void setChoosePictureDialogOptionOnClickListener(final View dialogView, int resourceId, final CameraGalleryInvoker.ImageSource imageSource, final AlertDialog dialog) {
        dialogView.findViewById(resourceId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoInvoker.setInvokerMode(imageSource);
                photoInvoker.start();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraGalleryUtility.IMAGE_SOURCE_CAMERA:
                    Uri capturedImageUri = photoInvoker.getCapturedImageUri();
                    performCrop(capturedImageUri);
                    break;

                case CameraGalleryUtility.IMAGE_SOURCE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    performCrop(selectedImageUri);
                    break;

                case CameraGalleryUtility.IMAGE_CROP:
                    Bitmap croppedImage = data.getExtras().getParcelable("data");
                    // Do not use for now. Image photo is only saved locally
                    //Uri finalImageUri = photoInvoker.getCroppedImageUri(croppedImage);

                    boolean result = photoInvoker.saveToInternalStorageByBitmap(croppedImage);
                    if (result) {
                        personalDetailsPresenter.addProfilePhoto(null, null, contextFileDirectory, null);
                    }
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    Uri croppedUri = CropImage.getActivityResult(data).getUri();
                    boolean saveResult = photoInvoker.saveToInternalStorageByUri(croppedUri);
                    if (saveResult) {
                        personalDetailsPresenter.addProfilePhoto(null, null, contextFileDirectory, null);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private InputStream getInputStreamFromFile(Uri uri) {
        try {
            return getActivity().getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void performCrop(Uri uri) {
        CropImage.activity(uri)
                .start(getContext(),this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), VitalityNumberActivity.class);
        switch (v.getId()) {
            case R.id.profile_image_container:
            case R.id.membership_pass_editBtn:
                editProfileImage();
                break;
            case R.id.info_btnView:
                intent.putExtra(KEY_TITLE, "vitality");
                startActivity(intent);
                break;
            case R.id.info_btnView2:
                intent.putExtra(KEY_TITLE, "membership");
                startActivity(intent);
                break;
            case R.id.info_btnView3:
                intent.putExtra(KEY_TITLE, "customer");
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onChildViewAttachedToWindow(View view) {    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateOnMenuItemFromVitalityStatus(getActivity(), menuItemType);
    }
    private void share() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.vitality_membership_pass_label));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getString(R.string.vitality_membership_pass_email_content) + " " +
                getString(R.string.terms_and_condition_link_9999)));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private File createBitmapToFile() {
        //create a file to write bitmap data
        try {

            String externalDir= getStoragePathAvailablity().toString();

            File directory = new File(externalDir);
            //check if directory is existing
            if (!directory.exists())
                directory.mkdirs();

            f = new File(externalDir + File.separator + "pass.png");

            if (f.exists()) {
                f.delete(); //limit the filename and the file to 1
            }

            f.createNewFile(); //create the file
            Bitmap bitmap = ((BitmapDrawable) qrCodeView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (IOException e) {
            Log.e("IOException", "" + e.getMessage());
        }
        return null;
    }

    protected void marketUIUpdate(String membershipNumber, String partyId) {
        digitalPassCardview.setVisibility(View.VISIBLE);
        membershipNumbeContainer.setVisibility(View.VISIBLE);
        customerNumberContainer.setVisibility(View.VISIBLE);
        vitalityConcat = membershipNumber;
    }

    public class createBitmapToFile extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return ( createBitmapToFile() !=null)? true: false;
        }
        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (true) {
                share();
            } else {
                Toast.makeText(getContext(), "Error on fetching image", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verifyStoragePermissions() {
        int permission = checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((permission != PackageManager.PERMISSION_GRANTED )  || (permissionRead != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions( PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE );
        }else{
            new MembershipPassFragment.createBitmapToFile().execute();
        }

    }

    public File getStoragePathAvailablity() {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return (isSDPresent) ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) : getContext().getFilesDir();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (wasPermissionsRequestCancelled(permissions, grantResults)) {  return;      }

        if(requestCode == REQUEST_EXTERNAL_STORAGE){
            if (wasPermissionGranted(grantResults)) {
                new MembershipPassFragment.createBitmapToFile().execute();
            }
        }
        else{
            if (wasPermissionGranted(grantResults)) {
                photoInvoker.start();
            }
        }
    }

    private boolean wasPermissionGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean wasPermissionsRequestCancelled(@NonNull String[] permissions, @NonNull int[] grantResults) {
        return permissions.length == 0 && grantResults.length == 0;
    }
    private void setupRecyclerView(@NonNull View parentView) {
        helpRecyclerView=parentView.findViewById(R.id.help_recycler_view);

        container = new ContainersRecyclerViewAdapter(createAdapters());
        helpRecyclerView.setAdapter(container);
        helpRecyclerView.addOnChildAttachStateChangeListener(this);
        //ViewUtilities.scrollToTop(helpRecyclerView);
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> createAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(getMenuAdapter());

        return adapters;
    }
    private GenericRecyclerViewAdapter<CardMarginSettings, MenuContainerViewHolder> getMenuAdapter() {
        return new MenuBuilder(getActivity())
                .addMenuItem(com.vitalityactive.va.menu.MenuItem.Builder.help(Color.parseColor(globalTintColor)))
                .setClickListener(this)
                .build();
    }
}
