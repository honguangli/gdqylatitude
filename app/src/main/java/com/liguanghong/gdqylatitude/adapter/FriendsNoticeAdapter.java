package com.liguanghong.gdqylatitude.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.NoticesManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendsNoticeAdapter extends BaseAdapter {
    Context context;

    public FriendsNoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return NoticesManager.getInstance().getFriendNotices().size();
    }

    @Override
    public Object getItem(int i) {
        return NoticesManager.getInstance().getFriendNotices().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friends_notice, null);

        ImageView iv_friend_icon = v.findViewById(R.id.iv_friend_icon);
        TextView tv_friend_name = v.findViewById(R.id.tv_friend_name);
        TextView tv_friend_notice = v.findViewById(R.id.tv_friend_notice);
        TextView tv_friend_argee = v.findViewById(R.id.tv_friend_argee);

        NoticeMsg noticeMsg = (NoticeMsg)getItem(i);
        final User user = JSONObject.parseObject(noticeMsg.getData().toString(), User.class);
        tv_friend_name.setText(user.getLogname());
        byte[] friends = Base64.decode(user.getHeadportrait(), Base64.DEFAULT);
        iv_friend_icon.setImageBitmap(ImageUtils.getPicFromBytes(friends,null));
        tv_friend_notice.setText(noticeMsg.getExtra());

        if(noticeMsg.getNoticetype().equals(MessageType.FRIENDAPPLY)){
            tv_friend_argee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFriend(user);
                }
            });
        } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDDENIED)){
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setText("已拒绝");
            tv_friend_argee.setTextColor(Color.parseColor("#BFBFBF"));
        } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDAGREED)){
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setTextColor(Color.parseColor("#25C6FC"));
            tv_friend_argee.setText("已同意");
        }

        return v;
    }

    /**
     * 添加好友操作
     */
    private void addFriend(final User user){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("targetuserid", user.getUserid() + "")
                .build();
        HttpUtil.postEnqueue("user/addfriend", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友管理", "添加好友失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            Friend friend = new Friend();
                            friend.setFriendid(user.getUserid());
                            friend.setFriend(user);
                            FriendsManager.getInstance().addFriend(friend);
                        } else{

                        }
                        Log.i("好友管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}