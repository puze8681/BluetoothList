package kr.puze.bluetooth

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bluetooth.view.*

class BluetoothRecyclerAdapter(var items: ArrayList<String>, var context: Context) : RecyclerView.Adapter<BluetoothRecyclerAdapter.ViewHolder>() {
    @SuppressLint("LongLogTag")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("LOGTAG, ImageCheckInRecyclerAdapter", "onCreate")
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bluetooth, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick?.onItemClick(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context!!
        fun bind(item: String) {
            itemView.text_item.text = item
        }
    }

    var itemClick: ItemClick? = null

    interface ItemClick {
        fun onItemClick(view: View?, position: Int)
    }
}
