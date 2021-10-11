package com.mozzarelly.cbthelper.viewentries

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.analyze.AnalyzeActivity
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import org.threeten.bp.format.DateTimeFormatter
import kotlin.reflect.KClass


class ViewEntriesActivity : CBTActivity<EntriesViewModel>() {

    override val layout = R.layout.activity_view_entries
    override val viewModel: EntriesViewModel by cbtViewModel()

    private lateinit var entriesAdapter: EntryAdapter
    private lateinit var recycler: RecyclerView

    override fun EntriesViewModel.setup() {
        load()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Your entries"

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            start<AddEntryActivity>("forceNew" to "true")
        }

        entriesAdapter = EntryAdapter().apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(entry: Entry?) {
                    entry?.id ?: return
                    start<AnalyzeActivity>(entry.id)
                }
            })
        }

        findViewById<RecyclerView>(R.id.entries).run {
            recycler = this
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = entriesAdapter

            attachHelper(ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    delete(entriesAdapter.getEntryAt(viewHolder.adapterPosition))
                }
            }))
        }

        observe(viewModel.allEntries) {
            entriesAdapter.submitList(it)
            recycler.smoothScrollToPosition(0)
        }
    }

    override val onReturnFrom = mapOf<KClass<*>, (Int) -> Unit>(
        AddEntryActivity::class to { result ->
            showSavedEntryDialog(result)
        }
    )

    fun delete(entry: Entry){
        viewModel.delete(entry)
        Snackbar.make(findViewById(R.id.entries), "Entry deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") { viewModel.undelete(entry) }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Entry> = object : DiffUtil.ItemCallback<Entry>() {
            override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem.date == newItem.date
        }
    }

    inner class EntryAdapter : ListAdapter<Entry, EntryAdapter.EntryListItemHolder?>(DIFF_CALLBACK) {
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
            private val button: View = itemView.findViewById(R.id.menuButton)

            @SuppressLint("SetTextI18n")
            fun bind(entry: Entry){
                title.text = when {
                    entry.situationDetail?.startsWith("DEBUG:") == true -> entry.situationDetail?.removePrefix("DEBUG:")
                    entry.situationType -> "A situation at ${entry.situationDetail}"
                    else -> "A conversation with ${entry.situationDetail}"
                }

                //emotions.text = """You felt ${emotionText(entry.emotion1, entry.emotion2, entry.emotion3)?.toLowerCase(Locale.US) ?: " emotions"}."""
                subtitle.text = formatter.format(entry.date)

                itemView.setOnClickListener {
                    val position = adapterPosition
                    listener?.takeIf { position != RecyclerView.NO_POSITION }?.onItemClick(getItem(position))
                }

                button.setOnClickListener {
                    PopupMenu(this@ViewEntriesActivity, button).apply {
                        inflate(R.menu.menu_entry)

                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.action_edit -> {
                                    start<AddEntryActivity>(entry.id)
                                    true
                                }
                                R.id.action_delete -> {
                                    delete(entry)
                                    true
                                }
                                R.id.action_analyze -> {
                                    start<AnalyzeActivity>(entry.id)
                                    true
                                }
                                else -> false
                            }
                        }

                        show()
                    }
                }
            }
        }

        fun setOnItemClickListener(listener: OnItemClickListener?) {
            this.listener = listener
        }
    }
}

fun RecyclerView.attachHelper(helper: ItemTouchHelper) {
    helper.attachToRecyclerView(this)
}

interface OnItemClickListener {
    fun onItemClick(entry: Entry?)
}
