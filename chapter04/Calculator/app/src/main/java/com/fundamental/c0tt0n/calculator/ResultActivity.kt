package com.fundamental.c0tt0n.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.expressionLabel
import kotlinx.android.synthetic.main.activity_result.resultLabel

class ResultActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    val price = intent.getIntExtra(EXT_PRICE, 0)
    val discount = intent.getIntExtra(EXT_DISCOUNT, 0)
    expressionLabel.text = getString(R.string.expression_label, price, discount)

    val discountPrice = price * (100 - discount) / 100
    resultLabel.text = getString(R.string.result_label, discountPrice)
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
