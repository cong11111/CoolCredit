package com.tiny.cash.loan.card.message

import android.os.Bundle
import android.view.View
import com.tiny.cash.loan.card.base.BaseActivity
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityMessageDetailsBinding
import com.tiny.cash.loan.card.net.response.data.bean.MessageResult.ItemsBean

class MessageDetailsActivity : BaseActivity() {
    private var binding: ActivityMessageDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageDetailsBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root) //DataBindingUtil.setContentView(this, R.layout.activity_message_details);
        val item = intent.getSerializableExtra("bean") as ItemsBean?
        binding!!.tvMessage.text = item!!.title
        binding!!.tvMessageName.text = item.content
        binding!!.tvMessageTime.text = item.ctime
        binding!!.llTitle.ivBack.setOnClickListener { v: View? -> finish() }
        binding!!.llTitle.tvTitle.text = "Message"
    }
}