package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.SingleRepresentativeElementBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter(private val clickListener: RepresentativeListener): ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class RepresentativeViewHolder private constructor(val binding: SingleRepresentativeElementBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative, clickListener: RepresentativeListener) {
        binding.representative = item
        binding.clickListener = clickListener
        binding.representativePhoto.setImageResource(R.drawable.ic_profile)

        item.official.channels?.let {
            showSocialLinks(it)
        }

        item.official.urls?.let {
            showWWWLinks(it)
        }


        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): RepresentativeViewHolder {
                val binding = SingleRepresentativeElementBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return RepresentativeViewHolder(binding)
            }
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) { enableLink(binding.facebookIcon, facebookUrl) }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) { enableLink(binding.twitterIcon, twitterUrl) }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>()  {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}

class RepresentativeListener (val clickListener: (representative: Representative) -> Unit) {
    fun onClick(representative: Representative) = clickListener(representative)
}