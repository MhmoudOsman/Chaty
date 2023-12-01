package com.chaty.app.messages_screen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.chaty.app.R
import com.chaty.app.base.BaseViewHolder
import com.chaty.app.databinding.ItemContactBinding
import com.chaty.domain.user.models.UserModel

class ContactAdapter : ListAdapter<UserModel, BaseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder  = ContactViewHolder(
        ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            super.onBind(position)
            binding.apply {
                val item = getItem(position)
                imgPic.load(item.image){
                    error(R.drawable.img_profile_pic)
                }
                tvName.text = item.displayName
            }
        }

        override fun onClick(v: View?) {

        }

        override fun clear() {

        }

    }
    class DiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }

}