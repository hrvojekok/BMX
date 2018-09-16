package hr.etfos.mgrgic.bmxridesleep;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.SearchViewHolder>{

    Context context;
    ArrayList<String> userNameList;
    ArrayList<String> riderList;
    ArrayList<String> ridingList;
    ArrayList<String> emailList;
    ArrayList<String> locationList;

    class SearchViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewUserName;
        TextView textViewRider;
        TextView textViewRiding;
        TextView textViewEmail;
        TextView textViewLocation;


        public SearchViewHolder(View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.userNameGoesHere);
            textViewRider = itemView.findViewById(R.id.riderGoesHere);
            textViewRiding = itemView.findViewById(R.id.ridingGoesHere);
            textViewEmail = itemView.findViewById(R.id.emailGoesHere);
            textViewLocation = itemView.findViewById(R.id.locationGoesHere);

        }
    }

    public searchAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> riderList, ArrayList<String> ridingList,  ArrayList<String> emailList, ArrayList<String> locationList) {
        this.context = context;
        this.userNameList = userNameList;
        this.riderList = riderList;
        this.ridingList = ridingList;
        this.emailList = emailList;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public searchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);

        return new searchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {

        holder.textViewUserName.setText(userNameList.get(position));
        holder.textViewRider.setText(riderList.get(position));
        holder.textViewRiding.setText(ridingList.get(position));
        holder.textViewEmail.setText(emailList.get(position));
        holder.textViewLocation.setText(locationList.get(position));

        holder.textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Full Name Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, sendEmailActivity.class);

                String email = emailList.get(position);

                intent.putExtra("email", email);
                //Toast.makeText(context, email, Toast.LENGTH_SHORT).show();

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }
}
