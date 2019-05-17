package endgame.data.dreamcorporation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;

import endgame.data.dreamcorporation.profile.Word;
import endgame.data.dreamcorporation.profile.WordAdapter;

public class HomeFragment extends Fragment {

  private View v;
  private EditText key;
  private TextView title;
  private int counter = 0;
  private ListView listView;
  private ArrayList<Word> words;
  private SpeedDialView speedDial;
  private double balance;

  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//  private Users thisUser;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_home, container, false);

//    firebase.getBalance(mAuth.getUid(), new GetFirebase.GetBalanceCallback() {
//      @Override
//      public void onCallback(double balance) {
//        balance = balance;
//      }
//    });
    
//    mDatabase.setPersistenceEnabled(true);

    // Balance
    TextView balanceTextView = v.findViewById(R.id.home_balance);
    CardView cardView = v.findViewById(R.id.card);
    ImageView cardBg = v.findViewById(R.id.card_background);
    TextView levelTextView = v.findViewById(R.id.level);
    
    balance = GetFirebase.getUsers(mAuth.getUid()).getBalance();
    
    balanceTextView.setText(getString(R.string.currency) + " " + balance);

    if (balance < 50) {
      cardBg.setImageResource(R.drawable.bronze);
    } else if (balance < 150) {
      cardBg.setImageResource(R.drawable.silver);
      levelTextView.setText("Silver");
    } else if (balance < 300) {
      cardBg.setImageResource(R.drawable.gold);
      levelTextView.setText("Gold");
    } else if (balance >= 300) {
      cardBg.setImageResource(R.drawable.platinum);
      levelTextView.setText("Platinum");
    }

//    usersRef.child(mAuth.getUid()).child("b").addListenerForSingleValueEvent(new ValueEventListener() {
//      @Override
//      public void onDataChange(DataSnapshot dataSnapshot) {
//        balanceTextView.setText(getString(R.string.currency) + " " + dataSnapshot.getValue().toString());
//      }
//
//      @Override
//      public void onCancelled(@NonNull DatabaseError databaseError) {}
//    });

    speedDial = v.findViewById(R.id.home_fab);
    speedDial.inflate(R.menu.menu_fab);

    speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
      @Override
      public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
        switch (speedDialActionItem.getId()) {
          case R.id.fabScanQR:
//            scanQR();
            Intent toQr = new Intent(getActivity(), DialogQrScanner.class);
            startActivity(toQr);
            return false;
          case R.id.fabEnterDetails:
            enterUID();
            return false;
          default:
            return false;
        }
      }
    });

//    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//      @Override
//      public void onDataChange(DataSnapshot dataSnapshot) {
//        Log.e("users", dataSnapshot.getValue().toString());
//        HashMap<String, String> a = (HashMap<String, String>) dataSnapshot.getValue();
//
//        Log.e("individual", a.toString());
//      }
//      @Override
//      public void onCancelled(@NonNull DatabaseError databaseError) {
//      }
//    });

    displayList(v);

    return v;
  }

//  private void scanQRCode(){
//    new IntentIntegrator(getActivity())
//            .setOrientationLocked(false)
//            .setBeepEnabled(false)
//            .initiateScan();
//  }

//  public void scanQR() {
//    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//    intent.setAction(Intents.Scan.ACTION);
//    intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "Scan upline's QR Code");
//    intent.putExtra(Intents.Scan.CAMERA_ID, 0);
//    intent.putExtra(Intents.Scan.BEEP_ENABLED, true);
//    intent.putExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, false);
////            intent.putExtra("Scan uplines's QR Code", "QR_CODE_MODE");
//    getActivity().startActivityForResult(intent, 0);
//  }
//
//  @Override
//  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//    if (scanResult != null) {
//      tempUpline = scanResult.getContents();
//      Toast.makeText(getContext(), scanResult.getContents(), Toast.LENGTH_LONG).show();
//
//      Balance.calcRev();
//      addUpline();
//      uplineAccountAddDownline();
////      Toast.makeText(this, scanResult.getContents(), Toast.LENGTH_LONG).show();
//    } else {
//      super.onActivityResult(requestCode, resultCode, data);
//      Toast.makeText(getContext(), scanResult.getContents(), Toast.LENGTH_LONG).show();
//    }
//  }

  private void enterUID(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    LayoutInflater inflater = requireActivity().getLayoutInflater();
    View temp = inflater.inflate(R.layout.dialog_key,null);
    title = temp.findViewById(R.id.title);
    final TextView tempTitle = title;
    key = temp.findViewById(R.id.key);
    final EditText tempKey = key;
    String uplineUID = mAuth.getUid();
    tempTitle.setText("Enter upline UID");
    builder.setView(temp).setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String tempInput = tempKey.getText().toString();
        Log.v("tempInput: ", tempInput);
        if(!tempInput.equals(mAuth.getUid()))
          Toast.makeText(getContext(), "Fuck you! 妈的，叫你填啦，干你！JIBAI！", Toast.LENGTH_LONG).show();
        else if(!tempInput.equals(mAuth.getUid())&&tempInput.length()!=0)
          Toast.makeText(getContext(),"妈的！是不会放对的是吗？！干你！",Toast.LENGTH_LONG).show();
        else if(tempInput.equals(mAuth.getUid()))
          Toast.makeText(getContext(),"终于对了！恭喜恭喜",Toast.LENGTH_LONG).show();
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
  }

  public void displayList(View view){
    words = new ArrayList<Word>();
    words.add(new Word(getResources().getString(R.string.example),getResources().getString(R.string.example)));
    words.add(new Word("CHANGE TO BETTER ONE, this no ripple then tapped",getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    words.add(new Word(getResources().getString(R.string.example), getResources().getString(R.string.example)));
    WordAdapter itemAdapter = new WordAdapter(getActivity(),  words);
    listView = (ListView) view.findViewById(R.id.home_listView);
    listView.setAdapter(itemAdapter);
  }
}