package com.skripsi.presensigps.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.DataModel
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.ui.fragment.ReportFragment
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    val back: ImageView by lazy { findViewById(R.id.icBack) }
    val parentPrintPDF: ConstraintLayout by lazy { findViewById(R.id.parentPrintPdf) }
    val parentReport: ConstraintLayout by lazy { findViewById(R.id.parentReport) }
    val btnPrint: MaterialButton by lazy { findViewById(R.id.btnPrint) }
    val btnPrintPDF: MaterialButton by lazy { findViewById(R.id.btnPrintPDF) }
    val inputWhen: AutoCompleteTextView by lazy { findViewById(R.id.inputWhen) }
    val inputYear: TextInputEditText by lazy { findViewById(R.id.inputYear) }
    private val itemCetak = listOf(
        "hari ini",
        "januari",
        "februari",
        "maret",
        "april",
        "mei",
        "juni",
        "juli",
        "agustus",
        "september",
        "oktober",
        "november",
        "desember",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        loadFragment(ReportFragment("all"))

        sharedPref = PreferencesHelper(this)

        back.setOnClickListener { finish() }

        parentPrintPDF.visibility = View.GONE
        btnPrint.setOnClickListener {
            parentPrintPDF.visibility = View.VISIBLE
        }

        parentReport.setOnClickListener {
            parentPrintPDF.visibility = View.GONE
        }


        val adapterCetak =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemCetak)
        inputWhen.setAdapter(adapterCetak)

        btnPrintPDF.setOnClickListener {
            val sWhen = inputWhen.text.toString()
            val sYear = inputYear.text.toString()

            if (!inputWhen.text.isNullOrEmpty()) {
                var whenReport = ""
                when (sWhen) {
                    "hari ini" -> whenReport = "today"
                    "januari" -> whenReport = "01"
                    "februari" -> whenReport = "02"
                    "maret" -> whenReport = "03"
                    "april" -> whenReport = "04"
                    "mei" -> whenReport = "05"
                    "juni" -> whenReport = "06"
                    "juli" -> whenReport = "07"
                    "agustus" -> whenReport = "08"
                    "september" -> whenReport = "09"
                    "oktober" -> whenReport = "10"
                    "november" -> whenReport = "11"
                    "desember" -> whenReport = "12"
                }
                printReport(whenReport, sWhen, "report",sYear)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Pilih jadwal terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun printReport(whenReport: String, sWhen: String, type: String, sYear: String) {

        ApiClient.SetContext(this).instances.apiCreatePdfReport(whenReport, whenReport, sYear)
            .enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status
                        val data = response.body()?.data

                        if (status == true) {


                            if (data != null) {
                                createPDF(data, sWhen, sYear, type)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "tidak ada data",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        } else {
                            Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal : " + response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Gagal : " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameReport, fragment)
            commit()
        }
    }

    private lateinit var scaleBitmap: Bitmap
    private lateinit var bitmap: Bitmap
    private val pageWidth = 1200
    private val pageHeight = 2400
    private lateinit var dateTime: Date
    private lateinit var dateFormat: DateFormat

    @SuppressLint("SimpleDateFormat")
    private fun createPDF(
        result: ArrayList<DataModel>,
        sWhen: String,
        sYear: String,
        type: String
    ) {

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_horizontal)
        scaleBitmap = Bitmap.createScaledBitmap(bitmap, pageWidth, 518, false)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )

        dateTime = Date()

        val pdfDocument = PdfDocument()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        canvas.drawBitmap(scaleBitmap, 0f, 0f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.BLACK
        paint.textSize = 35f
        canvas.drawText(
            "Nama: " + sharedPref.getString(Constant.PREF_USER_POSITION),
            20f,
            590f,
            paint
        )

        paint.textAlign = Paint.Align.RIGHT
        dateFormat = SimpleDateFormat("dd/MM/yy")
        canvas.drawText(
            "Tanggal: " + dateFormat.format(dateTime),
            (pageWidth - 20).toFloat(),
            590f,
            paint
        )
        dateFormat = SimpleDateFormat("HH:mm:ss")
        canvas.drawText(
            "Pukul: " + dateFormat.format(dateTime),
            (pageWidth - 20).toFloat(),
            640f,
            paint
        )

        if (type == "report") {
            canvas.drawText(
                "laporan: $sWhen, $sYear",
                (pageWidth - 20).toFloat(),
                690f,
                paint
            )
        } else if (type == "presence") {
            canvas.drawText(
                "Presensi Kehadiran: $sWhen, $sYear",
                (pageWidth - 20).toFloat(),
                690f,
                paint
            )
        }

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(20f, 780f, (pageWidth - 20).toFloat(), 860f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL

        var y = 950
        //

        if (type == "report") {
            if (sWhen == "hari ini") {
                canvas.drawText("No.", 40f, 830f, paint)
                canvas.drawText("Nama", 150f, 830f, paint)
                canvas.drawText("Melapor", 700f, 830f, paint)
                canvas.drawText("Ket.", 1050f, 830f, paint)
                canvas.drawLine(130f, 790f, 130f, 840f, paint)
                canvas.drawLine(680f, 790f, 680f, 840f, paint)
                canvas.drawLine(1030f, 790f, 1030f, 840f, paint)

                var no = 1
                for (i in result) {
                    val timestampCreatedAt: Timestamp = i.created_at
                    val dateCreatedAt = java.sql.Date(timestampCreatedAt.time)

                    val simpleDateFormatTime =
                        SimpleDateFormat("HH:mm:ss")

                    val time = simpleDateFormatTime.format(dateCreatedAt).toString()

                    canvas.drawText(no.toString(), 40f, y.toFloat(), paint)
                    canvas.drawText(i.name, 150f, y.toFloat(), paint)
                    canvas.drawText(time, 700f, y.toFloat(), paint)
                    canvas.drawText("--", 1100f, y.toFloat(), paint)

                    no += 1
                    y += 100
                }
            } else {
                canvas.drawText("No.", 40f, 830f, paint)
                canvas.drawText("Nama", 150f, 830f, paint)
                canvas.drawText("Jumlah Laporan", 700f, 830f, paint)
                canvas.drawText("Ket.", 1050f, 830f, paint)
                canvas.drawLine(130f, 790f, 130f, 840f, paint)
                canvas.drawLine(680f, 790f, 680f, 840f, paint)
                canvas.drawLine(1030f, 790f, 1030f, 840f, paint)

                var no = 1
                for (i in result) {
                    canvas.drawText(no.toString(), 40f, y.toFloat(), paint)
                    canvas.drawText(i.name, 150f, y.toFloat(), paint)
                    canvas.drawText(i.count.toString(), 700f, y.toFloat(), paint)
                    canvas.drawText("--", 1100f, y.toFloat(), paint)

                    no += 1
                    y += 100
                }

            }
        } else if (type == "presence") {

            if (sWhen == "hari ini") {
                canvas.drawText("No.", 40f, 830f, paint)
                canvas.drawText("Nama", 150f, 830f, paint)
                canvas.drawText("Presensi", 700f, 830f, paint)
                canvas.drawText("Ket.", 1050f, 830f, paint)
                canvas.drawLine(130f, 790f, 130f, 840f, paint)
                canvas.drawLine(680f, 790f, 680f, 840f, paint)
                canvas.drawLine(1030f, 790f, 1030f, 840f, paint)

                var no = 1
                for (i in result) {

                    val timestampCreatedAt: Timestamp = i.created_at
                    val dateCreatedAt = java.sql.Date(timestampCreatedAt.time)

                    val simpleDateFormatTime =
                        SimpleDateFormat("HH:mm:ss")

                    val time = simpleDateFormatTime.format(dateCreatedAt).toString()

                    canvas.drawText(no.toString(), 40f, y.toFloat(), paint)
                    canvas.drawText(i.name, 150f, y.toFloat(), paint)
                    canvas.drawText(time, 700f, y.toFloat(), paint)
                    canvas.drawText("--", 1100f, y.toFloat(), paint)

                    no += 1
                    y += 100
                }
            } else {
                canvas.drawText("No.", 40f, 830f, paint)
                canvas.drawText("Nama", 150f, 830f, paint)
                canvas.drawText("Jumlah Kehadiran", 700f, 830f, paint)
                canvas.drawText("Ket.", 1050f, 830f, paint)
                canvas.drawLine(130f, 790f, 130f, 840f, paint)
                canvas.drawLine(680f, 790f, 680f, 840f, paint)
                canvas.drawLine(1030f, 790f, 1030f, 840f, paint)

                var no = 1
                for (i in result) {
                    canvas.drawText(no.toString(), 40f, y.toFloat(), paint)
                    canvas.drawText(i.name, 150f, y.toFloat(), paint)
                    canvas.drawText(i.count.toString(), 700f, y.toFloat(), paint)
                    canvas.drawText("--", 1100f, y.toFloat(), paint)

                    no += 1
                    y += 100
                }

            }
        }

        pdfDocument.finishPage(page)
        val date = SimpleDateFormat("dd_MM_yy")
        val time = SimpleDateFormat("HH_mm_ss")

        var file: File? = null
        if (type == "report") {
            file = File(
                Environment.getExternalStorageDirectory(),
                "Download/laporan_sales_tgl_${date.format(dateTime)}_pkl_${time.format(dateTime)}.pdf"
//            "/sales_laporan_tgl_${date.format(dateTime)}_pkl_${time.format(dateTime)}.pdf"
            )
        } else if (type == "presence") {

            file = File(
                Environment.getExternalStorageDirectory(),
                "Download/presensi_sales_tgl_${date.format(dateTime)}_pkl_${time.format(dateTime)}.pdf"
            )
        }

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "cetak pdf tersimpan di folder Download", Toast.LENGTH_LONG)
                .show()
            parentPrintPDF.visibility = View.GONE
        } catch (e: IOException) {
            Toast.makeText(this, "Gagal cetak laporan", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            parentPrintPDF.visibility = View.GONE
        }

        pdfDocument.close()
    }
}