package org.wordpress.android.ui.reader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.wordpress.android.R
import org.wordpress.android.WordPress
import org.wordpress.android.models.ReaderTagList
import org.wordpress.android.ui.reader.ReaderTypes.ReaderPostListType
import org.wordpress.android.ui.reader.viewmodels.ReaderParentPostListViewModel
import javax.inject.Inject

class ReaderParentPostListFragment : Fragment(R.layout.reader_parent_post_list_fragment) {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ReaderParentPostListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as WordPress).component().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO should we show a loading view before we load the tags?
        initViewModel(view)
    }

    private fun initViewModel(view: View) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReaderParentPostListViewModel::class.java)
        startObserving(view)
        viewModel.start()
    }

    private fun startObserving(view: View) {
        viewModel.tabs.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                initViewPager(it, view)
            }
        })
    }

    private fun initViewPager(tags: ReaderTagList, view: View) {
        val demoCollectionAdapter = DemoCollectionAdapter(this, tags)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tags[position].tagDisplayName // TODO should we use displayname or title?
        }.attach()
    }

    class DemoCollectionAdapter(parent: Fragment, private val tags: ReaderTagList) : FragmentStateAdapter(parent) {
        override fun getItemCount(): Int = tags.size

        override fun createFragment(position: Int): Fragment {
            return ReaderPostListFragment.newInstanceForTag(tags[position], ReaderPostListType.TAG_FOLLOWED, true)
        }
    }
}
