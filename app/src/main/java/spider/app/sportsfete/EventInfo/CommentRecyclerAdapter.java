package spider.app.sportsfete.EventInfo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import spider.app.sportsfete.FireBaseServices.Comment;
import spider.app.sportsfete.R;

/**
 * Created by srikanth on 2/2/17.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    List<Comment> commentList;
    Context context;


    public CommentRecyclerAdapter(List<Comment> commentList, Context context){
        this.commentList = commentList;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_commentary_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        Comment commentItem = commentList.get(position);

        String string=commentItem.getComment();
        holder.commentUsername.setText(commentItem.getUsername());
        holder.commentTv.setText(fromHtml(string));
        holder.commentTimeTv.setText(commentItem.getTimestamp());
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
