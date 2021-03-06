package gghost.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    RecyclerView recyclerView;
    SimpleItemRecyclerViewAdapter adapter;

    ArrayList<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //Инициализируем RecyclerView
        this.recyclerView = ItemListActivity.this.findViewById(R.id.item_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new SimpleItemRecyclerViewAdapter(this, this.postList, mTwoPane);
        this.recyclerView.setAdapter(this.adapter);

        System.out.println("size after assignment:" + ItemListActivity.this.postList);


        //Отправляем запрос
        NetworkService.getInstance()
                .getJSONApi()
                .getAllPosts()
                .enqueue(new Callback<ArrayList<Post>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {


                        Handler mainHandler = new Handler(ItemListActivity.this.getMainLooper());
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                ItemListActivity.this.postList = response.body();
                                ItemListActivity.this.adapter.mValues = response.body();
                                System.out.println("size after fetch:" + ItemListActivity.this.adapter.mValues);
                                ItemListActivity.this.adapter.notifyDataSetChanged();
                            }
                        };
                        mainHandler.post(myRunnable);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

                    }
                });

    }

//    private void setupRecyclerView() {
//        //Здесь нужно присвоить зафетченные данные адаптеру
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, this.postList, mTwoPane));
//    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        public ArrayList<Post> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post item = (Post) view.getTag();
                if (mTwoPane) {

                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getId() + "" );
                    arguments.putSerializable(ItemDetailFragment.ARG_ARRAY_ID, item);

                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
//                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                    intent.putExtra(ItemDetailFragment.ARG_ARRAY_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      ArrayList<Post> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            System.out.println("here we are!");

            System.out.println("post id: " + mValues.get(0).getId());

            holder.mIdView.setText(String.format("%d", mValues.get(position).getId()));
//            holder.mIdView.setText("1");
            holder.mContentView.setText(mValues.get(position).getTitle());
//            holder.mContentView.setText("mytext lorem ipsum");

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}