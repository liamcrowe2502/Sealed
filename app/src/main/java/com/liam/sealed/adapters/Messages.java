package com.liam.sealed.adapters;

import static com.liam.sealed.chatWindows.chatWindow.reciverIImg;
import static com.liam.sealed.chatWindows.chatWindow.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.liam.sealed.R;
import com.liam.sealed.modelClass.Message;
import com.liam.sealed.utility.EncryptionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public Messages(Context context, ArrayList<Message> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages = messagesAdpterArrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        /*
        if (holder.getClass()==senderVierwHolder.class){
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }else { reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        } */
        if (holder.getClass() == senderVierwHolder.class) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            try {
                String decryptedMessage = EncryptionHelper.decrypt(messages.getMessage());
                viewHolder.msgtxt.setText(decryptedMessage);
            } catch (Exception e) {
                viewHolder.msgtxt.setText("Error decrypting message");
            }
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            try {
                String decryptedMessage = EncryptionHelper.decrypt(messages.getMessage());
                viewHolder.msgtxt.setText(decryptedMessage);
            } catch (Exception e) {
                viewHolder.msgtxt.setText("Error decrypting message");
            }
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message messages = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class  senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgSenderSet);
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recieverTextSet);
        }
    }
}
