package com.example.pill

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val pills:List<PillClass>): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

//    var onItemClick: ((DataClass) -> Unit)? = null

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: Any){
        mListener = listener as onItemClickListener
    }

    class ViewHolderClass(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val rvPillTypeImage: ImageView = itemView.findViewById(R.id.ivPillTypeImage)
        val rvPillName: TextView = itemView.findViewById(R.id.itvPillName)
        val rvPillDetail: TextView = itemView.findViewById(R.id.itvPillDetail)
        val rvTime: TextView = itemView.findViewById(R.id.itvTime)

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
        return pills.size

    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentPill = pills[position]
        //laman ng item view ng recycler view
        val imagePillType = when (currentPill.pillType){
            1 -> R.drawable.icon_pill
            2 -> R.drawable.icon_capsule
            3 -> R.drawable.icon_bottle_trans
            4 -> R.drawable.icon_injection
            else -> R.drawable.icon_pill
        }
        holder.rvPillTypeImage.setImageResource(imagePillType)
        holder.rvPillName.text = currentPill.pillName
        holder.rvPillDetail.text = "${currentPill.dosage} pill, ${currentPill.recur}"
        holder.rvTime.text = currentPill.timesOfDay


        //pangclick sa item sa loob ng recyclerview
//        holder.itemView.setOnClickListener{
//            onItemClick?.invoke(currentItem)
//        }

    }
}