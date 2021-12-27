package com.joaquim.joaquim_teste.ui.home.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.extensions.changeSeparator
import com.joaquim.joaquim_teste.data.commom.extensions.toDate
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.databinding.CardHomeEventsBinding

class HomeEventsAdapter(
    private val events: List<LocalObjectBoxDbEventDetailsItem>,
    val onClick: (LocalObjectBoxDbEventDetailsItem) -> Unit
) : RecyclerView.Adapter<HomeEventsAdapter.EventsView>() {

    private var onAttach: Boolean = false
    private val duration: Long = 200

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventsView = EventsView(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_home_events,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventsView, position: Int) {
        val eventDetailsItem = events[position]
        holder.binding.eventItem = eventDetailsItem
        holder.binding.root.setOnClickListener { onClick.invoke(eventDetailsItem) }
        holder.bind(eventDetailsItem, position)
        setAnimation(holder.itemView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(itemView: View) {
        itemView.alpha = 0f

        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = duration / 2
        animator.duration = 250
        animatorSet.play(animator)
        animator.start()

    }

    inner class EventsView(val binding: CardHomeEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: LocalObjectBoxDbEventDetailsItem, position: Int) {
            with(binding) {
                cardHomeEventItemTitle.text = event.eventDetailTitle
                cardHomeEventItemDate.text = event.eventDetailDate
                cardHomeEventItemPrice.text = event.eventDetailPrice
                //TODO SET IMG
//                Glide.with(this).load(mask.img).into(mask_item_img)
            }
        }
    }

}

@BindingAdapter("getEventDate")
fun setEventDate(textView: TextView, eventDate: String) {
    //TODO FIX
    //java.lang.NumberFormatException: For input string: "Mon Aug 20 14:00:00 GMT-03:00 2018"
//    textView.text = textView.context.getString(
//        R.string.card_home_event_detail_place_holder_text).replace("#Placeholder_Text", eventDate.toLong().toDate())
}

@BindingAdapter("getEventPrice")
fun setEventPrice(textView: TextView, eventPrice: String) {
    textView.text = textView.context.getString(
        R.string.card_home_event_item_price_text,
        eventPrice.changeSeparator()
    )
}
