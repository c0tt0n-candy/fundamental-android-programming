package com.fundamental.c0tt0n.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fundamental.c0tt0n.calculator.R.string
import kotlinx.android.synthetic.main.activity_main.calculate
import kotlinx.android.synthetic.main.activity_main.discountEdit
import kotlinx.android.synthetic.main.activity_main.priceEdit

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    calculate.setOnClickListener {
      var isValid = true

      val priceText = priceEdit.text.toString()
      if (priceText.isEmpty()) {
        priceEdit.error = getString(string.price_error)
        isValid = false
      }

      val discountText = discountEdit.text.toString()
      if (discountText.isEmpty()) {
        discountEdit.error = getString(string.discount_error)
        isValid = false
      }

      if (isValid) {
        val price = Integer.parseInt(priceText)
        val discount = Integer.parseInt(discountText)

        // TODO: transit to ResultActivity
      }
    }
  }
}