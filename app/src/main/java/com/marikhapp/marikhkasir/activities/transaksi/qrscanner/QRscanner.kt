package com.marikhapp.marikhkasir.activities.transaksi.qrscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.marikhapp.marikhkasir.R
import kotlinx.android.synthetic.main.activity_q_rscanner.*

class QRscanner : CaptureActivity() {

    override fun initializeContent(): DecoratedBarcodeView? {
        setContentView(R.layout.activity_q_rscanner)
        cameraMainLayout.setOnClickListener {
            this.finish()
        }
        return findViewById(R.id.zxing_barcode_scanner)
    }
    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
