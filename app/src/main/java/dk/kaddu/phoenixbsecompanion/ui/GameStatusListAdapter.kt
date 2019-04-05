package dk.kaddu.phoenixbsecompanion.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.kaddu.phoenixbsecompanion.R
import dk.kaddu.phoenixbsecompanion.data.GameStatus
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class GameStatusListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<GameStatusListAdapter.GameStatusViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var gameStatusList = emptyList<GameStatus>() // cached copy of GameStatus

    inner class GameStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameStatusCurrentDayItemView = itemView.findViewById<TextView>(R.id.current_day_text_view)
        val gameStatusTurnsDownloadedItemView = itemView.findViewById<TextView>(R.id.turns_downloaded_text_view)
        val gameStatusTurnsProcessedItemView = itemView.findViewById<TextView>(R.id.turns_processed_text_view)
        val gameStatusTurnsUploadedItemView = itemView.findViewById<TextView>(R.id.turns_uploaded_text_view)
        val gameStatusDayFinishedItemView = itemView.findViewById<TextView>(R.id.day_finished_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameStatusViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return GameStatusViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameStatusViewHolder, position: Int) {
        val current = gameStatusList[position]
        holder.gameStatusCurrentDayItemView.current_day_text_view.text = current.current_date.toString()
        holder.gameStatusTurnsDownloadedItemView.turns_downloaded_text_view.text = current.turns_downloaded.toString()
        holder.gameStatusTurnsProcessedItemView.turns_processed_text_view.text = current.turns_processed.toString()
        holder.gameStatusTurnsUploadedItemView.turns_uploaded_text_view.text = current.turns_uploaded.toString()
//        holder.gameStatusDayFinishedItemView.turns_uploaded_text_view.text = current.day_finished.toString()
    }

    internal fun setGameStatusList(gameStatusList: List<GameStatus>) {
        this.gameStatusList = gameStatusList
        notifyDataSetChanged()
    }

    override fun getItemCount() = gameStatusList.size
}