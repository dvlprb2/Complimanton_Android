package com.example.complimaton.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.complimaton.R
import com.example.complimaton.managers.ProfileManager
import com.example.complimaton.screens.tabs.ProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn

class TopComplimentsWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val intent = Intent(context, ProfileFragment::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(
                context.packageName, R.layout.top_compliments_widget
            )
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

            // Retrieve top compliments and update the TextView
            getTopCompliments(context) { topCompliments ->
                println("updating widget with data: $topCompliments")
                updateWidgetViews(views, topCompliments, appWidgetManager, appWidgetId)
            }
        }
    }

    private fun getTopCompliments(context: Context, callback: (List<String>) -> Unit) {
        val profileManager = ProfileManager()
        val currentUser = GoogleSignIn.getLastSignedInAccount(context)

        currentUser?.let {
            profileManager.getTopCompliments(it.id!!) { topCompliments ->
                println(topCompliments.take(3))
                callback(topCompliments.take(3))
            }
        }
    }

    private fun updateWidgetViews(
        views: RemoteViews, compliments: List<String>, manager: AppWidgetManager, widgetId: Int
    ) {
        views.setTextViewText(R.id.topComplimentsTextView1, compliments.getOrNull(0) ?: "")
        views.setTextViewText(R.id.topComplimentsTextView2, compliments.getOrNull(1) ?: "")
        views.setTextViewText(R.id.topComplimentsTextView3, compliments.getOrNull(2) ?: "")

        manager.updateAppWidget(widgetId, views)
    }
}
