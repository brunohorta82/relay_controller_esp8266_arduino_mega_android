package bhsystems.eu.relaycontroller.controllers.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * BindableViewHolder
 * Created by ivooliveira on 08/03/17.
 */

public abstract class BindableViewHolder<T> extends RecyclerView.ViewHolder {
    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(int position, T t);

}