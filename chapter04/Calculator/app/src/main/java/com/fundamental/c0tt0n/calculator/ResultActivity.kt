package com.fundamental.c0tt0n.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)
  }

  companion object {
    private const val EXT_PRICE = "ext_price"
    private const val EXT_DISCOUNT = "ext_discount"

    fun getIntent(context: Context, price: Int, discount: Int): Intent =
        Intent(context, ResultActivity::class.java).apply {
          putExtra(EXT_PRICE, price)
          putExtra(EXT_DISCOUNT, discount)
        }
  }
}
