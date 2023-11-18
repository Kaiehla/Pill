package com.example.pill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val dataList:ArrayList<DataClass>): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

//    var onItemClick: ((DataClass) -> Unit)? = null

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: Any){
        mListener = listener as onItemClickListener
    }

    class ViewHolderClass(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.image)
        val rvTitle: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return dataList.size

    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle

        //pangclick sa item sa loob ng recyclerview
//        holder.itemView.setOnClickListener{
//            onItemClick?.invoke(currentItem)
//        }

    }
}