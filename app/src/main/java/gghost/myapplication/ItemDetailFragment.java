package gghost.myapplication;


import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ARRAY_ID = "array_key";

    /**
     * The dummy content this fragment is presenting.
     */
    @Nullable
    public Post mItem = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ARRAY_ID)) {
            System.out.println("Я МЫ ПОБЕДА");
            System.out.println(getArguments().getSerializable(ARG_ARRAY_ID));
            this.mItem = (Post) getArguments().getSerializable(ARG_ARRAY_ID);
        } else {
            System.out.println("no ARG_ARRAY_ID item in the arguments");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText("title: \"" + mItem.getTitle() + "\"\n\n" + mItem.getBody());
        }

        return rootView;
    }
}