package ga.forntoh.bableschool.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.ui.category.CategoryActivity
import java.net.URL

class MessagingService: FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "01"
        const val TAG = "Notifications"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "Message : ${remoteMessage.data}")

        remoteMessage.data.let {

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, CategoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("id", 0L)
                putExtra("title", "News")
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(it["title"])
                    .setContentText(it["message"])
                    .setSmallIcon(R.drawable.ic_asset_3)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            DownloadImage(notification, applicationContext)
                    .execute(it["pictureUrl"])
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Message : $token")
    }

    internal class DownloadImage(private val notification: NotificationCompat.Builder, val context: Context): AsyncTask<String, Void, Bitmap>() {

        private fun downloadImageBitmap(url: String): Bitmap? {
            val inputStream = URL(url).openStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            return downloadImageBitmap(params.first()!!)
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            notification.setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(result)
                    .bigLargeIcon(null))
            val manager = NotificationManagerCompat.from(context)
            manager.notify(0, notification.build())
        }
    }
}