package com.mozzarelly.cbthelper.viewentries

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.analyze.AnalyzeEntryActivity
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import kotlinx.android.synthetic.main.activity_view_entries.*
import org.threeten.bp.format.DateTimeFormatter


class ViewEntriesActivity : CBTActivity<EntriesViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entries)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Completed entries"

        viewModel.loadEntries()

        fab.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java))
        }

        entries.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)

            val adapter = EntryAdapter().apply {
                setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
                    override fun onItemClick(entry: Entry?) {
                        entry?.id ?: return
                        startActivity(Intent(this@ViewEntriesActivity, AnalyzeEntryActivity::class.java)
                            .putExtra("id", entry.id)
                        )
                    }
                })

                observe(viewModel.allEntries){
                    submitList(it)
                }
            }

            this.adapter = adapter

            attachHelper(ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.delete(adapter.getEntryAt(viewHolder.adapterPosition))
                    Toast.makeText(this@ViewEntriesActivity, "Entry deleted", Toast.LENGTH_SHORT).show()
                }
            }))
        }
    }

//    override fun getActivityViewModel() = viewModelProvider.getAndInit<EntriesViewModel>()
    override val viewModel: EntriesViewModel by viewModels { viewModelProvider }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}

fun RecyclerView.attachHelper(helper: ItemTouchHelper) {
    helper.attachToRecyclerView(this)
}

class EntryAdapter : ListAdapter<Entry, EntryAdapter.EntryListItemHolder?>(DIFF_CALLBACK) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mma 'on' MMM d")

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false)
        return EntryListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryListItemHolder, position: Int) {
        val currentEntry = getItem(position) ?: error("null")
        holder.bind(currentEntry)
    }

    fun getEntryAt(position: Int): Entry = getItem(position)

    inner class EntryListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.firstLine)
        private val subtitle: TextView = itemView.findViewById(R.id.secondLine)

        @SuppressLint("SetTextI18n")
        fun bind(entry: Entry){
            if (entry.situationType)
                title.text = "A situation at ${entry.situationDetail}"
            else
                title.text = "A conversation with ${entry.situationDetail}"

            //emotions.text = """You felt ${emotionText(entry.emotion1, entry.emotion2, entry.emotion3)?.toLowerCase(Locale.US) ?: " emotions"}."""
            subtitle.text = formatter.format(entry.date)

            itemView.setOnClickListener {
                val position = adapterPosition
                listener?.takeIf { position != RecyclerView.NO_POSITION }?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(entry: Entry?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Entry> = object : DiffUtil.ItemCallback<Entry>() {
            override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem.date == newItem.date
        }
    }
}

